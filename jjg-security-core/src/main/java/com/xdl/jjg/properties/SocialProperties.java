package com.xdl.jjg.properties;

/**
 * @Author: ciyuan
 * @Date: 2019/5/25 23:01
 */
public class SocialProperties {

    private String filterProcessUrl = "/auth";

    private QQProperties qq = new QQProperties();

    private WeiXinProperties weixin = new WeiXinProperties();

    private WeiXinAppProperties weixinApp = new WeiXinAppProperties();

    public WeiXinAppProperties getWeixinApp() {
        return weixinApp;
    }

    public void setWeixinApp(WeiXinAppProperties weixinApp) {
        this.weixinApp = weixinApp;
    }

    public String getFilterProcessUrl() {
        return filterProcessUrl;
    }

    public void setFilterProcessUrl(String filterProcessUrl) {
        this.filterProcessUrl = filterProcessUrl;
    }

    public QQProperties getQq() {
        return qq;
    }

    public void setQq(QQProperties qq) {
        this.qq = qq;
    }

    public WeiXinProperties getWeixin() {
        return weixin;
    }

    public void setWeixin(WeiXinProperties weixin) {
        this.weixin = weixin;
    }
}
