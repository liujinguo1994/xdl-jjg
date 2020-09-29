package com.xdl.jjg.properties;

/**
 * @Author: ciyuan
 * @Date: 2019/5/18 20:06
 */
public class BrowserProperties {

    private String loginPage = "/security-signIn.html";

    private String signUpUrl = "/security-signUp.html";

    /**
     * 设置退出登录的url
     * 默认为空，如果为空就返回json数据给前端
     */
    private String signOutUrl;

    /**
     * 设置访问的方式 默认返回的时json
     */
    private LoginType loginType = LoginType.JSON;


    /**
     * 用户存储的token过期的时间
     * 默认时间为一周
     */
    private int rememberMeSeconds = 60 * 60 * 24 * 7;

    private SessionProperties session = new SessionProperties();

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getSignUpUrl() {
        return signUpUrl;
    }

    public void setSignUpUrl(String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }

    public SessionProperties getSession() {
        return session;
    }

    public void setSession(SessionProperties session) {
        this.session = session;
    }

    public String getSignOutUrl() {
        return signOutUrl;
    }

    public void setSignOutUrl(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }
}
