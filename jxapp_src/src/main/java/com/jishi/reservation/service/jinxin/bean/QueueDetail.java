package com.jishi.reservation.service.jinxin.bean;

import lombok.Data;

import java.util.Date;

/**
 * Created by liangxiong on 2017/11/10.
 */
@Data
public class QueueDetail {
    private String YSID;  //医生ID
    private String YS;    //医生姓名
    private String ZC;    //职称
    private Long KSID;    //科室ID
    private String KSMC;  //科室名称
    private String HL;    //挂号类型
    private Date RQ;      //挂号日期
    private String BRID;    //病人ID
    private String BR;    //病人姓名
    private String BRPD;  //病人排队信息
    private String BRPDTX;//病人排队提醒
}
