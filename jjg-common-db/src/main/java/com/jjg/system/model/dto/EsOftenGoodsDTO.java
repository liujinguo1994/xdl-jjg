package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 常买商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
@ToString
public class EsOftenGoodsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 自定义分类id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customCategoryId;

    /**
     * 商品链接
     */
    private String goodsUrl;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 商品id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long goodsId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后修改时间
     */
    private Long updateTime;
}
