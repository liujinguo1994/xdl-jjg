package com.xdl.jjg.web.service.Impl;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.JsonUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsConnectSettingDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.enums.*;
import com.shopx.member.api.model.domain.plugin.ThreadContextHolder;
import com.shopx.member.api.model.domain.vo.Auth2Token;
import com.shopx.member.api.model.domain.vo.ConnectSettingConfigItem;
import com.shopx.member.api.model.domain.vo.ConnectSettingParametersVO;
import com.shopx.member.api.service.IEsConnectSettingService;
import com.shopx.member.api.service.QqLoginService;
import com.sohu.tv.cachecloud.client.basic.util.HttpUtils;
import net.sf.json.JSONObject;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * QQ 服务实现类
 * </p>
 *
 * @author yuanj 595831328@qq.com
 * @since 2019-07-20
 */
@Service
public class QqLoginServiceImpl implements QqLoginService {

    private static Logger logger = LoggerFactory.getLogger(QqLoginServiceImpl.class);

    private static Pattern unionidPattern = Pattern.compile("\"openid\":\"(.+)\"");

    private static Pattern accessTokenPattern = Pattern.compile("access_token=(.+)&expires_in");

    protected HttpServletRequest request;

    protected Map map = new HashMap(16);


    @Autowired
    private IEsConnectSettingService connectSettingService;


