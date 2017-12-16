package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Id;

import javax.persistence.Table;

@Data
@ApiModel("banner信息")
@Table(name = "banner")
public class Banner {
    @Id
    @ApiModelProperty("banner的ID")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("banner的url")
    private String bannerUrl;
    @ApiModelProperty("跳转的url")
    private String jumpUrl;
    @ApiModelProperty("排序")
    private Integer orderNumber;
    @ApiModelProperty("状态标示:0:正常 1:禁用  99:删除")
    private Integer enable;
    @ApiModelProperty("隐藏/显示 标示:0:显示 1:隐藏 ")
    private Integer display;



}
