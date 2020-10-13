package com.xdl.jjg.response.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.InputStream;

/**
 * 
 * 文件上传入参
 * @author zh
 * @version v2.0
 * @since v7.0
 * 2018年3月19日 下午4:42:51
 */
@ApiModel
@Data
public class FileDTO {
	/** 文件流 */
	@ApiModelProperty(name="stream",value="文件流",required=true)
	private InputStream stream;
	/** 文件名称 */
	@NotEmpty(message="文件名称不能为空")
	@ApiModelProperty(name="name",value="文件名称",required=true)
	private String name;
}
