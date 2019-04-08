package com.ebupt.portal.canyon.common.filter;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义登录及登录重定向过滤器
 *
 * @author chy
 * @date 2019-03-13 11:03
 */
public class LoginAndRedirectFilter extends FormAuthenticationFilter {

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		String loginUrl = getLoginUrl();
		loginUrl += "?redirectUrl=" + ((HttpServletRequest) request).getRequestURI();
		WebUtils.issueRedirect(request, response, loginUrl);
	}

}
