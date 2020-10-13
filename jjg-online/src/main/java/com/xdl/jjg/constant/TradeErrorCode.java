package com.xdl.jjg.constant;

import com.xdl.jjg.response.service.BaseErrorCode;
import lombok.ToString;


/**
 * 错误code
 * @author HQL
 */
@ToString
public class TradeErrorCode extends BaseErrorCode {

    public static final TradeErrorCode SYS_ERROR = new TradeErrorCode(31000,"系统错误");
    public static final TradeErrorCode PARAM_ERROR = new TradeErrorCode(31002,"参数错误");
    public static final TradeErrorCode DATA_NOT_EXIST = new TradeErrorCode(31003,"数据不存在");
    public static final TradeErrorCode UNAUTHORIZED_OPERATION = new TradeErrorCode(31004,"无权操作");
    public static final TradeErrorCode LOGIN_INVALIDATION = new TradeErrorCode(31005,"您的会话已经失效，请重新登陆再创建订单");

    public static final TradeErrorCode SAVE_ORDER_ERROR = new TradeErrorCode(31008,"订单保存异常");

    public static final TradeErrorCode TRADE_DATE_ERROR_CODE = new TradeErrorCode(31009,"交易数据异常");

    public static final TradeErrorCode TRADE_DATE_NOT_EXIST = new TradeErrorCode(31009,"交易信息为空");
    /** 购物车错误异常 */
    public static final TradeErrorCode CART_COUNT_MAX = new TradeErrorCode(32000,"购物车数量已达到上限");
    public static final TradeErrorCode CART_EMPTY = new TradeErrorCode(32001,"购物车为空");
    public static final TradeErrorCode CART_GOODS_NOT_EXIST = new TradeErrorCode(32002,"该购物车商品不存在");

    /** 订单错误异常 */
    public static final TradeErrorCode ORDER_SN_NOT_NULL = new TradeErrorCode(33000,"订单编号不能为空");
    public static final TradeErrorCode GET_ORDER_MESSAGE_ERROR = new TradeErrorCode(33001,"订单详情查询失败");
    public static final TradeErrorCode GET_ORDER_GOODS_MESSAGE_ERROR = new TradeErrorCode(33002,"订单商品详情查询失败");
    public static final TradeErrorCode ORDER_STATUS_CHANGE_ERROR = new TradeErrorCode(33003,"订单状态修改失败");
    public static final TradeErrorCode ORDER_SERVICE_STATUS_CHANGE_ERROR = new TradeErrorCode(33003,"订单售后状态修改失败");
    public static final TradeErrorCode ORDER_DOES_NOT_EXIST = new TradeErrorCode(33004,"订单不存在");
    public static final TradeErrorCode ORDER_CANCEL_ERROR = new TradeErrorCode(33015,"订单取消异常");
    public static final TradeErrorCode ORDER_ROG_ERROR = new TradeErrorCode(33014,"订单确认收货异常");
    public static final TradeErrorCode ORDER_SHIP_ERROR = new TradeErrorCode(33014,"订单发货异常");
    public static final TradeErrorCode ORDER_CAN_NOT_SHIP_ERROR = new TradeErrorCode(33013,"订单已申请退款，不能发货");
    public static final TradeErrorCode UNAUTHORIZED_OPERATION_ORDER = new TradeErrorCode(31004,"无权操作此订单");
    /**交易*/
    public static final TradeErrorCode GET_PAY_METHOD_ERROR = new TradeErrorCode(33004,"获取支付方式失败");
    public static final TradeErrorCode GET_TRADE_ERROR = new TradeErrorCode(33005,"交易信息不存在");
    public static final TradeErrorCode PAYMENT_METHOD_NOT_OPENED = new TradeErrorCode(33006,"支付方式未开启");
    public static final TradeErrorCode PAYMENT_METHOD_NOT_EXIST = new TradeErrorCode(33007,"支付方式不存在");
    public static final TradeErrorCode PAYMENT_BILLS_DO_NOT_EXIST = new TradeErrorCode(33008,"支付账单不存在");
    public static final TradeErrorCode VALIDATION_FAILED = new TradeErrorCode(33009,"验证失败");
    public static final TradeErrorCode MONEY_DIFFERENT = new TradeErrorCode(33010,"金额不一致");
    public static final TradeErrorCode PAY_MONEY_DIFFERENT = new TradeErrorCode(33010,"付款金额和应付金额不一致");

    // 库存错误异常 */
    /** 商品超出可购买数量 */
    public static final TradeErrorCode OVERSELL_ERROR = new TradeErrorCode(35100,"商品超出可购买数量");
    /** 商品低于起购数量 */
    public static final TradeErrorCode LOWERSELL_ERROR = new TradeErrorCode(35102,"商品低于起购数量");
    /**SKU查询失败*/
    public static final TradeErrorCode SKU_MESSAGE_ERROR = new TradeErrorCode(35103,"SKU查询失败");
    /** 该商品无货 */
    public static final TradeErrorCode NO_GOODS_ERROR = new TradeErrorCode(35101,"该商品无货");
    /** 商品数据查询失败 */
    public static final TradeErrorCode GOODS_DATE_QUERY_ERROR = new TradeErrorCode(35102,"商品数据查询失败");


