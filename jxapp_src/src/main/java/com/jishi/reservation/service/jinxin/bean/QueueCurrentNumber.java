package com.jishi.reservation.service.jinxin.bean;

import lombok.Data;

import java.util.Date;

/**
 * Created by liangxiong on 2017/11/14.
 */
@Data
public class QueueCurrentNumber {
    private String departHisId;
    private String doctorHisId;
    private int currNum;
    private Date time;
}
