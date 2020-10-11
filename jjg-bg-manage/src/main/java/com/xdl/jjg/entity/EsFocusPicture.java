package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("es_focus_picture")
public class EsFocusPicture extends Model<EsFocusPicture> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 图片地址
     */
    @TableField("pic_url")
	private String picUrl;
    /**
     * 操作类型
     */
    @TableField("operation_type")
	private String operationType;
    /**
     * 操作参数
     */
    @TableField("operation_param")
	private String operationParam;
    /**
     * 操作地址
     */
    @TableField("operation_url")
	private String operationUrl;
    /**
     * 客户端类型 APP/WAP/PC
     */
    @TableField("client_type")
	private String clientType;
    /**
     * 背景色
     */
    @TableField("operation_color")
	private String operationColor;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
