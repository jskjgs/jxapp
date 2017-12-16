package com.jishi.reservation.otherService.im.neteasy.operation;

import com.alibaba.fastjson.JSONArray;
import com.jishi.reservation.otherService.im.HttpParam;
import com.jishi.reservation.otherService.im.neteasy.IMHttpNeteasy;
import com.jishi.reservation.otherService.im.neteasy.model.IMTeam;
import com.jishi.reservation.otherService.im.neteasy.model.IMUser;
import com.jishi.reservation.otherService.im.neteasy.response.ResUserCreate;

/**
 * Created by liangxiong on 2017/10/24.
 */
public class TeamOperation {

    public static final String TEAM_CREATE = "/team/create.action";
    public static final String TEAM_ADD = "/team/add.action";
    public static final String TEAM_KICK = "/team/kick.action";
    public static final String TEAM_REMOVE = "/team/remove.action";
    public static final String TEAM_UPDATE = "/team/update.action";
    public static final String TEAM_QUERY = "/team/query.action";
    public static final String TEAM_CHANGE_OWNER = "/team/changeOwner.action";
    public static final String TEAM_ADD_MANAGER = "/team/addManager.action";
    public static final String TEAM_REMOVE_MANAGER = "/team/removeManager.action";
    public static final String TEAM_JOIN_TEAMS = "/team/joinTeams.action";
    public static final String TEAM_UPDA_TETEAM_NICK = "/team/updateTeamNick.action";
    public static final String TEAM_MUTE_TEAM = "/team/muteTeam.action";
    public static final String TEAM_MUTET_LIST = "/team/muteTlist.action";
    public static final String TEAM_LEAVE = "/team/leave.action";
    public static final String TEAM_MUTET_LIST_ALL = "/team/muteTlistAll.action";
    public static final String TEAM_LIST_TEAM_MUTE = "/team/listTeamMute.action";

    private IMHttpNeteasy imHttpNeteasy;

    public TeamOperation(IMHttpNeteasy imHttpNeteasy) {
        this.imHttpNeteasy = imHttpNeteasy;
    }

    //创建群
    public void createTeam(IMTeam team) throws Exception {
        if (team == null) {
            //return null;
        }
        HttpParam httpParam = new HttpParam();
        //httpParam.add("accid", user.getAccid(), 32);
        String httpRslt = imHttpNeteasy.doPost(TEAM_CREATE, httpParam);
        ResUserCreate userCreate = JSONArray.parseObject(httpRslt, ResUserCreate.class);
        //return userCreate.getInfo();
    }
}
