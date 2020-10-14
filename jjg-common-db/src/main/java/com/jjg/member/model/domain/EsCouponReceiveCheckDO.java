package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsCouponReceiveCheckDO extends Model<EsCouponReceiveCheckDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Integer id;

    /**
     * 手机号
     */
	private String mobile;

    /**
     * 创建时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 更新时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

    /**
     * 用户名
     */
	private String userName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
