package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.AndroidVersion;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Repository
public interface AndroidVersionMapper extends MyMapper<AndroidVersion> {



    @Select({
            "select * from android_version limit 1"
    })
    AndroidVersion checkUpdateForAndroid();
}
