package com.ebupt.portal.canyon.common.shiro;

import com.ebupt.portal.canyon.common.util.TimeUtil;
import com.ebupt.portal.canyon.system.entity.Menu;
import com.ebupt.portal.canyon.system.entity.User;
import com.ebupt.portal.canyon.system.service.MenuService;
import com.ebupt.portal.canyon.system.service.RoleMenuService;
import com.ebupt.portal.canyon.system.service.UserRoleService;
import com.ebupt.portal.canyon.system.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm，用于授权和鉴权
 *
 * @author chy
 * @date 2019-03-08 16:40
 */
@Component
public class EbShiroRealm extends AuthorizingRealm {

	private final UserService userService;
	private final MenuService menuService;
	private final UserRoleService userRoleService;
	private final RoleMenuService roleMenuService;

	@Lazy
	@Autowired
	public EbShiroRealm(UserRoleService userRoleService, RoleMenuService roleMenuService, UserService userService,
	                    MenuService menuService) {
		this.userRoleService = userRoleService;
		this.roleMenuService = roleMenuService;
		this.userService = userService;
		this.menuService = menuService;
	}

	private static final String EXPIRED = "2";
	private static final String LOCKED = "3";
	private static final String CANCELLED = "4";

	/**
	 * 用于授权
	 *
	 * @param principals
	 *                      身份集合信息
	 * @return
	 *                      授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();
		Set<String> roles = this.userRoleService.findByUserName(userName);
		Set<String> menuIds = this.roleMenuService.findByRoles(roles);
		Set<Menu> menus = this.menuService.findByMenuIds(menuIds, "2");

		// 权限处理
		Set<String> permissions = new HashSet<>();
		if (menus != null) {
			for (Menu menu: menus) {
				permissions.add(menu.getUrl());
			}
		}

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(roles);
		authorizationInfo.setStringPermissions(permissions);
		return authorizationInfo;
	}

	/**
	 * 用于认证
	 *
	 * @param token
	 *                  用户提交的身份和凭证
	 * @return
	 *                  认证信息
	 * @throws AuthenticationException
	 *                  异常
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = (String) token.getPrincipal();
		SimpleAuthenticationInfo authorizationInfo;

		User user = this.userService.findByUserName(userName);
		if (user != null) {
			String state = user.getState();
			// 判断密码是否过期
			boolean pwdExpired = false;
			if (EXPIRED.equals(user.getState())) {
				// 密码已过期
				pwdExpired = true;
			} else if (user.getPwdValidity() > 0
					&& TimeUtil.getTimeDiff(TimeUtil.getCurrentTime14(), user.getLastPwdTime()) >= user.getPwdValidity()) {
				// 密码存在有效期，且已过期
				pwdExpired = true;
			}

			if (pwdExpired) {
				// 密码已过期
				throw new ExpiredCredentialsException();
			} else if (CANCELLED.equals(state)) {
				// 已注销
				throw new DisabledAccountException();
			} else if (LOCKED.equals(state)) {
				// 多次登录失败锁定
				throw new LockedAccountException();
			} else {
				// 正常
				authorizationInfo = new SimpleAuthenticationInfo(userName,
						user.getPassword(), ByteSource.Util.bytes(user.getSalt()), super.getName());
			}
		} else {
			// 不存在该账户
			throw new UnknownAccountException();
		}

		return authorizationInfo;
	}
}
