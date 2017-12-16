package com.jishi.reservation.service.his.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 挂号
 * Created by zbs on 2017/10/6.
 */
@Data
public class ConfirmRegister {
    @ApiModelProperty("病人ID")
    String brid = "";
    @ApiModelProperty("合作单位，固定传入第三方名称")
    String hzdw = "";
    @ApiModelProperty("获取号源时返回的出诊记录ID，如果没有返回，此节点传空。 --- 否")
    String czjlid = "";
    @ApiModelProperty("号码")
    String hm = "";
    @ApiModelProperty("号序")
    String hx = "";
    @ApiModelProperty("预约方式，固定传入第三方名称")
    String yyfs = "";
    @ApiModelProperty("费别(根据优惠人群接口获取的费别，如果没有传空)  --- 否")
    String fb = "";
    @ApiModelProperty("预约时间，格式(YYYY-MM-DD HH24:MI:SS)")
    String yysj = "";
    @ApiModelProperty("金额")
    String je = "";
    @ApiModelProperty("说明，固定传入第三方名称")
    String sm = "";
    @ApiModelProperty("如果是医保病人，则传“医保病人  --- 否")
    String brlx = "";
    @ApiModelProperty("机器码，用于区分不同终端或帐户操作。以固定前缀+用户注册帐号作为机器码")
    String jqm = "";
    @ApiModelProperty("结算卡类别，固定传入第三方名称")
    String jsklb = "";
    @ApiModelProperty("结算卡号，不传 --- 否")
    String jskh = "";
    @ApiModelProperty("结算方式，传空 --- 否")
    String jsfs = "";
    @ApiModelProperty("结算金额")
    String jsje = "";
    @ApiModelProperty("交易流水号")
    String jylsh = "";
    @ApiModelProperty("交易名称，固定传“交易信息 --- 否")
    String jymc = "交易信息";
    @ApiModelProperty("交易内容，传“支付帐号|姓名 --- 否")
    String jylr = "";
}
