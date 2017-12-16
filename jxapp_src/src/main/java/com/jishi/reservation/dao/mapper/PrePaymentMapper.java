package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Doctor;
import com.jishi.reservation.dao.models.PrePayment;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrePaymentMapper extends MyMapper<PrePayment> {



    @Select({
            "select * from pre_payment where order_id = #{orderId}"
    })
    PrePayment queryByOrderId(@Param("orderId") Long orderId);
}
