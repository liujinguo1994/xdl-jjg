package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 商品快照
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsGoodsSnapshotDO extends Model<EsGoodsSnapshotDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 商品id
     */
	private Long goodsId;

    /**
     * 商品名称
     */
	private String name;

    /**
     * 商品编号
     */
	private String goodsSn;

    /**
     * 品牌名称
     */
	private String brandName;

    /**
     * 分类名称
     */
	private String categoryName;

    /**
     * 商品类型
     */
	private String goodsType;

    /**
     * 重量
     */
	private Double weight;

    /**
     *
     */
	private String intro;

    /**
     * 商品价格
     */
	private Double money;

    /**
     * 商品成本价
     */
	private Double cost;

    /**
     * 商品市场价
     */
	private Double mktmoney;

    /**
     * 参数json
     */
	private String paramsJson;

    /**
     * 图片json
     */
	private String imgJson;

    /**
     * 快照时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 所属卖家
     */
	private Long shopId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
