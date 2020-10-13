package com.xdl.jjg.social.weixin.connect;

import com.xdl.jjg.social.weixin.api.Weixin;
import com.xdl.jjg.social.weixin.api.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @Author: ciyuan
 * @Date: 2019/5/26 4:51
 */
public class WeixinAdapter implements ApiAdapter<Weixin> {

    private String openId;

    public WeixinAdapter() {

    }

    public WeixinAdapter(String openId) {
        this.openId = openId;
    }

    /**
     * 测试当前的API是否可用
     * 默认true Weixin永远是可以用的
     *
     * @param api
     * @return
     */
    @Override
    public boolean test(Weixin api) {
        return true;
    }

    @Override
    public void setConnectionValues(Weixin api, ConnectionValues values) {
        WeixinUserInfo userInfo = api.getUserInfo(openId);
        values.setProviderUserId(userInfo.getOpenid());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
        values.setProfileUrl(userInfo.getUnionid());
    }

    @Override
    public UserProfile fetchUserProfile(Weixin api) {
        return null;
    }

    /**
     * 在某些社交网站上有用
     * Weixin不需要配置
     *
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(Weixin api, String message) {
        //TODO do nothing
    }
}
