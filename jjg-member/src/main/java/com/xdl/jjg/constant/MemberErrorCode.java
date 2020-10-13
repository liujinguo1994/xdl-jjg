package com.xdl.jjg.constant;


import com.xdl.jjg.response.service.BaseErrorCode;
import lombok.ToString;

/**
 * 错误code
 */
@ToString
public class MemberErrorCode extends BaseErrorCode {

    public static final MemberErrorCode SYS_ERROR = new MemberErrorCode(-1,"系统错误");
    public static final MemberErrorCode ITEM_NOT_FOUND = new MemberErrorCode(1,"用户未找到");
    public static final MemberErrorCode PARAM_ERROR = new MemberErrorCode(2,"参数错误");
    public static final MemberErrorCode MEMBER_REGISTOR = new MemberErrorCode(20000,"该用户已注册");
    public static final MemberErrorCode SHOP_EXIST = new MemberErrorCode(20001,"店铺不存在");
    public static final MemberErrorCode MEMBER_COLLECTION_SHOP_EXIST = new MemberErrorCode(20002,"该店铺已收藏");
    public static final MemberErrorCode MEMBER_COLLECTION_SHOP_MY = new MemberErrorCode(20003,"不可收藏自己店铺");
    public static final MemberErrorCode AUTHORITY = new MemberErrorCode(20004,"你无权限操作");
    public static final MemberErrorCode MEMBER_COLLCETION_GOODS_EXIST = new MemberErrorCode(20005,"该商品已经收藏");
    public static final MemberErrorCode MEMBER_COLLECTION_GOODS_MY = new MemberErrorCode(20006,"不可以收藏自己店铺商品");
    public static final MemberErrorCode DATA_NOT_EXIST = new MemberErrorCode(20007,"数据不存在");
    public static final MemberErrorCode SET_ADDRESS = new MemberErrorCode(20007,"请填写收货地址！");
    public static final MemberErrorCode ACCOUNT_FORBIDDEN = new MemberErrorCode(20008,"该账号已被禁用，请联系管理员!");
    public static final MemberErrorCode ACCOUNT_PASS_ERROR = new MemberErrorCode(20009,"账号密码错误");
    public static final MemberErrorCode FORM_IS_ERROR = new MemberErrorCode(20010,"格式有误");
    public static final MemberErrorCode SHOP_HAS_EXIST = new MemberErrorCode(20011,"已存在该店铺");
    public static final MemberErrorCode SHOP_MENU_Identifier = new MemberErrorCode(20012,"菜单唯一标识重复");
    public static final MemberErrorCode SHOP_MENU_PARENT = new MemberErrorCode(20013,"父级菜单不存在");
    public static final MemberErrorCode SHOP_MENU_OUTINDEX = new MemberErrorCode(20014,"菜单级别最多为3级");
    public static final MemberErrorCode SHOP_MENU_NOTEXIT = new MemberErrorCode(20015,"不存在此菜单");
    public static final MemberErrorCode CUSTOM_NOTEXIT = new MemberErrorCode(20016,"父分类不存在");
    public static final MemberErrorCode CUSTOM_OUTINDES = new MemberErrorCode(20017,"最多为三级分类,添加失败");
    public static final MemberErrorCode CUSTOM_HAS_CHILD = new MemberErrorCode(20018,"当前分类有子分类，不能更换上级分类");
    public static final MemberErrorCode CUSTOM_NOT_ROLE = new MemberErrorCode(20019,"此角色不存在");
    public static final MemberErrorCode COUPON__LIMIT = new MemberErrorCode(20020,"领取店铺优惠券已经到达限制");
    public static final MemberErrorCode CUSTOM_ERROR_SILDE = new MemberErrorCode(20021,"存在无效幻灯片，无法进行编辑操作");
    public static final MemberErrorCode EXIST_CUSTOM = new MemberErrorCode(20022,"该分类下存在子分类不允许删除");
    public static final MemberErrorCode EXIST_GOODS = new MemberErrorCode(20023,"该分类下存在商品不允许删除");
    public static final MemberErrorCode EXIST_SILDE = new MemberErrorCode(20024,"不存在此幻灯片，无法删除");
    public static final MemberErrorCode EXIST_CUST = new MemberErrorCode(20025,"不存在此分类");
    public static final MemberErrorCode EXIST_COMPNAY = new MemberErrorCode(20026,"此公司已存在");
    public static final MemberErrorCode NOTEXIST_COMPANY = new MemberErrorCode(20027,"不存在此公司");
    public static final MemberErrorCode NOTEXIST_TEM = new MemberErrorCode(20028,"详情页模板不存在");
    public static final MemberErrorCode NOTEXIST_CLERK = new MemberErrorCode(20029,"不存在改店员");
    public static final MemberErrorCode ERROE_ADMIN = new MemberErrorCode(20030,"无法删除超级管理员");
    public static final MemberErrorCode MEMBER_BAND = new MemberErrorCode(20031,"当前会员已经失效，无法恢复此店员");
    public static final MemberErrorCode EXIST_DISCOUNT = new MemberErrorCode(20032,"公司折扣已存在");
    public static final MemberErrorCode NOTEXIST_DISCOUNT = new MemberErrorCode(20033,"不存在此公司折扣");
    public static final MemberErrorCode ROLE_SHOP = new MemberErrorCode(20034,"不是该店铺角色");
    public static final MemberErrorCode ERROR_WEIGHT_VALUE = new MemberErrorCode(20035,"权重设置错误");
    public static final MemberErrorCode THEMES_NOT_RIGHT = new MemberErrorCode(20036,"模版类型不匹配");
    public static final MemberErrorCode THEMES_AGAIN = new MemberErrorCode(20037,"店铺模版标识重复");
    public static final MemberErrorCode THEMES_HAS_DEFAULT = new MemberErrorCode(20038,"已存在默认模板");
    public static final MemberErrorCode EXIST_CONFIGURE = new MemberErrorCode(20039,"已存在一个购物车配置");
    public static final MemberErrorCode NOTEXIST_CONFIGURE = new MemberErrorCode(20040,"不存在此购物车配置");
    public static final MemberErrorCode NOTALLOW_CONFIGURE = new MemberErrorCode(20041,"唯一购物车配置无法删除");
    public static final MemberErrorCode EXIST_CART = new MemberErrorCode(20042,"购物车已存在");
    public static final MemberErrorCode NOTEXIST_SHOPTEM = new MemberErrorCode(20043,"不存在改店铺模板");
    public static final MemberErrorCode NOTEXIST_CARTSKU = new MemberErrorCode(20044,"购物车中不存在此商品");
    public static final MemberErrorCode NAVIGA_OUT_LENGTH = new MemberErrorCode(20045,"导航栏菜单名称已经超出最大限制");
    public static final MemberErrorCode NOTEXIST_SHOP = new MemberErrorCode(20046,"店铺初始化失败");
    public static final MemberErrorCode NOTSTRP1_SHOP = new MemberErrorCode(20047,"完成上一步才可进行此步操作");
    public static final MemberErrorCode TIME_ERROR = new MemberErrorCode(20048,"营业执照开始时间不能大于结束时间");
    public static final MemberErrorCode HASAPPLY_ERROR = new MemberErrorCode(20049,"店铺在申请中，不允许此操作");
    public static final MemberErrorCode ERROR_PASSWORD = new MemberErrorCode(20050,"密码输入错误");
    public static final MemberErrorCode CLERK_ERROR = new MemberErrorCode(20051,"不是店铺店员，不允许此操作");
    public static final MemberErrorCode MEMBER_NAME_NOT_EMPTY = new MemberErrorCode(20052,"用户名不能为空!");
    public static final MemberErrorCode MEMBER_PASSWORD_NOT_EMPTY = new MemberErrorCode(20053,"密码不能为空!");
    public static final MemberErrorCode MEMBER_CODE_NOT_EMPTY = new MemberErrorCode(20054,"验证码不能为空!");
    public static final MemberErrorCode MEMBER_UUID_NOT_EMPTY = new MemberErrorCode(20055,"uuid不能为空!");
    public static final MemberErrorCode MEMBER_LAND_FAIL = new MemberErrorCode(20056,"登陆失败");
    public static final MemberErrorCode MEMBER_CODE_ERROR = new MemberErrorCode(20057,"验证码错误!");
    public static final MemberErrorCode MEMBER_ACCOUNT_NOT_EXIST = new MemberErrorCode(20054,"账号不存在!");
    public static final MemberErrorCode MEMBER_PASS_ERROR = new MemberErrorCode(20054,"密码错误!");
    public static final MemberErrorCode LANDING_FAILURE = new MemberErrorCode(20055,"登陆失败");
    public static final MemberErrorCode TOKEN_EMPTY = new MemberErrorCode(20056,"token获取不到");
    public static final MemberErrorCode ERROR_SMS_CODE = new MemberErrorCode(20057,"验证码错误");
    public static final MemberErrorCode NOT_EXIST_COMM = new MemberErrorCode(20058,"不存在该评论");
    public static final MemberErrorCode NOT_EXIST_REPORT = new MemberErrorCode(20059,"不存在该举报信息");
    public static final MemberErrorCode COMPLAINT_TYPE_BIND_REASON = new MemberErrorCode(20060,"该类型已绑定投诉原因请先删除投诉原因");
    public static final MemberErrorCode NOT_LOGIN = new MemberErrorCode(20061,"当前用户未登陆");
    public static final MemberErrorCode TRADE_COUPON_ERROR = new MemberErrorCode(20062,"交易模块优惠券查询失败");
    public static final MemberErrorCode MOBILE_ERROR = new MemberErrorCode(20063,"请输入手机号码");
    public static final MemberErrorCode MEMBER_ACCOUNT_IS_EXIST = new MemberErrorCode(20064,"账号存在!");
    public static final MemberErrorCode GOOD_IS_NOT_EXIST = new MemberErrorCode(20065,"商品不存在!");
    public static final MemberErrorCode GOOD_ERROR_CATAERRY = new MemberErrorCode(20066,"查询该商品级别失败!");
    public static final MemberErrorCode EXIST_NAME = new MemberErrorCode(20067,"用户名已注册!");
    public static final MemberErrorCode EXIST_EMAIL = new MemberErrorCode(20068,"邮箱已注册!");
    public static final MemberErrorCode EXIST_MOBILE = new MemberErrorCode(20069,"手机号已注册!");
    public static final MemberErrorCode MEMBER_LOW_OF_BALANCE = new MemberErrorCode(20070,"余额不足");
    public static final MemberErrorCode COMMETNID_NOT_EXIST = new MemberErrorCode(20071,"评论id不能为空");
    public static final MemberErrorCode COMMETN_NOT_EXIST = new MemberErrorCode(20072,"不存在该评论");
    public static final MemberErrorCode COMMENT_EXIST_SUPPORT = new MemberErrorCode(20073,"该用户已点赞");
    public static final MemberErrorCode MEMBER_ERROR_TOKEN = new MemberErrorCode(20074,"获取发票token异常");
    public static final MemberErrorCode GOODS_IS_EMPTY = new MemberErrorCode(20075,"商品不存在");
    public static final MemberErrorCode NOT_SUPPORT_RECEIPT = new MemberErrorCode(20076,"该商品不支持开此类发票");
    public static final MemberErrorCode GOODS_ARCH_EMPTY = new MemberErrorCode(20077,"商品档案不存在");
    public static final MemberErrorCode RECEIIPT_ERROR = new MemberErrorCode(20078,"获取发票信息异常");
    public static final MemberErrorCode OPEN_RECEIIPT_ERROR = new MemberErrorCode(20079,"开票失败");
    public static final MemberErrorCode EXISTS_ADD_COMMENT = new MemberErrorCode(20080,"已添加追评");
    public static final MemberErrorCode MEMBER_NAME_EXIST = new MemberErrorCode(20081,"用户名已注册!");
    public static final MemberErrorCode MOBILE_FORMAT_ERROR = new MemberErrorCode(20082,"请输入正确的手机号码");
    public static final MemberErrorCode ACCOUNT_EMPTY = new MemberErrorCode(20083,"账号不能为空!");
    public static final MemberErrorCode COMPANY_CODE_EMPTY = new MemberErrorCode(20084,"请输入企业企业标识符!");
    public static final MemberErrorCode PARENT_NOT_EXIST = new MemberErrorCode(20085,"父分组不存在");
    public static final MemberErrorCode SHOP_GROUP_NOT_EXIST = new MemberErrorCode(20086,"店铺分组不存在");
    public static final MemberErrorCode TOP_GROUP_NOT_UPDATE = new MemberErrorCode(20087,"顶级分类不可修改上级分类");
    public static final MemberErrorCode UNABLE_ADD_SUPER = new MemberErrorCode(20088,"无法添加超级管理员");
    public static final MemberErrorCode FAIL_ADD_MEMBER = new MemberErrorCode(20089,"会员添加失败");
    public static final MemberErrorCode MEMBER_NOT_EXIST = new MemberErrorCode(20090,"会员不存在");
    public static final MemberErrorCode MOBILE_ALREADY_BINDING = new MemberErrorCode(20091,"该手机号已绑定");
    public static final MemberErrorCode ADDRESS_NOT_EXIST = new MemberErrorCode(20092,"地址不存在");
    public static final MemberErrorCode COMMENTED = new MemberErrorCode(20093,"该订单已被评论");
    public static final MemberErrorCode RESUBMIT = new MemberErrorCode(20094,"不可重复提交");
    public static final MemberErrorCode COMPLAINT_NOT_EXIST = new MemberErrorCode(20095,"投诉信息不存在");
    public static final MemberErrorCode REVOKE = new MemberErrorCode(20095,"撤销");
    public static final MemberErrorCode NON_REVOKE = new MemberErrorCode(20095,"不撤销");
    public static final MemberErrorCode LOGIN_PASS_ERROR_NUM = new MemberErrorCode(20096,"密码错误已超过5次，请联系管理员!");
    public static final MemberErrorCode SORT_EXISTS = new MemberErrorCode(20097,"分组排序不能重复!");
    public static final MemberErrorCode MOBILE_IS_NULL = new MemberErrorCode(20098,"手机号不能有空!");
    public static final MemberErrorCode BALANCE_IS_NULL = new MemberErrorCode(20099,"余额不能为空!");
    public static final MemberErrorCode NAME_IS_NULL = new MemberErrorCode(20100,"用户名不能为空!");
    public static final MemberErrorCode COMPANY_CODE_IS_NULL = new MemberErrorCode(20101,"企业标识符不能为空!");
    public static final MemberErrorCode COMPANY_CODE_NOT_EXIST = new MemberErrorCode(20102,"该企业标识符不存在");


    public MemberErrorCode(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

}
