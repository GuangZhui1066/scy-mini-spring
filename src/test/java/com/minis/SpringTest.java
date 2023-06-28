package com.minis;

import java.util.Map;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class SpringTest {

    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        /**
         * 测试获取三个 bean
         */
        ScyTestServiceImpl scyTestService = (ScyTestServiceImpl) context.getBean("scyTestService");
        scyTestService.sayHello();
        System.out.println("scyTestService property1: " + scyTestService.getProperty1());
        System.out.println("scyTestService property2: " + scyTestService.getProperty2());

        ScyBaseService baseService = scyTestService.getScyBaseService();
        System.out.println(baseService.baseHello());

        ScyCircleService circleService = baseService.getScyCircleService();
        circleService.getScyTestService().sayHello();
    }

}
