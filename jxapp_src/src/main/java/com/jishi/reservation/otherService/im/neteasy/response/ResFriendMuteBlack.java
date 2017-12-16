package com.jishi.reservation.otherService.im.neteasy.response;

import java.util.List;

import lombok.Data;

/**
 * Created by liangxiong on 2017/10/26.
 */
@Data
public class ResFriendMuteBlack {
    private int code;
    private List<String> mutelist;
    private List<String> blacklist;
}
