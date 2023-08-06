package com.minis.test.test2;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.iocTestClass.ScyTestServiceImpl;
import com.minis.web.RequestMapping;

public class HelloScyBean {

    @Autowired
    private ScyTestServiceImpl scyTestService;

    @RequestMapping("/scy")
    public String doGet() {
        return "hello scy!" + scyTestService.getProperty1();
    }

}
