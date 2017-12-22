package com.jishi.reservation.controller;

import com.jishi.reservation.controller.base.MyBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by liangxiong on 2017/12/21.
 */
@RestController
@RequestMapping("/hisMessage")
@Slf4j
@Api(description = "his消息通知接口")
public class HisMessageController extends MyBaseController {

    @ApiOperation(value = "his消息通知接口")
    @RequestMapping(value = "/call", method = RequestMethod.POST)
    @ResponseBody
    public String call(HttpServletRequest request) throws Exception {
        //读取参数
        InputStream inputStream ;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s ;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null){
            sb.append(s);
        }
        in.close();
        inputStream.close();
        log.info(sb.toString());
        return hisMessageRetrunDataSuccess();
    }

    private  String hisMessageRetrunDataSuccess() {
        return hisMessageRetrunData(true, null);
    }

    private  String hisMessageRetrunData(boolean success, String message) {
        StringBuffer sb = new StringBuffer();
        sb.append("<ROOT>");
        sb.append("<STATE><![CDATA[");
        sb.append(success ? "T" : "F");
        sb.append("]]></STATE>");
        if (!success) {
            sb.append("<ERROR><MSG><![CDATA[");
            sb.append(message);
            sb.append("]]></MSG></ERROR>");
        }
        sb.append("</ROOT>");
        return sb.toString();
    }

}
