package com.jjg.system.model.dto;

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
public class EsUserFeedbackDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 用户反馈文本
     */
    private String feedbackText;

    /**
     * 用户反馈满意度
     */
    private Integer feedbackSatisfaction;

    /**
     * 用户反馈手机号
     */
    private String feedbackPhone;

    /**
     * 用户反馈姓名
     */
    private String feedbackName;

    /**
     * 反馈功能模块
     */
    private String feedbackModule;

    protected Serializable pkVal() {
        return this.id;
    }

}
