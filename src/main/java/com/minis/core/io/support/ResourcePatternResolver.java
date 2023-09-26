package com.minis.core.io.support;

import com.minis.core.io.ResourceLoader;

// todo: 实现
public interface ResourcePatternResolver extends ResourceLoader {

    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    //Resource[] getResources(String var1) throws IOException;

}
