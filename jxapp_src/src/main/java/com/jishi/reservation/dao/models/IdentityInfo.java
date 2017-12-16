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
@ApiModel("就诊人，和account关联")
@Table(name = "identity_info")
public class IdentityInfo {

    @Id
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("关联的账号id")
    private Long accountId;
    @ApiModelProperty("身份证号码")
    private String identityCode;
    @ApiModelProperty("his存的唯一ID")
    private String brId;
    @ApiModelProperty("是否有效的标志，0有效，1无效")
    private Integer enable;
}
