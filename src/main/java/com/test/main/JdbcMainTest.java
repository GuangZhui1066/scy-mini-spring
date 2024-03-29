package com.test.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;
import com.test.jdbc.entity.User;
import com.test.jdbc.service.UserService;

public class JdbcMainTest {

    @Autowired
    private UserService userService;


    @RequestMapping("/jdbc/callback")
    @ResponseBody
    public User doTest1() {
        User user = userService.getUserInfo(1);
        return user;
    }

    @RequestMapping("/jdbc/callback2")
    @ResponseBody
    public User doTest2() {
        User user = userService.getUserInfo2(1);
        return user;
    }

    /**
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/jdbc/argSetter?name=scy&birthday=2022-08-15
     */
    @RequestMapping("/jdbc/argSetter")
    @ResponseBody
    public User doTest3(User param) {
        User user = userService.getUserInfo3(param.getName(), param.getBirthday());
        return user;
    }

    /**
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/jdbc/resultSet?idStart=0
     */
    @RequestMapping("/jdbc/resultSet")
    @ResponseBody
    public List<User> doTest4(HttpServletRequest request, HttpServletResponse response) {
        int idStart = Integer.parseInt(request.getParameter("idStart"));
        List<User> users = userService.getUserList(idStart);
        return users;
    }

    /**
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/jdbc/mybatis?id=1
     */
    @RequestMapping("/jdbc/mybatis")
    @ResponseBody
    public User doTest5(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = userService.getUserById(id);
        return user;
    }

    /**
     * 访问：http://localhost:8080/scy_mini_spring_war_exploded/jdbc/update?id=1&name=scy22&age=222&birthday=2022-08-22
     */
    @RequestMapping("/jdbc/update")
    @ResponseBody
    public Integer doTest6(User user) {
        return userService.updateUser(user);
    }

}
