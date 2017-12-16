package com.jishi.reservation.otherService.im.neteasy.model;

import lombok.Data;

/**
 * Created by liangxiong on 2017/10/24.
 */
@Data
public class IMUser {
    private String accid;   //云信ID，最大长度32字符，必须保证一个APP内唯一, 以接口返回结果中的accid为准
    private String name;    //昵称，最大长度64字符
    private String props;   //json属性，第三方可选填，最大长度1024
    private String icon;    //头像URL，第三方可选填，最大长度1024
    private String token;   //登录token值，最大长度128字符,如果未指定，会自动生成token
    private String sign;    //用户签名，最大长度256字符
    private String email;   //用户email，最大长度64字符
    private String birth;   //用户生日，最大长度16字符
    private String mobile;  //用户mobile，最大长度32字符
    private int gender;  //用户性别，0表示未知，1表示男，2女表示女，其它会报参数错误
    private String ex;      //用户名片扩展字段，最大长度1024字符
}
