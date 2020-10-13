package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2020-03-11 14:39:37
 */
@Data
@TableName("es_uploader")
public class EsUploader implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 存储名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 是否开启
	 */
	@TableField("open")
	private Integer open;

	/**
	 * 存储配置
	 */
	@TableField("config")
	private String config;

	/**
	 * 存储插件id
	 */
	@TableField("bean")
	private String bean;

}
