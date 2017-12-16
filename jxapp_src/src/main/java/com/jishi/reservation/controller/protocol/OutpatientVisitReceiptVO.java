package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liangxiong on 2017/10/29.
 */
@Data
@ApiModel("指定就诊的费用信息")
public class OutpatientVisitReceiptVO {

    @ApiModelProperty("单据号")
    private String docNumber;
    @ApiModelProperty("支付方式")
    private String paymentType;
    @ApiModelProperty("时间")
    private Date date;
    @ApiModelProperty("医生姓名")
    private String doctor;
    @ApiModelProperty("单据费用")
    private BigDecimal docAmount;
    @ApiModelProperty("费目列表")
    private List<ReceiptItem> itemLIst;

    @Data
    public static class ReceiptItem {
        @ApiModelProperty("费目，如药品费、检验费等")
        private String fm;
        @ApiModelProperty("明细列表")
        private List<ReceiptMX> mxList;
    }

    @Data
    public static class ReceiptMX {
        @ApiModelProperty("名称")
        private String name;
        @ApiModelProperty("规格")
        private String format;
        @ApiModelProperty("数量")
        private String number;
        @ApiModelProperty("单位")
        private String unit;
        @ApiModelProperty("单价")
        private String unitPtrice;
        @ApiModelProperty("金额")
        private String amount;
    }
}
