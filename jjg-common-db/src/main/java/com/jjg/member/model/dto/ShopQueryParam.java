package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺查询条件
 * </p>
 *
 * @author yuanj 595831328@qq.com
 * @since 2019-06-24
 */
@Data
@ToString
public class ShopQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 店铺状态
     */
    private String state;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺注册时间最小值
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long shopCreatetimeStart;

    /**
     * 店铺注册时间最大值
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long shopCreatetimeEnd;


}
