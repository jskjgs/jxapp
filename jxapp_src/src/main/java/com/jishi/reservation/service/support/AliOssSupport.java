package com.jishi.reservation.service.support;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.jishi.reservation.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sloan on 2017/9/3.
 */

@Service
@Slf4j
public class AliOssSupport {


    public String uploadImage(MultipartFile file,String folderPostition) throws IOException {


        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(Constant.END_POINT, Constant.ACCESS_KEY_ID, Constant.ACCESS_KEY_SECRET);

        InputStream fileInputStream = file.getInputStream();
        String filePath = folderPostition+file.getOriginalFilename();
        PutObjectResult putObjectResult = ossClient.putObject(Constant.BUCKET_NAME, filePath, fileInputStream);
        log.info(JSONObject.toJSONString(putObjectResult));
        // 关闭client
        ossClient.shutdown();

        return Constant.BATH_ALI_URL + filePath;
    }


}
