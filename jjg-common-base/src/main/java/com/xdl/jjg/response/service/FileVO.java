package com.xdl.jjg.response.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 文件上传返回值封装
 * @author zh
 * @version v2.0
 * @since v7.0
 * 2018年3月19日 下午4:42:51
 */
@ApiModel
@Data
public class FileVO {
	/** 文件名称 */
	@ApiModelProperty(name="name",value="文件名称",required=true)
	private String name;
	/** 文件后缀 */
	@ApiModelProperty(name="ext",value="文件后缀",required=true)
	private String ext;
	/** url */
	@ApiModelProperty(name="url",value="图片地址",required=true)
	private String url;
}
