package com.test.jdbc.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.minis.jdbc.core.JdbcTemplate;
import com.test.jdbc.entity.User;

/**
 * 查询 user 表的 JDBC 实现
 */
public class UserJdbcImpl extends JdbcTemplate {

    //@Override
    protected Object doInStatement(ResultSet rs) {
        User user = null;

        try {
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                user.setBirthday(new Date(rs.getDate("birthday").getTime()));
            } else {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

}
