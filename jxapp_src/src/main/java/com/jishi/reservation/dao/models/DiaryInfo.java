package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by liangxiong on 2017/12/20.
 */
@Data
@ApiModel("日记附加信息")
@Table(name = "diary_info")
public class DiaryInfo {
    @Id
    @ApiModelProperty("主键id")
    private Long diaryId;
    @ApiModelProperty("浏览量")
    private Integer scanNum;
    @ApiModelProperty("点赞数量")
    private Integer likedNum;
}
