package com.minis.context.support;

import java.util.ArrayList;
import java.util.List;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.context.ApplicationEvent;
import com.minis.context.ApplicationEventPublisher;
import com.minis.context.ApplicationListener;
import com.minis.context.event.ContextRefreshedEvent;
import com.minis.context.event.SimpleApplicationEventPublisher;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

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
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    DefaultListableBeanFactory beanFactory;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();


    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = defaultListableBeanFactory;

        if (isRefresh) {
            try {
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }


    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }


    /**
     * 事件机制
     */
    @Override
    public void registerListeners() {
        String[] beanNamesForType = this.beanFactory.getBeanNamesForType(ApplicationListener.class);
        for (String beanName : beanNamesForType) {
            try {
                Object bean = this.beanFactory.getBean(beanName);
                if (bean instanceof ApplicationListener) {
                    this.getApplicationEventPublisher().addApplicationListener((ApplicationListener<?>) bean);
                }
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }


    /**
     * 注册 BeanFactoryPostProcessor
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
        System.out.println("ClassPathXmlApplicationContext try to postProcessBeanFactory");

        String[] beanNamesForType = this.beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class);
        for (String beanName : beanNamesForType) {
            System.out.println("ClassPathXmlApplicationContext postProcessBeanFactory : " + beanName);
            try {
                Object bean = this.beanFactory.getBean(beanName);
                if (bean instanceof BeanFactoryPostProcessor) {
                    this.beanFactoryPostProcessors.add((BeanFactoryPostProcessor) (this.beanFactory.getBean(beanName)));
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

    /**
     * 注册所有 BeanPostProcessor
     *
     * 注意：
     *   AutowiredAnnotationBeanPostProcessor 要在 BeanNameAutoProxyCreator 之前注册 (application.xml 中 AutowiredAnnotationBeanPostProcessor 的 bean 配置在前面)
     *   否则如果在需要被代理的 bean 中使用了 @Autowired (比如 ActionOneImpl 中的 user 属性)，那么这个 @Autowired 的属性就不会被注入。
     *   因为如果被代理的 bean (actionOne) 先被 BeanNameAutoProxyCreator 处理，那么注入到 BeanFactory 中的 bean 就会被替换为代理对象 ($Proxy)；
     *   等 AutowiredAnnotationBeanPostProcessor 再处理的时候，无法在代理对象 ($Proxy) 中找到这个需要被注入的属性 (user)
     *
     *   可以用 BeanPostProcessor 的 order 属性控制加载顺序
     */
    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        System.out.println("ClassPathXmlApplicationContext try to registerBeanPostProcessors");

        String[] beanNamesForType = this.beanFactory.getBeanNamesForType(BeanPostProcessor.class);
        for (String beanName : beanNamesForType) {
            System.out.println("ClassPathXmlApplicationContext registerBeanPostProcessors : " + beanName);
            try {
                Object bean = this.beanFactory.getBean(beanName);
                if (bean instanceof BeanPostProcessor) {
                    this.beanFactory.addBeanPostProcessor((BeanPostProcessor) (this.beanFactory.getBean(beanName)));
                }
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

}