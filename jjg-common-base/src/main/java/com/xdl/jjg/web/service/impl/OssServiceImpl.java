package com.xdl.jjg.web.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.xdl.jjg.support.OssProperties;
import com.xdl.jjg.web.service.OssService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/24日
 * @Description 阿里云文件上传服务
 */
@Service
public class OssServiceImpl implements OssService {


//    public static final String OSS_ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";
//    public static final String OSS_ACCESS_KEY_ID = "rvCyNiasyIq1JGHD";
//    public static final String OSS_ACCESS_KEY_SECRET = "VzKWi0s3y3CXSzqKr7b8E1NBmYGNgo";
//    public static final String OSS_BUCKET_NAME = "bucket-cl-jjg20190624";

    @Autowired
    private OssProperties ossProperties;


    private OSSClient getOSSClient() {
        OSSClient ossClient = new OSSClient(ossProperties.getEndpoint(),
                ossProperties.getAccessId(), ossProperties.getAccessKey());
        return ossClient;
    }


    @Override
    public String putObject(File file) {
        String keyFormat = "%s/%d/%d/%d/%s";
        LocalDate now = LocalDate.now();
        String key = String.format(keyFormat, "jjg/auction/main", now.getYear(), now.getMonthValue(),
                now.getDayOfMonth(), UUID.randomUUID().toString()
                        .replaceAll("-", "") + "." + FilenameUtils.getExtension(file.getName()));

        OSSClient ossClient = getOSSClient();
        PutObjectResult result = ossClient.putObject(ossProperties.getBucket(), key, file);
        key = ossProperties.getCdnUrl() + "/" + key;
        System.out.println("result:" + result);
        return key;
    }

}
