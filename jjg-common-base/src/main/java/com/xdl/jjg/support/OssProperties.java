package com.xdl.jjg.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/23 10:00
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssProperties {

    /**
     * AccessKeyId
     */
    private String accessId;

    /**
     * AccessKeySecret
     */
    private String accessKey;

    /**
     * endpoint
     */
    private String endpoint;

    /**
     * bucketName
     */
    private String bucket;

    /**
     * host
     */
    private String host;

    /**
     * 上传回调服务器的url
     */
    private String callbackUrl;

    /**
     *用户上传文件时制定的前缀
     */
    private String dir;

    private String cdnUrl;


}
