package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.Banner;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerMapper extends MyMapper<Banner> {



    @Delete({
            "<script>delete from banner where  id in " +
                    "<foreach collection = \"item\" item = \"item\" open=\"(\" separator=\",\" close=\")\">" +
                    "#{item}" +
                    "</foreach>" +
                    "</script>"
    })
    void deleteBannerBatch(@Param("item") List<Long> item);


    @Select({
            "<script>" +
                    "select * from banner where 1 = 1 " +
                    " <if test=\"bannerId != null\"> " +
                    "     AND id = #{bannerId} " +
                    " </if> " +
                    " <if test=\"name != null\"> " +
                    "     AND name like CONCAT('%',#{name},'%') " +
                    " </if> " +
                    " <if test=\"enable != null\"> " +
                    "     AND enable = #{enable} " +
                    " </if> " +
                    "</script>"
    })
    List<Banner> queryBanner(@Param("bannerId") Long bannerId,@Param("name") String name,@Param("enable") Integer enable);

    @Select({
            "SELECT max(order_number) from banner"
    })
    Integer queryMaxTop();
}
