package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * Created by zbs on 2017/10/5.
 */
@Data
@ApiModel("住院信息列表")
public class HospitalizationInfoVO {

    @ApiModelProperty("科室")
    String ks;
    @ApiModelProperty("主治医师")
    String zzys;
    @ApiModelProperty("住院状态0-出院，1-在院，2-出院未结帐")
    String state;
    @ApiModelProperty("入院时间")
    Date rysj;
    @ApiModelProperty("出院时间")
    Date cysj;
    @ApiModelProperty("就诊人信息")
    String  name;
    @ApiModelProperty("剩余金额")
    String syje;
    @ApiModelProperty("已结金额")
    String yjje;
    @ApiModelProperty("已用金额")
    String yyje;
    @ApiModelProperty("入院次数")
    String rycs;
    @ApiModelProperty("预缴金额")
    String yujiaojine;
    @ApiModelProperty("病人id")
    String brid;


    @ApiModelProperty("开单时间")
    Date createTime;
    @ApiModelProperty("支付时间")
    Date payTime;

    @ApiModelProperty("住院号")
    String zyh;









}
