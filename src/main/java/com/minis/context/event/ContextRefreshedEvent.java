package com.minis.context.event;

import com.minis.context.ApplicationContext;

/**
 * 事件：上下文刷新完成
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    public ContextRefreshedEvent(ApplicationContext event) {
        super(event);
    }

    @Override
    public String toString() {
        return this.msg;
    }

}
