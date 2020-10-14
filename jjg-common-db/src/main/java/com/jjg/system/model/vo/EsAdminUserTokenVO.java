package com.jjg.member.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;


/**
 * <p>
 *
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@Data
@Accessors(chain = true)
public class EsAdminUserTokenVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */


    @ApiModelProperty(required = false, value = "userId")
    private Long userId;

    /**
     * token
     */
    @ApiModelProperty(required = false, value = "token")
    private String token;

    /**
     * 过期时间
     */
    @JsonSerialize(using = ToStringSerializer.class)

    @ApiModelProperty(required = false, value = "expireTime")
    private Long expireTime;

    /**
     * 更新时间
     */

    @ApiModelProperty(required = false, value = "updateTime")
    private Long updateTime;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String face;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", example = "1")
    private Long roleId;


    protected Serializable pkVal() {
        return this.userId;
    }

}
