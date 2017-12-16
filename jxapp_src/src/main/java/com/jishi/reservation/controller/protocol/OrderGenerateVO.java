package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sloan on 2017/10/28.
 */

@Data
@ApiModel("生成订单的对象")
public class OrderGenerateVO {

    @ApiModelProperty("单据号")
    private String orderNumber;

    @ApiModelProperty("支付宝返回的orderString")
    private String orderString;

}
