package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 短信业务传递参数使用DTO
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSmsSendDTO implements Serializable {

    private static final long serialVersionUID = -6222644764379603685L;
    /**
     * 手机号码(必填)
     */
    private String mobile;

    /**
     * 模板id(必填)
     */
    private String templateId;

    /**
     * 验证码
     */
    private String code;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 物流单号
     */
    private String shipSn;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 密码
     */
    private String password;
}
