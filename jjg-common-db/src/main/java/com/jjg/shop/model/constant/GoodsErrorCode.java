package com.jjg.shop.model.constant;

import com.xdl.jjg.response.service.BaseErrorCode;
import lombok.ToString;


/**
 * 错误code
 */
@ToString
public class GoodsErrorCode extends BaseErrorCode {

    public static final GoodsErrorCode SYS_ERROR = new GoodsErrorCode(-10000,"系统错误");
    public static final GoodsErrorCode ITEM_NOT_FOUND = new GoodsErrorCode(10001,"用户未找到");
    public static final GoodsErrorCode PARAM_ERROR = new GoodsErrorCode(100002,"参数错误");
    public static final GoodsErrorCode GOODS_EXIS = new GoodsErrorCode(10003,"商品档案SKU已存在");
    public static final GoodsErrorCode DATA_NOT_EXIST = new GoodsErrorCode(10004,"数据不存在");

    public static final GoodsErrorCode UN_DER = new GoodsErrorCode(10005,"存在不能下架的商品，不能操作");
    public static final GoodsErrorCode UP_SHELF = new GoodsErrorCode(10006,"存在不能上架的商品，不能操作");
    public static final GoodsErrorCode DATA_NOT_BELONG = new GoodsErrorCode(10007,"存在不属于您的商品，不能操作");
    public static final GoodsErrorCode DATA_NOT_DELETE = new GoodsErrorCode(10008,"存在不能删除的商品，不能操作");
    public static final GoodsErrorCode DATA_NOT_RECYCLE  = new GoodsErrorCode(10009,"存在不能放入回收站的商品，不能操作");
    public static final GoodsErrorCode DATA_NOT_REVERT  = new GoodsErrorCode(10010,"存在不能还原的商品，不能操作");
    public static final GoodsErrorCode REASONS_FOR_REFUSAL   = new GoodsErrorCode(10011,"拒绝原因不能为空，不能操作");
    public static final GoodsErrorCode DATA_AUDITED   = new GoodsErrorCode(10012,"商品已经审核");
    public static final GoodsErrorCode SHOP_NOT_EXIST   = new GoodsErrorCode(10013,"店铺不存在或已关闭");
    public static final GoodsErrorCode NO_AUTH   = new GoodsErrorCode(10014,"无权限访问");
    public static final GoodsErrorCode CATEGORY_NOT_EXIST   = new GoodsErrorCode(10015,"商品父分类不存在");
    public static final GoodsErrorCode CATEGORY_MAX   = new GoodsErrorCode(10016,"商品分类最多为三级");
    public static final GoodsErrorCode EXIST_CHILDREN   = new GoodsErrorCode(10017,"当前分类有子分类，不能更换上级分类");
    public static final GoodsErrorCode CATEGORY_EXIST_GOODS   = new GoodsErrorCode(10018,"该分类下存在商品，不允许删除");
    public static final GoodsErrorCode GOODS_EXIST   = new GoodsErrorCode(10019,"商品已存在不允许重复添加");
    public static final GoodsErrorCode TAG_EXIST_GOODS   = new GoodsErrorCode(10020,"标签下存在绑定商品，不允许删除");
    public static final GoodsErrorCode PARAMETER_EXIST   = new GoodsErrorCode(10021,"参数组名不能重复");
    public static final GoodsErrorCode QUANTITY_SHORTAGE   = new GoodsErrorCode(10022,"库存扣减失败，库存不足");
    public static final GoodsErrorCode EXIST_DISCOUNT  = new GoodsErrorCode(10023,"该分类下绑定了折扣不允许删除");

    public static final GoodsErrorCode REASON_NOT_BLANK  = new GoodsErrorCode(10024,"审核备注不能为空");
    public static final GoodsErrorCode REFUND_SN_NOT_EXIST  = new GoodsErrorCode(10025,"退款单号不存在");
    public static final GoodsErrorCode ORDER_SN_NOT_EXIST  = new GoodsErrorCode(10026,"关联订单号不存在");
    public static final GoodsErrorCode PAY_MONEY_MAX  = new GoodsErrorCode(10027,"退款金额不能大于支付金额");

    public static final GoodsErrorCode VERIFICATION_CODE = new GoodsErrorCode(10028,"验证码错误");
    public static final GoodsErrorCode PASSWORD_NOT_CORRECT = new GoodsErrorCode(10029,"密码不正确");
    public static final GoodsErrorCode LOGIN_FAIL = new GoodsErrorCode(10030,"登录失败");
    public static final GoodsErrorCode TAG_SORT_EXIST   = new GoodsErrorCode(10031,"标签排序已存在");
    public GoodsErrorCode(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

}
