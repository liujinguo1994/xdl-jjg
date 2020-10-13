package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 卖家端会员信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsSellerMemberInFoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    @ApiModelProperty(required = false,value = "会员id",example = "1")
    private Long memberId;
    /**
     * 用户名
     */
    @ApiModelProperty(required = false,value = "用户名")
    private String name;
    /**
     * 手机号
     */
    @ApiModelProperty(required = false,value = "手机号")
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty(required = false,value = "邮箱")
    private String email;
    /**
     * 最近浏览时间
     */
    @ApiModelProperty(required = false,value = "最近浏览时间",example = "0")
    private Long scanneTime;
    /**
     * 最近下单时间
     */
    @ApiModelProperty(required = false,value = "最近下单时间",example = "0")
    private Long orderTime;
    /**
     * 店铺会员类型
     */
    @ApiModelProperty(required = false,value = "店铺会员类型")
    private String type;

}
