package com.xdl.jjg.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年07月2019/7/27日
 * @Description 微信支付自动配置
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
public class WxPayAutoConfiguration {

  @Autowired
  protected WeChatAPPProperties weChatAPPProperties;
  @Autowired
  protected WeChatJSAPIProperties weChatJSAPIProperties;



  /**
   * 构造微信支付服务对象.
   *
   * @return 微信支付service
   */
  @Bean(name = "appPayService")
  public WxPayService appPayService() {
    return getWxPayServiceImpl(weChatAPPProperties);
  }

  /**
   * 构造微信支付服务对象.
   *
   * @return 微信支付service
   */
  @Bean(name = "mpPayService")
  public WxPayService mpPayService() {
    return getWxPayServiceImpl(weChatJSAPIProperties);
  }

  public WxPayServiceImpl getWxPayServiceImpl(WeChatProperties properties){

    final WxPayServiceImpl mpPayService = new WxPayServiceImpl();
    WxPayConfig payConfig = new WxPayConfig();
    payConfig.setAppId(StringUtils.trimToNull(properties.getAppId()));
    payConfig.setMchId(StringUtils.trimToNull(properties.getMchId()));
    payConfig.setMchKey(StringUtils.trimToNull(properties.getKey()));
    payConfig.setKeyPath(StringUtils.trimToNull(properties.getKeyPath()));
    mpPayService.setConfig(payConfig);
    return mpPayService;
  }


}
