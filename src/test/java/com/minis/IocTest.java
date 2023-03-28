package com.minis;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class IocTest {

    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        ScyIocTestService scyIocTestService = (ScyIocTestService) context.getBean("scyIocTestService");
        scyIocTestService.hello();
    }
}
