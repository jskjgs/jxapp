package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.IMAccessRecord;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liangxiong on 2017/10/29.
 */
@Repository
public interface IMAccessRecordMapper extends MyMapper<IMAccessRecord> {
    @Select({"SELECT * FROM im_access_record where user_id = ${userId} ORDER BY last_access_date DESC LIMIT 0,${size}"})
    List<IMAccessRecord> selectByUserId(@Param("userId") long userId, @Param("size") long size);

    @Select({"SELECT * FROM im_access_record where doctor_id = ${doctorId} ORDER BY last_access_date DESC LIMIT 0,${size}"})
    List<IMAccessRecord> selectByDoctorId(@Param("doctorId") long doctorId, @Param("size") long size);

    @Select({"SELECT * FROM im_access_record where doctor_his_id = ${doctorHisd} ORDER BY last_access_date DESC LIMIT 0,${size}"})
    List<IMAccessRecord> selectByDoctorhisId(@Param("doctorHisd") long doctorHisd, @Param("size") long size);

    @Select({"SELECT * FROM im_access_record where user_id = ${userId} AND doctor_id = ${doctorId} ORDER BY last_access_date DESC"})
    List<IMAccessRecord> selectAppointRecord(@Param("userId") long userId, @Param("doctorId") long doctorId);
}
