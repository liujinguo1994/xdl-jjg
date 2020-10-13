package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * 店铺详细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_shop_detail")
public class EsShopDetail extends Model<EsShopDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺详细id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 店铺id
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 店铺所在省id
     */
    @TableField("shop_province_id")
	private Long shopProvinceId;
    /**
     * 店铺所在市id
     */
    @TableField("shop_city_id")
	private Long shopCityId;
    /**
     * 店铺所在县id
     */
    @TableField("shop_county_id")
	private Long shopCountyId;
    /**
     * 店铺所在镇id
     */
    @TableField("shop_town_id")
	private Long shopTownId;
    /**
     * 店铺所在省名称
     */
    @TableField("shop_province")
	private String shopProvince;
    /**
     * 店铺所在市名称
     */
    @TableField("shop_city")
	private String shopCity;
    /**
     * 店铺所在县名称
     */
    @TableField("shop_county")
	private String shopCounty;
    /**
     * 店铺所在镇名称
     */
    @TableField("shop_town")
	private String shopTown;
    /**
     * 店铺详细地址
     */
    @TableField("shop_add")
	private String shopAdd;
    /**
     * 公司名称
     */
    @TableField("company_name")
	private String companyName;
    /**
     * 公司地址
     */
    @TableField("company_address")
	private String companyAddress;
    /**
     * 公司电话
     */
    @TableField("company_phone")
	private String companyPhone;
    /**
     * 电子邮箱
     */
    @TableField("company_email")
	private String companyEmail;
    /**
     * 员工总数
     */
    @TableField("employee_num")
	private Integer employeeNum;
    /**
     * 注册资金
     */
    @TableField("reg_money")
	private Double regMoney;
    /**
     * 联系人姓名
     */
    @TableField("link_name")
	private String linkName;
    /**
     * 联系人电话
     */
    @TableField("link_phone")
	private String linkPhone;
    /**
     * 法人姓名
     */
    @TableField("legal_name")
	private String legalName;
    /**
     * 法人身份证
     */
    @TableField("legal_id")
	private String legalId;
    /**
     * 法人身份证照片
     */
    @TableField("legal_img")
	private String legalImg;
    /**
     * 营业执照号
     */
    @TableField("license_num")
	private String licenseNum;
    /**
     * 营业执照所在省id
     */
    @TableField("license_province_id")
	private Long licenseProvinceId;
    /**
     * 营业执照所在市id
     */
    @TableField("license_city_id")
	private Long licenseCityId;
    /**
     * 营业执照所在县id
     */
    @TableField("license_county_id")
	private Long licenseCountyId;
    /**
     * 营业执照所在镇id
     */
    @TableField("license_town_id")
	private Long licenseTownId;
    /**
     * 营业执照所在省名称
     */
    @TableField("license_province")
	private String licenseProvince;
    /**
     * 营业执照所在市名称
     */
    @TableField("license_city")
	private String licenseCity;
    /**
     * 营业执照所在县
     */
    @TableField("license_county")
	private String licenseCounty;
    /**
     * 营业执照所在镇
     */
    @TableField("license_town")
	private String licenseTown;
    /**
     * 营业执照详细地址
     */
    @TableField("license_add")
	private String licenseAdd;
    /**
     * 成立日期
     */
    @TableField("establish_date")
	private Long establishDate;
    /**
     * 营业执照有效期开始
     */
    @TableField("licence_start")
	private Long licenceStart;
    /**
     * 营业执照有效期结束
     */
    @TableField("licence_end")
	private Long licenceEnd;
    /**
     * 法定经营范围
     */
	private String scope;
    /**
     * 营业执照电子版
     */
    @TableField("licence_img")
	private String licenceImg;
    /**
     * 组织机构代码
     */
    @TableField("organization_code")
	private String organizationCode;
    /**
     * 组织机构电子版
     */
    @TableField("code_img")
	private String codeImg;
    /**
     * 一般纳税人证明电子版
     */
    @TableField("taxes_img")
	private String taxesImg;
    /**
     * 银行开户名
     */
    @TableField("bank_account_name")
	private String bankAccountName;
    /**
     * 银行开户账号
     */
    @TableField("bank_number")
	private String bankNumber;
    /**
     * 开户银行支行名称
     */
    @TableField("bank_name")
	private String bankName;
    /**
     * 开户银行所在省id
     */
    @TableField("bank_province_id")
	private Long bankProvinceId;
    /**
     * 开户银行所在市id
     */
    @TableField("bank_city_id")
	private Long bankCityId;
    /**
     * 开户银行所在县id
     */
    @TableField("bank_county_id")
	private Long bankCountyId;
    /**
     * 开户银行所在镇id
     */
    @TableField("bank_town_id")
	private Long bankTownId;
    /**
     * 开户银行所在省名称
     */
    @TableField("bank_province")
	private String bankProvince;
    /**
     * 开户银行所在市名称
     */
    @TableField("bank_city")
	private String bankCity;
    /**
     * 开户银行所在县名称
     */
    @TableField("bank_county")
	private String bankCounty;
    /**
     * 开户银行所在镇民名称
     */
    @TableField("bank_town")
	private String bankTown;
    /**
     * 开户银行许可证电子版
     */
    @TableField("bank_img")
	private String bankImg;
    /**
     * 税务登记证号
     */
    @TableField("taxes_certificate_num")
	private String taxesCertificateNum;
    /**
     * 纳税人识别号
     */
    @TableField("taxes_distinguish_num")
	private String taxesDistinguishNum;
    /**
     * 税务登记证书
     */
    @TableField("taxes_certificate_img")
	private String taxesCertificateImg;
    /**
     * 店铺经营类目
     */
    @TableField("goods_management_category")
	private String goodsManagementCategory;
    /**
     * 店铺等级
     */
    @TableField("shop_level")
	private Integer shopLevel;
    /**
     * 店铺等级申请
     */
    @TableField("shop_level_apply")
	private Integer shopLevelApply;
    /**
     * 店铺相册已用存储量
     */
    @TableField("store_space_capacity")
	private Double storeSpaceCapacity;
    /**
     * 店铺logo
     */
    @TableField("shop_logo")
	private String shopLogo;
    /**
     * 店铺横幅
     */
    @TableField("shop_banner")
	private String shopBanner;
    /**
     * 店铺简介
     */
    @TableField("shop_desc")
	private String shopDesc;
    /**
     * 是否推荐
     */
    @TableField("shop_recommend")
	private Integer shopRecommend;
    /**
     * 店铺主题id
     */
    @TableField("shop_themeid")
	private Long shopThemeid;
    /**
     * 店铺主题模版路径
     */
    @TableField("shop_theme_path")
	private String shopThemePath;
    /**
     * 店铺主题id
     */
    @TableField("wap_themeid")
	private Long wapThemeid;
    /**
     * wap店铺主题
     */
    @TableField("wap_theme_path")
	private String wapThemePath;
    /**
     * 店铺信用
     */
    @TableField("shop_credit")
	private Integer shopCredit;
    /**
     * 店铺好评率
     */
    @TableField("shop_praise_rate")
	private Double shopPraiseRate;
    /**
     * 店铺描述相符度
     */
    @TableField("shop_description_credit")
	private Double shopDescriptionCredit;
    /**
     * 服务态度分数
     */
    @TableField("shop_service_credit")
	private Double shopServiceCredit;
    /**
     * 发货速度分数
     */
    @TableField("shop_delivery_credit")
	private Double shopDeliveryCredit;
    /**
     * 店铺收藏数
     */
    @TableField("shop_collect")
	private Integer shopCollect;
    /**
     * 店铺商品数
     */
    @TableField("goods_num")
	private Integer goodsNum;
    /**
     * 店铺客服qq
     */
    @TableField("shop_qq")
	private String shopQq;
    /**
     * 是否自营
     */
    @TableField("self_operated")
	private Integer selfOperated;
    /**
     * 申请开店进度
     */
	private Integer step;

    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 手机端背景图片
     */
    @TableField("back_image")
    private String backImage;

    @TableField("banner_url")
    private String bannerUrl;
}
