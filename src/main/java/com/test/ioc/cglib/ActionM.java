package com.test.ioc.cglib;

import com.minis.beans.factory.annotation.Autowired;

public class ActionM {

    @Autowired
    private EctionN ectionN;

    public EctionN getEctionN() {
        return ectionN;
    }

}
