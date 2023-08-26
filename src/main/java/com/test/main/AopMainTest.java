package com.test.main;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;
import com.test.aop.service.ActionOne;
import com.test.aop.service.ActionOneImpl;
import com.test.aop.service.ActionTwo;
import com.test.aop.service.ActionTwoImpl;
import com.test.aop.dynamicProxy.DynamicProxyHelper;

public class AopMainTest {

    @Autowired
    private ActionOne actionOne;


    /**
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/aop/dynamicProxy?msg=scy
     */
    @RequestMapping("/aop/dynamicProxy")
    @ResponseBody
    public void doTest1(HttpServletRequest request, HttpServletResponse response) {
        String msg = request.getParameter("msg");

        ActionOne actionOne = new ActionOneImpl();
        DynamicProxyHelper proxyHelperOne = new DynamicProxyHelper(actionOne);
        ActionOne proxyOne = (ActionOne) proxyHelperOne.getProxy();
        String resultOne = proxyOne.doActionOne(msg);

        ActionTwo actionTwo = new ActionTwoImpl();
        DynamicProxyHelper proxyHelperTwo = new DynamicProxyHelper(actionTwo);
        ActionTwo proxyTwo = (ActionTwo) proxyHelperTwo.getProxy();
        String resultTwo = proxyTwo.doActionTwo(msg);

        try {
            String result = "one: " + resultOne + "\n" + "two: " + resultTwo;
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/aop/factoryBean?msg=scy
     */
    @RequestMapping("/aop/factoryBean")
    @ResponseBody
    public void doTest2(HttpServletRequest request, HttpServletResponse response) {
        String msg = request.getParameter("msg");
        String result = actionOne.doActionOne(msg);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
