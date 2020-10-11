package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("es_uploader")
public class EsUploader extends Model<EsUploader> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 存储名称
     */
	private String name;
    /**
     * 是否开启
     */
	private Integer open;
    /**
     * 存储配置
     */
	private String config;
    /**
     * 存储插件id
     */
	private String bean;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
