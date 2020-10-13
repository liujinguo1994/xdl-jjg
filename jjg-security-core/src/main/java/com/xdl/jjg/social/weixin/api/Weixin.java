package com.xdl.jjg.social.weixin.api;

/**
 * @Author: ciyuan
 * @Date: 2019/5/26 4:10
 * 微信获取用户信息的接口
 */
public interface Weixin {

    /**
     * 获取微信的用户信息
     *
     * @param openId
     * @return
     */
    WeixinUserInfo getUserInfo(String openId);

}
