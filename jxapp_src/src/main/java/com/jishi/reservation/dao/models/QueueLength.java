package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by liangxiong on 2017/11/15.
 */
@Table(name = "queue_length")
@ApiModel("门诊叫号的通知队列长度")
@Data
public class QueueLength {
    @Id
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("his医生id")
    private String doctorHisId;
    @ApiModelProperty("his部门id")
    private String departHisId;
    @ApiModelProperty("名称")
    private String doctorName;
    @ApiModelProperty("名称")
    private String departName;
    @ApiModelProperty("通知队列长度")
    private int length;
}
