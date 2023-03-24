package com.minis;

import com.minis.context.ClassPathXmlApplicationContext;

public class IocTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        ScyIocTestService scyIocTestService = (ScyIocTestService) context.getBean("scyIocTestService");
        scyIocTestService.hello();
    }
}
