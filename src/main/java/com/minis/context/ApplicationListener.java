package com.minis.context;

import java.util.EventListener;

/**
 * 事件监听器
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 监听到事件之后处理事件
     */
    void onApplicationEvent(E event);

}
