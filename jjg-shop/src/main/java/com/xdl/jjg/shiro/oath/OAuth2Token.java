package com.xdl.jjg.shiro.oath;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * OAuth2Token
 */
public class OAuth2Token implements AuthenticationToken {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String uid;         //用户的标识
    private String token;           //json web token值

    public OAuth2Token(String token, String uid) {
        this.token = token;
        this.uid = uid;
    }

    public Object getPrincipal() {
        return this.uid;
    }

    public Object getCredentials() {
        return this.token;
    }
}
