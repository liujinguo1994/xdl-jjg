package com.jjg.member.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class EsFileDTO {
	/** 文件流 */
	@ApiModelProperty(name="stream",value="文件流",required=true)
	private InputStream stream;
	/** 文件名称 */
	@NotEmpty(message="文件名称不能为空")
	@ApiModelProperty(name="name",value="文件名称",required=true)
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InputStream getStream() {
		return stream;
	}
	public void setStream(InputStream stream) {
		this.stream = stream;
	}


	@Override
	public String toString() {
		return "FileDTO{" +
				"stream=" + stream +
				", name='" + name + '\'' +
				'}';
	}
}