    /** 活动错误异常 */
    public static final TradeErrorCode PROMOTION_ERROR = new TradeErrorCode(36000,"活动时间过期");
    public static final TradeErrorCode PROMOTION_KUCUN_ERROR = new TradeErrorCode(36000,"秒杀商品已抢完！");
    public static final TradeErrorCode DELIVERY_ERROR = new TradeErrorCode(36100,"部分商品不支持自提");
    public static final TradeErrorCode FULL_DISCOUNT_NOT_EXIST = new TradeErrorCode(36001,"满减活动不存在");
    public static final TradeErrorCode SECKILL_NOT_EXIST = new TradeErrorCode(36001,"秒杀活动不存在");
    public static final TradeErrorCode SECKILL_GOODS_EXIST = new TradeErrorCode(36001,"同一个商品不能同时参加多个时间段");
    public static final TradeErrorCode MINUS_NOT_EXIST = new TradeErrorCode(36002,"单品立减活动不存在");
    public static final TradeErrorCode PROMOTION_GOODS_NOT_NULL = new TradeErrorCode(36003,"活动商品不能为空");
    public static final TradeErrorCode PROMOTION_TIME_NOT_NULL = new TradeErrorCode(36004,"活动时间不能为空");
    public static final TradeErrorCode PROMOTION_SHOP_NOT_NULL = new TradeErrorCode(36005,"活动店铺不能为空");
    public static final TradeErrorCode ACTIVITY_TIME_CONFLICT = new TradeErrorCode(36006,"活动时间冲突");
    public static final TradeErrorCode NO_MERCHANDISE = new TradeErrorCode(36007,"没有参加活动的商品");
    public static final TradeErrorCode PROMOTION_EXIT_NOW = new TradeErrorCode(36008,"当前时间内已存在此类活动");
    public static final TradeErrorCode CAN_NOT_ADD_THE_DAY = new TradeErrorCode(36009,"不可添加当天以及之前的时间点");
    public static final TradeErrorCode CONFLICT_OF_ACTIVITY_COMMODITIES = new TradeErrorCode(36010,"活动商品冲突");
    public static final TradeErrorCode NO_ACTIVE_GOODS = new TradeErrorCode(36011,"没有活动商品");
    public static final TradeErrorCode ACTIVE_IS_STARTED = new TradeErrorCode(36012,"活动已经开始");
    public static final TradeErrorCode GIFTS_NOT_EXIST = new TradeErrorCode(36013,"赠品不存在");
    public static final TradeErrorCode GIFTS_DEL_ERROE = new TradeErrorCode(36013,"赠品已绑定正在进行的满减活动，无法删除！");

    public static final TradeErrorCode COUPON_DEL_ERROE = new TradeErrorCode(36014,"优惠券操作失败，存在未使用的券");
    public static final TradeErrorCode COUPON_DEL_FAIL = new TradeErrorCode(36014,"优惠券未下架 删除失败！");
    public static final TradeErrorCode COUPON_NOT_ENOUGH = new TradeErrorCode(36015,"未达到优惠券使用最低限额");
    public static final TradeErrorCode COUPON_DATA_NOT_EXIST = new TradeErrorCode(31003,"优惠券数据不存在");


    public static final TradeErrorCode META_IS_NOT_EXIST = new TradeErrorCode(36013,"订单拓展信息不存在");
    /** 售后错误异常 */
    public static final TradeErrorCode AFTER_SALE_NOT_EXIST = new TradeErrorCode(37011,"售后维护配置信息不存在");
    public static final TradeErrorCode AFTER_SALE_IS_EXIST = new TradeErrorCode(37012,"分类已经存在");
    public static final TradeErrorCode ORDER_NOT_RECEIVED = new TradeErrorCode(37013,"未收货订单不可退货");
    public static final TradeErrorCode NON_CANCELLING = new TradeErrorCode(37013,"已发货订单不可取消");
    public static final TradeErrorCode REFUND_AMOUNT_IS_ZERO = new TradeErrorCode(37014,"退款金额为0，无需申请退款/退货，请与平台联系解决");
    public static final TradeErrorCode ABNORMAL_QUANTITY_OF_GOODS = new TradeErrorCode(37015,"申请售后货品数量不能大于购买数量");
    public static final TradeErrorCode REFUND_METHOD_REQUIRED = new TradeErrorCode(37015,"退款方式必填");
    public static final TradeErrorCode APPLICATION_SUBMITTED = new TradeErrorCode(37015,"申请已提交");
    public static final TradeErrorCode REFUND_FORM_DOES_NOT_EXIST = new TradeErrorCode(37015,"退款单不存在");
    public static final TradeErrorCode REFUND_NOT_LG_PAY = new TradeErrorCode(37016,"退款金额不能大于支付金额");

    /***该订单未存在退款信息*/
    public static final TradeErrorCode REFUND_NOT_EXIST  = new TradeErrorCode(37013,"该订单未存在退款信息");

