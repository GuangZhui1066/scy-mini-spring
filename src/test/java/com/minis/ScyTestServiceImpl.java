package com.minis;

public class ScyTestServiceImpl implements ScyTestService {

    /**
     * 构造器参数
     */
    private String name;
    private Integer age;

    /**
     * 成员属性
     */
    private String property1;
    private Integer property2;

    /**
     * 依赖的bean
     */
    private ScyBaseService baseService;

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

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public Integer getProperty2() {
        return property2;
    }

    public void setProperty2(Integer property2) {
        this.property2 = property2;
    }

    public ScyBaseService getBaseService() {
        return baseService;
    }

    public void setBaseService(ScyBaseService baseService) {
        this.baseService = baseService;
    }

}
