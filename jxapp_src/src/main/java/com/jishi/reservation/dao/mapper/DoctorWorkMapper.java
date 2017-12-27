package com.jishi.reservation.dao.mapper;

import com.doraemon.base.dao.base.MyMapper;
import com.jishi.reservation.dao.models.DoctorWork;
import com.jishi.reservation.dao.models.IMAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by liangxiong on 2017/10/27.
 */
@Repository
public interface DoctorWorkMapper extends MyMapper<DoctorWork> {



    @Select({
            "select * from doctor_work where h_doctor_id = #{hDoctorId} and date_format(working_time,\"%Y-%m-%e\") = #{wordDate} and enable = 0"
    })
    DoctorWork queryByHIdAndDate(@Param("hDoctorId") String hDoctorId,@Param("wordDate") String workDate);
}
