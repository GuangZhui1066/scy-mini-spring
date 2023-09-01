package com.test.aop.advice;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.minis.beans.factory.annotation.Autowired;

public class TransactionManager {

    @Autowired
    private DataSource writeDataSource;

    Connection conn = null;

    protected void doBegin() throws SQLException {
        conn = writeDataSource.getConnection();
        if (conn.getAutoCommit()) {
            conn.setAutoCommit(false);
        }
    }

    protected void doCommit() throws SQLException {
        conn.commit();
    }

    protected void doRollBack() throws SQLException {
        conn.rollback();
    }

}
