package com.jishi.reservation.otherService.im.neteasy.response;

import lombok.Data;

/**
 * Created by liangxiong on 2017/10/25.
 */
@Data
public class ResComm {
    private int code;
    private String desc;

    public boolean isSuccess() {
        return code == 200;
    }
}
