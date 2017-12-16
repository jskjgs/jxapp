package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liangxiong on 2017/10/28.
 */

@Data
@ApiModel("历次门诊就诊记录的单据信息(处方详情)")
public class OutpatientVisitPrescriptionVO {
    @ApiModelProperty("单据号")
    private String docNumber;
    @ApiModelProperty("日期")
    private Date date;
    @ApiModelProperty("医生姓名")
    private String doctor;
    @ApiModelProperty("单据费用")
    private BigDecimal docAmount;
    @ApiModelProperty("诊断信息")
    private String info;
    @ApiModelProperty("docList")
    private List<PrescriptionDocment> docList;


    @Data
    public static class PrescriptionDocment {
        @ApiModelProperty("类别，处方、检验、检查等等")
        private String type;
        @ApiModelProperty("明细")
        private List<PrescriptionMX> mxList;
    }

    @Data
    public static class PrescriptionMX {
        @ApiModelProperty("用法")
        private String usage;
        @ApiModelProperty("名称")
        private String name;
        @ApiModelProperty("规格(LB为处方时返回)")
        private String format;
        @ApiModelProperty("单量(LB为处方时返回)")
        private String singleValue;
        @ApiModelProperty("用量(LB为处方时返回)")
        private BigDecimal usageValue;
        @ApiModelProperty("单位(LB为处方时返回)")
        private String unit;
        @ApiModelProperty("病历ID(LB为检验、检查时返回)，HIS中报告的唯一标识")
        private String medicalRecordId;
        @ApiModelProperty("报告来源，1-HIS，2-外检报告(LB为检验、检查时返回)")
        private int reportSource;
        @ApiModelProperty("报告来源说明(LB为检验、检查时返回)")
        private String reportSourceNote;
    }
}
