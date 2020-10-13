package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
public class EsGroupBuyCatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
	private Long id;

    /**
     * 父类id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

    /**
     * 分类名称
     */
	private String catName;

    /**
     * 分类结构目录
     */
	private String catPath;

    /**
     * 分类排序
     */
	private Integer catOrder;

	protected Serializable pkVal() {
		return this.id;
	}

}
