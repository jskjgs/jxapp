package com.jishi.reservation.service.jinxin.bean;

import lombok.Data;

import java.util.Date;

/**
 * Created by liangxiong on 2017/11/10.
 */
@Data
public class QueueDetail {
    private String doctorId;  //医生ID
    private String departmentId;    //科室ID
    private String patientId;    //病人ID
    private String registerType;    //挂号类型
    private Date registerDate;
    private Integer currentNum;     //当前就诊人号码
    private Integer queueNum;       //本病人就诊号码
    private Integer waitingNum;    //需等待的人数
    private Integer state;         //当前状态（0-已预约，1-等待中，2-正在就诊，3-已过号）
}
