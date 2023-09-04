package com.minis.context;

import java.util.EventObject;

/**
 * 事件
 */
public abstract class ApplicationEvent extends EventObject {

    protected String msg = null;

    public ApplicationEvent(Object event) {
        super(event);
        this.msg = event.toString();
    }

}
