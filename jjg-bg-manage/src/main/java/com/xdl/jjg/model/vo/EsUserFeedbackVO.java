package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 *
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-06-03
 */
@Data
@Accessors(chain = true)
public class EsUserFeedbackVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(required = false, value = "id")
    private Long id;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 用户反馈文本
     */

    @ApiModelProperty(required = false, value = "反馈文本信息")
    private String feedbackText;

    /**
     * 用户反馈满意度
     */

    @ApiModelProperty(required = false, value = "用户反馈满意度")
    private Integer feedbackSatisfaction;

    /**
     * 用户反馈手机号
     */

    @ApiModelProperty(required = false, value = "用户反馈手机号")
    private String feedbackPhone;

    /**
     * 用户反馈姓名
     */

    @ApiModelProperty(required = false, value = "用户反馈姓名")
    private String feedbackName;

    /**
     * 反馈功能模块
     */

    @ApiModelProperty(required = false, value = "模块：cart: 购物车    checkout: 结算页     goodsInfo: 商品详情    search: 搜索页")
    private String feedbackModule;


}
