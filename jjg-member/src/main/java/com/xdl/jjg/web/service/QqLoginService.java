package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsConnectSettingDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.vo.Auth2Token;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * QQ 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-20
 */
public interface QqLoginService {

    /**
     * 获取授权登录的url
     *
     * @return
     */
    DubboResult<String> getLoginUrl();

    /**
     * 登录成功后的回调方法
     *
     * @return
     */
//    DubboResult<Auth2Token> loginCallback();

    /**
     * 填充会员信息
     *
     * @param auth2Token
     * @param esMemberDO
     * @return
     */
//    DubboResult<EsMemberDO> fillInformation(Auth2Token auth2Token, EsMemberDO esMemberDO);



    /**
     * 拼装配置参数
     *
     * @return
     */
    DubboResult<EsConnectSettingDO> assembleConfig();
}
