package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 收货地址表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_address")
public class EsMemberAddress extends Model<EsMemberAddress> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员ID
     */
    @TableField("member_id")
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
    @TableField("province_id")
	private Long provinceId;
    /**
     * 所属城市ID
     */
    @TableField("city_id")
	private Long cityId;
    /**
     * 所属县(区)ID
     */
    @TableField("county_id")
	private Long countyId;
    /**
     * 所属城镇ID
     */
    @TableField("town_id")
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
     * 是否为默认收货地址0默认，1非默认
     */
    @TableField("def_address")
	private Integer defAddress;
    /**
     * 地址别名
     */
    @TableField("ship_address_name")
	private String shipAddressName;



}
