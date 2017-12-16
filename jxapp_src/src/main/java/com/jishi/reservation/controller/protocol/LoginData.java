package com.jishi.reservation.controller.protocol;

import com.jishi.reservation.dao.models.Credentials;
import com.jishi.reservation.dao.models.IdentityInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by sloan on 2017/9/7.
 */

@Data
public class LoginData {

    private String token;

    private String nickname;
    private String headPortrait;
    private String telephone;
    private String pushId;
    private Long accountId;

    private List<IdentityInfo> identityInfoList;
    private List<Credentials> credentialsList;
}
