package com.ebupt.portal.User.service.impl;

import com.ebupt.portal.User.entity.User;
import com.ebupt.portal.User.repository.TUserRepository;
import com.ebupt.portal.User.service.UserService;
import com.ebupt.portal.common.Results.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final TUserRepository userRepository;

    @Autowired
    public UserServiceImpl(TUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public JSONResult findAll() {
        List<User> list = this.userRepository.findAll();
        return JSONResult.OK(list);
    }

    @Override
    public JSONResult findByName(String name) {
        User user = this.userRepository.findByName(name);
        return JSONResult.OK(user);
    }

    @Override
    public JSONResult update(User user) {
        this.userRepository.save(user);
        return JSONResult.OK();
    }

    @Override
    public JSONResult deleteById(int id) {
        this.userRepository.deleteById(id);
        return JSONResult.OK();
    }
}
