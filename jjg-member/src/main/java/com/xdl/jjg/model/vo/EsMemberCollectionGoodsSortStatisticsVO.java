package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.member.api.model.domain.EsCategoryMemberDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberCollectionGoodsSortStatisticsVO implements Serializable {

    /**
     * 全部数量
     */
    @ApiModelProperty(required = false,value = "全部数量",example = "1")
    private Integer allNum;
    /**
     * 失效数量
     */
    @ApiModelProperty(required = false,value = "失效数量")
    private Integer invalidNum;
    /**
     * 降价数量
     */
    @ApiModelProperty(required = false,value = "降价数量")
    private Integer cutNum;
    /**
     * 标签列表
     */
    @ApiModelProperty(required = false,value = "标签列表")
    private List<EsCategoryMemberDO> labelList;

}
