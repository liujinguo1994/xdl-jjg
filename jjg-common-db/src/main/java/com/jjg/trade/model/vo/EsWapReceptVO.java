package com.jjg.member.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 手机端发票列表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-04-08
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Api
public class EsWapReceptVO extends Model<EsWapReceptVO> {

    private static final long serialVersionUID = 1L;

    /**
     * 子订单订单编号
     */
	@ApiModelProperty(value = "子订单订单编号")
	private String orderSn;
    /**
     * 店铺ID
     */
	@ApiModelProperty(value = "店铺ID")
	private Long shopId;
    /**
     * 店铺名称
     */
	@ApiModelProperty(value = "店铺名称")
	private String shopName;

    /**
     * 订单创建时间
     */
	@ApiModelProperty(value = "订单创建时间")
	private Long createTime;

	/**
     *  订单中的商品信息
	 */
	@ApiModelProperty(value = "订单中的商品信息")
	private List<EsBuyerOrderItemsVO> esBuyerOrderItemsVO;


}
