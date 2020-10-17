package com.xdl.jjg.oss.config;

import com.aliyun.oss.OSSClient;
import com.xdl.jjg.oss.OssClientFactoryBean;
import com.xdl.jjg.oss.OssProperties;
import com.xdl.jjg.oss.upload.OSSUploader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author: HQL 236154186@qq.com
 * @since: 2019/6/17 14:19
 */
//@Configuration
@ConditionalOnClass({OSSClient.class})
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfig {

    private final OssProperties ossProperties;

    public OssAutoConfig(final OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @Bean
    public OssClientFactoryBean ossClientFactoryBean() {
        final OssClientFactoryBean factoryBean = new OssClientFactoryBean();
        factoryBean.setEndpoint(this.ossProperties.getEndpoint());
        factoryBean.setAccessKeyId(this.ossProperties.getAccessKeyId());
        factoryBean.setAccessKeySecret(this.ossProperties.getAccessKeySecret());
        factoryBean.setBucketName(this.ossProperties.getBucketName());
        factoryBean.setDirectory(this.ossProperties.getDirectory());
        return factoryBean;
    }

    @Bean
    public OSSUploader ossUploader(){
        final OSSUploader ossUploader = new OSSUploader();
        return ossUploader;
    }
}
