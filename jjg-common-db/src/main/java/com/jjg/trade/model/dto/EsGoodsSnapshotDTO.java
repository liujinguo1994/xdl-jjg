package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class EsGoodsSnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 商品id
     */
	@JsonSerialize(using = ToStringSerializer.class)
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
     * 商品详情
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
	private Long createTime;

    /**
     * 所属卖家
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

	protected Serializable pkVal() {
		return this.id;
	}

}