    /** 结算订单异常 */
    public static final TradeErrorCode COMPANY_BILL_NOT_EXIST = new TradeErrorCode(38011,"签约公司结算单信息不沉溺在");
    public static final TradeErrorCode RECEIPT_TITLE_NOT_NULL = new TradeErrorCode(38012,"发票抬头必填");
    public static final TradeErrorCode RECEIPT_CONTENT_NOT_NULL = new TradeErrorCode(38013,"发票内容必填");
    public static final TradeErrorCode TAX_NO_NOT_NULL = new TradeErrorCode(38014,"发票税号必填");
    public static final TradeErrorCode ADDRESS_NOT_EXIST = new TradeErrorCode(38015,"收货地址不存在");
    public static final TradeErrorCode PLEASE_FILL_ADDRESS = new TradeErrorCode(38015,"请填写收货地址");
    public static final TradeErrorCode INCORRECT_TRADE_STATUS = new TradeErrorCode(38016,"交易状态不正确，无法支付");
    public static final TradeErrorCode TRADE_DOES_NOT_EXIST = new TradeErrorCode(38017,"交易不存在");
    public static final TradeErrorCode OVERPAYMENT_OF_BALANCE = new TradeErrorCode(38018,"支付的余额不应大于订单金额");
    public static final TradeErrorCode LOW_OF_BALANCE = new TradeErrorCode(38019,"余额不足");
    public static final TradeErrorCode CONFIGURATION_DOES_NOT_EXIST= new TradeErrorCode(38020,"结算配置不存在");
    public static final TradeErrorCode NO_SETTLEABLE_DATA= new TradeErrorCode(38021,"没有可结算数据");


    /** 物流信息异常 */
    public static final TradeErrorCode DELIVERY_MODE_NOT_EXIST = new TradeErrorCode(39011,"配送方式不存在");

    public static final TradeErrorCode GOODS_NOT_IN_AREA = new TradeErrorCode(39013,"不在配送范围内");
    public static final TradeErrorCode  SAVE_DELIVERY= new TradeErrorCode(39018,"请填写自提信息");
    public static final TradeErrorCode CAN_NOT_ADD_AREA = new TradeErrorCode(39013,"此地区已在其他模板，无法添加");

    public static final TradeErrorCode TEMPLATE_DATE_NOT_EXIST = new TradeErrorCode(39014,"模板数据不存在");
    /**自提点和自提日期存在数据绑定*/
    public static final TradeErrorCode DELIVERY_DATE_BINDING  = new TradeErrorCode(39012,"自提点和自提日期存在数据绑定");

    public static final TradeErrorCode RECEIVE_UPPER_LIMIT  = new TradeErrorCode(39015,"该优惠券领取已达上限");

    public static final TradeErrorCode NOT_LOGIN = new TradeErrorCode(39016,"当前用户未登陆");

    public static final TradeErrorCode ALREADY_SUPPORT = new TradeErrorCode(39017,"已点赞");
    public static final TradeErrorCode ALREADY_CANCEL = new TradeErrorCode(39017,"已取消");
    public static final TradeErrorCode AREA_NOT_SUPPORTED = new TradeErrorCode(39018,"生鲜暂配送杭州部分地区");
    public static final TradeErrorCode PASSWORD_SAME = new TradeErrorCode(39019,"密码最近有使用过，请更换!");

    public static final TradeErrorCode GOOD_ID_NOT = new TradeErrorCode(39020,"商品id不能为空!");
    public static final TradeErrorCode SHOP_ID_NOT = new TradeErrorCode(39021,"店铺id不能为空!");

    /**
     * 人寿返回错误号
     * @param errorCode
     * @param errorMsg
     */
    public static final TradeErrorCode ORDER_FOFMAT = new TradeErrorCode(400,"订单格式不正确");
    public static final TradeErrorCode SYSTEM_ERROR = new TradeErrorCode(500,"sys server error");
    public static final TradeErrorCode NO_LOGIN = new TradeErrorCode(403,"No sign found");

    public static final TradeErrorCode ORDER_SN_IS_NULL = new TradeErrorCode(5001,"订单号不能为空");
    public static final TradeErrorCode GOODS_ITEMS_IS_NULL = new TradeErrorCode(5002,"不存在的订单详情");
    public static final TradeErrorCode GOODS_ID_IS_NULL = new TradeErrorCode(5003,"商品id不能为空");
    public static final TradeErrorCode LOGI_COMPANY_IS_NULL = new TradeErrorCode(5004,"物流公司不能为空");
    public static final TradeErrorCode SHIP_NO_IS_NULL = new TradeErrorCode(5005,"物流单号不能为空");

    /**
     * 海康返回错误号
     * @param errorCode
     * @param errorMsg
     */
    public static final TradeErrorCode HIK_PAY = new TradeErrorCode(5006,"海康餐卡支付不能退款，请联系客服");
    public static final TradeErrorCode HIK_PAY_EOORE_BACK = new TradeErrorCode(5007,"新增反馈异常");
    public static final TradeErrorCode HIK_PAY_HAS_DATA= new TradeErrorCode(5008,"此订单号已申请，请耐心等候");

    public TradeErrorCode(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

}
