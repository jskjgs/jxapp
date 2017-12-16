package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Department;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DepartmentMapper extends MyMapper<Department> {



    @Select({
            "select * from department where id = #{id}"
    })
    Department queryById(@Param("id") Long id);


    @Select({
            "select * from department where enable = 0"
    })
    List<Department> queryAllEnable();
}
