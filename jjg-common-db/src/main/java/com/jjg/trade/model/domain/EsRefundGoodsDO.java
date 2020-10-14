package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
public class EsRefundGoodsDO extends Model<EsRefundGoodsDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 退货(款)编号
     */
	private String refundSn;
    /**
     * 商品id
     */
	private Long goodsId;
    /**
     * SKUid
     */
	private Long skuId;
    /**
     * 发货数量
     */
	private Integer shipNum;
    /**
     * 商品价格
     */
	private Double money;
    /**
     * 退货数量
     */
	private Integer returnNum;
    /**
     * 退货入库数量
     */
	private Integer storageNum;
    /**
     * 商品编号
     */
	private String goodsSn;
    /**
     * 商品名称
     */
	private String goodsName;
    /**
     * 商品图片
     */
	private String goodsImage;
    /**
     * 规格数据
     */
	private String specJson;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
