package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * 会员表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member")
public class EsMember extends Model<EsMember> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 会员登陆用户名
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 会员登陆密码
     */
    private String password;
    /**
     * 会员性别
     */
    private Integer sex;
    /**
     * 会员生日
     */
    private Long birthday;
    /**
     * 所属省份ID
     */
    @TableField("province_id")
    private Long provinceId;
    /**
     * 所属城市ID
     */
    @TableField("city_id")
    private Long cityId;
    /**
     * 所属省份名称
     */
    private String province;
    /**
     * 所属城市名称
     */
    private String city;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 新手机号
     */
    @TableField(exist = false)
    private String newMobile;
    /**
     * 座机号码
     */
    private String tel;
    /**
     * 成长值
     */
    private Integer grade;
    /**
     * 可用积分
     */
    @TableField("consum_point")
    private Long consumPoint;
    /**
     * 上次登陆时间
     */
    @TableField("last_login")
    private Long lastLogin;
    /**
     * 邮件是否已验证
     */
    @TableField("is_cheked")
    private Integer isCheked;
    /**
     * 注册IP地址
     */
    @TableField("register_ip")
    private String registerIp;
    /**
     * 会员信息是否完善
     */
    @TableField("info_full")
    private Integer infoFull;
    /**
     * 会员头像
     */
    private String face;
    /**
     * 身份证号
     */
    private String identity;
    /**
     * 会员状态(0,正常 1，禁用)
     */
    private Integer state;
    /**
     * QQ账号
     */
    @TableField("qq_id")
    private String qqId;
    /**
     * 微信账号
     */
    @TableField("wechat_id")
    private String wechatId;
    /**
     * 微博账号
     */
    @TableField("weibo_id")
    private String weiboId;
    /**
     * 支付宝账号
     */
    @TableField("alipay_id")
    private String alipayId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 余额
     */
    @TableField("member_balance")
    private Double memberBalance;
    /**
     * 企业标识符
     */
    @TableField("company_code")
    private String companyCode;
    /**
     * 盐
     */
    private String salt;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
    /**
     * 真实姓名
     */
    @TableField("card_name")
    private String cardName;
    /**
     * 用户唯一标识
     */
    @TableField("openid")
    private String openid;
    /**
     * 登录态标识
     */
    @TableField("skey")
    private String skey;

    /**
     * 黑卡标识
     */
    @TableField("black_card")
    private Integer blackCard;

    /**
     * 黑卡折扣力度
     */
    @TableField("black_discount")
    private Double blackDiscount;


}
