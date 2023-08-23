package com.minis.batis;

/**
 * SqlSession 的工厂，用于创建 SqlSession
 */
public interface SqlSessionFactory {

    SqlSession openSession();

    MapperNode getMapperNode(String name);

}
