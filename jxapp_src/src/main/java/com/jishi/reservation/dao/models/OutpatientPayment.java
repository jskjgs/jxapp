package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liangxiong on 2017/11/6.
 */
@Data
@Table(name = "outpatient_payment")
@ApiModel("门诊缴费信息")
public class OutpatientPayment {

    @Id
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("本系统账号id")
    private Long accountId;
    @ApiModelProperty("his病人id")
    private String brId;
    @ApiModelProperty("his挂号单号")
    private String registerNumber;
    @ApiModelProperty("本系统账单id")
    private Long orderId;
    @ApiModelProperty("本系统账单号")
    private String orderNumber;
    @ApiModelProperty("his单据号，可以有多个单据用','分隔")
    private String docmentId;
    @ApiModelProperty("his单据类型(1-交费单，4-挂号单)")
    private Integer docmentType;
    @ApiModelProperty("状态(0-未支付，1-已支付，2-已退款，3-已支付并与his确认)")
    private Integer status;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("支付时间")
    private Date payTime;

}
