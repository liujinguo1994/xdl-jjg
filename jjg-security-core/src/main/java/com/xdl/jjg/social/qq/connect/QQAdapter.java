package com.xdl.jjg.social.qq.connect;

import com.xdl.jjg.social.qq.api.QQ;
import com.xdl.jjg.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @Author: ciyuan
 * @Date: 2019/5/25 22:18
 */
public class QQAdapter implements ApiAdapter<QQ> {

    /**
     * 测试当前的API是否可用
     * 默认true QQ永远是可以用的
     *
     * @param api
     * @return
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }

    /**
     * 适配
     *
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo userInfo = api.getUserInfo();
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        //设置个人主页，QQ不需要填写此项
        values.setProfileUrl(null);
        //设置服务商的用户ID（openId）
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    /**
     * 在某些社交网站上有用
     * QQ不需要配置
     *
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(QQ api, String message) {
        //TODO do nothing
    }
}
