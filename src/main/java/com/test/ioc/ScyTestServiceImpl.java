package com.test.ioc;

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
    private ScyBaseService scyBaseService;

    /**
     * 类的无参构造器
     */
    public ScyTestServiceImpl() {
        System.out.println("ScyTestService constructor. no arguments.");
    }

    /**
     * 类的含参构造器
     */
    public ScyTestServiceImpl(String name, Integer age) {
        this.name = name;
        this.age = age;
        System.out.println("ScyTestService constructor. arguments: " + this.name + "," + this.age);
    }

    /**
     * init 方法
     */
    public void initScyTestService() {
        this.setProperty1("after init-method: " + this.getProperty1());
        System.out.println("ScyTestService init-method done.");
    }

    @Override
    public void sayHello() {
        System.out.println("ScyTestService sayHello! " + this.name + "," + this.age);
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

    public ScyBaseService getScyBaseService() {
        return scyBaseService;
    }

    public void setScyBaseService(ScyBaseService scyBaseService) {
        this.scyBaseService = scyBaseService;
        System.out.println("ScyTestService setBaseService done.");
    }

}
