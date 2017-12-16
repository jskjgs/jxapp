package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sloan on 2017/9/6.
 */

@Data
@ApiModel("预约成功返回对象")
public class RegisterCompleteVO {
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("预约id")
    private Long registerId;
    @ApiModelProperty("科室")
    private String department;
    @ApiModelProperty("医生名称")
    private String doctor;
    @ApiModelProperty("病人名称")
    private String patient;
    @ApiModelProperty("地点")
    private String position;
    @ApiModelProperty("预约时间")
    private Date agreeTime;
    @ApiModelProperty("时间段")
    private String timeInterval;
    private Integer payType;  //支付方式
    @ApiModelProperty("支付金额 真实价格")
    private BigDecimal price;  //支付金额
    @ApiModelProperty("支付时间")
    private Date payTime;       //支付时间
    @ApiModelProperty("完成时间")
    private Date completeTime;  //完成时间
    @ApiModelProperty("订单编号")
    private String orderCode;     //订单编号
    @ApiModelProperty("支付倒计时")
    private Long countDownTime;     //支付倒计时
    @ApiModelProperty("序列号")
    private String serialNumber;     //序列号
    @ApiModelProperty("商品名称")
    private String subject; //商品名称
    @ApiModelProperty("商品描述")
    private String des;     //商品描述
    @ApiModelProperty("优惠金额")
    private BigDecimal yhje;  //优惠金额
    @ApiModelProperty("状态")
    private Integer state;  //优惠金额



}
