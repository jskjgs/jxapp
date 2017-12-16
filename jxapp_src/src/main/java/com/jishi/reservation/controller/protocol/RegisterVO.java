package com.jishi.reservation.controller.protocol;

import com.jishi.reservation.dao.models.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by zbs on 2017/8/11.
 */
@Data
@ApiModel("预约信息")
public class RegisterVO {
    @ApiModelProperty("预约详细信息")
    Register register;
    @ApiModelProperty("账号详细信息")
    Account account;
    @ApiModelProperty("病人详细信息")
    PatientInfo patientInfo;
    @ApiModelProperty("预约医生信息")
    Doctor doctor;
    @ApiModelProperty("科室详细信息")
    Department department;
}
