package com.minis.web.bind.support;

import com.minis.web.bind.WebDataBinder;

/**
 * 初始化 WebDataBinder
 */
public interface WebBindingInitializer {

    void initBinder(WebDataBinder binder);

}
