package com.minis.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

/**
 * JdbcTemplate
 *
 * 作用：
 *   将 JDBC 流程中固定的部分作为模板写死，，流程中可以变化的部分让子类重写
 */
public class JdbcTemplate {

    /**
     * 读写操作的数据源分离
     */
    private DataSource readDataSource;
    private DataSource writeDataSource;

    public JdbcTemplate() {
    }

    public DataSource getReadDataSource() {
        return readDataSource;
    }

    public void setReadDataSource(DataSource readDataSource) {
        this.readDataSource = readDataSource;
    }

    public DataSource getWriteDataSource() {
        return writeDataSource;
    }

    public void setWriteDataSource(DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }


    public Object query(StatementCallback stmtCallback) {
        Connection con = null;
        Statement stmt = null;

        try {
            con = readDataSource.getConnection();
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
            con = readDataSource.getConnection();
            pstmt = con.prepareStatement(sql);

            // 通过 argumentSetter 统一设置 sql 中的参数值
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);

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

    /**
     * 查询结果是多行数据
     */
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        RowMapperResultSetExtractor<T> resultExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 建立数据库连接
            con = readDataSource.getConnection();
            // 准备 sql 语句
            pstmt = con.prepareStatement(sql);
            // 为 sql 语句设置参数
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);
            // 执行 sql 语句
            rs = pstmt.executeQuery();

            // 把 sql 执行的结果集映射为对象列表，返回
            return resultExtractor.extractData(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception ignored) {}
        }

        return null;
    }

    /**
     * 更新：可以执行 insert update delete 这些写操作
     */
    public int update(String sql, Object[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = writeDataSource.getConnection();
            pstmt = con.prepareStatement(sql);

            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);

            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception ignored) {};
        }

        return 0;
    }

}

