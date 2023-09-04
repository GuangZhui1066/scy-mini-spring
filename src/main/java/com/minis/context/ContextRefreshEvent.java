package com.minis.context;

/**
 * 事件：上下文刷新完成
 */
public class ContextRefreshEvent extends ApplicationContextEvent {

    public ContextRefreshEvent(ApplicationContext event) {
        super(event);
    }

    @Override
    public String toString() {
        return this.msg;
    }

}
