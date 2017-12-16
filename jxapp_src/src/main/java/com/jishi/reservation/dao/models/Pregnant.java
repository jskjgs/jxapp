package com.jishi.reservation.dao.models;

/**
 * Created by sloan on 2017/8/27.
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 孕妇信息 (扑来个男的)
 */

@Table(name = "pregnant")
@ApiModel("孕妇信息")
@Data
public class Pregnant {

    @Id
    @ApiModelProperty("主键id")
    private Long id;  //主键id
    @ApiModelProperty("用户ID")
    private Long accountId;
    @ApiModelProperty("病人ID")
    private Long patientId;   //关联的patientInfo表的id
    @ApiModelProperty("孕妇姓名")
    private String name;
    @ApiModelProperty("孕妇生日")
    private Date birth;
    @ApiModelProperty("孕妇住址")
    private String livingAddress;
    @ApiModelProperty("上次月经时间")
    private Date lastMenses;
    @ApiModelProperty("电话")
    private String telephone;
    @ApiModelProperty("老公姓名")
    private String husbandName;
    @ApiModelProperty("老公电话")
    private String husbandTelephone;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否有效；0有效")
    private Integer enable;
    @ApiModelProperty("创建时间")
    private Date createTime;


}