    /**
     * 初始化信息登录的参数
     */
    protected void initConnectSetting() {

        List<EsConnectSettingDO> list =null;
        DubboPageResult<EsConnectSettingDO> result = connectSettingService.getList();
        if (result.isSuccess()){
            list = result.getData().getList();
        }
        ;
        for (EsConnectSettingDO connectSettingDO : list) {
            List<ConnectSettingParametersVO> configList = connectSettingDO.getClientList();
            for (ConnectSettingParametersVO connectSettingParametersVO : configList) {
                List<ConnectSettingConfigItem> lists = connectSettingParametersVO.getConfigList();
                for (ConnectSettingConfigItem connectSettingConfigItem : lists) {
                    map.put(connectSettingConfigItem.getKey(), connectSettingConfigItem.getValue());
                }
            }
        }
        request = ThreadContextHolder.getHttpRequest();

    }
    @Override
    public DubboResult<String> getLoginUrl() {
        try{
            initConnectSetting();
            //将回调地址存入redis中
            String uuid = UUID.randomUUID().toString();
            String callBack = this.getCallBackUrl(ConnectTypeEnum.QQ.value());

            String a= "https://graph.qq.com/oauth2.0/authorize?" +
                    "response_type=code" +
                    "&client_id=" + map.get("qq_pc_app_id") +
                    "&redirect_uri=" + callBack +
                    "&state=" + uuid;
            return DubboResult.success(a);
        } catch (ArgumentException ae) {
            logger.error("获取授权登录的url", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("获取授权登录的url", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<Auth2Token> loginCallback() {
        try{
            initConnectSetting();
            //通过Authorization Code获取Access Token
            String code = request.getParameter("code");
            String redirectUri = this.getCallBackUrl(ConnectTypeEnum.QQ.value());
            String url = "https://graph.qq.com/oauth2.0/token?" +
                    "grant_type=authorization_code" +
                    "&client_id=" + map.get("qq_pc_app_id") +
                    "&client_secret=" + map.get("qq_pc_app_key") +
                    "&code=" + code +
                    "&redirect_uri=" + redirectUri;
            String content = HttpUtils.doGet(url, "UTF-8", 1000, 1000);
            String accessToken = "";
            Matcher matcher = accessTokenPattern.matcher(content);
            while (matcher.find()) {
                accessToken = matcher.group(1);
            }
            url = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;
            content = HttpUtils.doGet(url, "UTF-8", 1000, 1000);
            String unionId = "";
            Matcher unionIdMatcher = unionidPattern.matcher(content);
            while (unionIdMatcher.find()) {
                unionId = unionIdMatcher.group(1);
            }
            Auth2Token auth2Token = new Auth2Token();
            auth2Token.setUnionid(unionId);
            auth2Token.setAccessToken(accessToken);
            return DubboResult.success(auth2Token);
        } catch (ArgumentException ae) {
            logger.error("登录成功后的回调方法", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("登录成功后的回调方法", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsMemberDO> fillInformation(Auth2Token auth2Token, EsMemberDO esMemberDO) {
        try{
            initConnectSetting();
            //获取会员信息
            String url = "https://graph.qq.com/user/get_user_info?" +
                    "access_token=" + auth2Token.getAccessToken() +
                    "&oauth_consumer_key=" + map.get("qq_pc_app_id").toString() +
                    "&openid=" + auth2Token.getUnionid();
            String content = HttpUtils.doGet(url, "UTF-8", 1000, 1000);
            JSONObject json = JSONObject.fromObject(content);
            String gender = json.getString("gender");
            //完善会员信息
            esMemberDO.setNickname(json.getString("nickname"));
            if ("男".equals(gender)) {
                esMemberDO.setSex(1);
            } else {
                esMemberDO.setSex(0);
            }
            esMemberDO.setFace(json.getString("figureurl"));
            return DubboResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("登录成功后的回调方法", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("登录成功后的回调方法", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 存储中间页信息及拼接回调地址
     *
     * @param type 登录类型
     * @return
     */
    protected String getCallBackUrl(String type) {
        if (isWap()) {
            return getDomain() + "/passport/connect/wap/" + type + "/callback";
        }
        return getDomain() + "/passport/connect/pc/" + type + "/callback";
    }

    @Override
    public DubboResult<EsConnectSettingDO> assembleConfig() {
        try{
            EsConnectSettingDO connectSetting = new EsConnectSettingDO();
            List<ConnectSettingParametersVO> list = new ArrayList<>();
            for (QqConnectConfigGroupEnum qqConnectConfigGroup : QqConnectConfigGroupEnum.values()) {
                ConnectSettingParametersVO connectSettingParametersVO = new ConnectSettingParametersVO();
                List<ConnectSettingConfigItem> lists = new ArrayList<>();
                for (QqConnectConfigItemEnum qqConnectConfigItem : QqConnectConfigItemEnum.values()) {
                    ConnectSettingConfigItem connectSettingConfigItem = new ConnectSettingConfigItem();
                    connectSettingConfigItem.setKey("qq_" + qqConnectConfigGroup.value() + "_" + qqConnectConfigItem.value());
                    connectSettingConfigItem.setName(qqConnectConfigItem.getText());
                    lists.add(connectSettingConfigItem);
                }
                connectSettingParametersVO.setConfigList(lists);
                connectSettingParametersVO.setName(qqConnectConfigGroup.getText());
                list.add(connectSettingParametersVO);
            }
            connectSetting.setName("QQ参数配置");
            connectSetting.setType(ConnectTypeEnum.QQ.value());
            connectSetting.setConfig(JsonUtil.objectToJson(list));
            return DubboResult.success(connectSetting);
        } catch (ArgumentException ae) {
            logger.error("拼装配置参数", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("拼装配置参数", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    private boolean isWap() {

        boolean flag = false;

        String header = ThreadContextHolder.getHttpRequest().getHeader("User-Agent");
        if (header == null) {
            return flag;
        }

        String[] keywords = {"Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser", "Mobile"};
        for (String s : keywords) {
            if (header.contains(s)) {
                flag = true;
                break;
            }
        }
        return flag;

    }

    /**
     * 根据 request获取完整的域名，如http://www.abc.com:8080
     * 如果端口为80则为:http://www.abc.com
     *
     * @return
     */
    public static String getDomain() {
        try {
            HttpServletRequest request = ThreadContextHolder.getHttpRequest();
            if (request == null) {
                return "";
            }
            String port = "" + request.getServerPort();
            if ("80".equals(port)) {
                port = "";
            } else {
                port = ":" + port;
            }

            String contextPath = request.getContextPath();
            if ("/".equals(contextPath)) {
                contextPath = "";
            }

            String domain = "http://" + request.getServerName() + port;
            domain += contextPath;
            return domain;
        } catch (NullPointerException e) {
            return "http://localhost:8080";
        }
    }

}
