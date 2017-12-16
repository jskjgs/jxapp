package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Manager;
import com.jishi.reservation.dao.models.Pregnant;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ManagerMapper extends MyMapper<Manager> {



    @Select({
            "select * from manager where account = #{account}"
    })
    Manager findAccountByUserName(@Param("account") String account);

    @Select({
            "select * from manager where id = #{id}"
    })
    Manager findById(@Param("id") Long id);


    @Select({
            "select * from manager where enable = 0"
    })
    List<Manager> selectEnableManager();
}
