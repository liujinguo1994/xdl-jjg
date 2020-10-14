package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-18 09:39:18
 */
@Data
@ApiModel
public class EsShopPromiseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键")
	private Long id;

    /**
     * 卖家id
     */
	@ApiModelProperty(value = "卖家id")
	private Long shopId;

    /**
     * 承诺内容
     */
	@ApiModelProperty(value = "承诺内容")
	private String content;

    /**
     * 有效状态
     */
	@ApiModelProperty(value = "有效状态 1 有效 2无效")
	private Integer state;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

    /**
     * 更新时间
     */
	@ApiModelProperty(value = "更新时间")
	private Long updateTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
