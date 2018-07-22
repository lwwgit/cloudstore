package com.example.cloudstore.enums;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Author jitdc
 * @Date Create in 17:32 2018/7/5
 * @Description:
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResultStatus {
    /**
     * 1 开头为判断文件在系统的状态
     */
    IS_HAVE(100, "文件已存在！"),

    OUT_SPACE(104, "内存不足！"),

    NO_HAVE(101, "该文件没有上传过。"),

    ING_HAVE(102, "该文件上传了一部分。"),

    IS_ERROR(103,"发生错误");

    private final int value;

    private final String reasonPhrase;


    ResultStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
