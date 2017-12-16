package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liangxiong on 2017/10/29.
 */
@Data
@ApiModel("用户咨询历史记录")
@Table(name = "im_access_record")
public class IMAccessRecord {
  @Id
  @ApiModelProperty("主键ID，无意义")
  private Long id;
  @ApiModelProperty("用户账号")
  private Long userId ;
  @ApiModelProperty("医生账号id")
  private Long doctorId;
  @ApiModelProperty("医生his账号id")
  private String doctorHisId;
  @ApiModelProperty("首次访问时间")
  private Date firstAccessDate;
  @ApiModelProperty("最后访问时间")
  private Date lastAccessDate;
}
