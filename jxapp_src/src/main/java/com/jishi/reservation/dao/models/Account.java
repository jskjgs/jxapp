package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Id;

import javax.persistence.Table;

@Data
@ApiModel("账号信息")
@Table(name = "account")
public class Account {
    @Id
    @ApiModelProperty("账号ID")
    private Long id;
    @ApiModelProperty("推送ID")
    private String pushId ;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("账号密码")
    private String passwd;
    @ApiModelProperty("头像")
    private String headPortrait;
    @ApiModelProperty("昵称")
    private String nick;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("状态标示:0:正常 1:禁用  99:删除")
    private Integer enable;
}
