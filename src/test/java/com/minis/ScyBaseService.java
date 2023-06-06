package com.minis;

public class ScyBaseService {

    private ScyCircleService scyCircleService;

    public ScyBaseService() {
        System.out.println("ScyBaseService constructor. no arguments.");
    }

    public String baseHello() {
        return "hello, baseService success.";
    }

    public ScyCircleService getScyCircleService() {
        return scyCircleService;
    }

    public void setScyCircleService(ScyCircleService scyCircleService) {
        this.scyCircleService = scyCircleService;
        System.out.println("ScyBaseService setCircleService done.");
    }

}
