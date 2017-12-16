package com.jishi.reservation.otherService.im.neteasy.response;

import java.util.List;

import com.jishi.reservation.otherService.im.neteasy.model.IMFriend;
import lombok.Data;

/**
 * Created by liangxiong on 2017/10/26.
 */
@Data
public class ResFriendGet {
	private int code;
	private int size;
	private List<IMFriend> friends;
}
