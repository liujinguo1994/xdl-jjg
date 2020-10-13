package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-07-06
 */
@Data
@Accessors(chain = true)
public class EsCouponReceiveCheckVO implements Serializable {

private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
			
						@ApiModelProperty(required = false,value = "id")
		private Integer id;

	/**
	 * 手机号
	 */
				@ApiModelProperty(required = false,value = "mobile")
		private String mobile;

	/**
	 * 创建时间
	 */
			
				@ApiModelProperty(required = false,value = "createTime")
		private Long createTime;

	/**
	 * 更新时间
	 */
			
				@ApiModelProperty(required = false,value = "updateTime")
		private Long updateTime;

	/**
	 * 用户名
	 */

				@ApiModelProperty(required = false,value = "userName")
		private String userName;




protected Serializable pkVal() {
			return this.id;
		}

		}
