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
@ApiModel("历次门诊就诊记录")
public class OutpatientVisitRecordVO {

    @ApiModelProperty("病人Id")
    private String brid;
    @ApiModelProperty("日期")
    private Date date;
    @ApiModelProperty("挂号单号")
    private String rgisterNum;
    @ApiModelProperty("诊断信息")
    private String zdxx;
    @ApiModelProperty("就诊科室")
    private String department;
    @ApiModelProperty("就诊科室ID")
    private String departmentId;
    @ApiModelProperty("主治医生")
    private String doctor;
    @ApiModelProperty("费用金额")
    private BigDecimal amount;
    @ApiModelProperty("单据")
    private List<RecordMX> docList;

    @Data
    public static class RecordMX {
        @ApiModelProperty("单据名称")
        private String docName;
        @ApiModelProperty("单据数量")
        private int docNum;
    }
}
