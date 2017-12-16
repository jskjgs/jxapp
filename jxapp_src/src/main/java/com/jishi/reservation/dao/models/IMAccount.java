package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by liangxiong on 2017/10/27.
 */
@Data
@ApiModel("账号信息")
@Table(name = "im_account")
public class IMAccount {
    @Id
    @ApiModelProperty("主键ID，无意义")
    private Long id;
    @ApiModelProperty("用户账号")
    private Long accountId ;
    @ApiModelProperty("医生账号id")
    private Long doctorId;
    @ApiModelProperty("医生his账号id")
    private String doctorHisId;
    @ApiModelProperty("im账号id")
    private String imAccId;
    @ApiModelProperty("im token，客户端用于登录等操作")
    private String imToken;
    @ApiModelProperty("账号类型（0用户，1医生）")
    private int type;
}
