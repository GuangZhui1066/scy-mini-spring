package com.minis.test.jdbc;

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

}
