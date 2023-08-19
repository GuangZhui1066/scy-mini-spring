package com.minis.test.test2;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.iocTestClass.ScyTestServiceImpl;
import com.minis.web.bind.annotation.RequestMapping;

public class HelloScyBean {

    @Autowired
    private ScyTestServiceImpl scyTestService;

    @RequestMapping("/scy")
    public String doGet() {
        return "hello scy!" + scyTestService.getProperty1();
    }

    /**
     * 测试自动把 HTTP 请求中的字符串参数转换为结构体
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/scyParam?name=aa&age=25
     */
    @RequestMapping("/scyParam")
    public String doGet3(ScyParam scyParam) {
        return "hello! " + scyParam.getName() + ", " + scyParam.getAge();
    }

    /**
     * 测试用户自定义的参数类型 (Date) 转换器
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/scyDateParam?name=aa&age=25&birthday=1999-12-17
     */
    @RequestMapping("/scyDateParam")
    public String doGet4(ScyParam scyParam) {
        return "hello! " + scyParam.getName() + ", " + scyParam.getAge() + ", " + scyParam.getBirthday();
    }

}
