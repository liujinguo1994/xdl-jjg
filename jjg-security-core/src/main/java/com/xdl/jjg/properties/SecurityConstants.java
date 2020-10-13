/**
 *
 */
package com.xdl.jjg.properties;

/**
 * @author zhailiang
 */
public interface SecurityConstants {

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";

    /**
     * 当请求需要身份认证时，默认跳转的url
     *
     * @see
     */
    String DEFAULT_UNAUTHENTICATION_URL = "/authentication/require";

    /**
     * 默认的用户名密码登录请求处理url
     */
    String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";

    /**
     * 默认的手机验证码登录请求处理url
     */
    String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/authentication/mobile";
    /**
     * 默认密钥登录请求处理url
     */
    String DEFAULT_LOGIN_PROCESSING_URL_SECRET = "/authentication/secret";

    /**
     * 默认登录页面
     *
     * @see
     */
    String DEFAULT_LOGIN_PAGE_URL = "/security-signIn.html";

    /**
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";

    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";

    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";

    /**
     * 密钥 传递密钥的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_SECRET = "secret";

    /**
     * session失效默认的跳转地址
     */
    String DEFAULT_SESSION_INVALID_URL = "/security-sessionInvalid.html";

    /**
     * 使用openid登录时的请求url
     */
    String DEFAULT_OPEN_ID_URL = "/authentication/openId";

    /**
     * 使用openId登录时的key1
     */
    String DEFAULT_PARAMETER_NAME_OPENID = "openId";

    /**
     * 使用openId登录时的key2
     */
    String DEFAULT_PARAMETER_NAME_PROVIDERID = "providerId";


}
