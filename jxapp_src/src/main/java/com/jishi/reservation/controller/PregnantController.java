package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.dao.models.*;
import com.jishi.reservation.service.*;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
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
@RequestMapping("/pregnant")
@Slf4j
@Api(description = "孕妇信息相关接口")
public class PregnantController extends MyBaseController {

    @Autowired
    PregnantService pregnantService;


    @ApiOperation(value = "更新孕妇信息")
    @RequestMapping(value = "updatePregnant", method =RequestMethod.POST )
    @ResponseBody
    public JSONObject updatePregnant(
            @ApiParam(value = "就诊人的ID", required = false) @RequestParam(value = "patientId", required = false) Long patientId,
            @ApiParam(value = "孕妇姓名", required = false) @RequestParam(value = "name", required = false) String  name,
            @ApiParam(value = "出生年月", required = false) @RequestParam(value = "birth", required = false) Long birth,
            @ApiParam(value = "现居地址", required = false) @RequestParam(value = "livingAddress", required = false) String livingAddress,
            @ApiParam(value = "末次月经时间", required = false) @RequestParam(value = "lastMenses", required = false) Long lastMenses,
            @ApiParam(value = "联系电话", required = false) @RequestParam(value = "telephone", required = false) String telephone,
            @ApiParam(value = "丈夫姓名", required = false) @RequestParam(value = "husbandName", required = false) String  husbandName,
            @ApiParam(value = "丈夫电话", required = false) @RequestParam(value = "husbandTelephone", required = false) String husbandTelephone,
            @ApiParam(value = "备注", required = false) @RequestParam(value = "remark", required = false) String remark

    ) throws Exception {

        pregnantService.updatePregnant(patientId,name,birth,livingAddress,lastMenses,telephone,husbandName,husbandTelephone,EnableEnum.EFFECTIVE.getCode(),remark);

        return  ResponseWrapper().addMessage("孕妇信息修改成功.").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "查询指定孕妇信息 还没看到姓名的模糊搜索要求，所以是精确匹配姓名",response = Pregnant.class)
    @RequestMapping(value = "queryPregnant", method =RequestMethod.GET )
    @ResponseBody
    public JSONObject queryPregnant(
            @ApiParam(value = "孕妇信息ID", required = false) @RequestParam(value = "pregnantId", required = false) Long pregnantId,
            @ApiParam(value = "病人ID", required = false) @RequestParam(value = "patientinfoId", required = false) Long patientinfoId,
            @ApiParam(value = "孕妇姓名", required = false) @RequestParam(value = "name", required = false) String  name
    ) throws Exception {

        List<Pregnant> list = pregnantService.queryPregnant(pregnantId,patientinfoId,name,EnableEnum.EFFECTIVE.getCode());

        return  ResponseWrapper().addMessage("查询成功.").addData(list).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "删除孕妇  ")
    @RequestMapping(value = "deletePregnant", method ={RequestMethod.POST,RequestMethod.DELETE} )
    @ResponseBody
    public JSONObject deletePregnant(
            @ApiParam(value = "孕妇信息ID", required = true) @RequestParam(value = "pregnantId", required = true) Long pregnantId
    ) throws Exception {
        Preconditions.checkNotNull(pregnantId,"请传入必须的参数：patientInfoId");
        pregnantService.deleteById(pregnantId);

        return  ResponseWrapper().addMessage("操作成功.").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


}
