package com.minis.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * 为 PreparedStatement 中的参数赋值
 */
public class ArgumentPreparedStatementSetter {

    private final Object[] args; //参数数组

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    
    /**
     * 设置 SQL 参数
     */
    public void setValues(PreparedStatement pstmt) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                doSetValue(pstmt, i + 1, arg);
            }
        }
    }

    /**
     * 对某个参数，设置参数值
     */
    protected void doSetValue(PreparedStatement pstmt, int parameterPosition, Object argValue) throws SQLException {
        Object arg = argValue;
        if (arg instanceof String) {
            pstmt.setString(parameterPosition, (String) arg);
        }    else if (arg instanceof Integer) {
            pstmt.setInt(parameterPosition, (int) arg);
        }    else if (arg instanceof Date) {
            pstmt.setDate(parameterPosition, new java.sql.Date(((Date) arg).getTime()));
        }
    }

}
