package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Doctor;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorMapper extends MyMapper<Doctor> {

    @Select({" <script>" +
            " select * from doctor " +
            " where 1=1 " +
            " <if test=\"name != null\"> " +
            "     AND name like CONCAT('%',#{name},'%') " +
            " </if> " +
            " <if test=\"departmentIds != null\"> " +
            "     AND department_ids like CONCAT('%',#{departmentIds},'%') " +
            " </if> " +
            " <if test=\"id != null\"> " +
            "     AND id = #{id} " +
            " </if> " +
            " <if test=\"type != null\"> " +
            "     AND type = #{type} " +
            " </if> " +
            " <if test=\"enable != null\"> " +
            "     AND enable = #{enable} " +
            " </if> " +
            "</script>"
    })
    List<Doctor> queryByAttr(Doctor doctor);


    @Select({
            "SELECT * FROM doctor WHERE "
    })
    List<Doctor> queryByDepartment(@Param("departmentId") String departmentId, @Param("enable") Integer enable);


    @Select({
            "select max(order_number ) from doctor"
    })
    Integer queryMaxOrderNumber();


    @Select({
            "select * from doctor where id = #{doctorId}"
    })
    Doctor queryById(@Param("doctorId") Long doctorId);


    @Select({
            "select * from doctor where h_id = #{hId}"
    })
    Doctor queryByHid(@Param("hId") String hId);


    @Select({
            "select * from doctor where h_id = #{hDoctorId}"
    })
    Doctor queryByHdoctorId(@Param("hDoctorId") String hDoctorId);

    @Select({
            "select * from doctor where department_id = #{hId}"
    })
    List<Doctor> queryByDepartmentHid(@Param("hId") String hId);


    @Select({
            "select * from doctor where enable = 0"
    })
    List<Doctor> queryAllValidDoctor();
}
