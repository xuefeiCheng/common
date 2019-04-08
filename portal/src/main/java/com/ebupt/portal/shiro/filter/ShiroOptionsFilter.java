package com.ebupt.portal.shiro.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*", filterName = "shiroOptionsFilter")
public class ShiroOptionsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 允许哪些Origin发起跨域请求
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许请求的方法
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,DELETE");
        // 缓存时间，该时间内不需要再发送预检请求
        response.setHeader("Access-Control-Max-Age", "3600");
        // 允许跨域请求包含头部信息
        response.setHeader("Access-Control-Allow-Headers", "token,Origin,X-Requested-With,Content-Type,Accept");
        // 允许浏览器携带用户身份信息(cookie)
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
