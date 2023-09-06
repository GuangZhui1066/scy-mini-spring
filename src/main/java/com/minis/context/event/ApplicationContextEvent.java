package com.minis.context.event;

import com.minis.context.ApplicationContext;
import com.minis.context.ApplicationEvent;

/**
 * 上下文 (ApplicationContext) 相关事件
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }

}
