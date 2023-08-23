package com.minis.batis;

import java.util.List;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

/**
 * 用于执行数据库语句，并返回执行结果
 */
public interface SqlSession {

    /**
     * 查询一条数据
     */
    Object selectOne(String sqlId, Object[] args, PreparedStatementCallback pstmtCallback);

    /**
     * 查询多行数据
     */
    List<Object> selectList(String sqlId, Object[] args, PreparedStatementCallback pstmtCallback);

    /**
     * 插入
     */
    int insert();

    /**
     * 更新
     */
    int update();


    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);

}
