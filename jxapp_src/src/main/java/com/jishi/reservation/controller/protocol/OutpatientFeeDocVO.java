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
@ApiModel("门诊缴费单据")
public class OutpatientFeeDocVO {
      @ApiModelProperty("单据号")
      private String documentNum;
      @ApiModelProperty("单据类型")
      private int documentType;
      @ApiModelProperty("单据金额")
      private BigDecimal documentAmount;
      @ApiModelProperty("开单时间")
      private Date documentDate;
      @ApiModelProperty("是否结算卡支付，0-否，1-是")
      private int hasPayCard;
      @ApiModelProperty("支付状态，1-已支付，0-未支付")
      private int payStatus;
      @ApiModelProperty("已退金额")
      private BigDecimal returnNumber;
}
