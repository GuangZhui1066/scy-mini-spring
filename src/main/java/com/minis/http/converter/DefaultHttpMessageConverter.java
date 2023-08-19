package com.minis.http.converter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.minis.util.ObjectMapper;

/**
 * HttpMessageConverter 的默认实现，将 Controller 返回的 Object 转为 JSON
 */
public class DefaultHttpMessageConverter implements HttpMessageConverter {

    String defaultContentType = "text/json;charset=UTF-8";

    String defaultCharacterEncoding = "UTF-8";

    ObjectMapper objectMapper;

    public DefaultHttpMessageConverter() {
    }


    @Override
    public void write(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType(defaultContentType);
        response.setCharacterEncoding(defaultCharacterEncoding);
        writeInternal(obj, response);
        response.flushBuffer();
    }

    private void writeInternal(Object obj, HttpServletResponse response) throws IOException{
        String sJsonStr = this.objectMapper.writeValuesAsString(obj);
        PrintWriter pw = response.getWriter();
        pw.write(sJsonStr);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
