package com.ebupt.portal.canyon.common.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域预检请求过滤器
 *
 * @author chy
 * @date 2019-03-18 18:53
 */
@Component
@WebFilter(urlPatterns = "/*")
public class OptionsFilter implements Filter {

	private static final String CROS_OPTIONS = "OPTIONS";

	@Value("${cros.allow:*}")
	private String allowOrigin;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String origin = req.getHeader("Origin");
		String[] allows = allowOrigin.split(",");
		boolean allowCros = false;
		for (String allow: allows) {
			if (allow.equals(origin) || "*".equals(allow)) {
				allowCros = true;
				break;
			}
		}

		if (allowCros) {
			// 允许哪些Origin发起跨域请求
			resp.setHeader("Access-Control-Allow-Origin", origin);
			// 允许请求的方法
			resp.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,PUT,DELETE");
			// 缓存时间，该时间内不需要再发送预检请求
			resp.setHeader("Access-Control-Max-Age", "3600");
			// 允许跨域请求包含头部信息
			resp.setHeader("Access-Control-Allow-Headers", "token,Origin,X-Requested-With,Content-Type,Accept");
			// 允许浏览器携带用户身份信息(cookie)
			resp.setHeader("Access-Control-Allow-Credentials", "true");
		}

		// 跨域处理
		if (CROS_OPTIONS.equals(req.getMethod()) && allowCros) {
			resp.setStatus(200);
			return;
		}

		chain.doFilter(request, response);
	}

}
