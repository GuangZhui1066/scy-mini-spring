package com.minis.jdbc.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * JdbcTemplate
 *
 * 作用：
 *   将 JDBC 流程中固定的部分作为模板写死，，流程中可以变化的部分让子类重写
 */
public abstract class JdbcTemplate {

    public JdbcTemplate() {
    }

    public Object query(String sql) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Object returnObj = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/MiniSpring?useSSL=false&useUniCode=true&characterEncoding=utf8&user=root&password=");
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            // 处理sql返回的数据，具体逻辑由子类实现
            returnObj = doInStatement(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (Exception ignored) {};
        }

        return returnObj;
    }

    /**
     * 模版方法
     */
    protected abstract Object doInStatement(ResultSet rs);

}

