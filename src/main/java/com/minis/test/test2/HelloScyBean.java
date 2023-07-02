package com.minis.test.test2;

import com.minis.web.RequestMapping;

public class HelloScyBean {

    @RequestMapping("/scy")
    public String doGet() {
        return "hello scy!";
    }

}
