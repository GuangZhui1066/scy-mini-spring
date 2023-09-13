package com.test.main;

import java.util.Map;

import com.minis.beans.BeansException;
import com.minis.context.event.ContextRefreshedEvent;
import com.minis.context.support.ClassPathXmlApplicationContext;
import com.test.ioc.ScyBaseService;
import com.test.ioc.ScyCircleService;
import com.test.ioc.ScyTestServiceImpl;
import com.test.ioc.listener.MyContextRefreshListener;
import com.test.ioc.threeLevelCache.ActionP;
import com.test.ioc.threeLevelCache.EctionQ;

public class SpringTest {

    public static void main(String[] args) throws BeansException {
        //ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

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
        MyContextRefreshListener myContextRefreshListener = new MyContextRefreshListener();
        // ClassPathXmlApplicationContext 在执行 refresh() 方法时，已经注册过一个事件监听器，这里再注册第二个监听器
        context.addApplicationListener(myContextRefreshListener);
        context.publishEvent(new ContextRefreshedEvent(context));


        /**
         * 测试环境
         */
        //context.setEnvironment();
        //context.getApplicationName();
        //context.getEnvironment();


        /**
         * 二级缓存不能解决有代理对象时的循环依赖问题
         *   actionP 和 ectionQ 循环依赖，其中 actionP 被代理
         *   当bean被加载完以后，上下文中的 actionP 是代理对象，但 ectionQ 中包含的 actionP 是真实对象
         *   所以会导致 actionP != ectionQ.getActionP()
         *
         *   这是因为在创建 actionP 时，会先将 actionP 的真实对象放入二级缓存，然后在为其填充 ectionQ 属性时，会去创建 ectionQ，
         *   创建 ectionQ 的过程中，会为 ectionQ 填充其 actionP 属性，这时会从二级缓存中拿出 actionP 的真实对象来为 ectionQ 的属性赋值
         */
        ActionP actionP = (ActionP) context.getBean("actionP");
        EctionQ ectionQ = (EctionQ) context.getBean("ectionQ");
        ActionP actionPInQ = ectionQ.getActionP();
        System.out.println(actionP == actionPInQ);

    }

}
