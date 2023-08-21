package com.minis.jdbc.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * JdbcTemplate
 *
 * 作用：
 *   将 JDBC 流程中固定的部分作为模板写死，，流程中可以变化的部分让子类重写
 */
public class JdbcTemplate {

    public JdbcTemplate() {
    }

    public Object query(StatementCallback stmtCallback) {
        Connection con = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/MiniSpring?useSSL=false&useUniCode=true&characterEncoding=utf8&user=root&password=");

            stmt = con.createStatement();
            return stmtCallback.doInStatement(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception ignored) {};
        }

        return null;
    }

}

