package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Pregnant;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface PregnantMapper extends MyMapper<Pregnant> {


    @Select({
            "select * from pregnant where patient_id = #{id}"
    })
    Pregnant queryByPatientId(@Param("id") Long id);


    @Delete({
            "delete from pregnant where id = #{id}"
    })
    void deleteById(@Param("id") Long id);
}
