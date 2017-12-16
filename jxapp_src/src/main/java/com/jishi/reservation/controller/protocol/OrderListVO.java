package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by sloan on 2017/10/23.
 */

@Data
@ApiModel("订单列表页VO")

public class OrderListVO {


    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("就诊科室")
    private String department;

    @ApiModelProperty("医生姓名")
    private String doctorName;

    @ApiModelProperty("预约时间")
    private Date agreeTime;

    @ApiModelProperty("就诊人姓名")
    private String patientName;

    @ApiModelProperty("状态")
    private Integer status;

}
