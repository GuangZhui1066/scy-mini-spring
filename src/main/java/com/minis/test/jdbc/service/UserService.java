package com.minis.test.jdbc.service;

import java.sql.ResultSet;
import java.util.Date;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.test.jdbc.entity.User;

public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public User getUserInfo(int id) {
        final String sql = "select id, name, age, birthday from user where id = " + id;

        return (User) jdbcTemplate.query(
            (stmt) -> {
                ResultSet rs = stmt.executeQuery(sql);
                User user = null;
                if (rs.next()) {
                    user = new User();
                    user.setId(id);
                    user.setName(rs.getString("name"));
                    user.setAge(rs.getInt("age"));
                    user.setBirthday(new Date(rs.getDate("birthday").getTime()));
                }
                return user;
            }
        );
    }

}
