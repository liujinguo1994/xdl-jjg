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
 * @author LiuJG 344009799@qq.com
 * @since 2019-10-26
 */
@Data
@Accessors(chain = true)
public class EsSearchKeyWordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;

    /**
     * 搜索关键字
     */
	private String searchKeyword;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long memberId;

	protected Serializable pkVal() {
		return this.id;
	}

}
