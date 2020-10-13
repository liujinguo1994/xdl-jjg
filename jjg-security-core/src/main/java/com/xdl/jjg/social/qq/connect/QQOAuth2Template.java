package com.xdl.jjg.social.qq.connect;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @Author: ciyuan
 * @Date: 2019/5/26 1:40
 */
public class QQOAuth2Template extends OAuth2Template {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        //此项标识为true，发送请求的时候才会携带client_id（appId）和client_secret（appSecret）源码org.springframework.social.oauth2.OAuth2Template 134
        setUseParametersForClientAuthentication(true);

    }

    /**
     * 将返回的格式按照QQ的标准做一个自定义解析
     * QQ返回的消息是一个字符串而不是一个标准的json 因此这里需要对其返回的数据进行处理
     * QQ格式：access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
     * 处理成将其处理结果封装到AccessGrant
     *
     * @param accessTokenUrl
     * @param parameters
     * @return 解析封装后的AccessGrant
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        logger.info("获取的accessToken的响应：" + responseStr);
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");

        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");

        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }

    /**
     * 增加http转换器，将QQ返回的text/html格式转换成json
     *
     * @return
     */
    @Override
    protected RestTemplate getRestTemplate() {
        RestTemplate restTemplate = super.getRestTemplate();

        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return restTemplate;
    }
}
