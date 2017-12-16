package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@ApiModel("科室信息")
@Table(name = "department")
public class Department {
    @Id
    @ApiModelProperty("科室ID")
    private Long id;
    @ApiModelProperty("科室名称")
    private String name;
    @ApiModelProperty("科室位置")
    private String position;
    @ApiModelProperty("状态标示:0:正常 1:禁用  99:删除")
    private Integer enable;

    @ApiModelProperty("his存的科室id")
    private String hId;

    @Transient
    @ApiModelProperty("医生列表")
    private List<Doctor> doctorList;

}
