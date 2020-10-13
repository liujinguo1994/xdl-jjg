package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsSearchKeyWordDO extends Model<EsSearchKeyWordDO> {

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
	private Long memberId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
