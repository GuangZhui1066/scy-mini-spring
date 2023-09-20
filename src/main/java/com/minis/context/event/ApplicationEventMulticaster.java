package com.minis.context.event;

import com.minis.context.ApplicationEvent;
import com.minis.context.ApplicationListener;

public interface ApplicationEventMulticaster {

    /**
     * 添加事件监听者
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 移除事件监听者
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 发布事件
     */
    void multicastEvent(ApplicationEvent event);

}
