package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jjg.trade.model.vo.EsOrderItemsVO;
import com.jjg.trade.model.vo.EsOrderVO;
import com.jjg.trade.orderCheck.OrderOperateAllowable;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 订单主表-es_trade
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
public class EsBuyerTradeDO extends Model<EsTradeDO> {

    private static final long serialVersionUID = 1L;

	/**
	 * 交易编号
	 */
	private String tradeSn;

	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 买家id
	 */
	private Long memberId;

	/**
	 * 总价格
	 */
	private Double totalMoney;

	/**
	 * 商品价格
	 */
	private Double goodsMoney;

	/**
	 * 运费
	 */
	private Double freightMoney;

	/**
	 * 优惠的金额
	 */
	private Double discountMoney;

	/**
	 * 第三方支付金额
	 */
	private Double payMoney;

	/**
	 * 余额支付金额
	 */
	private Double balance;

	/**
	 * 交易创建时间
	 */
	private Long createTime;

	/**
	 * 订单状态
	 */
	private String orderStatus;

	/**
	 * 服务状态
	 */
	private String serviceStatus;

	/**
	 * 关键字
	 */
	private String keyword;

	/**
	 * 收货人姓名
	 */
	private String consigneeName;

	/**
	 * 收货地址
	 */
	private String consigneeAddress;

	/**
	 * 订单集合
	 */
	private List<EsOrderItemsVO> orderItemList;

	/**
	 * 订单集合 TODO delete 待删除
	 */
	private List<EsOrderVO> orderList;

	/**
	 * 操作权限
	 */
	private OrderOperateAllowable orderOperateAllowable;

	public EsBuyerTradeDO(){
		OrderOperateAllowable orderOperateAllowable = new OrderOperateAllowable();
		orderOperateAllowable.setAllowCancel(false);
		orderOperateAllowable.setAllowPay(false);
		this.orderOperateAllowable = orderOperateAllowable;
	}
}
