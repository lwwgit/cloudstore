package com.example.cloudstore.enums;




public enum VipStatusEnum implements CodeEnum {
    NEW(0, "VIP会员"),
    FINISHED(1, "普通用户"),
    ;

    private Integer code;

    private String message;

    VipStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
