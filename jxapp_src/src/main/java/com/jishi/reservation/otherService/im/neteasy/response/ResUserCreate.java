package com.jishi.reservation.otherService.im.neteasy.response;

import com.jishi.reservation.otherService.im.neteasy.model.IMUser;
import lombok.Data;


/**
 * Created by liangxiong on 2017/10/25.
 */
@Data
public class ResUserCreate {
    private int code;
    private IMUser info;
}
