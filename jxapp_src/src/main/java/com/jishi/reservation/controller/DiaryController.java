package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;

import com.jishi.reservation.controller.protocol.DiaryContentVO;
import com.jishi.reservation.dao.models.Diary;

import com.jishi.reservation.service.DiaryService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;

import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/diray")
@Slf4j
@Api(description = "日记接口")
public class DiaryController extends MyBaseController {


    @Autowired
    private DiaryService diaryService;


    @ApiOperation(value = "app 查看 日记",response = DiaryContentVO.class)
    @RequestMapping(value = "scan", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject scan(
            @ApiParam(value = "日记的id") @RequestParam(value = "id") Long id
    ){

        log.info("查看日记 id:"+id);
        Diary diary = diaryService.queryById(id);
        return ResponseWrapper().addMessage("查询成功").addData(diary).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

    }


    @ApiOperation(value = "app 用户发布日记/支持修改 传diaryId就是修改")
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject top(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "日记的标题") @RequestParam(value = "title",required = false) String title,
            @ApiParam(value = "日记的内容 json格式保存   eg:[{\"fontName\":\"宋体\",\"lineSpace\":10,\"fontSize\":10,\"text\":\"我是文字\",\"type\":1,\"textColor\":\"red\"},{\"width\":200,\"type\":0,\"url\":\"http://jishikeji-hospital.oss-cn-shenzhen.aliyuncs.com/image/doctor/WechatIMG198.jpg\",\"height\":200}]",required = true)
            @RequestParam(value = "content") String content,
            @ApiParam(value = "日记的id") @RequestParam(value = "diaryId",required = false) Long diaryId,
            @ApiParam(value = "日记是否锁定  0锁定，只有自己能看  1不锁定 大家都能看") @RequestParam(value = "isLock") Integer isLock
            ) throws Exception {

        if(diaryId ==null ){
            diaryService.publish(accountId,title,content,isLock);

        } else {

            Preconditions.checkNotNull(diaryService.queryById(diaryId),"该id没有对应的日记");
            diaryService.update(diaryId,title,content,isLock);
        }
        return ResponseWrapper().addMessage("添加成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "app app的日记列表  日记圈/我的日记",response = Diary.class)
    @RequestMapping(value = "queryPage", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryPage(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "是否查\"我的日记\" 0 查，1 不查", required = false) @RequestParam(value = "isMy", defaultValue = "1") Integer isMy,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "startPage", defaultValue = "1") Integer startPage,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
            ) throws Exception {

        PageInfo<Diary> page = diaryService.queryPage(accountId,isMy,startPage,pageSize);
        return ResponseWrapper().addMessage("查询成功").addData(page).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

    }


    @ApiOperation(value = "app 给日记点赞/取消点赞")
    @RequestMapping(value = "likeDiary", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject likeDiary(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "日记的id", required = true) @RequestParam(value = "diaryId") Long diaryId) throws Exception {

        diaryService.likeDiary(diaryId, accountId);
        Integer likedNumber = diaryService.queryLikedNumber(diaryId);

        return ResponseWrapper().addMessage("操作成功").addData(likedNumber).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

    }


    @ApiOperation(value = "app 给日记增加浏览次数")
    @RequestMapping(value = "addScanNum", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addScanNum(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "日记的id", required = true) @RequestParam(value = "diaryId") Long diaryId) throws Exception {

       // String ip = IpTool.getIp(request);
        diaryService.addScanNum(diaryId,null,null);

        return ResponseWrapper().addMessage("增加成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "app 删除 日记 token传递，如果不是发布者删除，会提示错误信息")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "日记的id ",required = true) @RequestParam(value = "diaryId") Long diaryId
    ) throws Exception {
        Integer state = diaryService.delete(diaryId,accountId);
        switch (state){
            case 0:
                return ResponseWrapper().addMessage("操作成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
            case 1:
                return ResponseWrapper().addMessage("您无权执行该操作").ExeSuccess(ReturnCodeEnum.FAILED.getCode());

        }
        return null;
    }


    @ApiOperation(value = "app 上锁/解锁 日记 token传递，如果不是发布者上锁，会提示错误信息")
    @RequestMapping(value = "lock", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject lock(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "日记的id") @RequestParam(value = "diaryId") Long diaryId
    ) throws Exception {
        Integer state = diaryService.lock(diaryId,accountId);
        switch (state){
            case 0:
                return ResponseWrapper().addMessage("操作成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
            case 1:
                return ResponseWrapper().addMessage("您无权执行该操作").ExeSuccess(ReturnCodeEnum.FAILED.getCode());

        }
        return null;
    }

}
