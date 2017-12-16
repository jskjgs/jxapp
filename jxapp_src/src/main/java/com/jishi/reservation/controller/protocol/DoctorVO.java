package com.jishi.reservation.controller.protocol;

import com.jishi.reservation.dao.models.Department;
import com.jishi.reservation.dao.models.Doctor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by zbs on 2017/8/11.
 */
@Data
@ApiModel("医生信息")
public class DoctorVO {
    @ApiModelProperty("医生详细信息")
    Doctor doctor;
    @ApiModelProperty("所属科室详细信息")
    List<Department> departmentList;
}
