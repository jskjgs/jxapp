package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liangxiong on 2017/10/29.
 */
@Data
@ApiModel("im发起聊天")
public class IMChatInfo {
    @ApiModelProperty("发起者accid")
    private String imSourceId;
    @ApiModelProperty("目标accid")
    private String imDestId;
    @ApiModelProperty("发起者token")
    private String imToken;
}
