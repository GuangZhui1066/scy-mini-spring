package com.minis.batis;

import java.util.List;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

public class DefaultSqlSession implements SqlSession {

    private JdbcTemplate jdbcTemplate;

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }


    @Override
    public Object selectOne(String sqlId, Object[] args, PreparedStatementCallback pstmtCallback) {
        String sql = this.sqlSessionFactory.getMapperNode(sqlId).getSql();
        System.out.println("sqlId: " + sqlId + ", sql: " + sql);

        return jdbcTemplate.query(sql, args, pstmtCallback);
    }

    @Override
    public List<Object> selectList(String sqlId, Object[] args, PreparedStatementCallback pstmtCallback) {
        return null;
    }

    @Override
    public int insert() {
        return 0;
    }

    @Override
    public int update() {
        return 0;
    }

}
