package com.minis.http.converter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * 将 Controller 的返回值转换成字符串数据流
 */
public interface HttpMessageConverter {

    void write(Object obj, HttpServletResponse response) throws IOException;

}
