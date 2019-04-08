package com.ebupt.portal.canyon.system.service.impl;

import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.util.EncryptUtil;
import com.ebupt.portal.canyon.common.util.PageableUtil;
import com.ebupt.portal.canyon.common.util.SystemConstant;
import com.ebupt.portal.canyon.common.util.TimeUtil;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.dao.UserCacheDao;
import com.ebupt.portal.canyon.system.dao.UserDao;
import com.ebupt.portal.canyon.system.entity.Menu;
import com.ebupt.portal.canyon.system.entity.User;
import com.ebupt.portal.canyon.system.service.MenuService;
import com.ebupt.portal.canyon.system.service.RoleMenuService;
import com.ebupt.portal.canyon.system.service.UserRoleService;
import com.ebupt.portal.canyon.system.service.UserService;
import com.ebupt.portal.canyon.system.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 用户管理业务层实现
 *
 * @author chy
 * @date 2019-03-07 20:57
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	private final UserDao userDao;
	private final MenuService menuService;
	private final UserCacheDao userCacheDao;
	private final UserRoleService userRoleService;
	private final RoleMenuService roleMenuService;

	@Autowired
	public UserServiceImpl(UserDao userDao, MenuService menuService, UserCacheDao userCacheDao,
	                       UserRoleService userRoleService, RoleMenuService roleMenuService) {
		this.userDao = userDao;
		this.menuService = menuService;
		this.userCacheDao = userCacheDao;
		this.userRoleService = userRoleService;
		this.roleMenuService = roleMenuService;
	}

	private static final int MAX_LOGIN = 5;
	private static final long VERIFY_TERM = 1000 * 60 * 5;
	private static final int REMIND_DAY = 3;

	@Override
	public User findByUserName(String userName) {
		return this.userDao.findByUserName(userName);
	}

	@Override
	public UpdateEnum updatePwd(PwdVo pwdVo) {
		User user = this.findByUserName(pwdVo.getUserName());
		String oldPwd = EncryptUtil.md5WithSpecialSalt(pwdVo.getOldPwd(), user.getSalt());
		if (user.getPassword().equals(oldPwd)) {
			String newSalt = EncryptUtil.randomSalt(6);
			String newPwd = EncryptUtil.md5WithSpecialSalt(pwdVo.getNewPwd(), newSalt);
			this.userDao.updatePwd(pwdVo.getUserName(), newPwd, newSalt, TimeUtil.getCurrentTime14());
			return UpdateEnum.SUCCESS;
		}
		return UpdateEnum.PASSWORD_ERROR;
	}

	@Override
	public UpdateEnum updateUser(UserVo user) {
		this.userDao.updateUser(user);
		return UpdateEnum.SUCCESS;
	}

	@Override
	public UpdateEnum updateState(String userName, String state) {
		this.userDao.updateState(userName, state);
		return UpdateEnum.SUCCESS;
	}

	@Override
	public UpdateEnum save(UserVo user, boolean initRole) {
		User u = this.findByUserName(user.getUserName());
		if (u == null) {
			u = this.userDao.save(new User(user));
			if (u.getId() != null) {
				if (initRole) {
					// 初始化拥有的角色信息
					Set<String> roles = new HashSet<>();
					roles.add(SystemConstant.BASE_ROLE);
					return this.userRoleService.updateUserRoles(new UserRoleVo(user.getUserName(),
							SystemConstant.ADD, roles));
				}
			}
			return UpdateEnum.UNKNOWN_ERROR;
		}
		return UpdateEnum.REPEAT_NAME_ERROR;
	}

	@Override
	public JsonResult login(HttpServletRequest request, LoginVo loginVo) {
		JsonResult jsonResult;
		// 获取session
		HttpSession session = request.getSession();
		String sessionId = session.getId();

		// 判断验证码是否正常
		String verifyCode = (String) session.getAttribute("code");
		Long timestamp = (Long) session.getAttribute("code_time");
		if (verifyCode == null || timestamp == null) {
			jsonResult = JsonResult.error("验证码已过期，请重新获取");
		} else {
			long timeDiff = System.currentTimeMillis() - timestamp;
			if (timeDiff > VERIFY_TERM) {
				jsonResult = JsonResult.error("验证码已过期，请重新获取");
			} else if (!verifyCode.toLowerCase().equals(loginVo.getVerifyCode().toLowerCase())) {
				jsonResult = JsonResult.error("验证码错误，请重新输入");
			} else {
				// 获取用户主体
				Subject subject = SecurityUtils.getSubject();
				String userName = loginVo.getUserName();

				// 登录校验
				UsernamePasswordToken usernamePasswordToken =
						new UsernamePasswordToken(userName, loginVo.getPassword());
				try {
					subject.login(usernamePasswordToken);

					// 修改登录时间
					this.userDao.updateLoginTime(userName, TimeUtil.getCurrentTime14());
					// 删除登录失败信息
					this.userCacheDao.deleteLoginErrorTimes(userName);

					// 清除验证码信息
					session.removeAttribute("code");
					session.removeAttribute("code_time");

					Map<String, Object> loginInfo = this.getLoginInfo(userName, sessionId);
					jsonResult = JsonResult.ok(loginInfo);
				} catch (LockedAccountException e) {
					jsonResult = JsonResult.error("该账号已被锁定，请联系管理员进行处理");
				} catch (UnknownAccountException e) {
					jsonResult = JsonResult.error("未知的用户");
				} catch (ExpiredCredentialsException e) {
					jsonResult = JsonResult.error("该密码已过期，请联系管理员进行处理");

					// 修改用户状态
					this.userDao.updateState(userName, "2");
				} catch (DisabledAccountException e) {
					jsonResult = JsonResult.error("该账号已注销，请联系管理员进行处理");
				} catch (IncorrectCredentialsException e) {
					int loginTimes = this.userCacheDao.getLoginErrorTimes(userName, 0);
					int remindTimes = MAX_LOGIN - loginTimes - 1;
					if (remindTimes == 0) {
						// 更新账号状态
						this.userDao.updateState(userName, "3");
						jsonResult = JsonResult.error("该账号已被锁定，请联系管理员进行处理");
						// 删除缓存中信息
						this.userCacheDao.deleteLoginErrorTimes(userName);
					} else {
						jsonResult = JsonResult.error("密码错误，" + remindTimes + "次后将被锁定");
						// 更新缓存中的失败次数
						this.userCacheDao.updateLoginErrorTimes(userName, loginTimes + 1);
					}
				}
			}
		}
		return jsonResult;
	}

	@Override
	public Page<User> findByPage(PageVo<UserSearchVo> page) {
		UserSearchVo search = page.getSearch();
		Specification<User> specification = ((root, query, criteriaBuilder) -> {
			List<Predicate> predicateList = new ArrayList<>();
			if (search != null) {
				if (StringUtils.isNotEmpty(search.getUserName())) {
					predicateList.add(criteriaBuilder.like(root.get("userName"), search.getUserName() + "%"));
				}
				if (StringUtils.isNotEmpty(search.getStartTime())) {
					predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), search.getStartTime()));
				}
				if (StringUtils.isNotEmpty(search.getEndTime())) {
					predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), search.getEndTime()));
				}
				if (search.getState() != null) {
					predicateList.add(criteriaBuilder.equal(root.get("state"), search.getState()));
				}
			}

			Predicate[] predicates = new Predicate[predicateList.size()];
			return query.where(predicateList.toArray(predicates)).getRestriction();
		});

		return this.userDao.findAll(specification, PageableUtil.getPageable(page));
	}

	@Override
	public UpdateEnum updateUserState(UserStateVo userStateVo) {
		this.userDao.updateState(userStateVo.getUserName(), userStateVo.getState());
		return UpdateEnum.SUCCESS;
	}

	@Override
	public UpdateEnum update(UserVo userVo) {
		this.userDao.updateUser(userVo);
		return UpdateEnum.SUCCESS;
	}

	/**
	 * 获取指定用户所拥有的菜单信息
	 *
	 * @param userName
	 *                  用户名称
	 * @return
	 *                  菜单信息结合
	 */
	private Set<MenuVo> getMenuInfo(String userName) {
		// 获取菜单信息
		Set<String> roles = this.userRoleService.findByUserName(userName);
		Set<String> menuIds = this.roleMenuService.findByRoles(roles);
		Set<Menu> menus = this.menuService.findByMenuIds(menuIds, "1");
		Set<MenuVo> menuVo = new HashSet<>();
		if (menus != null) {
			for (Menu menu: menus) {
				menuVo.add(new MenuVo(menu.getMenuId(), menu.getMenuName(), menu.getParentMenu()));
			}
		}

		// 组装返回消息
		return menuVo;
	}

	/**
	 * 获取登录信息
	 *
	 * @param userName
	 *                  登录用户名
	 * @param sessionId
	 *                  sessionId
	 * @return
	 *                  登录返回信息
	 */
	private Map<String, Object> getLoginInfo(String userName, String sessionId) {
		// 获取用户菜单信息
		Map<String, Object> map = new HashMap<>(5);
		map.put("menu", this.getMenuInfo(userName));
		map.put("token", sessionId);

		User user = this.findByUserName(userName);
		// 密码过期提示
		if (user.getPwdValidity() != 0) {
			long diff = user.getPwdValidity() -
					TimeUtil.getTimeDiff(TimeUtil.getCurrentTime14(), user.getLastPwdTime());
			if (diff <= REMIND_DAY) {
				map.put("expired", diff);
			}
		}
		// 第一次登录提示
		if (StringUtils.isEmpty(user.getLastPwdTime())) {
			map.put("firstLogin", true);
		}
		return map;
	}
}
