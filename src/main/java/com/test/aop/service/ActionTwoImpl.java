package com.test.aop.service;

public class ActionTwoImpl implements ActionTwo {

    @Override
    public String doActionTwo(String msg) {
        return "Proxy.ActionTwoImpl. do action two: " + msg;
    }

}
