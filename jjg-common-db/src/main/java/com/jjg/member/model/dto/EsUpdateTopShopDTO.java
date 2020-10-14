package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺置顶操作
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsUpdateTopShopDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 操作类型 1取消置顶，2置顶
     */
    private Integer sort;
    /**
     * 店铺id
     */
    private Long id;


}
