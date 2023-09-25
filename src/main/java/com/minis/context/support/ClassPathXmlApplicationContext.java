package com.minis.context.support;

import java.util.ArrayList;
import java.util.List;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Context 负责整合容器的启动过程
 *   1. 创建 BeanFactory
 *   1. 读XML文件，从中解析出Bean定义
 *   3. 实例化这些 bean，并将它们注入到 IoC 容器中
 *
 * 功能:
 *   识别XML文件中的Bean定义，创建Bean，并放入IoC容器中管理
 *   支持XML配置方式或者注解的方式进行Bean的依赖注入
 *   构建 BeanFactory 体系
 *   容器应用上下文
 *   事件机制
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();


    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        setConfigLocations(fileName);

        if (isRefresh) {
            try {
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册 BeanFactoryPostProcessor
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
        System.out.println("ClassPathXmlApplicationContext try to postProcessBeanFactory");

        String[] beanNamesForType = getBeanFactory().getBeanNamesForType(BeanFactoryPostProcessor.class);
        for (String beanName : beanNamesForType) {
            System.out.println("ClassPathXmlApplicationContext postProcessBeanFactory : " + beanName);
            try {
                Object bean = getBeanFactory().getBean(beanName);
                if (bean instanceof BeanFactoryPostProcessor) {
                    this.beanFactoryPostProcessors.add((BeanFactoryPostProcessor) bean);
                }
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }

        // 执行 BeanFactoryPostProcessor，处理 BeanDefinition
        for (BeanFactoryPostProcessor processor : this.beanFactoryPostProcessors) {
            try {
                processor.postProcessBeanFactory(bf);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

}