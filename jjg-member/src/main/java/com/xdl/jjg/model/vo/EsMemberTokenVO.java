package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员token表VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-10 14:47:36
 */
@Data
@ApiModel
public class EsMemberTokenVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	@ApiModelProperty(value = "会员ID")
	private Long id;

    /**
     * token
     */
	@ApiModelProperty(value = "token")
	private String token;

    /**
     * 过期时间
     */
	@ApiModelProperty(value = "过期时间")
	private Long expireTime;

    /**
     * 更新时间
     */
	@ApiModelProperty(value = "更新时间")
	private Long updateTime;


}
