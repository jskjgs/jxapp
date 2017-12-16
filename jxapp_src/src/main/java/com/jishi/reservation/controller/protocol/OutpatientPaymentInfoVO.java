package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liangxiong on 2017/10/25.
 */
@Data
@ApiModel("门诊缴费记录")
public class OutpatientPaymentInfoVO implements Comparable<OutpatientPaymentInfoVO> {
    @ApiModelProperty("病人ID")
    private String brid;
    @ApiModelProperty("病人姓名")
    private String patientName;
    @ApiModelProperty("单据号，挂号单号")
    private String documentNum;
    @ApiModelProperty("挂号类型")
    private String registerType;
    @ApiModelProperty("预约时间")
    private Date registerDate;
    @ApiModelProperty("开单科室")
    private String department;
    @ApiModelProperty("执行状态")
    private String exeStatus;
    @ApiModelProperty("单据类型，1-收费单，4-挂号单")
    private int documentType;
    @ApiModelProperty("支付状态 0-待支付，1-已支付，2-已退费")
    private int paymentStatus;
    @ApiModelProperty("是否预约，0-挂号并缴费，1-预约不缴费")
    private int hasRegister;
    @ApiModelProperty("金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("未支付金额")
    private BigDecimal unpaidAmount;
    @ApiModelProperty("未支付单据列表，以','分隔")
    private String unpaidDocIds;
    @ApiModelProperty("最新一次开单时间")
    private Date lastDocDate;
    @ApiModelProperty("上一次支付时间，可能为空")
    private Date lastPaidDate;
    @ApiModelProperty("医生ID")
    private String doctorId;
    @ApiModelProperty("医生姓名")
    private String doctorName;
    @ApiModelProperty("是否结算卡支付，0-否，1-是")
    private int hasPayCard;
    @ApiModelProperty("医嘱列表")
    private List<OutpatientAdviceVO> adviceList;

    // 按时间和单据号排序
    @Override
    public int compareTo(OutpatientPaymentInfoVO o) {
        if (this.getLastDocDate() != null && o.getLastDocDate() != null) {
            return this.getLastDocDate().compareTo(o.getLastDocDate());
        } else if (this.getRegisterDate() != null && o.getRegisterDate() != null) {
            return this.getRegisterDate().compareTo(o.getRegisterDate());
        } else if (this.getDocumentNum() != null && o.getDocumentNum() != null) {
            return this.getDocumentNum().compareTo(o.getDocumentNum());
        }
        return 0;
    }
}
