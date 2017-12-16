package com.jishi.reservation.controller.protocol;

import lombok.Data;

import java.util.List;

/**
 * Created by sloan on 2017/5/27.
 */

@Data
public class AdminLogInfoData {

    private String token;
    private String account;
    private List<String> permissionList;

}
