package com.lilemy.clientsdk.model.enums;

import lombok.Getter;

/**
 * 四则运算符
 */
@Getter
public enum OperationEnum {

    SUM("加法", "sum"),
    SUBTRACT("减法", "subtract"),
    MULTIPLY("乘法", "multiply"),
    DIVIDE("除法", "divide");

    private final String text;

    private final String value;

    OperationEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

}
