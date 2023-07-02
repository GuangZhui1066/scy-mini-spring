package com.minis.test;

import com.minis.web.RequestMapping;

public class HelloWorldBean {

    @RequestMapping("/helloworld")
    public String doGet() {
        return "hello world!";
    }

    public String doPost() {
        return "hello world!";
    }

}
