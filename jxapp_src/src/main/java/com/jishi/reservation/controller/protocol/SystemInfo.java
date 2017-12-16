package com.jishi.reservation.controller.protocol;

import lombok.Data;

import java.util.Date;

/**
 * Created by liangxiong on 2017/11/10.
 */
@Data
public class SystemInfo {
    private Date date;
    private String ServerLocalIp;
    private String ServerWanIp;
}
