package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.IMAccount;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by liangxiong on 2017/10/27.
 */
@Repository
public interface IMAccountMapper extends MyMapper<IMAccount> {

    @Select({"SELECT * FROM im_account where account_id = ${accountId}"})
    IMAccount selectByAccountId(@Param("accountId") long accountId);

    @Select({"SELECT * FROM im_account where doctor_id = ${doctorId}"})
    IMAccount selectByDoctorId(@Param("doctorId") long doctorId);

    @Select({"SELECT * FROM im_account where doctor_his_id = ${doctorHisd}"})
    IMAccount selectByDoctorhisId(@Param("doctorHisd") String doctorHisd);

    @Select({"SELECT * FROM im_account where im_acc_id = ${imAccId}"})
    IMAccount selectByIMAccountId(@Param("imAccId") String imAccId);

}
