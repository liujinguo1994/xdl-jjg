package com.xdl.jjg.test;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author sanqi
 * @version 1.0
 * @date 2019/7/24 14:51
 */

public class FileTest {

    public static final String OSS_ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";
    public static final String OSS_ACCESS_KEY_ID = "rvCyNiasyIq1JGHD";
    public static final String OSS_ACCESS_KEY_SECRET = "VzKWi0s3y3CXSzqKr7b8E1NBmYGNgo";
    public static final String OSS_BUCKET_NAME = "bucket-cl-jjg20190624";

    private static class InstanceHolder {
        private static final OSSClient instance = new OSSClient(OSS_ENDPOINT, OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET);
    }

    private static final OSSClient getInstance() {
        return FileTest.InstanceHolder.instance;
    }


    public static void main(String[] args) {

        File file = new File("C:\\Users\\A\\Desktop\\mipmap-xxhdpi\\形状 13.png");

        String keyFormat = "%s/%d/%d/%d/%s";
        String originalFilename = file.getName();
        String baseName =  FilenameUtils.getBaseName(originalFilename);
        System.out.println(baseName);
        System.out.println(FilenameUtils.getExtension(file.getName()));
        LocalDate now = LocalDate.now();
        String key = String.format(keyFormat, "message", now.getYear(), now.getMonthValue(),
                now.getDayOfMonth(),UUID.randomUUID().toString()
                        .replaceAll("-", "")+"."+FilenameUtils.getExtension(file.getName()));
        System.out.println("http://bucket-cl-jjg20190624.oss-cn-hangzhou.aliyuncs.com"+"/"+key);
        OSSClient ossClient = getInstance();
        PutObjectResult result = ossClient.putObject(OSS_BUCKET_NAME, key, file);
        System.out.println("result:"+result);

    }







}
