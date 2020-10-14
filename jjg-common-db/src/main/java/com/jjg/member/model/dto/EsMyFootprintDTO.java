package com.jjg.member.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员足迹表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:45
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMyFootprintDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 访问时间
     */
    private Long createTime;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 商品价格
     */
    private Double money;

    /**
     * 商品图片
     */
    private String img;

    /**
     * 会员ID
     */
    private Long memberId;


}
