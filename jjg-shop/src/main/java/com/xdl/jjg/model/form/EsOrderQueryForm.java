package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单明细表-es_orderQueryForm
 * </p>
 *
 * @author WANGAF 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
public class EsOrderQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;


    /**
     * 子订单订单编号
     */
	@ApiModelProperty(value = "国寿订单号")
	private String lfcId;
    /**
     * 买家账号
     */
	@ApiModelProperty(value = "买家账号")
	private String memberName;

    /**
     * 订单状态
     */
	@ApiModelProperty(value = "订单状态")
	private String orderState;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;
	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	private String shipName;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Long createTime;
	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private Long createTimeEnd;

	/**
	 * 订单来源 (pc、wap、app)
	 */
	@ApiModelProperty(value = "订单来源 (pc、wap、app)")
	private String clientType;


	private Long startTime;

	private Long endTime;
}
