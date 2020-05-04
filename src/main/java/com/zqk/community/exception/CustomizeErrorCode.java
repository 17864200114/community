package com.zqk.community.exception;


public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND("你找的问题不见了，请返回首页");

    @Override
    public String getMessage() {
        return message;
    }

    private String message;


    CustomizeErrorCode(String message) {
        this.message = message;
    }
}
