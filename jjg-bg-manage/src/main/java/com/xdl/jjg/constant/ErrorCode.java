package com.xdl.jjg.constant;

import com.xdl.jjg.response.service.BaseErrorCode;
import lombok.ToString;


/**
 * 错误code
 */
@ToString
public class ErrorCode extends BaseErrorCode {

    public static final ErrorCode SYS_ERROR = new ErrorCode(40000,"系统错误");
    public static final ErrorCode ITEM_NOT_FOUND = new ErrorCode(40001,"用户未找到");
    public static final ErrorCode PARAM_ERROR = new ErrorCode(40002,"参数错误");
    public static final ErrorCode MENU_IDENTIFIER_REPETITION = new ErrorCode(40003,"菜单唯一标识重复");
    public static final ErrorCode PARENT_MENU_NOT_EXIT = new ErrorCode(40004,"父级菜单不存在");
    public static final ErrorCode MENU_GRADE_ERROR = new ErrorCode(40005,"菜单级别最多为4级");
    public static final ErrorCode MENU_NOT_EXIT = new ErrorCode(40006,"当前菜单不存在");
    public static final ErrorCode ID_IS_NULL = new ErrorCode(40007,"id为空");
    public static final ErrorCode PASSWORD_FORMAT_ERROR = new ErrorCode(40008,"密码格式不正确");
    public static final ErrorCode USERNAME_EXIT = new ErrorCode(40009,"管理员名称重复");
    public static final ErrorCode ROLE_IS_NULL = new ErrorCode(40010,"角色为空");
    public static final ErrorCode DEPARTMENT_IS_NULL = new ErrorCode(40011,"部门为空");
    public static final ErrorCode FACE_IS_NULL = new ErrorCode(40012,"图像为空");
    public static final ErrorCode ADMIN_USER_IS_NULL = new ErrorCode(40013,"当前管理员不存在");
    public static final ErrorCode ADMIN_USER_MUST_HAVE_ONE = new ErrorCode(40014,"必须保留一个超级管理员");
    public static final ErrorCode ROLE_NOT_EXIT = new ErrorCode(40015,"角色不存在");
    public static final ErrorCode DATA_NOT_EXIST = new ErrorCode(40016,"数据不存在");
    public static final ErrorCode NAME_PASSWORD_ERROR = new ErrorCode(40017,"用户名或密码错误");
    public static final ErrorCode PASSWORD_IS_NULL = new ErrorCode(40018,"新密码不能为空");
    public static final ErrorCode OLD_PASSWORD_IS_NULL = new ErrorCode(40019,"原始密码不能为空");
    public static final ErrorCode OLD_PASSWORD_ERROR = new ErrorCode(40020,"原始密码错误");
    public static final ErrorCode REGIONS_NOT_EXIT = new ErrorCode(40021,"此地区不存在");
    public static final ErrorCode MOBILE_IS_NULL = new ErrorCode(40022,"手机号码不能为空");
    public static final ErrorCode PARENT_ID_IS_NULL = new ErrorCode(40023,"父id为空");
    public static final ErrorCode PARENT_DEPARTMENT_IS_NULL = new ErrorCode(40024,"父部门不存在");
    public static final ErrorCode DEPARTMENT_GRADE_MORE_THAN_THREE = new ErrorCode(40025,"最多为三级部门,添加失败");
    public static final ErrorCode HAVE_CHILDREN_DEPARTMENT = new ErrorCode(40026,"当前部门有子部门，不能更换上级部门");
    public static final ErrorCode HAVE_CHILDREN_DEPARTMENT_NOT_DELETE = new ErrorCode(40027,"当前部门有子部门，不能删除");
    public static final ErrorCode HAVE_USER_NOT_DELETE = new ErrorCode(40028,"该部门已关联管理员，不能删除");
    public static final ErrorCode PARENT_REGIONS_IS_NULL = new ErrorCode(40029,"当前地区父地区id无效");
    public static final ErrorCode REGIONS_IS_NULL = new ErrorCode(40030,"当前地区不存在");
    public static final ErrorCode SMS_PLATEFORM_EXIT = new ErrorCode(40031,"该短信方案已经存在");
    public static final ErrorCode SMS_PLATEFORM_NOT_EXIT = new ErrorCode(40032,"该短信方案不存在");
    public static final ErrorCode EMAIL_ERROR = new ErrorCode(40033,"邮箱格式有误");
    public static final ErrorCode MOBILE_ERROR = new ErrorCode(40034,"手机号码格式有误");
    public static final ErrorCode NOT_ADMIN = new ErrorCode(40035,"只有超级管理员才能操作");
    public static final ErrorCode LOGI_COMPANY_NOT_EXIT = new ErrorCode(40036,"物流公司不存在");
    public static final ErrorCode LOGI_COMPANY_CODE_IS_EXIT = new ErrorCode(40037,"物流公司代码重复");
    public static final ErrorCode LOGI_COMPANY_NAME_IS_EXIT = new ErrorCode(40038,"物流公司名称重复");
    public static final ErrorCode LOGI_COMPANY_KDCODE_IS_EXIT = new ErrorCode(40039,"快递鸟公司代码重复");
    public static final ErrorCode EXIT_CHILDREN_MENU_NOT_DEL = new ErrorCode(40040,"存在子菜单,不能删除");
    public static final ErrorCode IMAGE_IS_NULL = new ErrorCode(40041,"移动端导航，图片必传");
    public static final ErrorCode NAME_IS_LONG = new ErrorCode(40042,"导航栏菜单名称已经超出最大限制");
    public static final ErrorCode SITE_NAVIGATION_NOT_EXIT = new ErrorCode(40043,"导航栏不存在");
    public static final ErrorCode PICTURE_SIZE_TOO_BIG = new ErrorCode(40044,"焦点图数量不能超过五张");
    public static final ErrorCode GET_OPEN_SMS_ERROR = new ErrorCode(40045,"获取开启的短信网关异常");
    public static final ErrorCode SMS_SMS_ERROR = new ErrorCode(40046,"发送短信请求失败");
    public static final ErrorCode SMTP_IS_NULL = new ErrorCode(40047,"未找到可用smtp方案");
    public static final ErrorCode GET_SMTP_ERROR = new ErrorCode(40048,"获取smtp方案异常");
    public static final ErrorCode SEND_EMAIL_ERROR = new ErrorCode(40049,"邮件发送失败！");
    public static final ErrorCode NOT_ASSIGN_MEMBER = new ErrorCode(40050,"请指定发送会员！");
    public static final ErrorCode PARENT_CATEGORY_NOT_EXIT = new ErrorCode(40051,"父分类不存在！");
    public static final ErrorCode CATEGORY_MORE_THAN_TWO = new ErrorCode(40052,"最多为二级分类,添加失败！");
    public static final ErrorCode UNABLE_UPDATE = new ErrorCode(40053,"特殊的文章分类，不可修改");
    public static final ErrorCode UNABLE_DELETE = new ErrorCode(40054,"特殊的文章分类，不可删除");
    public static final ErrorCode ARTICLE_UNABLE_DELETE = new ErrorCode(40055,"该文章不可删除，只可修改");
    public static final ErrorCode GET_PROGRESS_ERROR = new ErrorCode(40056,"获取进度信息异常");
    public static final ErrorCode WAYBILL_EXIT = new ErrorCode(40057,"该电子面单方案已经存在");
    public static final ErrorCode WAYBILL_NOT_EXIT = new ErrorCode(40058,"该电子面单不存在");
    public static final ErrorCode NOT_AWAIT_ISSUE_NOT_UPDATE = new ErrorCode(40059,"只有待发布才可以编辑");
    public static final ErrorCode ISSUE_STATE_NOT_DELETE = new ErrorCode(40060,"存在已发布状态的商品不可以删除");
    public static final ErrorCode STATE_error_NOT_RELEASE = new ErrorCode(40061,"存在不是待发布状态的商品不可以发布");
    public static final ErrorCode DELETE_SHOP_LOGI_COMPANY_ERROR = new ErrorCode(40062,"删除卖家端物流公司出错");
    public static final ErrorCode START_DAY_ERROR = new ErrorCode(40063,"活动时间不能小于当前时间");
    public static final ErrorCode APPLY_TIME_SMALL = new ErrorCode(40064,"报名截止时间不能小于当前时间");
    public static final ErrorCode APPLY_TIME_BIG = new ErrorCode(40065,"报名截止时间不能大于活动时间");
    public static final ErrorCode RANGE_LIST_EXIT = new ErrorCode(40066,"抢购区间的值不能重复");
    public static final ErrorCode RANGE_ERROR = new ErrorCode(40067,"抢购区间必须在0点到23点的整点时刻");
    public static final ErrorCode EXIT_CHILDREN_CATEGORY_NOT_DEL = new ErrorCode(40068,"存在子分类,不能删除");
    public static final ErrorCode COMPANY_ID_IS_NULL = new ErrorCode(40069,"签约公司不能为空");
    public static final ErrorCode URL_ERROR = new ErrorCode(40070,"商品链接异常");
    public static final ErrorCode GOODS_RANKING_COUNT_ERROR = new ErrorCode(40071,"首页榜单个数不能超过三个");
    public static final ErrorCode ADVERTISING_EXIT = new ErrorCode(40072,"该位置广告已存在");

    public ErrorCode(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

}
