package com.minis.context;

import java.util.EventListener;

/**
 * 事件监听器
 */
public class ApplicationListener implements EventListener {

    /**
     * 监听到事件之后处理事件
     */
    void onApplicationEvent(ApplicationEvent event) {
        System.out.println("ApplicationListener handle event: " + event.toString());
    }

}
