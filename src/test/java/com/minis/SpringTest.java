package com.minis;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class SpringTest {

    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        ScyTestServiceImpl scyTestService = (ScyTestServiceImpl) context.getBean("scyTestService");
        scyTestService.sayHello();
        System.out.println(scyTestService.getProperty1());
        System.out.println(scyTestService.getProperty2());
        System.out.println(scyTestService.getBaseService().baseHello());
    }

}
