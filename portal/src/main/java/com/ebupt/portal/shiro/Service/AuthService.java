package com.ebupt.portal.shiro.Service;

import com.ebupt.portal.common.Results.JSONResult;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    JSONResult checkLogin(String username, String password, HttpServletRequest request);

}
