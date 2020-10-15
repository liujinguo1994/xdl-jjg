package com.jjg.system.model.enums;

/**
 * @Description 短信模板编号枚举类
 */
public enum SmsTemplateCodeEnum {

//==============================================阿里云短信模板（start）===================================
    /**
     * 信息变更验证码
     */
    SMS_105250661("信息变更验证码"),

    /**
     * 修改密码验证码
     */
    SMS_105250662("修改密码验证码"),

    /**
     * 用户注册验证码
     */
    SMS_105250663(" 用户注册验证码"),

    /**
     * 登录异常验证码
     */
    SMS_105250664("登录异常验证码"),

    /**
     * 登录确认验证码
     */
    SMS_105250665("登录确认验证码"),

    /**
     * 短信测试
     */
    SMS_105250666("短信测试"),

    /**
     * 身份验证验证码
     */
    SMS_105250667("身份验证验证码"),

    /**
     * 订单发货通知
     */
    SMS_129759394("订单发货通知"),

    /**
     * 订单发货通知
     */
    SMS_129764489("订单发货通知"),

    /**
     * 订单发货通知
     */
    SMS_129749552("订单发货通知"),

    /**
     * 订单发货通知
     */
    SMS_130845586("订单发货通知"),

    /**
     * 商品降价通知
     */
    SMS_172598407("商品降价通知"),

    /**
     * 人寿蛋糕卡券提醒正式版
     */
    SMS_177253177("人寿蛋糕卡券提醒正式版");


//=========================================================阿里云短信模板 (end) =========================================

    private String description;

    SmsTemplateCodeEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
