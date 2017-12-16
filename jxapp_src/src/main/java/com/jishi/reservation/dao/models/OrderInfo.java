package com.jishi.reservation.dao.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sloan on 2017/10/17.
 */

@Data
@Table(name = "order_info")
@ApiModel("订单信息")
public class OrderInfo {

    @Id
    private Long id;
    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("his唯一病人id")
    private String brId;
    @ApiModelProperty("我们系统的账号 id")
    private Long accountId;
    @ApiModelProperty("对应的预约 id")
    private Long registerId;
    @ApiModelProperty("商品名称")
    private String subject;
    @ApiModelProperty("商品描述")
    private String des;
    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("优惠金额")
    private BigDecimal discount;
    @ApiModelProperty("订单类型  1：挂号订单  2：住院预交款订单 ，3 门诊订单  之后加的往后累加.")
    private Integer type;
    @ApiModelProperty("支付方式  1 支付宝；2 微信")
    private Integer payType;
    @ApiModelProperty("订单状态 0：已完成(已付款)； 1：待付款 ；2：已取消")
    private Integer status;
    @ApiModelProperty("是否有效的标志 0：有效 ；1：无效")
    private Integer enable;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("支付时间")
    private Date payTime;
    @ApiModelProperty("第三方支付的流水号")
    private String thirdOrderNumber;

    @ApiModelProperty("操作时间")
    private String czsj;

    @ApiModelProperty("his的挂号单号")
    private String ghdh;

    @ApiModelProperty("结算id")
    private String jsid;



    @ApiModelProperty("购买者邮箱")
    private String buyerEmail;

    @ApiModelProperty("商家邮箱")
    private String sellerEmail;

    @ApiModelProperty("购买者id")
    private String buyerId;





}
