package com.minis.context.support;

public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext {

    private String[] configLocations;

    public AbstractRefreshableConfigApplicationContext() {
    }


    protected String[] getConfigLocations() {
        return this.configLocations;
    }

    public void setConfigLocations(String... locations) {
        if (locations != null) {
            this.configLocations = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
                this.configLocations[i] = locations[i];
            }
        }
        else {
            this.configLocations = null;
        }
    }

}
