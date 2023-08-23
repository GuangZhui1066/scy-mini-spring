package com.test.controller;

import com.minis.web.bind.annotation.RequestMapping;

public class HelloWorldBean {

    @RequestMapping("/helloworld")
    public String doGet() {
        return "hello world!";
    }

    public String doPost() {
        return "hello world!";
    }

}
