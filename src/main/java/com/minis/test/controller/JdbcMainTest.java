package com.minis.test.controller;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.jdbc.entity.User;
import com.minis.test.jdbc.service.UserService;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;

/**
 *
 * 注意：
 *   1. 需要在 out 目录中添加
 */
public class JdbcMainTest {

    @Autowired
    private UserService userService;


    @RequestMapping("/jdbc/callback")
    @ResponseBody
    public User doTest1() {
        User user = userService.getUserInfo(1);
        return user;
    }

    @RequestMapping("/jdbc/callback2")
    @ResponseBody
    public User doTest2() {
        User user = userService.getUserInfo2(1);
        return user;
    }

    /**
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded//jdbc/argSetter?name=scy&birthday=2022-08-15
     */
    @RequestMapping("/jdbc/argSetter")
    @ResponseBody
    public User doTest3(User param) {
        User user = userService.getUserInfo3(param.getName(), param.getBirthday());
        return user;
    }

}