package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("预约信息")
@Table(name = "register")
public class Register {
    @Id
    @ApiModelProperty("预约ID")
    private Long id;
    @ApiModelProperty("账号ID")
    private Long accountId;
    @ApiModelProperty("订单ID")
    private Long orderId;
    @ApiModelProperty("病人ID")
    private String brId;
    @ApiModelProperty("病人名称")
    private String patientName;
    @ApiModelProperty("医生名称")
    private String doctorName;
    @ApiModelProperty("科室ID")
    private String departmentId;
    @ApiModelProperty("科室名称")
    private String  department;
    @ApiModelProperty("预约的医生ID his存的医生id")
    private String doctorId;
    @ApiModelProperty("预约时间")
    private Date agreedTime;
    @ApiModelProperty("状态（0  预约成功 1 待支付 2 取消预约")
    private Integer status;
    @ApiModelProperty("状态标示:0:正常 1:禁用  99:删除")
    private Integer enable;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("his的号码 HM")
    private String hm;
    @ApiModelProperty("his的号序 HX")
    private String hx;

    @ApiModelProperty("序列号")
    private String serialNumber;


    @Transient
    private Integer payType;  //支付方式
    @Transient
    private BigDecimal price;  //支付金额
    @Transient
    private Date payTime;       //支付时间
    @Transient
    private Date completeTime;  //完成时间
    @Transient
    private String orderCode;     //订单编号
    @Transient
    private Long countDownTime;     //支付倒计时
    @Transient
    @ApiModelProperty("优惠金额")
    private BigDecimal discount;     //优惠金额

    @Transient
    @ApiModelProperty("预约地点")
    private String location;

}
