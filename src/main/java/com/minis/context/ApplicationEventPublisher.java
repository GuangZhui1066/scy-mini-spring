package com.minis.context;

/**
 * 发布事件的接口
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

}
