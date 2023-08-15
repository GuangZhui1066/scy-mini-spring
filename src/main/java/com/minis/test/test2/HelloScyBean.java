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

    /**
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/scyParam?name=aa&age=25
     */
    @RequestMapping("/scyParam")
    public String doGet3(ScyParam scyParam) {
        return "hello! " + scyParam.getName() + ", " + scyParam.getAge();
    }

}
