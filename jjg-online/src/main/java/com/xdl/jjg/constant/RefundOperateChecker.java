package com.xdl.jjg.constant;

import com.xdl.jjg.model.enums.ProcessStatusEnum;
import com.xdl.jjg.model.enums.RefundOperateEnum;
import com.xdl.jjg.model.enums.RefundTypeEnum;
import com.xdl.jjg.model.vo.RefundStep;

import java.util.HashMap;
import java.util.Map;

/**
 * 退货操作检测，看某状态下是否允许某操作
 * @author zjp
 * @version v7.0
 * @since v7.0 上午11:20 2018/5/2
 */
public class RefundOperateChecker {

	/**
	 * 退款
	 */
	private static final Map<ProcessStatusEnum, RefundStep>  REFUND_FLOW = new HashMap<ProcessStatusEnum, RefundStep>();
	/**
	 *退货
	 */
	private static final Map<ProcessStatusEnum, RefundStep> RETURN_FLOW = new HashMap<ProcessStatusEnum, RefundStep>();
	/**
	 *换货
	 */
	private static final Map<ProcessStatusEnum, RefundStep>  CHANGE_FLOW = new HashMap<ProcessStatusEnum, RefundStep>();

	static{
		initRefundflow();
		initReturnGoodsflow();
		initChangeGoodsflow();
	}
	/**
	 * 退款
	 */
	private static void initRefundflow() {
		/**
		 *待处理
		 */
		REFUND_FLOW.put(ProcessStatusEnum.TO_BE_PROCESS, new RefundStep(ProcessStatusEnum.TO_BE_PROCESS, RefundOperateEnum.SELLER_APPROVAL));
		/**
		 * 待退款 可进行卖家退款，取消
		 */
		 REFUND_FLOW.put(ProcessStatusEnum.WAIT_REFUND, new RefundStep(ProcessStatusEnum.WAIT_REFUND, RefundOperateEnum.SELLER_REFUND));
		/**
		 *退款失败
		 */
		 REFUND_FLOW.put(ProcessStatusEnum.REFUND_FAIL, new RefundStep(ProcessStatusEnum.REFUND_FAIL, RefundOperateEnum.SELLER_REFUND));
		/**
		 *完成
		 */
		 REFUND_FLOW.put(ProcessStatusEnum.COMPLETED, new RefundStep(ProcessStatusEnum.COMPLETED));
	}
	/**
	 * 退货
	 */
	private static void initReturnGoodsflow() {
		/**
		 *待处理
		 */
		RETURN_FLOW.put(ProcessStatusEnum.TO_BE_PROCESS, new RefundStep(ProcessStatusEnum.TO_BE_PROCESS, RefundOperateEnum.SELLER_APPROVAL));
		/**
		 *待入库 可进行卖家入库
		 */
		RETURN_FLOW.put(ProcessStatusEnum.WAIT_IN_STORAGE, new RefundStep(ProcessStatusEnum.WAIT_IN_STORAGE, RefundOperateEnum.STOCK_IN));
		/**
		 * 待退款 可进行卖家退款，取消
		 */
		 RETURN_FLOW.put(ProcessStatusEnum.WAIT_REFUND, new RefundStep(ProcessStatusEnum.WAIT_REFUND, RefundOperateEnum.SELLER_REFUND));
		/**
		 *退款失败
		 */
		 RETURN_FLOW.put(ProcessStatusEnum.REFUND_FAIL, new RefundStep(ProcessStatusEnum.REFUND_FAIL, RefundOperateEnum.SELLER_REFUND));
		 /**
		 *完成
		 */
		 RETURN_FLOW.put(ProcessStatusEnum.COMPLETED, new RefundStep(ProcessStatusEnum.COMPLETED));
	}
	/**
	 * 换货
	 */
	private static void initChangeGoodsflow() {
		/**
		 *待处理
		 */
		CHANGE_FLOW.put(ProcessStatusEnum.TO_BE_PROCESS, new RefundStep(ProcessStatusEnum.TO_BE_PROCESS, RefundOperateEnum.SELLER_APPROVAL));
		/**
		 *待入库 可进行卖家入库
		 */
		 CHANGE_FLOW.put(ProcessStatusEnum.WAIT_IN_STORAGE, new RefundStep(ProcessStatusEnum.WAIT_IN_STORAGE, RefundOperateEnum.STOCK_IN));
		/**
		 * 待发货 可进行卖家发货，取消
		 */
		 CHANGE_FLOW.put(ProcessStatusEnum.WAIT_SHIP, new RefundStep(ProcessStatusEnum.WAIT_SHIP, RefundOperateEnum.SELLER_DELIVERY));
		/**
		 *完成
		 */
		 CHANGE_FLOW.put(ProcessStatusEnum.COMPLETED, new RefundStep(ProcessStatusEnum.COMPLETED));
	}

	/**
	 * 校验操作是否允许
	 * @param type 退款类型
	 * @param status 售后状态
	 * @param operate 操作类型
	 * @return 是否允许操作
	 */
	public static boolean checkAllowable(RefundTypeEnum type, ProcessStatusEnum status, RefundOperateEnum operate){

		Map<ProcessStatusEnum, RefundStep> flow = null;

		if(type.equals(RefundTypeEnum.RETURN_MONEY)){
			//退款
			flow =  REFUND_FLOW;
		}else if(type.equals(RefundTypeEnum.RETURN_GOODS)){
			//退货
			flow =  RETURN_FLOW;
		}else if(type.equals(RefundTypeEnum.CHANGE_GOODS)){
			//换货
			flow =  CHANGE_FLOW;
		}
		if(flow == null){
			return false;
		}

		RefundStep step =  flow.get(status);

		return step.checkAllowable(operate);

	}

}
