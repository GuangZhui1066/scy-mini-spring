package com.minis;

public class ScyTestServiceImpl implements ScyTestService {

    /**
     * 构造器参数
     */
    private String name;
    private Integer age;

    /**
     * 类的无参构造器
     */
    public ScyTestServiceImpl() {
    }

    /**
     * 类的含参构造器
     */
    public ScyTestServiceImpl(String name, Integer age) {
        this.name = name;
        this.age = age;
        System.out.println("constructor: " + this.name + "," + this.age);
    }

    @Override
    public void sayHello() {
        System.out.println("hello! " + this.name + "," + this.age);
    }

}
