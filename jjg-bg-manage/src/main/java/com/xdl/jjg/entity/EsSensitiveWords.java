package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_sensitive_words")
public class EsSensitiveWords extends Model<EsSensitiveWords> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 敏感词名称
     */
    @TableField("word_name")
	private String wordName;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 删除状态  0正常 1 删除
     */
    @TableField("is_del")
	private Integer isDel;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
