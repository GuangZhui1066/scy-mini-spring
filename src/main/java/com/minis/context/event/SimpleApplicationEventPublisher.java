package com.minis.context.event;

import java.util.ArrayList;
import java.util.List;

import com.minis.context.ApplicationEvent;
import com.minis.context.ApplicationEventPublisher;
import com.minis.context.ApplicationListener;

/**
 * 实现一个简单的事件发布者
 */
public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {

    List<ApplicationListener> listeners = new ArrayList<>();

    @Override
    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.listeners.add(listener);
    }

}
