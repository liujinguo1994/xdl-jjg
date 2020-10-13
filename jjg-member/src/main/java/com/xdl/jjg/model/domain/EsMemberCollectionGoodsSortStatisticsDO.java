package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberCollectionGoodsSortStatisticsDO implements Serializable {


    /**
     * 全部数量
     */
    private Integer allNum;
    /**
     * 失效数量
     */
    private Integer invalidNum;
    /**
     * 降价数量
     */
    private Integer cutNum;

    /**
     * 标签列表
     */
    private List<EsCategoryMemberDO> labelList;

}
