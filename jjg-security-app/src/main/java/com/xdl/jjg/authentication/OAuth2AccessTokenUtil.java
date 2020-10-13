package com.xdl.jjg.authentication;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/17 14:58
 */
@Component
public class OAuth2AccessTokenUtil {

    private String BASIC = "basic ";

    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * 这里有多种AuthorizationServerTokenServices的实现，因此必须指定bean的名称为defaultAuthorizationServerTokenServices
     */
    @Autowired
    private AuthorizationServerTokenServices defaultAuthorizationServerTokenServices;


    public OAuth2AccessToken get(HttpServletRequest request, Authentication authentication) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.toLowerCase().startsWith(BASIC)) {
            throw new UnapprovedClientAuthenticationException("请求头中无Client信息");
        }
        //抽取并且解码header字符串
        String[] tokens = new String[0];
        try {
            tokens = extractAndDecodeHeader(header, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert tokens.length == 2;

        //从解码的信息中获取clientId 和 clientSecret
        String clientId = tokens[0];
        String clientSecret = tokens[1];
        //通过clientId获取clientDetails信息
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        //验证clientDetails信息
        if (null == clientDetails) {
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在：" + clientId);
        } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配：" + clientId);
        }
        //获取TokenRequest
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");
        //获取OAuth2Request
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        //获取OAuth2Authentication
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        //生成OAuth2AccessToken
        OAuth2AccessToken token = defaultAuthorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        return token;
    }

    /**
     * 抽取并解码header字符串
     *
     * @param header
     * @param request
     * @return
     * @throws IOException
     */
    public String[] extractAndDecodeHeader(String header, HttpServletRequest request)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }
        //将字符串转换成UTF-8编码
        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

}
