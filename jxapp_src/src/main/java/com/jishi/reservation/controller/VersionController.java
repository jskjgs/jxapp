package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.dao.models.AndroidVersion;
import com.jishi.reservation.service.VersionService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/version")
@Slf4j
@Api(description = "版本接口")
public class VersionController extends MyBaseController {



    @Autowired
    VersionService versionService;


    /**
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "安卓 检查更新", notes = "")
    @RequestMapping(value = "checkUpdateForAn", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkUpdateForAndroid(
            @ApiParam(value = "版本 暂时随便填") @RequestParam(value = "version") String version,
            @ApiParam(value = "类型 默认来自安卓,0是安卓，1是IOS") @RequestParam(value = "type",defaultValue = "0") Integer type

    ) throws Exception {

        AndroidVersion androidVersion = versionService.checkUpdateForAndroid(version);


        return ResponseWrapper().addData(androidVersion).addMessage("请求成功!").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

}
