package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Diary;
import com.jishi.reservation.dao.models.Permission;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends MyMapper<Permission>{


    @Select({
            "select * from permission where permission_id = #{key}"
    })
    Permission queryByKey(@Param("key") String key);
}
