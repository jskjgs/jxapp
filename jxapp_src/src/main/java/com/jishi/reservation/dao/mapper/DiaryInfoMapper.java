package com.jishi.reservation.dao.mapper;

import com.doraemon.base.dao.base.MyMapper;
import com.jishi.reservation.dao.models.DiaryInfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by liangxiong on 2017/12/20.
 */
@Repository
public interface DiaryInfoMapper extends MyMapper<DiaryInfo> {

    @Select("SELECT * FROM diary_info WHERE diary_info.diary_id=#{diaryId}")
    DiaryInfo queryByDiaryId(Long diaryId);

    @Update({
            "SET @update_num := -1; " +
            "UPDATE diary_info SET liked_num = (SELECT @update_num := liked_num+1) WHERE diary_id=#{diaryId} LIMIT 1; " +
            "SELECT @update_num; "})
    Integer addLikedNum(Long diaryId);


    @Update({
            "SET @update_num := -1; " +
            "UPDATE diary_info SET liked_num = (SELECT @update_num := liked_num-1) WHERE diary_id=#{diaryId} and liked_num>0 LIMIT 1; " +
            "SELECT @update_num; "})
    Integer reduceLikedNum(Long diaryId);


    @Update({
            "SET @update_num := -1; " +
            "UPDATE diary_info SET scan_num = (SELECT @update_num := scan_num+1) WHERE diary_id=#{diaryId} LIMIT 1; " +
            "SELECT @update_num; "})
    Integer addScanNum(Long diaryId);


    @Update({
            "update diary_info set diary_info.scan_num=(select count(*) from diary_scan where diary_scan.diary_id=diary_info.diary_id)," +
            "diary_info.liked_num=(select count(*) from diary_liked where diary_liked.diary_id=diary_info.diary_id)"})
    void updateScanAndLikedNumber();

}
