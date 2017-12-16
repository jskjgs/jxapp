package com.jishi.reservation.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.dao.models.Banner;
import com.jishi.reservation.service.HomeService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.enumPackage.ReturnMessageEnum;
import com.jishi.reservation.service.support.AliOssSupport;
import com.jishi.reservation.service.support.FileSupport;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/ad/home")
@Slf4j
@Api(description = "后台管理平台的的首页(banner)接口")
public class AdminHomeController extends MyBaseController {



    @Autowired
    HomeService homeService;

    @Autowired
    AliOssSupport ossSupport;


    //*******************  banner  *********************************
    @ApiOperation(value = "增加banner")
    @RequestMapping(value = "addBanner", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject addBanner(
            @ApiParam(value = "banner 名称", required = true) @RequestParam(value = "name", required = true) String name,
            @ApiParam(value = "banner 图片"  )@RequestParam(value = "file")MultipartFile file,
            @ApiParam(value = "跳转的url", required = true) @RequestParam(value = "jumpUrl", required = true) String jumpUrl,
            @ApiParam(value = "序号", required = true) @RequestParam(value = "orderNumber", required = true) Integer orderNumber
    ) throws Exception {
        Preconditions.checkNotNull(name,"请传入必须的参数：name");
        Preconditions.checkNotNull(jumpUrl,"请传入必须的参数：jumpUrl");
        Preconditions.checkNotNull(orderNumber,"请传入必须的参数：orderNumber");
        if(FileSupport.checkImageFile(file.getOriginalFilename(), file)) {
            String fileUrl = ossSupport.uploadImage(file, Constant.BANNER_PATH);
            homeService.addBanner(name,fileUrl, jumpUrl,orderNumber);
            return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
        }else {
            return ResponseWrapper().addMessage(ReturnMessageEnum.FILE_NOT_FIX.getMessage()).ExeFaild(ReturnCodeEnum.FAILED.getCode());
        }

    }

    @ApiOperation(value = "修改banner")
    @RequestMapping(value = "modifyBanner", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject modifyBanner(
            @ApiParam(value = "banner的ID", required = true) @RequestParam(value = "bannerId", required = true) Long bannerId,
            @ApiParam(value = "banner 名称", required = true) @RequestParam(value = "name", required = true) String name,
            @ApiParam(value = "跳转的url", required = true) @RequestParam(value = "jumpUrl", required = true) String jumpUrl,
            @ApiParam(value = "banner 图片"  )@RequestParam(value = "file",required = false)MultipartFile file,
            @ApiParam(value = "序号", required = false) @RequestParam(value = "orderNumber", required = false) Integer orderNumber) throws Exception {

        Preconditions.checkNotNull(bannerId,"请传入必须的参数：bannerId");
        Preconditions.checkNotNull(name,"请传入必须的参数：name");
        Preconditions.checkNotNull(jumpUrl,"请传入必须的参数：jumpUrl");


        if(file != null) {
            if(FileSupport.checkImageFile(file.getOriginalFilename(), file)) {
                String fileUrl = ossSupport.uploadImage(file, Constant.BANNER_PATH);
                homeService.modifyBanner(bannerId, name,fileUrl, jumpUrl,orderNumber);
            }else {
                return ResponseWrapper().addMessage(ReturnMessageEnum.FILE_NOT_FIX.getMessage()).ExeFaild(ReturnCodeEnum.FAILED.getCode());

            }

        }else {
            homeService.modifyBanner(bannerId, name,null, jumpUrl,orderNumber);

        }

        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "查询单个banner", response = Banner.class)
    @RequestMapping(value = "queryBanner", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryBanner(
            @ApiParam(value = "有效否(为空 查询全部  0 正常 1 禁用 99 删除)", required = false) @RequestParam(value = "enable", required = false) Integer enable,
            @ApiParam(value = "banner的ID", required = true) @RequestParam(value = "bannerId", required = true) Long bannerId) throws Exception {

        Preconditions.checkNotNull(bannerId,"请传入必须的参数：bannerId");
        List<Banner> bannerList = homeService.queryBanner(bannerId,null,enable);
        Banner banner = bannerList ==null || bannerList.size() == 0 ? null : bannerList.get(0);
        return ResponseWrapper().addData(banner).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "查询全部banner", response = Banner.class)
    @RequestMapping(value = "queryAllBanner", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryAllBanner(
            @ApiParam(value = "名称 关键字", required = false) @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "有效否(为空 查询全部  0 正常 1 禁用 99 删除)", required = false) @RequestParam(value = "enable", required = false) Integer enable,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "排序", required = false) @RequestParam(value = "orderBy", required = false) String orderBy,
            @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc) throws Exception

    {
        PageInfo<Banner> bannerList = homeService.queryBannerPageInfo(null,name,enable, Paging.create(pageNum,pageSize,orderBy,desc));
        return ResponseWrapper().addData(bannerList).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }




    @ApiOperation(value = "删除单个banner")
    @RequestMapping(value = "deleteBanner", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject deleteBanner(
            @ApiParam(value = "banner的图片ID", required = true) @RequestParam(value = "bannerId", required = true) Long bannerId) throws Exception {
        homeService.deleteBanner(bannerId);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "隐藏/显示 单个banner")
    @RequestMapping(value = "hideOrShowBanner", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject hideOrShowBanner(
            @ApiParam(value = "banner的图片ID", required = true) @RequestParam(value = "bannerId", required = true) Long bannerId) throws Exception {
        Preconditions.checkNotNull(bannerId,"请传入必须的参数：bannerId");

        homeService.hideOrShowBanner(bannerId);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "批量删除banner")
    @RequestMapping(value = "deleteBannerBatch", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject deleteBannerBatch(
            @ApiParam(value = "banner的ID  ','分隔", required = true) @RequestParam(value = "bannerIdList", required = true) String bannerIdList) throws Exception {
        Preconditions.checkNotNull(bannerIdList,"请传入必须的参数：bannerIdList");

        homeService.deleteBannerBatch(bannerIdList);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "置顶banner")
    @RequestMapping(value = "topBanner", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject topBanner(
            @ApiParam(value = "banner的ID ", required = true) @RequestParam(value = "bannerId", required = true) Long bannerId,
            @ApiParam(value = "用户输入的排序  越大越靠前", required = true) @RequestParam(value = "sort", required = true) Integer sort ) throws Exception {

        Preconditions.checkNotNull(bannerId,"请传入必须的参数：bannerId");
        Preconditions.checkNotNull(sort,"请传入必须的参数：sort");
        homeService.sortBanner(bannerId,sort);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }



}
