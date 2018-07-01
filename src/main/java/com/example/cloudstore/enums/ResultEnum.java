package com.example.cloudstore.enums;


public enum ResultEnum {
    SUCCESS(0, "成功"),

    USER_IS_EXIT(1, "用户名已存在"),


    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
