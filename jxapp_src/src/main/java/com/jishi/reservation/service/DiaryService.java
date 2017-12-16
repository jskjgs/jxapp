package com.jishi.reservation.service;

import com.alibaba.fastjson.JSONObject;
import com.doraemon.base.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.controller.protocol.DiaryContentVO;
import com.jishi.reservation.controller.protocol.ImageVO;
import com.jishi.reservation.dao.mapper.AccountMapper;
import com.jishi.reservation.dao.mapper.DiaryLikedMapper;
import com.jishi.reservation.dao.mapper.DiaryMapper;
import com.jishi.reservation.dao.mapper.DiaryScanMapper;
import com.jishi.reservation.dao.models.Account;
import com.jishi.reservation.dao.models.Diary;
import com.jishi.reservation.dao.models.DiaryLiked;
import com.jishi.reservation.dao.models.DiaryScan;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class DiaryService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    DiaryMapper diaryMapper;

    @Autowired
    DiaryScanMapper diaryScanMapper;

    @Autowired
    DiaryLikedMapper diaryLikedMapper;

    public PageInfo<Diary> queryDiaryPageInfo(String name,Integer status, Long startTime,Long endTime,Paging paging) {
        if(paging != null)
            PageHelper.startPage(paging.getPageNum(),paging.getPageSize(),paging.getOrderBy());
        return new PageInfo(queryDiary(name,status, startTime, endTime));
    }

    private List<Diary> queryDiary(String name,Integer status,Long startTime,Long endTime) {

        List<Diary> diaryList = diaryMapper.queryDiary(name, status, startTime, endTime);
        Diary topDiary = diaryMapper.queryTopDiary();
        for (Diary diary : diaryList) {
                if(Objects.equals(diary.getId(), topDiary.getId())){
                    diary.setIsTop(true);
                }else {
                    diary.setIsTop(false);
                }
        }

        return diaryList;
    }

    public void verify(Long id, Integer status) {
        Diary diary = diaryMapper.queryById(id);
        Preconditions.checkNotNull(diary,"该id没有对应的日记信息");
        diary.setStatus(status);
        diaryMapper.updateByPrimaryKeySelective(diary);
    }

    public void show(Long id) {
        Diary diary = diaryMapper.queryById(id);
        Preconditions.checkNotNull(diary,"该id没有对应的日记信息");
        diary.setEnable(diary.getEnable() == 1?EnableEnum.EFFECTIVE.getCode():EnableEnum.INVALID.getCode());
        diaryMapper.updateByPrimaryKeySelective(diary);
    }

    public void top(Long id) {
        Diary diary = diaryMapper.queryById(id);
        Preconditions.checkNotNull(diary,"该id没有对应的日记信息");
        Integer maxSort =  diaryMapper.queryMaxSort();
        diary.setSort(maxSort+1);
        diaryMapper.updateByPrimaryKeySelective(diary);

    }


    public void update(Long id, String title, String content,Integer lock) throws UnsupportedEncodingException {


        Diary diary = diaryMapper.queryById(id);
        diary.setTitle(title);
        Gson gson = new Gson();


        List<DiaryContentVO> contentList = gson.fromJson(content,
                new TypeToken<List<DiaryContentVO>>() {
                }.getType());


        String brief = "";
        for (DiaryContentVO diaryContentVO : contentList) {
            if(diaryContentVO.getType() == 1){
                brief = diaryContentVO.getText();
                break;
            }
            break;
        }


        diary.setContent(JSONObject.toJSONString(contentList));
        diary.setIsLock(lock);
        diary.setBrief(brief);
        diary.setStatus(1);
        diaryMapper.updateByPrimaryKeySelective(diary);
    }

    public static String generateRandomId() throws Exception {

        String prefix = "diary";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
        String format = sdf.format(date);
        return prefix+format+ RandomUtil.getRandomLetterAndNum(6);
    }


    public void publish(Long accountId,String title,String content,Integer lock) throws Exception {

        Gson gson = new Gson();


        //gson处理，把内容解析为内容数组
        List<DiaryContentVO> contentList = gson.fromJson(content,
                new TypeToken<List<DiaryContentVO>>() {
                }.getType());


        for(int i = 0;i<contentList.size();i++){
            contentList.get(i).setContentId(generateRandomId());
        }


        Diary diary = new Diary();

        diary.setTitle(title);
        //设置简介的默认值
        String brief = "";
        for (DiaryContentVO diaryContentVO : contentList) {
            //遍历日记内容，取到第一个type是text且内容不为""的文本作为简介
            if(diaryContentVO.getType() == 1 && !diaryContentVO.getText().equals("")){
                //长度待定,,
                brief = diaryContentVO.getText();
                log.info("brief："+brief);
                break;
            }
        }
        diary.setContent(JSONObject.toJSONString(contentList));
        diary.setEnable(0);
        diary.setAccountId(accountId);
        diary.setNick(accountMapper.queryById(accountId).getNick());
        diary.setUrl("");
        diary.setStatus(1);
        diary.setSort(0);
        diary.setIsLock(lock);
        diary.setCreateTime(new Date());
        diary.setBrief(brief);
        diaryMapper.insertReturnId(diary);

    }

    public Diary queryById(Long id) {

        Diary diary = diaryMapper.queryById(id);
        Preconditions.checkNotNull(diary,"该id没有对应的日记。");

        String content = diary.getContent();

        Gson gson = new Gson();


        List<DiaryContentVO> contentList = gson.fromJson(content,
                new TypeToken<List<DiaryContentVO>>() {
                }.getType());

        Account account = accountMapper.queryById(diary.getAccountId());
        diary.setContentVOList(contentList);
        diary.setContent(null);
        diary.setScanNum(diaryScanMapper.queryCountByDiaryId(diary.getId()));
        diary.setLikedNum(diaryLikedMapper.queryCountByDiaryId(diary.getId()));
        diary.setAvatar(account.getHeadPortrait());

        diary.setNick(account.getNick());
        return diary;
    }

    public PageInfo<Diary> queryPage(Long accountId,Integer isMy,Integer startPage, Integer pageSize) {

        Gson gson = new Gson();
        if(pageSize == 0){

                pageSize = diaryMapper.queryEnableAndVerified(accountId,isMy).size();

        }

        PageHelper.startPage(startPage,pageSize).setOrderBy("create_time desc");
        List<Diary> list =  diaryMapper.queryEnableAndVerified(accountId,isMy);
        PageInfo<Diary> pageInfo = new PageInfo<>(list);
        log.info("返回日记长度："+list.size());
        if(list!=null && list.size() != 0){
            for (Diary diary : list) {
                log.info("日记id:"+diary.getId());
                //获取浏览数和点赞数
                diary.setScanNum(diaryScanMapper.queryCountByDiaryId(diary.getId()));
                diary.setLikedNum(diaryLikedMapper.queryCountByDiaryId(diary.getId()));
                //查询用户头像
                diary.setAvatar(accountMapper.queryById(diary.getAccountId()).getHeadPortrait());
                List<ImageVO> paramList = new ArrayList<>();
                List<DiaryContentVO> contentList = gson.fromJson(diary.getContent(),
                        new TypeToken<List<DiaryContentVO>>() {
                        }.getType());
                int i = 0;
                for (DiaryContentVO diaryContentVO : contentList) {

                    if(i == 4)
                        break;
                    if(diaryContentVO.getType() == 0){
                        ImageVO vo = new ImageVO();
                        vo.setUrl(diaryContentVO.getUrl());
                        vo.setHeight(diaryContentVO.getHeight());
                        vo.setWidth(diaryContentVO.getWidth());

                        paramList.add(vo);
                        i++;
                    }

                }
                diary.setImgList(paramList);
                diary.setContent(null);

            }
        }


        return  pageInfo;
    }

    public Integer likeDiary(Long diaryId, Long accountId) {

        DiaryLiked param = new DiaryLiked();
        param.setDiaryId(diaryId);
        param.setAccountId(accountId);

        DiaryLiked liked = diaryLikedMapper.selectOne(param);
        if(liked == null){
            param.setCreateTime(new Date());
            Preconditions.checkState(diaryLikedMapper.insert(param) == 1,"评论点赞失败");
            return 1;
        }else {
            Preconditions.checkState(diaryLikedMapper.delete(param) == 1,"取消点赞失败");
            return 0;
        }

    }

    public void addScanNum(Long diaryId, String ip, Long accountId) {

        DiaryScan scan = new DiaryScan();
//        if(accountId != -1L) {
//            scan.setAccountId(String.valueOf(accountId));
//        }else{
//            scan.setAccountId(ip);
//        }
        scan.setDiaryId(diaryId);
//        if(diaryScanMapper.selectOne(scan) == null) {
//            diaryScanMapper.insert(scan);
//        }
        diaryScanMapper.insert(scan);
    }

    public Integer delete(Long diaryId, Long accountId) {

        Diary diary = diaryMapper.queryById(diaryId);
        Preconditions.checkNotNull(diary,"该id没有对应的日记信息");
        if(!diary.getAccountId().equals(accountId))
            return 1;

        diary.setEnable(EnableEnum.DELETE.getCode());
        diaryMapper.updateByPrimaryKeySelective(diary);
        return 0;
    }

    public Integer lock(Long diaryId, Long accountId) {
        Diary diary = diaryMapper.queryById(diaryId);
        Preconditions.checkNotNull(diary,"该id没有对应的日记信息");
        if(!diary.getAccountId().equals(accountId))
            return 1;
        diary.setIsLock(diary.getIsLock()==1?0:1);
        diaryMapper.updateByPrimaryKeySelective(diary);
        return 0;

    }

    public Integer queryLikedNumber(Long diaryId) {

        return diaryLikedMapper.queryCountByDiaryId(diaryId);
    }

    public PageInfo<Diary> queryByAccountId(Long accountId, Integer startPage, Integer pageSize) {

        PageHelper.startPage(startPage,pageSize).setOrderBy("id desc");
        List<Diary> diaryList = diaryMapper.queryByAccountId(accountId);
        PageInfo<Diary> pageInfo = new PageInfo<>(diaryList);
        return pageInfo;
    }

}
