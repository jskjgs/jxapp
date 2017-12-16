package com.jishi.reservation.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jishi.reservation.dao.mapper.AndroidVersionMapper;
import com.jishi.reservation.dao.models.AndroidVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sloan on 2017/11/9.
 */

@Service
public class VersionService {

    @Autowired
    AndroidVersionMapper androidVersionMapper;

    public AndroidVersion checkUpdateForAndroid(String version) {

        AndroidVersion androidVersion = androidVersionMapper.checkUpdateForAndroid();
        Gson gson = new Gson();

        List<String> list = gson.fromJson(androidVersion.getUpdateContent(),
                new TypeToken<List<String>>() {
                }.getType());
        androidVersion.setContentList(list);
        androidVersion.setUpdateContent(null);
        //如果当前版本和给的版本一致，就不更新；不一致就返回

        androidVersion.setIsNeedUpdate(!androidVersion.getCurrentVersion().equals(version));

        return androidVersion;

    }
}
