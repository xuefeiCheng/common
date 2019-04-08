package com.ebupt.portal.shiro.Service.impl;

import com.ebupt.portal.common.Results.JSONResult;
import com.ebupt.portal.common.constant.APIConstant;
import com.ebupt.portal.common.utils.StringUtil;
import com.ebupt.portal.common.utils.TimeUtil;
import com.ebupt.portal.shiro.Service.AuthService;
import com.ebupt.portal.shiro.pojo.MenuInfo;
import com.ebupt.portal.shiro.repository.RoleMenuRepository;
import com.ebupt.portal.shiro.repository.UserRoleReposiroty;
import com.ebupt.portal.system.entity.MenuEntity;
import com.ebupt.portal.system.repository.MenuReposiroty;
import com.ebupt.portal.system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service("loginService")
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleReposiroty userRoleReposiroty;
    private final RoleMenuRepository roleMenuRepository;
    private final MenuReposiroty menuReposiroty;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, UserRoleReposiroty userRoleReposiroty,
                           RoleMenuRepository roleMenuRepository, MenuReposiroty menuReposiroty) {
        this.userRepository = userRepository;
        this.userRoleReposiroty = userRoleReposiroty;
        this.roleMenuRepository = roleMenuRepository;
        this.menuReposiroty = menuReposiroty;
    }

    @Override
    public JSONResult checkLogin(String username, String password, HttpServletRequest request) {
        JSONResult jsonResult;

        // 获取用户主体
        Subject subject = SecurityUtils.getSubject();

        // 登录校验
        UsernamePasswordToken usernamePasswordToken =
                new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
            this.userRepository.updateTimeByUserName(TimeUtil.getCurrentTime14(), username);

            // 获取HTTPSession
            HttpSession session = request.getSession();
            session.setAttribute("login_user", username);

            // 获取用户角色数据
            Set<String> roles = getRolesByUserName(username);
            // 获取用户权限数据
            Set<MenuInfo> menuList = getPermissionsByUserName(roles);

            Map<String, Object> map = new HashMap<>();
            map.put(APIConstant.AUTHORIZATION, session.getId());
            map.put("menu", menuList);
            jsonResult = JSONResult.OK(map);
        } catch (LockedAccountException e) {
            jsonResult = JSONResult.LOGIN_FAIL("账户已锁定，请联系管理员解锁账户");
        } catch (DisabledAccountException e) {
            jsonResult = JSONResult.LOGIN_FAIL("该账户已注销");
        } catch (ExpiredCredentialsException e) {
            jsonResult = JSONResult.LOGIN_FAIL("密码已过期，请联系管理员重置密码");
        } catch (IncorrectCredentialsException e) {
            jsonResult = JSONResult.LOGIN_FAIL("密码错误");
        } catch (AuthenticationException e) {
            jsonResult = JSONResult.LOGIN_FAIL("该用户不存在");
        } catch (Exception e) {
            jsonResult = JSONResult.LOGIN_FAIL("登录失败");
            log.error("登录失败:{}", e.getMessage());
        }

        return jsonResult;
    }

    /**
     * 根据用户名获取角色集合
     *
     * @param userName
     *                  用户名
     * @return
     *                  角色集合
     */
    private Set<String> getRolesByUserName(String userName) {
        return this.userRoleReposiroty.findRoleIdByUserName(userName);
    }

    /**
     * 根据角色集合获取菜单信息
     *
     * @param roles
     *                  角色集合
     * @return
     *                  菜单集合
     */
    private Set<MenuInfo> getPermissionsByUserName(Set<String> roles) {
        Set<MenuInfo> permissions = new HashSet<>();

        if (roles != null) {
            for (String role: roles) {
                Set<String> menus = this.roleMenuRepository.findMenuIdByRoleId(role);
                if (menus != null && menus.size() > 0) {
                    for (String menuId: menus) {
                        MenuEntity entity = this.menuReposiroty.findByMenuId(menuId);
                        if (entity != null) {
                            String url = StringUtil.isEmpty(entity.getUrl()) ?
                                    null: entity.getUrl().split(":")[1];
                            if (url != null)
                                url = "#".equals(url) ? "#": "/" + url;
                            String parentMenu = StringUtil.isEmpty(entity.getParentMenu()) ?
                                    null: entity.getParentMenu();
                            permissions.add(new MenuInfo(entity.getMenuId(), entity.getMenuName(),
                                    url, parentMenu));
                        }
                    }
                }
            }
        }

        return permissions;
    }

}
