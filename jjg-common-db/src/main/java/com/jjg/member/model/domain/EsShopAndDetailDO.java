package com.jjg.member.model.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺详细
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-24
 */
@Data
@ToString
public class EsShopAndDetailDO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 会员ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long memberId;

	/**
	 * 会员名称
	 */
	private String memberName;

	/**
	 * 店铺状态
	 */
	private String state;

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 店铺创建时间
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopCreatetime;

	/**
	 * 店铺关闭时间
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopEndtime;

    /**
     * 店铺详细id
     */
	private Long id;

	/**
	 * 佣金比列
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Double commission;


    /**
     * 店铺所在省id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopProvinceId;

    /**
     * 店铺所在市id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopCityId;

    /**
     * 店铺所在县id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopCountyId;

    /**
     * 店铺所在镇id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopTownId;

    /**
     * 店铺所在省名称
     */
	private String shopProvince;

    /**
     * 店铺所在市名称
     */
	private String shopCity;

    /**
     * 店铺所在县名称
     */
	private String shopCounty;

    /**
     * 店铺所在镇名称
     */
	private String shopTown;

    /**
     * 店铺详细地址
     */
	private String shopAdd;

    /**
     * 公司名称
     */
	private String companyName;

    /**
     * 公司地址
     */
	private String companyAddress;

    /**
     * 公司电话
     */
	private String companyPhone;

    /**
     * 电子邮箱
     */
	private String companyEmail;

    /**
     * 员工总数
     */
	private Integer employeeNum;

    /**
     * 注册资金
     */
	private Double regMoney;

    /**
     * 联系人姓名
     */
	private String linkName;

    /**
     * 联系人电话
     */
	private String linkPhone;

    /**
     * 法人姓名
     */
	private String legalName;

    /**
     * 法人身份证
     */
	private String legalId;

    /**
     * 法人身份证照片
     */
	private String legalImg;

    /**
     * 营业执照号
     */
	private String licenseNum;

    /**
     * 营业执照所在省id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long licenseProvinceId;

    /**
     * 营业执照所在市id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long licenseCityId;

    /**
     * 营业执照所在县id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long licenseCountyId;

    /**
     * 营业执照所在镇id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long licenseTownId;

    /**
     * 营业执照所在省名称
     */
	private String licenseProvince;

    /**
     * 营业执照所在市名称
     */
	private String licenseCity;

    /**
     * 营业执照所在县
     */
	private String licenseCounty;

    /**
     * 营业执照所在镇
     */
	private String licenseTown;

    /**
     * 营业执照详细地址
     */
	private String licenseAdd;

    /**
     * 成立日期
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long establishDate;

    /**
     * 营业执照有效期开始
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long licenceStart;

    /**
     * 营业执照有效期结束
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long licenceEnd;

    /**
     * 法定经营范围
     */
	private String scope;

    /**
     * 营业执照电子版
     */
	private String licenceImg;

    /**
     * 组织机构代码
     */
	private String organizationCode;

    /**
     * 组织机构电子版
     */
	private String codeImg;

    /**
     * 一般纳税人证明电子版
     */
	private String taxesImg;

    /**
     * 银行开户名
     */
	private String bankAccountName;

    /**
     * 银行开户账号
     */
	private String bankNumber;

    /**
     * 开户银行支行名称
     */
	private String bankName;

    /**
     * 开户银行所在省id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long bankProvinceId;

    /**
     * 开户银行所在市id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long bankCityId;

    /**
     * 开户银行所在县id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long bankCountyId;

    /**
     * 开户银行所在镇id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long bankTownId;

    /**
     * 开户银行所在省名称
     */
	private String bankProvince;

    /**
     * 开户银行所在市名称
     */
	private String bankCity;

    /**
     * 开户银行所在县名称
     */
	private String bankCounty;

    /**
     * 开户银行所在镇民名称
     */
	private String bankTown;

    /**
     * 开户银行许可证电子版
     */
	private String bankImg;

    /**
     * 税务登记证号
     */
	private String taxesCertificateNum;

    /**
     * 纳税人识别号
     */
	private String taxesDistinguishNum;

    /**
     * 税务登记证书
     */
	private String taxesCertificateImg;

    /**
     * 店铺经营类目
     */
	private String goodsManagementCategory;

    /**
     * 店铺等级
     */
	private Integer shopLevel;

    /**
     * 店铺等级申请
     */
	private Integer shopLevelApply;

    /**
     * 店铺相册已用存储量
     */
	private Double storeSpaceCapacity;

    /**
     * 店铺logo
     */
	private String shopLogo;

    /**
     * 店铺横幅
     */
	private String shopBanner;

    /**
     * 店铺简介
     */
	private String shopDesc;

    /**
     * 是否推荐
     */
	private Integer shopRecommend;

    /**
     * 店铺主题id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopThemeid;

    /**
     * 店铺主题模版路径
     */
	private String shopThemePath;

    /**
     * 店铺主题id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wapThemeid;

    /**
     * wap店铺主题
     */
	private String wapThemePath;

    /**
     * 店铺信用
     */
	private Integer shopCredit;

    /**
     * 店铺好评率
     */
	private Double shopPraiseRate;

    /**
     * 店铺描述相符度
     */
	private Double shopDescriptionCredit;

    /**
     * 服务态度分数
     */
	private Double shopServiceCredit;

    /**
     * 发货速度分数
     */
	private Double shopDeliveryCredit;

    /**
     * 店铺收藏数
     */
	private Integer shopCollect;

    /**
     * 店铺商品数
     */
	private Integer goodsNum;

    /**
     * 店铺客服qq
     */
	private String shopQq;

    /**
     * 是否自营
     */
	private Integer selfOperated;

    /**
     * 申请开店进度
     */
	private Integer step;

	/**
	 * 热门商品列表
	 */
	private List<EsMemberGoodsDO> hotGoodList;

	/**
	 * 上新商品列表
	 */
	private List<EsMemberGoodsDO> newGoodList;
	private String bannerUrl;
	protected Serializable pkVal() {
		return this.id;
	}

}
