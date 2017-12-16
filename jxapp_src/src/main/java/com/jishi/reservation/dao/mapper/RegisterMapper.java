package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Register;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface RegisterMapper extends MyMapper<Register> {

    /**
     * 查找所有预约完成的预约信息..
     * @return
     */
    @Select({
            "select * from register where status = 2"
    })
    List<Register> queryEnableRegister();

    @Select({
            "select * from register where order_id = #{orderId}"
    })
    Register queryByOrderId(@Param("orderId") Long orderId);


    @Select({
            "select * from register"
    })
    List<Register> queryAllRegister();


    @Select({
            "select * from register where id = #{registerId}"
    })
    Register queryById(@Param("registerId") Long registerId);

    @Select({
            "select * from register where br_id = #{brid} and  date_format( agreed_time,\"%Y-%c-%d %I\" ) = #{timeStr} and doctor_id = #{doctorId} and status = 1"
    })
    List<Register> queryByBrIdTimeDoctorId(@Param("brid") String brid,@Param("timeStr") String timeStr,@Param("doctorId") String doctorId);


    @Select({
            " <script> select * from register where 1  = 1 " +
                    "<if test = \"accountId != null \"> and account_id = #{accountId} </if>"+
                    "<if test = \"registerId != null \"> and id = #{registerId} </if>"+
                    "<if test = \"status != null \"> and status = #{status} </if>"+
                    "<if test = \"enable != null \"> and enable = #{enable} </if>"+
                    //无效病人的就不返回了。

                    "</script>"
    })
    List<Register> selectCondition(@Param("accountId") Long accountId,@Param("registerId") Long registerId,@Param("status") Integer status,@Param("enable") Integer enable);

    @Select({
            " <script> select * from register where 1  = 1 and br_id in " +
                    "<foreach item = 'item' index = 'index' collection = 'idList' open = '(' separator = ',' close = ')'>#{item}</foreach>" +
                    "<if test = \"registerId != null \"> and id = #{registerId} </if>"+
                    "<if test = \"status != null \"> and status = #{status} </if>"+
                    "<if test = \"enable != null \"> and enable = #{enable} </if>" +
                    " order by id desc "+
                    //无效病人的就不返回了。

                    "</script>"
    })
    List<Register> selectConditionByBridList(@Param("idList") List<String> idList,@Param("registerId") Long registerId,@Param("status") Integer status,@Param("enable") Integer enable);
}
