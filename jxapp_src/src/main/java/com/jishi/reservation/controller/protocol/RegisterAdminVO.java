package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by sloan on 2017/10/29.
 */

@Data
@ApiModel("admin预约bean")
public class RegisterAdminVO {

    @ApiModelProperty("预约id")
    private Long id;
    @ApiModelProperty("预约时间")
    private Date registerTime;
    @ApiModelProperty("客户姓名")
    private String patientName;
    @ApiModelProperty("就诊卡号")
    private String jjkh;
    @ApiModelProperty("预留手机号")
    private String phone;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("医生姓名")
    private String doctorName;
    @ApiModelProperty("doctorId")
    private String doctorId;
    @ApiModelProperty("科室名称")
    private String department;
    @ApiModelProperty("科室id")
    private String departmentId;
    @ApiModelProperty("预约状态 过期未到诊 1，正常就诊 2 ，预约就诊 3")
    private String status;


}
