package com.minis;

public class ScyBaseService {

    private ScyCircleService circleService;

    public ScyBaseService() {
        System.out.println("ScyBaseService constructor. no arguments.");
    }

    public String baseHello() {
        return "hello, baseService success.";
    }

    public ScyCircleService getCircleService() {
        return circleService;
    }

    public void setCircleService(ScyCircleService circleService) {
        this.circleService = circleService;
        System.out.println("ScyBaseService setCircleService done.");
    }

}
