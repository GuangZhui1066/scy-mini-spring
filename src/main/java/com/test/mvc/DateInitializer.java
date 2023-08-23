package com.test.mvc;

import java.util.Date;

import com.minis.web.bind.WebDataBinder;
import com.minis.web.bind.support.WebBindingInitializer;

/**
 * 初始化 WebDataBinder: 为 WebDataBinder 注册自定义的 Date 类型转换器
 */
public class DateInitializer implements WebBindingInitializer {

    @Override
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class,"yyyy-MM-dd", false));
    }

}
