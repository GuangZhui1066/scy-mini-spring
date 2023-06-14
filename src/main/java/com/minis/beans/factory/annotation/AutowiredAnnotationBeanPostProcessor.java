package com.minis.beans.factory.annotation;

import java.lang.reflect.Field;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanPostProcessor;

/**
 * bean处理器，处理具有部分属性被 @Autowired 修饰的 bean
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            // 对bean的每一个属性进行判断，是否带有 @Autowired 注解
            for (Field field : fields) {
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                // 处理带有 @Autowired 注解的属性
                if (isAutowired) {
                    // 根据属性名查找同名的 bean
                    String fieldName = field.getName();
                    Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                    // 将这个bean注入此属性
                    try {
                        field.setAccessible(true);
                        field.set(bean, autowiredObj);
                        System.out.println("autowire " + fieldName + " for bean " + beanName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}

