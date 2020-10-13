package com.xdl.jjg.oss;

import com.aliyun.oss.OSSClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author: HQL 236154186@qq.com
 * @since: 2019/6/17 14:20
 */
public class OssClientFactoryBean implements FactoryBean<OSSClient>, InitializingBean, DisposableBean {

    private static final Log LOGER = LogFactory.getLog(OssClientFactoryBean.class);

    private OSSClient ossClient;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String directory;

    @Override
    public OSSClient getObject() throws Exception {
        return this.ossClient;
    }

    @Override
    public Class<?> getObjectType() {
        return OSSClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        if (this.ossClient != null) {
            this.ossClient.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.endpoint, "'endpoint' must be not null");
        Assert.notNull(this.accessKeyId, "'accessKeyId' must be not null");
        Assert.notNull(this.accessKeySecret, "'accessKeySecret' must be not null");
        Assert.notNull(this.accessKeySecret, "'accessKeySecret' must be not null");
        Assert.notNull(this.directory, "'directory' must be not null");
        this.ossClient = new OSSClient(this.endpoint, this.accessKeyId, this.accessKeySecret);
        LOGER.info("init OssClientFactoryBean is ok!");
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessKeyId(final String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(final String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public void setBucketName(String bucketName) { this.bucketName = bucketName;}

    public void setDirectory(String directory) { this.directory = directory; }

    public String getDirectory() { return directory; }
    public String getBucketName() {
        return bucketName;
    }
}
