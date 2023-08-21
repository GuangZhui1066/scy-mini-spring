package com.minis.test.jdbc.service;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.test.jdbc.entity.User;

public class UserService {

    public User getUserInfo(int id) {
        String sql = "select id, name, age, birthday from user where id = " + id;
        JdbcTemplate jdbcTemplate = new UserJdbcImpl();
        User user = (User) jdbcTemplate.query(sql);
        return user;
    }

}
