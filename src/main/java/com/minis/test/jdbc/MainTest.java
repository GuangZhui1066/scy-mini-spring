package com.minis.test.jdbc;

import com.minis.test.jdbc.entity.User;
import com.minis.test.jdbc.service.UserService;

public class MainTest {

    public static void main(String[] args) {
        UserService userService = new UserService();
        User user = userService.getUserInfo(1);
        System.out.println(user.getId() + user.getName() + user.getAge() + user.getBirthday());
    }
}
