package com.minis.util;

/**
 * 对象转换器：把对象转换为字符串
 */
public interface ObjectMapper {

    void setDateFormat(String dateFormat);

    void setDecimalFormat(String decimalFormat);

    /**
     * 将对象转为字符串
     */
    String writeValuesAsString(Object obj);

}
