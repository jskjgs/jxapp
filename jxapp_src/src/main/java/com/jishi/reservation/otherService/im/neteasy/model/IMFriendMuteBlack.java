package com.jishi.reservation.otherService.im.neteasy.model;

import java.util.List;

import lombok.Data;

/**
 * Created by liangxiong on 2017/10/25.
 */
@Data
public class IMFriendMuteBlack {
	private List<String> mutelist;
	private List<String> blacklist;
}
