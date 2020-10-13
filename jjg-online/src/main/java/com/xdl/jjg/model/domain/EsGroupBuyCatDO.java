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
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsGroupBuyCatDO extends Model<EsGroupBuyCatDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
	private Long id;

    /**
     * 父类id
     */
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
