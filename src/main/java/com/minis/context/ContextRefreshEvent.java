package com.minis.context;

public class ContextRefreshEvent extends ApplicationEvent {

    public ContextRefreshEvent(Object event) {
        super(event);
    }

    @Override
    public String toString() {
        return this.msg;
    }

}
