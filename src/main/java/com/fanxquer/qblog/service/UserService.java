package com.fanxquer.qblog.service;

import com.fanxquer.qblog.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    public User getByName(String name);

    public int countByName(String name);

    public boolean isUserInSession(HttpServletRequest request);
}
