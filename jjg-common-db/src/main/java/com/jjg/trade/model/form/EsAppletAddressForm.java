package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-用户地址
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-20 09:28:26
 */
@Data
@ApiModel
public class EsAppletAddressForm implements Serializable {

    private static final long serialVersionUID = -2403492930926151247L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;


    /**
     * 收货人姓名
     */
    @ApiModelProperty(required = true, value = "收货人姓名")
    @NotBlank(message = "收货人姓名不能为空")
    private String name;

    /**
     * 手机号码
     */
    @ApiModelProperty(required = true, value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String mobile;


    /**
     * 所属省份ID
     */
    @ApiModelProperty(value = "所属省份")
    private Long provinceId;

    /**
     * 所属城市ID
     */
    @ApiModelProperty(value = "所属城市")
    private Long cityId;

    /**
     * 所属县(区)ID
     */
    @ApiModelProperty(value = "所属县区ID")
    private Long countyId;

    /**
     * 所属城镇ID
     */
    @ApiModelProperty(value = "所属城镇ID")
    private Long townId;


    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "所属省份名称")
    private String province;
    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "所属城市名称")
    private String city;

    /**
     * 所属县(区)名称
     */
    @ApiModelProperty(value = "所属县区名称")
    private String county;

    /**
     * 所属城镇名称
     */
    @ApiModelProperty(value = "所属城镇名称")
    private String town;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;


    /**
     * 是否为默认收货地址 0默认，1非默认
     */
    @ApiModelProperty(required = true, value = "是否为默认收货地址 0默认，1非默认")
    @NotNull(message = "是否为默认不能为空")
    private Integer defAddress;

}
