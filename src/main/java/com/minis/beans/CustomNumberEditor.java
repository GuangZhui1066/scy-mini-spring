package com.minis.beans;

import java.text.NumberFormat;

import com.minis.util.NumberUtils;
import com.minis.util.StringUtils;

/**
 * 数字转换器：把字符串转换成数字类型
 */
public class CustomNumberEditor implements PropertyEditor {

    // 数据类型
    private Class<? extends Number> numberClass;

    // 指定格式
    private NumberFormat numberFormat;

    private boolean allowEmpty;

    private Object value;

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException {
        this(numberClass, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }


    /**
     * 将一个字符串转换成数字类型
     */
    @Override
    public void setAsText(String text) {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            setValue(null);
        }
        else if (this.numberFormat != null) {
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        }
        else {
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.value = (NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass));
        }
        else {
            this.value = value;
        }
    }

    /**
     * 将 number 转换成字符串
     */
    @Override
    public String getAsText() {
        Object value = this.value;
        if (value == null) {
            return "";
        }
        if (this.numberFormat != null) {
            return this.numberFormat.format(value);
        }
        else {
            return value.toString();
        }
    }

    @Override
    public Object getValue() {
        return this.value;
    }

}
