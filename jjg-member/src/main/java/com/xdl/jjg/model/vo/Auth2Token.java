package com.xdl.jjg.model.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 信任登录用户信息
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-18
 */
@Data
@ToString
public class Auth2Token implements Serializable {


    /**
     * 开放用户ID
     */
    private String unionid;

    /**
     * opneid
     */
    private String opneId;

    /**
     * OpenId类型
     */
    private String type;

    /**
     * 获取openid的令牌
     */
    private String accessToken;



}
