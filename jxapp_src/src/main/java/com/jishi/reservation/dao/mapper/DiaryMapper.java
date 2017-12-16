package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Diary;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryMapper extends MyMapper<Diary>{


    @Select({
            "<script>select  * from diary where 1 =1 and is_lock = 1 " +
                    "<if test = \"name != null \"> AND (nick like CONCAT('%',#{name},'%') or title like CONCAT('%',#{name},'%') or account_id like CONCAT('%',#{name},'%')  )</if> " +
                    "<if test = \"status != null\"> AND status = #{status}</if>" +
                    "<if test =\"startTime!=null\">and create_time &gt;  #{startTime}  </if>" +
                    "<if test =\"endTime!=null\">and create_time &lt;  #{endTime}  </if>" +
                    "</script>"
    })
    List<Diary> queryDiary(@Param("name") String name,@Param("status") Integer status,@Param("startTime") Long startTime,@Param("endTime") Long endTime);


    @Select({
            "select * from diary where id = #{id}"
    })
    Diary queryById(@Param("id") Long id);

    @Select({
            "select max(sort) from diary "
    })
    Integer queryMaxSort();

    @Select({
            "select * from diary order by sort desc limit 1"
    })
    Diary queryTopDiary();


    @Select({
            "<script>select  * from diary where enable = 0 " +
                    " <if test = \" isMy == 0 \"> AND account_id = #{accountId} </if>" +
                    "<if test = \"isMy == 1 \"> AND status  = 0 AND is_lock = 1 </if>" +

                    "</script>"
    })
    List<Diary> queryEnableAndVerified(@Param("accountId") Long accountId,@Param("isMy") Integer isMy);


    @Select({
            "select * from diary where account_id = #{accountId}"
    })
    List<Diary> queryByAccountId(@Param("accountId") Long accountId);
}
