package com.test.ioc.cglib;

import com.minis.beans.factory.annotation.Autowired;

public class EctionN {

    @Autowired
    private ActionM actionM;

    public ActionM getActionM() {
        return actionM;
    }

}
