package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liangxiong on 2017/10/25.
 */
@Data
@ApiModel("门诊缴费费目")
public class OutpatientFeeVO {
    @ApiModelProperty("费目名称")
    private String feeName;
    @ApiModelProperty("金额")
    private BigDecimal feeAmount;
    @ApiModelProperty("支付状态，1-已支付，0-未支付")
    private int feeStatus;
    //@ApiModelProperty("明细列表")
    //private List<OutpatientFeeItemVO> feeItemList;

    @Data
    @ApiModel("门诊缴费费目明细")
    public static class OutpatientFeeItemVO {
      @ApiModelProperty("名称")
      private String itemName;
      @ApiModelProperty("规格")
      private String itemFormat;
      @ApiModelProperty("数量")
      private int itemNumber;
      @ApiModelProperty("单位")
      private String itemUnit;
      @ApiModelProperty("单价")
      private BigDecimal itemAdvance;
    }
}
