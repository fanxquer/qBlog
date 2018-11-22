package com.fanxquer.qblog.service.impl;

import com.fanxquer.qblog.dao.UserMapper;
import com.fanxquer.qblog.entity.User;
import com.fanxquer.qblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getByName(String name) {
        return userMapper.getByName(name);
    }

    @Override
    public int countByName(String name) {
        return userMapper.countByName(name);
    }

    @Override
    public boolean isUserInSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");

        if(user == null)
            return false;
        else
            return true;
    }
}
