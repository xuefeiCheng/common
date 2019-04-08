package com.ebupt.portal.shiro.configs;

import com.ebupt.portal.common.constant.APIConstant;
import com.ebupt.portal.common.utils.TimeUtil;
import com.ebupt.portal.shiro.repository.RoleMenuRepository;
import com.ebupt.portal.shiro.repository.UserRoleReposiroty;
import com.ebupt.portal.system.entity.MenuEntity;
import com.ebupt.portal.system.entity.UserEntity;
import com.ebupt.portal.system.repository.MenuReposiroty;
import com.ebupt.portal.system.repository.UserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm，用于授权和鉴权
 */
@Component
public class EBRealm extends AuthorizingRealm {

    private final UserRepository userRepository;
    private final UserRoleReposiroty userRoleReposiroty;
    private final RoleMenuRepository roleMenuRepository;
    private final MenuReposiroty menuReposiroty;

    @Autowired
    @Lazy
    public EBRealm(UserRepository userRepository, UserRoleReposiroty userRoleReposiroty,
                   RoleMenuRepository roleMenuRepository, MenuReposiroty menuReposiroty) {
        this.userRepository = userRepository;
        this.userRoleReposiroty = userRoleReposiroty;
        this.roleMenuRepository = roleMenuRepository;
        this.menuReposiroty = menuReposiroty;
        super.setName("ebuptRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();

        // 获取用户角色数据
        Set<String> roles = getRolesByUserName(userName);
        // 获取用户权限数据
        Set<String> permissions = getPermissionsByUserName(roles);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        SimpleAuthenticationInfo authenticationInfo = null;
        // 从主体传过来的认证信息中,获得用户名
        String userName = (String) authenticationToken.getPrincipal();

        // 通过用户名获取用户详情
        UserEntity user = findUserInfoUserName(userName);
        if (user != null) {
            if ("2".equals(user.getState())
                    && TimeUtil.getCurrentTime14().compareTo(user.getEffectTime()) < 0) { // 异常账号，还在锁定期间
                throw new LockedAccountException();
            } else if ("3".equals(user.getState())) { // 已注销
                throw new DisabledAccountException();
            } else { // 正常账号，判断是否已过期
                if (user.getValidity() != 0 && TimeUtil.getTimeDiff14(user.getPwdTime(), TimeUtil.getCurrentTime14())
                        > user.getValidity() * APIConstant.DAY_MILLISECONDS) { // 密码已过期
                    throw new ExpiredCredentialsException();
                } else {
                    authenticationInfo = new SimpleAuthenticationInfo(userName, user.getPassword(), super.getName());
                }
            }
        }

        return authenticationInfo;
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
     * 根据角色集合获取权限集合
     *
     * @param roles
     *                  角色集合
     * @return
     *                  权限集合
     */
    private Set<String> getPermissionsByUserName(Set<String> roles) {
        Set<String> permissions = new HashSet<>();

        if (roles != null) {
            for (String role: roles) {
                Set<String> menus = this.roleMenuRepository.findMenuIdByRoleId(role);
                if (menus != null && menus.size() > 0) {
                    for (String menuId: menus) {
                        MenuEntity entity = this.menuReposiroty.findByMenuId(menuId);
                        if (entity != null)
                            permissions.add(entity.getUrl());
                    }
                }
            }
        }

        return permissions;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param userName
     *                  用户名
     * @return
     *                  密码
     */
    private UserEntity findUserInfoUserName(String userName) {
        return this.userRepository.findByUserName(userName);
    }

}
