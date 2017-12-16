package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@ApiModel("医生信息")
@Table(name = "doctor")
public class Doctor {
    @Id
    @ApiModelProperty("医生ID")
    private Long id;
    @ApiModelProperty("His系统存的医生ID")
    private String hId;
    @ApiModelProperty("His系统存的医生所属的科室ID")
    private String departmentId;
    @ApiModelProperty("医生名称")
    private String name;
    @ApiModelProperty("医生类型（0 普通医生 1 专家）")
    private String type;
    @ApiModelProperty("医生头像")
    private String headPortrait;
    @ApiModelProperty("医生部门")
    private String departmentIds;
    @ApiModelProperty("医生简介")
    private String about;
    @ApiModelProperty("医生职称")
    private String title;
    @ApiModelProperty("医生毕业学校")
    private String school;
    @ApiModelProperty("医生的擅长")
    private String goodDescribe;
    @ApiModelProperty("状态标示:0:正常 1:禁用  99:删除")
    private Integer enable;

    @ApiModelProperty("排序")
    private Integer orderNumber;


    @ApiModelProperty("科室名称")
    private String ksmc;


    @ApiModelProperty("his的號碼 HM")
    private String hm;


    @Transient
    private Integer isTop;

    @Transient
    @ApiModelProperty("his的醫生id")
    private String ysid;


    @ApiModelProperty("挂号单价")
    private String dj;

    @Transient
    @ApiModelProperty("号源名称")
    private String hymc;

    @Transient
    @ApiModelProperty("项目id")
    private String xmid;





}
