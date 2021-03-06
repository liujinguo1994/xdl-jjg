package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 收货地址表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberAddressDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 会员ID
     */
	private Long memberId;

    /**
     * 收货人姓名
     */
	private String name;

    /**
     * 收货人国籍
     */
	private String country;

    /**
     * 所属省份ID
     */
	private Long provinceId;

    /**
     * 所属城市ID
     */
	private Long cityId;

    /**
     * 所属县(区)ID
     */
	private Long countyId;

    /**
     * 所属城镇ID
     */
	private Long townId;

    /**
     * 所属县(区)名称
     */
	private String county;

    /**
     * 所属城市名称
     */
	private String city;

    /**
     * 所属省份名称
     */
	private String province;

    /**
     * 所属城镇名称
     */
	private String town;

    /**
     * 详细地址
     */
	private String address;

    /**
     * 邮编
     */
	private String zip;

    /**
     * 联系电话(一般指座机)
     */
	private String tel;

    /**
     * 手机号码
     */
	private String mobile;

    /**
     * 是否为默认收货地址 0默认，1非默认
     */
	private Integer defAddress;

    /**
     * 地址别名
     */
	private String shipAddressName;


}
