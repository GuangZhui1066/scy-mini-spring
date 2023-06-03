package com.minis;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class SpringTest {

    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        ScyTestServiceImpl scyIocTestService = (ScyTestServiceImpl) context.getBean("scyIocTestService");
        scyIocTestService.sayHello();
    }
}
