package com.minis.context;

import com.minis.beans.BeansException;

/**
 * 获取 applicationContext
 */
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
