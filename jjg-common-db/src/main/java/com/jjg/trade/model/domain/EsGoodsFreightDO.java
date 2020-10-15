package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 分类运费模板关联表
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsGoodsFreightDO extends Model<EsGoodsFreightDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
	private Long id;

    /**
     * 运费模板id
     */
	private Long modeId;

    /**
     * 商品分类id
     */
	private Long categoryId;

    /**
     * 是否删除(0 不 删除,1删除)
     */
	private Integer isDel;

    /**
     * 创建时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 修改时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
