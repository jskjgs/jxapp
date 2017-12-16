package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.dao.models.Banner;
import com.jishi.reservation.service.HomeService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.support.AliOssSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/home")
@Slf4j
@Api(description = "首页相关接口")
public class HomeController extends MyBaseController {

    @Autowired
    HomeService homeService;

    @Autowired
    AliOssSupport ossSupport;


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
            @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc) throws Exception{

        PageInfo<Banner> bannerList = homeService.queryBannerPageInfo(null,name,enable, Paging.create(pageNum,pageSize,orderBy,desc));
        return ResponseWrapper().addData(bannerList).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

}
