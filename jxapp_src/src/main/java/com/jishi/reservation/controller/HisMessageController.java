package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.service.HisMessageService;
import com.jishi.reservation.service.OutpatientQueueService;
import com.jishi.reservation.service.his.HisTool;
import com.jishi.reservation.service.jinxin.bean.QueueDetail;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Created by liangxiong on 2017/12/21.
 */
@RestController
@Slf4j
@Api(description = "his消息通知接口")
public class HisMessageController extends MyBaseController {

    @Autowired
    private HisMessageService hisMessageService;

    @Autowired
    private HisTool hisTool;

    @Autowired
    private OutpatientQueueService outpatientQueueService;

    private static final String HIS_MSG_PARAM_SERVICE_NAME = "SERVICE";
    private static final String HIS_MSG_PARAM_DATA = "DATAPARAM";


    @ApiOperation(value = "his消息通知接口")
    @RequestMapping(value = "/hisMessage/call", method = RequestMethod.POST)
    @ResponseBody
    public String call(HttpServletRequest request) throws Exception {

        String paramStr = getRequestBody(request);
        Map paramMap = request.getParameterMap();
        log.info(">>>>>>>>>>>>his消息通知数据-body：" + paramStr);
        log.info(">>>>>>>>>>>>his消息通知数据-param：" + JSONObject.toJSONString(paramMap));
        if (paramStr == null || paramStr.isEmpty()) {
            paramStr = (String) paramMap.get("CALLDATA");
        }
        boolean rslt = true;
        String message = null;
        if (paramStr != null && !paramStr.isEmpty()) {
            String serviceName = hisTool.getXmlAttribute(paramStr, HIS_MSG_PARAM_SERVICE_NAME);
            String serviceData = hisTool.getXmlAttribute(paramStr, HIS_MSG_PARAM_DATA);
            try {
                hisMessageService.executeMessage(serviceName, serviceData);
            } catch (Exception e) {
                rslt = false;
                message = e.getMessage();
                log.info(">>>>>>>>>>>>his消息处理异常");
                e.printStackTrace();
            }
        }

        return hisMessageRetrunData(rslt, message);
    }

    @ApiOperation(value = "排队叫号通知接口")
    @RequestMapping(value = "/queue/notification", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queueNotification(HttpServletRequest request) throws Exception {
        String token = request.getHeader(Constant.QUEUE_TOKEN_HEADER);
        if (token == null || !Constant.QUEUE_TOKEN.equals(token)) {
            return ResponseWrapper().addMessage("failed").ExeFaild();
        }
        String paramStr = getRequestBody(request);
        log.info(">>>>>>>>>>>>排队叫号通知数据-body：" + paramStr);
        List<QueueDetail> queueDetailList = JSONObject.parseArray(paramStr, QueueDetail.class);
        outpatientQueueService.queueNotification(queueDetailList);
        return ResponseWrapper().addMessage("success").ExeSuccess();
    }

    private String getRequestBody(HttpServletRequest request) throws Exception {
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
        return sb.toString();
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
