package com.jjg.member.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 签约公司
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsCompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 公司名称
     */
	private String companyName;

    /**
     * 公司编号
     */
	private String companyCode;

    /**
     * 是否有效
     */
    @TableLogic
	private Integer isDel;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 更新时间
     */
	private Long updateTime;

	/**
	 * 是否有效0为正常，1为禁用
	 */
	private Integer state;

	/**
	 * 结束时间
	 */
	@TableField(value = "end_time", fill = FieldFill.INSERT)
	private Long endTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
