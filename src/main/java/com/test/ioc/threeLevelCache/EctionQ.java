package com.test.ioc.threeLevelCache;

import com.minis.beans.factory.annotation.Autowired;

public class EctionQ {

    @Autowired
    private ActionP actionP;

    public ActionP getActionP() {
        return actionP;
    }

}
