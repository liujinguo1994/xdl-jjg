package com.jjg.member.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsSensitiveWordsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 敏感词名称
     */
    private String wordName;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 删除状态  0正常 1 删除
     */
    @TableLogic
    private Integer isDel;


}
