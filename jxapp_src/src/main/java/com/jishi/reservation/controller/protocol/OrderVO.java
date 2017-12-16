package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sloan on 2017/10/23.
 */
@Data
@ApiModel("订单完成页面VO")
public class OrderVO {

    @ApiModelProperty("序列号")
    private String serialNumber;
    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("完成时间")
    private Date completedTime;
    @ApiModelProperty("部门")
    private String department;
    @ApiModelProperty("医生名称")
    private String doctorName;
    @ApiModelProperty("病人名称")
    private String patientName;
    @ApiModelProperty("地点")
    private String position;
    @ApiModelProperty("预约时间")
    private Date registerTime;
    @ApiModelProperty("时段")
    private String timeInterval;
    @ApiModelProperty("支付方式  1支付宝 2微信")
    private Integer payType;
    @ApiModelProperty("支付金额")
    private BigDecimal price;
    @ApiModelProperty("确认状态  1失败  0成功")
    private Integer status;

    @ApiModelProperty("支付时间")
    private Date payTime;


}
