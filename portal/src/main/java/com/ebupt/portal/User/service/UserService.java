package com.ebupt.portal.User.service;

import com.ebupt.portal.User.entity.User;
import com.ebupt.portal.common.Results.JSONResult;

public interface UserService {

    JSONResult findAll();

    JSONResult findByName(String name);

    JSONResult update(User user);

    JSONResult deleteById(int id);
}
