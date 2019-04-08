package com.ebupt.portal.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class EBAuthenticationFilter extends PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 获取主体
        Subject subject = getSubject(request, response);

        // 获取请求路径
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        if (uri.startsWith("/"))
            uri = uri.substring(1);
        if (uri.endsWith("/"))
            uri = uri.substring(0, uri.length() - 2);

        if ("favicon.ico".equals(uri))
            return true;

        uri = req.getMethod().toUpperCase() + ":" + uri;

        // 判断是否有权访问
        return subject.isPermitted(uri);
    }
}
