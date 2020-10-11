package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 协议维护
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_agreement")
public class EsAgreement extends Model<EsAgreement> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 协议编号
     */
    @TableField("agr_no")
	private String agrNo;
    /**
     * 协议内容
     */
	private String content;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 更新时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;
    /**
     * 协议名称
     */
    @TableField("agr_name")
	private String agrName;
    /**
     * 协议版本
     */
	private String version;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
