package com.xdl.jjg.social.weixinapp.api;

import com.xdl.jjg.social.weixin.api.WeixinUserInfo;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/12 9:37
 */
public interface WeixinApp {

    /**
     * 获取微信的用户信息
     *
     * @param openId
     * @return
     */
    WeixinUserInfo getUserInfo(String openId);

}
