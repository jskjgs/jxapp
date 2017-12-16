package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by sloan on 2017/10/13.
 */

@Data
@ApiModel("就诊卡info")
@Table(name = "credentials")
public class Credentials {

    @Id
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("类别")
    private String idType;
    @ApiModelProperty("卡号")
    private String idNumber;
    @ApiModelProperty("his存的唯一ID")
    private String brId;
    @ApiModelProperty("门诊号")
    private String mzh;
    @ApiModelProperty("是否有效的标志，0有效，1无效")
    private Integer enable;
}
