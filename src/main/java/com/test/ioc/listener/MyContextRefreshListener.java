package com.test.ioc.listener;

import com.minis.context.ApplicationListener;
import com.minis.context.event.ContextRefreshedEvent;

/**
 * 自定义的事件监听器：监听上下文刷新完成的事件
 */
public class MyContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println(".........ApplicationContext refreshed.........beans count : " + event.getApplicationContext().getBeanDefinitionCount());
    }

}
