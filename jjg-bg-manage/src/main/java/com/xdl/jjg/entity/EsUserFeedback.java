package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("es_user_feedback")
public class EsUserFeedback extends Model<EsUserFeedback> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 用户反馈文本
     */
    @TableField("feedback_text")
    private String feedbackText;

    /**
     * 用户反馈满意度
     */
    @TableField("feedback_satisfaction")
    private Integer feedbackSatisfaction;

    /**
     * 用户反馈手机号
     */
    @TableField("feedback_phone")
    private String feedbackPhone;

    /**
     * 用户反馈姓名
     */
    @TableField("feedback_name")
    private String feedbackName;

    /**
     * 反馈功能模块
     */
    @TableField("feedback_module")
    private String feedbackModule;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
