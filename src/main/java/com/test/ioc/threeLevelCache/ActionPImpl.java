package com.test.ioc.threeLevelCache;

import com.minis.beans.factory.annotation.Autowired;

/**
 * 被代理
 */
public class ActionPImpl implements ActionP {

    @Autowired
    private EctionQ ectionQ;

    @Override
    public EctionQ getEctionQ() {
        return ectionQ;
    }

}
