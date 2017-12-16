package com.jishi.reservation.service.support;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.extra.model.v20170525.SendSmsRequest;
import com.jishi.reservation.util.extra.model.v20170525.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by sloan on 2017/9/3.
 */

@Slf4j
@Service
public class AliDayuSupport {


    public void sendynamicCode(String phone, String code ,String templateCode) throws Exception{

        IClientProfile profile = DefaultProfile.getProfile(Constant.REGION_ID, Constant.ACCESS_KEY_ID,
                Constant.ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint(Constant.REGION_ID, Constant.REGION_ID, Constant.PRODUCT, Constant.DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);

        //短信推荐使用单条调用的方式
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(Constant.HOSPITAL_NAME);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":"+code+"}");

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        log.info("返回结果："+ JSONObject.toJSONString(sendSmsResponse));
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
//请求成功
        }
    }


}
