package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sloan on 2017/10/31.
 */

@Data
@ApiModel("预交费查询")
public class PrePaymentRecordVO {

    @ApiModelProperty("金额")
    private String je;
    @ApiModelProperty("时间")
    private Date jksh;
    @ApiModelProperty("类型")
    private String lx;
    @ApiModelProperty("支付方式")
    private Integer zffs;
    @ApiModelProperty("订单号")
    private String orderNumber;

}
