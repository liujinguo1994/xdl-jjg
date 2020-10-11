package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsUserFeedbackDO extends Model<EsUserFeedbackDO> {

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
