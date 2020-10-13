package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel
public class EsMemberAddressForm implements Serializable {

    /**
     * 主键ID
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
    private Long id;
    /**
     * 会员ID
     */
    @ApiModelProperty(required = false, value = "会员ID", example = "1")
    private Long memberId;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(required = false, value = "收货人姓名")
    private String name;

    /**
     * 收货人国籍
     */
    @ApiModelProperty(required = false, value = "收货人国籍")
    private String country;

    /**
     * 所属省份ID
     */
    @ApiModelProperty(required = false, value = "所属省份")
    private Long provinceId;

    /**
     * 所属城市ID
     */
    @ApiModelProperty(required = false, value = "所属城市", example = "1")
    private Long cityId;

    /**
     * 所属县(区)ID
     */
    @ApiModelProperty(required = false, value = "所属县区ID", example = "1")
    private Long countyId;

    /**
     * 所属城镇ID
     */
    @ApiModelProperty(required = false, value = "所属城镇ID", example = "1")
    private Long townId;

    /**
     * 所属县(区)名称
     */
    @ApiModelProperty(required = false, value = "所属县区名称")
    private String county;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(required = false, value = "所属城市名称")
    private String city;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(required = false, value = "所属省份名称")
    private String province;

    /**
     * 所属城镇名称
     */
    @ApiModelProperty(required = false, value = "所属城镇名称")
    private String town;

    /**
     * 详细地址
     */
    @ApiModelProperty(required = false, value = "详细地址")
    private String address;

    /**
     * 邮编
     */
    @ApiModelProperty(required = false, value = "邮编")
    private String zip;

    /**
     * 联系电话(一般指座机)
     */
    @ApiModelProperty(required = false, value = "联系电话")
    private String tel;

    /**
     * 手机号码
     */
    @ApiModelProperty(required = true, value = "手机号码")
    private String mobile;

    /**
     * 是否为默认收货地址
     */
    @ApiModelProperty(required = false, value = "默认地址")
    private Integer defAddress;

    /**
     * 地址别名
     */
    @ApiModelProperty(required = false, value = "地址别名")
    private String shipAddressName;

}
