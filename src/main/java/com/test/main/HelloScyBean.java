package com.test.main;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;
import com.minis.web.servlet.ModelAndView;
import com.test.ioc.ScyTestServiceImpl;
import com.test.mvc.entity.ScyParam;
import com.test.mvc.entity.ScyReturnObj;

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

    /**
     * 测试
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/scyReturnObj?name=aa&age=25&birthday=1999-12-17
     */
    @RequestMapping("/scyReturnObj")
    @ResponseBody
    public ScyReturnObj doGet5(ScyParam scyParam) {
        ScyReturnObj returnObj = new ScyReturnObj();
        returnObj.setName(scyParam.getName());
        returnObj.setPrice(10.66 * scyParam.getAge());
        returnObj.setTime(scyParam.getBirthday());
        return returnObj;
    }

    /**
     * 测试 test.jsp 页面
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/scyMavTest?name=scyscy
     */
    @RequestMapping("/scyMavTest")
    public ModelAndView doGet6(ScyParam scyParam) {
        ModelAndView mav = new ModelAndView("test", "msg", scyParam.getName());
        return mav;
    }

    /**
     * 测试 error.jsp 页面
     */
    @RequestMapping("/scyMavError")
    public String doGet7() {
        return "error";
    }

}
