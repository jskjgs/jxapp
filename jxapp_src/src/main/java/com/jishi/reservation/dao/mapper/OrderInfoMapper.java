package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Credentials;
import com.jishi.reservation.dao.models.OrderInfo;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInfoMapper extends MyMapper<OrderInfo>{


    @Select({
            "select * from order_info where order_number = #{outTradeNo}"
    })
    OrderInfo queryByOutTradeNo(@Param("outTradeNo") String outTradeNo);


    @Select({
            "select * from order_info where id = #{orderId}"
    })
    OrderInfo queryById(@Param("orderId") Long orderId);


    @Select({
            "<script>" +
                    "select * from order_info where 1 =1 and account_id = #{accountId} " +
                    " and enable = #{enable}" +
                    "<if test = \"status != null\"> AND status = #{status}</if>" +
                    "</script>"
    })
    List queryOrderList(@Param("accountId") Long accountId,@Param("status") Integer status,@Param("enable") Integer enable);





    @Select({
            "<script>" +
                    "select * from order_info where 1 =1 " +
                    "<if test = \"orderId != null\"> AND id = #{orderId}</if>" +
                    "<if test = \"orderNumber != null\"> AND order_number = #{orderNumber}</if>" +
                    "</script>"
    })
    OrderInfo queryByIdOrOrderNumber(@Param("orderId") Long orderId,@Param("orderNumber") String orderNumber);


    @Select({
            "select * from order_info where order_number = #{orderNumber}"
    })
    OrderInfo queryByNumber(@Param("orderNumber") String orderNumber);

    @Select({
            "select * from order_info where type = 2 and status = 0 and br_id = #{brId}"
    })
    List<OrderInfo> queryPrePayment(@Param("brId") String brId);


    @Select({
            "select * from order_info where third_order_number = #{thirdOrderNumber}"
    })
    OrderInfo queryByThirdOrderNumber(@Param("thirdOrderNumber") String thirdOrderNumber);


    @Select({                               // 预约订单   未支付      有效
            "select * from order_info where type = 1 and status = 1 and enable = 0  and register_id is not null"
    })
    List<OrderInfo> queryAllWaitPayRegister();
}
