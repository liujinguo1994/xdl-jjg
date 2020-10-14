package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsLoginMemberVO implements Serializable {


    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(required = false, value = "会员登陆用户名")
    private String name;
    /**
     * 昵称
     */
    @ApiModelProperty(required = false,value = "昵称")
    private  String nickname;
    /**
     * 头像
     */
    @ApiModelProperty(required = false,value = "头像")
    private String face;
    /**
     * uid
     */
    @ApiModelProperty(required = false,value = "uid", example = "1")
    private Long id;
    /**
     * token
     */
    @ApiModelProperty(required = false,value = "token")
    private String token;

}
