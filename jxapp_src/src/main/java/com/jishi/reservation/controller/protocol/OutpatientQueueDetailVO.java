package com.jishi.reservation.controller.protocol;

import lombok.Data;

import java.util.Date;

/**
 * Created by liangxiong on 2017/11/10.
 */
@Data
public class OutpatientQueueDetailVO {
    private Long accountId;   //add by csr，账号id ，用它和brid来唯一确定病人
    private Long doctorId;        //医生ID
    private String doctorHisId;   //医生His ID
    private String doctorName;    //医生姓名
    private String doctorTitle;   //职称
    private Long departId;        //科室ID
    private String departName;    //科室名称
    private String registerType;  //挂号类型
    private Date registerDate;    //挂号日期
    private String brId;            //病人ID
    private String name;          //病人姓名
    private String queueInfo;    //病人排队信息
    private String queueeMinder;  //病人排队提醒
    private Integer currentNum;     //当前就诊人号码
    private Integer queueNum;       //本病人就诊号码
    private Integer needWaitNum;    //需等待的人数
    private Integer status;         //当前状态（0-已预约，1-等待中，2-正在就诊，3-已过号）
}
