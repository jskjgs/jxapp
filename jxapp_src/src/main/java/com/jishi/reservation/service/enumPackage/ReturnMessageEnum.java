package com.jishi.reservation.service.enumPackage;

/**
 * Created by zbs on 2017/8/10.
 */

public enum ReturnMessageEnum {

    FILE_NOT_FIX(1001, "图片格式不符合，请上传jpg,bmp,png，且大小不超过1M的图片");


    private int code;
    private String message;

    ReturnMessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
