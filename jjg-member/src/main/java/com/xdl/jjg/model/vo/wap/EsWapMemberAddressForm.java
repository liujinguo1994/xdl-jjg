package com.xdl.jjg.model.vo.wap;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 收货地址表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-01-08
 */
@Data
@ApiModel
public class EsWapMemberAddressForm implements Serializable {

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
     * 手机号码
     */
    @ApiModelProperty(required = true, value = "手机号码")
    private String mobile;


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
     * 所属省份名称
     */
    @ApiModelProperty(required = false, value = "所属省份名称")
    private String province;
    /**
     * 所属城市名称
     */
    @ApiModelProperty(required = false, value = "所属城市名称")
    private String city;

    /**
     * 所属县(区)名称
     */
    @ApiModelProperty(required = false, value = "所属县区名称")
    private String county;

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
     * 是否为默认收货地址
     */
    @ApiModelProperty(required = false, value = "默认地址",example = "1")
    private Integer defAddress;



}
