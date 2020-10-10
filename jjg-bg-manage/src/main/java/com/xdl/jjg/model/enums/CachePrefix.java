package com.xdl.jjg.model.enums;

/**
 * 缓存前缀
 */
public enum CachePrefix {

    /**
     * token
     */
    TOKEN,

    /**
     * 系统设置
     */
    SETTING,

    /**
     * 快递平台
     */
    EXPRESS,

    /**
     * 图片验证码
     */
    CAPTCHA,

    /**
     * 商品
     */
    GOODS,

    /**
     * 商品sku
     */
    SKU,

    /**
     * 商品分类
     */
    GOODS_CAT,
    /**
     * 浏览次数
     */
    VISIT_COUNT,
    /*
     * 存储方案
     */
    UPLOADER,
    /**
     * 地区
     */
    REGION,

    /**
     * 短信网关
     */
    SPlATFORM,
    /**
     * 短信验证码前缀
     */
    _CODE_PREFIX,
    /**
     * smtp
     */
    SMTP,
    /**
     * 系统设置
     */
    SETTINGS,
    /**
     * 电子面单
     */
    WAYBILL,
    /**
     * 短信验证码
     */
    SMS_CODE,
    /**
     * 访问令牌
     */
    ACCESS_TOKEN,
    /**
     * 刷新令牌
     */
    REFRESH_TOKEN,

    /**
     * 管理员角色权限对照表
     */
    ADMIN_URL_ROLE,

    /**
     * 店铺管理员角色权限对照表
     */
    SHOP_URL_ROLE,

    /**
     * 手机验证标识
     */
    MOBILE_VALIDATE,

    /**
     * 店铺运费模版
     */
    SHIP_TEMPLATE,

    //================促销=================
    /**
     * 促销活动
     */
    PROMOTION_KEY,

    /*** 单品立减 */
    STORE_ID_MINUS_KEY,

    /*** 第二件半价 */
    STORE_ID_HALF_PRICE_KEY,

    /*** 满优惠 */
    STORE_ID_FULL_DISCOUNT_KEY,

    /**
     * 限时抢购活动缓存key前缀
     */
    STORE_ID_SECKILL_KEY,

    /**
     * 团购活动缓存key前缀
     */
    STORE_ID_GROUP_BUY_KEY,

    /**
     * 积分商品缓存key前缀
     */
    STORE_ID_EXCHANGE_KEY,


    //================交易=================
    /**
     * 交易_会员购物车
     */
    CART_MEMBER_ID_PREFIX,

    /**
     * 交易_交易价格的前缀
     */
    PRICE_SESSION_ID_PREFIX,

    /**
     * 交易_交易单
     */
    TRADE_SESSION_ID_PREFIX,

    /**
     * 结算参数
     */
    CHECKOUT_PARAM_ID_PREFIX,

    /**
     * 交易单号前缀
     */
    TRADE_SN_CACHE_PREFIX,

    /**
     * 订单编号前缀
     */
    ORDER_SN_CACHE_PREFIX,

    /**
     * 订单编号前缀
     */
    PAY_LOG_SN_CACHE_PREFIX,

    /**
     * 交易
     */
    TRADE,

    /**
     * 商品好评率
     */
    GOODS_GRADE,
    /*
     * 所有地区
     */
    REGIONALL,
    /**
     * 分级别地区缓存
     */
    REGIONLIDEPTH,

    /**
     * 站点导航栏
     */
    SITE_NAVIGATION,
    /**
     * 信任登录
     */
    CONNECT_LOGIN,

    /**
     * 敏感词
     */
    SENSITIVE_WORDS,

    /**
     * 二维码
     */
    QR_CODE,
    /**
     * admin端登录图片验证码
     */
    SYSTEM_KAPTCHA,
    /**
     * 异常会员
     */
    EXCEPTION_MEMBER,
    /**
     * 异常会员余额
     */
    EXCEPTION_MEMBER_DEPOSIT;


    public String getPrefix() {
        return this.name() + "_";
    }
}
