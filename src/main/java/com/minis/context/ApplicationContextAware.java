package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.beans.factory.Aware;

/**
 * 获取 applicationContext
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
