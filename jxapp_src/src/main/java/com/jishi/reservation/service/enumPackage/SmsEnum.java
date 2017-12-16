package com.jishi.reservation.service.enumPackage;

/**
 * Created by sloan on 2017/9/1.
 */
public enum  SmsEnum {

    LOGIN_REGISTER("SMS_90995042","登录注册验证码"),
    CHANGE_BUNDLE_TELEPHONE_OLD("SMS_90975048","换绑手机号_原手机号"),
    CHANGE_BUNDLE_TELEPHONE_NEW("SMS_95035004","换绑手机号_新手机号");

    private String templateCode;
    private String name;
    SmsEnum(String templateCode,String name){
        this.templateCode = templateCode;
        this.name = name;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
