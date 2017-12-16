package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.DiaryLiked;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryLikedMapper extends MyMapper<DiaryLiked>{



    @Select({
            "select count(*) from diary_liked where diary_id = #{diaryId}"
    })
    Integer queryCountByDiaryId(@Param("diaryId") Long diaryId);
}
