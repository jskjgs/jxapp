package com.jishi.reservation.otherService.im.neteasy.model;

import lombok.Data;

/**
 * Created by liangxiong on 2017/10/26.
 */
@Data
public class IMTeam {
	private String tid;		//群唯一标识，创建群时会返回
	private String tname;	//群名称，最大长度64字符
	private String owner;	//群主用户帐号，最大长度32字符
	private String announcement;	//群公告，最大长度1024字符(非必须)
	private String intro;	//群描述，最大长度512字符(非必须)
	private int joinmode;	//群建好后，sdk操作时，0不用验证，1需要验证,2不允许任何人加入。其它返回414
	private String custom;	//自定义 最大长度1024字符
	private String icon;	//群头像，最大长度1024字符
	private int beinvitemode;	//被邀请人同意方式，0-需要同意(默认),1-不需要同意。其它返回414
	private int invitemode;		//谁可以邀请他人入群，0-管理员(默认),1-所有人。其它返回414
	private int uptinfomode;	//谁可以修改群资料，0-管理员(默认),1-所有人。其它返回414
	private int upcustommode;	//谁可以更新群自定义属性，0-管理员(默认),1-所有人。其它返回414
}
