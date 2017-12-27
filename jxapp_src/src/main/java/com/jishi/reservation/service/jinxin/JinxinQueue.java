package com.jishi.reservation.service.jinxin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doraemon.base.protocol.http.HttpAgent;
import com.jishi.reservation.service.jinxin.bean.QueueCurrentNumber;
import com.jishi.reservation.service.jinxin.bean.QueueDetail;
import com.jishi.reservation.util.Constant;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 锦欣医院排队服务接口
 * Created by liangxiong on 2017/11/14.
 */
@Log4j
@Service
public class JinxinQueue {

    public List<QueueDetail> queryQueueByDepartmentId(String departmentId) {
        String result = null;
        try {
            result = HttpAgent.create().sendGet(Constant.QUEUE_URL, departmentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && !result.isEmpty()) {
            JSONObject jsonObject = JSON.parseObject(result);
            Object code = jsonObject.get("code");
            if (code.equals(200) || code.equals("200")) {
                String data = jsonObject.getString("data");
                List<QueueDetail> queueDetailList = JSON.parseArray(data, QueueDetail.class);
                return queueDetailList == null ? Collections.emptyList() : queueDetailList;
            }
            log.info("获取部门id为" + departmentId + "的分诊排队队列信息失败：" + jsonObject.get("msg"));
        }
        return Collections.emptyList();
    }

}
