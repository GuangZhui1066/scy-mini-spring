package com.test.aop.service;

public class ActionOneImpl implements ActionOne {

    @Override
    public String doActionOne(String msg) {
       return "Proxy.ActionOneImpl. do action one: " + msg;
    }

}
