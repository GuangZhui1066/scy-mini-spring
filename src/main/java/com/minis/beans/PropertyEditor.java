package com.minis.beans;

/**
 * 类型转换器
 *
 * 作用：
 *  在 String 和 Object 之间进行转换
 */
public interface PropertyEditor {

    /**
     * 接收一个 String，将其转换成某种类型
     */
    void setAsText(String text);

    /**
     * 返回转换后的值
     */
    Object getValue();

    void setValue(Object value);

    String getAsText();

}
