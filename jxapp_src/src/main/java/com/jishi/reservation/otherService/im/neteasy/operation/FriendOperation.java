package com.jishi.reservation.otherService.im.neteasy.operation;

import com.alibaba.fastjson.JSONArray;
import com.jishi.reservation.otherService.im.HttpParam;
import com.jishi.reservation.otherService.im.neteasy.IMHttpNeteasy;
import com.jishi.reservation.otherService.im.neteasy.model.IMFriend;
import com.jishi.reservation.otherService.im.neteasy.model.IMFriendMuteBlack;
import com.jishi.reservation.otherService.im.neteasy.response.ResComm;
import com.jishi.reservation.otherService.im.neteasy.response.ResFriendGet;
import com.jishi.reservation.otherService.im.neteasy.response.ResFriendMuteBlack;

import java.util.Collections;
import java.util.List;


/**
 * Created by liangxiong on 2017/10/24.
 */
public class FriendOperation {
    private static final String FRIEND_ADD = "/friend/add.action";
    private static final String FRIEND_UPDATE = "/friend/update.action";
    private static final String FRIEND_DELETE = "/friend/delete.action";
    private static final String FRIEND_GET = "/friend/get.action";
    private static final String FRIEND_SET_SPECIAL_RELATION = "/friend/setSpecialRelation.action";
    private static final String FRIEND_LIST_BLACK_AND_MUTELIST = "/friend/listBlackAndMuteList.action";


    private IMHttpNeteasy imHttpNeteasy;

    public FriendOperation(IMHttpNeteasy imHttpNeteasy) {
      this.imHttpNeteasy = imHttpNeteasy;
    }

    /**
     * @description 加好友
     * @param sourceAccid 发起者accid
     * @param destAccid 接收者accid
     * @param type 1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
     * @param msg 最长256字符
     * @throws Exception
     **/
    public boolean friendAdd(String sourceAccid, String destAccid, int type,
    		String msg) throws Exception {
    	if (sourceAccid == null || destAccid == null) {
            return false;
        }
        HttpParam httpParam = new HttpParam();
        httpParam.add("accid", sourceAccid, 32);
        httpParam.add("faccid", destAccid, 32);
        httpParam.add("type", type);
        httpParam.add("msg", msg, 256);
        String httpRslt = imHttpNeteasy.doPost(FRIEND_ADD, httpParam);
        ResComm res = JSONArray.parseObject(httpRslt, ResComm.class);
        return res.isSuccess();
    }

    //更新好友相关信息
    public boolean friendUpdate(String sourceAccid, String destAccid) throws Exception {
    	return friendUpdate(sourceAccid, destAccid, null, null);
    }

    //更新好友相关信息 alias(可为空)给好友增加备注名，长度128 ex(可为空)长度256
    public boolean friendUpdate(String sourceAccid, String destAccid, String alias, String ex) throws Exception {
    	if (sourceAccid == null || destAccid == null) {
            return false;
        }
        HttpParam httpParam = new HttpParam();
        httpParam.add("accid", sourceAccid, 32);
        httpParam.add("faccid", destAccid, 32);
        if (alias != null) {
        	httpParam.add("alias", alias, 128);
        }
        if (ex != null) {
        	httpParam.add("ex", ex, 256);
        }
        String httpRslt = imHttpNeteasy.doPost(FRIEND_UPDATE, httpParam);
        ResComm res = JSONArray.parseObject(httpRslt, ResComm.class);
        return res.isSuccess();
    }

    //删除好友
    public boolean friendDelete(String sourceAccid, String destAccid) throws Exception {
    	if (sourceAccid == null || destAccid == null) {
            return false;
        }
        HttpParam httpParam = new HttpParam();
        httpParam.add("accid", sourceAccid, 32);
        httpParam.add("faccid", destAccid, 32);
        String httpRslt = imHttpNeteasy.doPost(FRIEND_DELETE, httpParam);
        ResComm res = JSONArray.parseObject(httpRslt, ResComm.class);
        return res.isSuccess();
    }

    //获取好友关系, 查询某时间点起到现在有更新的双向好友       是否应该返回状态码？？？
    //updatetime更新时间戳，接口返回该时间戳之后有更新的好友列表
    //createtime【Deprecated】定义同updatetime
    public List<IMFriend> friendGet(String accid, Long updatetime, Long createtime) throws Exception {
    	if (accid == null) {
            return Collections.emptyList();
        }
        HttpParam httpParam = new HttpParam();
        httpParam.add("accid", accid, 32);
        httpParam.add("updatetime", updatetime);
        httpParam.add("createtime", createtime);
        String httpRslt = imHttpNeteasy.doPost(FRIEND_GET, httpParam);
        ResFriendGet res = JSONArray.parseObject(httpRslt, ResFriendGet.class);
        if (res.getCode() != 200) {
        	return Collections.emptyList();
        }
        return res.getFriends();
    }

    //设置黑名单/静音
    //relationType 本次操作的关系类型,1:黑名单操作，2:静音列表操作
    //value 操作值，0:取消黑名单或静音，1:加入黑名单或静音
    public boolean setSpecialRelation(String accid, String targetAccId, int relationType, int value) throws Exception {
    	if (accid == null || targetAccId == null) {
            return false;
        }
        HttpParam httpParam = new HttpParam();
        httpParam.add("accid", accid, 32);
        httpParam.add("targetAcc", targetAccId, 32);
        httpParam.add("relationType", relationType);
        httpParam.add("value", value);
        String httpRslt = imHttpNeteasy.doPost(FRIEND_SET_SPECIAL_RELATION, httpParam);
        ResComm res = JSONArray.parseObject(httpRslt, ResComm.class);
        return res.isSuccess();
    }

    //查看指定用户的黑名单和静音列表
    public IMFriendMuteBlack listBlackAndMuteList(String accid) throws Exception {
    	if (accid == null) {
            return null;
        }
        HttpParam httpParam = new HttpParam();
        httpParam.add("accid", accid, 32);
        String httpRslt = imHttpNeteasy.doPost(FRIEND_LIST_BLACK_AND_MUTELIST, httpParam);
        ResFriendMuteBlack res = JSONArray.parseObject(httpRslt, ResFriendMuteBlack.class);
        if (res.getCode() != 200) {
        	return null;
        }
        IMFriendMuteBlack friendMuteBlack = new IMFriendMuteBlack();
        friendMuteBlack.setBlacklist(res.getBlacklist());
        friendMuteBlack.setMutelist(res.getMutelist());
        return friendMuteBlack;
    }
}
