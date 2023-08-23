package com.test.main;

import java.util.Map;

import com.minis.beans.BeansException;
import com.minis.context.ApplicationListener;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.context.ContextRefreshEvent;
import com.test.ioc.ScyBaseService;
import com.test.ioc.ScyCircleService;
import com.test.ioc.ScyTestServiceImpl;

public class SpringTest {

    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        /**
         * 测试获取 bean
         */
        ScyTestServiceImpl scyTestService = (ScyTestServiceImpl) context.getBean("scyTestService");
        scyTestService.sayHello();
        System.out.println("scyTestService property1: " + scyTestService.getProperty1());
        System.out.println("scyTestService property2: " + scyTestService.getProperty2());

        ScyBaseService baseService = scyTestService.getScyBaseService();
        System.out.println(baseService.baseHello());

        ScyCircleService circleService = baseService.getScyCircleService();
        circleService.getScyTestService().sayHello();


        /**
         * 测试 bean 工厂功能
         */
        Boolean test11 = context.containsBean("scyBaseService");
        Boolean test12 = context.containsSingleton("scyBaseService");
        Boolean test13 = context.containsBeanDefinition("scyBaseService");

        Boolean test2 = context.isPrototype("scyBaseService");
        Boolean test3 = context.isSingleton("scyBaseService");

        int test4 = context.getBeanDefinitionCount();
        String[] test5 = context.getBeanDefinitionNames();
        String[] test6 = context.getSingletonNames();

        // 测试 getBeansOfType() getType() 方法。有 bug
        Class<?> test7 = context.getType("scyBaseService");
        Map<String, ScyTestServiceImpl> test8 = context.getBeansOfType(ScyTestServiceImpl.class);

        //context.registerDependentBean();
        //context.getDependenciesForBean();
        //context.getDependentBeans();


        /**
         * 测试事件机制
         */
        ApplicationListener applicationListener = new ApplicationListener();
        // ClassPathXmlApplicationContext 在执行 refresh() 方法时，已经注册过一个事件监听器，这里再注册第二个监听器
        context.addApplicationListener(applicationListener);
        context.publishEvent(new ContextRefreshEvent("scy test event"));


        /**
         * 测试环境
         */
        //context.setEnvironment();
        //context.getApplicationName();
        //context.getEnvironment();

    }

}
