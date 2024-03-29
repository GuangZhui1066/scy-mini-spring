package com.minis.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 把 JDBC 返回的 ResultSet 数据集映射为一个对象集合
 */
public interface ResultSetExtractor<T> {

    T extractData(ResultSet rs) throws SQLException;

}
