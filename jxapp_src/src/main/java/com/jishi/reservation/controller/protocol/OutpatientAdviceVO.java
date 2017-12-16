package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by liangxiong on 2017/10/25.
 */
@Data
@ApiModel("门诊缴费医嘱")
public class OutpatientAdviceVO {
    @ApiModelProperty("医嘱类型")
    private String adviceType;
    @ApiModelProperty("医嘱ID")
    private String adviceId;
    @ApiModelProperty("医嘱名称")
    private String adviceName;
    @ApiModelProperty("费目列表")
    private List<OutpatientFeeVO> feeList;
    //@ApiModelProperty("单据列表")
    //private List<OutpatientFeeDocVO> feeDocList;

}
