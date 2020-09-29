package com.xdl.jjg.social.weixinapp.connect;

import com.xdl.jjg.social.weixin.api.WeixinUserInfo;
import com.xdl.jjg.social.weixinapp.api.WeixinApp;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/12 10:40
 */
public class WeixinAppAdapter implements ApiAdapter<WeixinApp> {

    private String openId;

    public WeixinAppAdapter() {
    }

    public WeixinAppAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(WeixinApp api) {
        return true;
    }

    @Override
    public void setConnectionValues(WeixinApp api, ConnectionValues values) {
        WeixinUserInfo userInfo = api.getUserInfo(openId);
        values.setProviderUserId(userInfo.getOpenid());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
        values.setProfileUrl(userInfo.getUnionid());
    }

    @Override
    public UserProfile fetchUserProfile(WeixinApp api) {
        return null;
    }

    @Override
    public void updateStatus(WeixinApp api, String message) {
        //TODO do nothing
    }
}
