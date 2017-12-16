package com.jishi.reservation.service.enumPackage;

/**
 * Created by sloan on 2017/9/1.
 */
public enum DateEnum {

    MORNING(1,"上午"),
    AFTERNOON(2,"下午");

    private int code;
    private String timeInteval;

    DateEnum(int code,String timeInteval){
        this.code = code;
        this.timeInteval = timeInteval;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTimeInteval() {
        return timeInteval;
    }

    public void setTimeInteval(String timeInteval) {
        this.timeInteval = timeInteval;
    }
}
