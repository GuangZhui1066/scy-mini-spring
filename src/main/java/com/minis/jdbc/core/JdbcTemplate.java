package com.minis.jdbc.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;

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

    /**
     * 查询 PreparedStatement
     *
     *  Statement: 所有参数已经被赋值的 sql 语句
     *  PreparedStatement: sql 中的参数需要赋值
     */
    public Object query(String sql, Object[] args, PreparedStatementCallback pstmtcallback) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/MiniSpring?useSSL=false&useUniCode=true&characterEncoding=utf8&user=root&password=");

            pstmt = con.prepareStatement(sql);
            // 按照顺序为 sql 语句的参数赋值
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof String) {
                    pstmt.setString(i + 1, (String) arg);
                } else if (arg instanceof Integer) {
                    pstmt.setInt(i + 1, (int) arg);
                } else if (arg instanceof Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((Date) arg).getTime()));
                }
            }

            return pstmtcallback.doInPreparedStatement(pstmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception ignored) {};
        }

        return null;
    }

}

