package com.test.aop.service;

import java.util.Date;

import com.minis.beans.factory.annotation.Autowired;
import com.test.jdbc.entity.User;
import com.test.jdbc.service.UserService;

public class ActionOneImpl implements ActionOne {

    @Autowired
    private UserService userService;

    @Override
    public String doActionOne(String msg) {
        User user = new User();
        user.setId(1);
        user.setName("update");
        user.setAge(1066);
        user.setBirthday(new Date());
        int ret = userService.updateUser(user);

        return "Proxy.ActionOneImpl. do action one: " + msg + ", updateResult:" + ret;
    }

    @Override
    public String doAnotherActionOne(String msg) {
        return "Proxy.ActionOneImpl. do another action one: " + msg;
    }

}
