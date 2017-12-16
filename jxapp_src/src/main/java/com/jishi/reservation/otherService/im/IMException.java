package com.jishi.reservation.otherService.im;

/**
 * Created by liangxiong on 2017/10/25.
 */
public class IMException extends Exception {

    private static final long serialVersionUID = 5830800504398577931L;
    private int code = -1;  //unknow error code
    private String url;

    public IMException(String message) {
        super(message);
    }

    public IMException(int code, String url, String message) {
        super(message);
        this.code = code;
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "IMException{" +
            "code=" + code +
            ", url='" + url +
            "'，message='" + getMessage() +
            "'}";
    }
}
