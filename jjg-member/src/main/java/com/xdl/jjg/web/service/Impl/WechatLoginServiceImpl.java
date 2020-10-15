package com.xdl.jjg.web.service.Impl;


import com.jjg.member.model.domain.EsConnectSettingDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.enums.ConnectTypeEnum;
import com.jjg.member.model.enums.WechatConnectConfigGroupEnum;
import com.jjg.member.model.enums.WechatConnectConfigItemEnm;
import com.jjg.member.model.vo.Auth2Token;
import com.jjg.member.model.vo.ConnectSettingConfigItem;
import com.jjg.member.model.vo.ConnectSettingParametersVO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.constants.ThreadContextHolder;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsConnectSettingService;
import com.xdl.jjg.web.service.WechatLoginService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 微信登录 服务实现类
 * </p>
 *
 * @author yuanj 595831328@qq.com
 * @since 2019-07-20
 */
@Service
public class WechatLoginServiceImpl implements WechatLoginService {

    private static Logger logger = LoggerFactory.getLogger(WechatLoginServiceImpl.class);

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
            String callBack = this.getCallBackUrl(ConnectTypeEnum.WECHAT.value());
            String ua = request.getHeader("user-agent").toLowerCase();
            if (ua.indexOf("micromessenger") > 0) {
                callBack = getDomain() + "/passport/connect/wechat/auth/back";
                String a="https://open.weixin.qq.com/connect/oauth2/authorize" +
                        "?appid=" + map.get("wechat_wechat_app_id").toString() +
                        "&redirect_uri=" + callBack +
                        "&response_type=code" +
                        "&scope=snsapi_userinfo" +
                        "&state=123#wechat_redirect";
                return DubboResult.success(a);
            }
            String b="https://open.weixin.qq.com/connect/qrconnect?" +
                    "appid=" + map.get("wechat_pc_app_id").toString() +
                    "&redirect_uri=" + callBack +
                    "&response_type=code" +
                    "&scope=snsapi_login" +
                    "&state=" + uuid +
                    "&connect_redirect=1#wechat_redirect";
            return DubboResult.success(b);
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
            //获取code
            String code = request.getParameter("code");
            //pc使用的是开放平台，微信端使用的是公众平台，参数是不一致
            String appId = StringUtil.toString(map.get("wechat_pc_app_id"));
            String key = StringUtil.toString(map.get("wechat_pc_app_key"));
            String ua = request.getHeader("user-agent").toLowerCase();
            if (ua.indexOf("micromessenger") > 0) {
                appId = StringUtil.toString(map.get("wechat_wechat_app_id"));
                key = StringUtil.toString(map.get("wechat_wechat_app_key"));
            }
            //通过code获取access_token及openid
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid=" + appId +
                    "&secret=" + key +
                    "&code=" + code +
                    "&grant_type=authorization_code";
            String content = HttpUtils.doGet(url, "UTF-8", 100, 1000);
            //获取openid
            JSONObject json = JSONObject.fromObject(content);
            String openid = json.getString("openid");
            String accessToken = json.getString("access_token");
            String unionId = json.getString("unionid");
            //将信息封装到对象当中
            Auth2Token auth2Token = new Auth2Token();
            auth2Token.setUnionid(unionId);
            auth2Token.setOpneId(openid);
            auth2Token.setAccessToken(accessToken);
            auth2Token.setType(ConnectTypeEnum.WECHAT.value());
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
            JSONObject jsonObject = this.getWechatInfo(auth2Token.getAccessToken(), auth2Token.getOpneId());
            esMemberDO.setNickname(jsonObject.getString("nickname"));
            esMemberDO.setFace(jsonObject.getString("headimgurl"));
            String sex = jsonObject.getString("sex");
            if ("1".equals(sex)) {
                esMemberDO.setSex(1);
            } else {
                esMemberDO.setSex(0);
            }
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
     * 获取微信用户信息
     *
     * @param accessToken token
     * @param openId      opneid
     * @return
     */
    private JSONObject getWechatInfo(String accessToken, String openId) {
        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + accessToken +
                "&openid=" + openId;
        //通过openid获取userinfo
        String content = HttpUtils.doGet(url, "UTF-8", 1000, 1000);
        JSONObject jsonObject = JSONObject.fromObject(content);
        return jsonObject;
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
            for (WechatConnectConfigGroupEnum wechatConnectConfigGroupEnum : WechatConnectConfigGroupEnum.values()) {
                ConnectSettingParametersVO connectSettingParametersVO = new ConnectSettingParametersVO();
                List<ConnectSettingConfigItem> lists = new ArrayList<>();
                for (WechatConnectConfigItemEnm wechatConnectConfigItem : WechatConnectConfigItemEnm.values()) {
                    ConnectSettingConfigItem connectSettingConfigItem = new ConnectSettingConfigItem();
                    connectSettingConfigItem.setKey("wechat_" + wechatConnectConfigGroupEnum.value() + "_" + wechatConnectConfigItem.value());
                    connectSettingConfigItem.setName(wechatConnectConfigItem.getText());
                    lists.add(connectSettingConfigItem);
                }
                connectSettingParametersVO.setConfigList(lists);
                connectSettingParametersVO.setName(wechatConnectConfigGroupEnum.getText());
                list.add(connectSettingParametersVO);
            }
            connectSetting.setName("微信参数配置");
            connectSetting.setType(ConnectTypeEnum.WECHAT.value());
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

    /**
     * 获取accesstocken
     * @return
     */
    public String getWXAccessTocken(){

        initConnectSetting();

        String url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential&" +
                "appid=" + map.get("wechat_miniprogram_app_id") + "&" +
                "secret=" + map.get("wechat_miniprogram_app_key");

        String content = HttpUtils.doGet(url, "UTF-8", 1000, 1000);

        //{"access_token":"16_OXa8nmH-APkaE3KhJUQz_rjSgOoFO0fF5e4GZiFesrPmLamCaIqGf_F1lCGX_gwKnwya0wkxjpNeQwtKtX9PGQ-FfKrlkEAIWwelHXB1S5zuGfFzgsLWHgpFxRA3gmAG_SJmVw7E14BSwAx5MOBfACAFHU","expires_in":7200}

        JSONObject object = JSONObject.fromObject(content);

        return object.get("access_token").toString();
    }

    /**
     * 小程序自动登录
     *
     * @return
     */
    public String miniProgramAutoLogin(String code) {

        initConnectSetting();

        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + map.get("wechat_miniprogram_app_id") + "&" +
                "secret=" + map.get("wechat_miniprogram_app_key") + "&" +
                "js_code=" + code + "&" +
                "grant_type=authorization_code";
        String content = HttpUtils.doGet(url, "UTF-8", 100, 1000);

        return content;
    }
}
