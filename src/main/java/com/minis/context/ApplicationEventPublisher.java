package com.minis.context;

/**
 * 发布事件的接口
 */
public interface ApplicationEventPublisher {

    /**
     * 发布事件
     */
    void publishEvent(ApplicationEvent event);

    /**
     * 添加事件监听器
     */
    void addApplicationListener(ApplicationListener listener);

}
