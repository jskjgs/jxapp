package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@ApiModel("病人信息")
@Table(name = "patientInfo")
public class PatientInfo {
    @Id
    @ApiModelProperty("病人ID")
    private Long id;
    @ApiModelProperty("账号ID")
    private Long accountId;
    @ApiModelProperty("病人姓名")
    private String name;
    @ApiModelProperty("病人电话")
    private String phone;
    @ApiModelProperty("病人身份证")
    private String idCard;
    @ApiModelProperty("状态标示:0:正常 1:禁用  99:删除")
    private Integer enable;

    @ApiModelProperty("病人id")
    private String brId;

    @ApiModelProperty("门诊号")
    private String mzh;



    @Transient
    @ApiModelProperty("孕妇id")
    private Long pregnantId;  //主键id


    @Transient
    @ApiModelProperty("孕妇生日")
    private Date birth;

    @Transient
    @ApiModelProperty("孕妇住址")
    private String livingAddress;

    @Transient
    @ApiModelProperty("上次月经时间")
    private Date lastMenses;

    @Transient
    @ApiModelProperty("老公姓名")
    private String husbandName;

    @Transient
    @ApiModelProperty("老公电话")
    private String husbandTelephone;

    @Transient
    @ApiModelProperty("备注")
    private String remark;

    @Transient
    @ApiModelProperty("是否有效；0有效")
    private Integer pregnantEnable;


}
