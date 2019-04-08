package com.ebupt.portal.canyon.common.shiro;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

/**
 * 指定权限比较器
 *
 * @author chy
 * @date 2019-03-14 11:27
 */
public class EbWildcardPermissionResolver implements PermissionResolver {

	@Override
	public Permission resolvePermission(String permissionString) {
		return new EbWildcardPermission(permissionString);
	}

}
