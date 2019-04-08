package com.ebupt.portal.canyon.common.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义权限校验及越权重定向过滤器
 *
 * @author chy
 * @date 2019-03-13 20:07
 */
public class PermissionsAndRedirectFilter extends PermissionsAuthorizationFilter {

	private static final String URL_SPLIT = "/";

	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		return super.isAccessAllowed(request, response, buildPermissions(request));
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		Subject subject = getSubject(request, response);
		// 未登录则跳转到登录界面
		if (subject.getPrincipal() == null) {
			saveRequestAndRedirectToLogin(request, response);
		} else {
			String unauthorizedUrl = getUnauthorizedUrl();
			// 未授权url存在，则重定向到未授权url，并添加源url
			if (StringUtils.hasText(unauthorizedUrl)) {
				unauthorizedUrl += "?redirectUrl=" + ((HttpServletRequest) request).getRequestURI();
				WebUtils.issueRedirect(request, response, unauthorizedUrl);
			} else {
				// 未授权url未配置，直接返回401响应码
				WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		return false;
	}

	/**
	 * 构造权限字符串集合
	 *
	 * @param request
	 *                  ServletRequest
	 * @return
	 *                  权限字符串集合
	 */
	private String[] buildPermissions(ServletRequest request) {
		String[] perms = new String[1];
		HttpServletRequest req = (HttpServletRequest) request;

		String method = req.getMethod().toUpperCase();
		String url = req.getRequestURI();
		if (url.startsWith(URL_SPLIT)) {
			url = url.substring(1);
		}
		if (url.endsWith(URL_SPLIT)) {
			url = url.substring(0, url.length() - 2);
		}

		perms[0] = method + ":" + url;
		return perms;
	}
}
