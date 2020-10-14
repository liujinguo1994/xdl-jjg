package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺查询条件
 * </p>
 *
 * @author yuanj 595831328@qq.com
 * @since 2019-06-28
 */
@Data
@ToString
public class ClerkQueryParamDTO implements Serializable {


    /**
     * 关键字
     */
    private String keyword;

    /**
     * 用户状态
     */
    private Integer state;

    /**
     * 店铺id
     */
    private Long shopId;

}
