package com.jjg.system.model.form;

import lombok.Data;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.util.Date;

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
public class EsAdminUserTokenForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long userId;

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long expireTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    protected Serializable pkVal() {
        return this.userId;
    }

}
