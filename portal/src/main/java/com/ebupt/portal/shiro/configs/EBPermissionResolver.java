package com.ebupt.portal.shiro.configs;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

public class EBPermissionResolver implements PermissionResolver {

    public EBPermissionResolver() {
    }

    @Override
    public Permission resolvePermission(String s) {
        return new EBPermission(s);
    }
}
