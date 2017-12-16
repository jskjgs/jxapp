package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.QueueLength;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liangxiong on 2017/11/15.
 */
@Repository
public interface QueueLengthMapper extends MyMapper<QueueLength> {

    @Select({"select * from queue_length"})
    List<QueueLength> queryAll();

    @Select({"select * from queue_length where doctor_his_id=#{doctorHisId}"})
    QueueLength queryByDoctorHisId(@Param("doctorHisId") String doctorHisId);

    @Select({"select * from queue_length where depart_his_id=#{departHisId}"})
    QueueLength queryByDepartHisId(@Param("departHisId") String departHisId);

    @Update({"update queue_length set length=#{length} where doctor_his_id=#{doctorHisId}"})
    boolean updateByDoctorHisId(@Param("doctorHisId") String doctorHisId, @Param("length") int length);

    @Update({"update queue_length set length=#{length} where depart_his_id=#{departHisId}"})
    boolean updateByDepartHisId(@Param("departHisId") String departHisId, @Param("length") int length);
}
