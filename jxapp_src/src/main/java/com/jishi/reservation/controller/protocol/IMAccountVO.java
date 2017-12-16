package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liangxiong on 2017/11/14.
 */
@Data
@ApiModel("im账号信息")
public class IMAccountVO {
    @ApiModelProperty("accid")
    private String imAccId;
    @ApiModelProperty("token")
    private String imToken;
}
