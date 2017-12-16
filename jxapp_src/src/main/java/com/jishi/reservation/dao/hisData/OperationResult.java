package com.jishi.reservation.dao.hisData;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zbs on 2017/9/21.
 */
@Data
public class OperationResult implements Serializable {
    private static final long serialVersionUID = -5939599230753662529L;

    public boolean succeed;
    public String msg;

    public OperationResult() {
    }

    public OperationResult(boolean succeed, String msg) {
        this.succeed = succeed;
        this.msg = msg;
    }

    //set get

    @Override
    public String toString() {
        return "OperationResult{" +
                "succeed=" + succeed +
                ", msg='" + msg + '\'' +
                '}';
    }
}
