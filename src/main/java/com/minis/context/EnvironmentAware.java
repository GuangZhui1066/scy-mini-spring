package com.minis.context;

import com.minis.beans.factory.Aware;
import com.minis.core.env.Environment;

public interface EnvironmentAware extends Aware {

    void setEnvironment(Environment environment);

}
