package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaoLin
 * @ClassName EsMemberGoodsDO
 * @Description 会员收藏
 * @create 2019/12/18 15:16
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberGoodsDO implements Serializable {
    /**
     * 商品ID
     */
    private Long id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private Double money;
    /**
     * 购买数量
     */
    private Integer buyCount;
    /**
     * 原图路径
     */
    private String original;
}
