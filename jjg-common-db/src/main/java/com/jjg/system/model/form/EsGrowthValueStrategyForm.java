package com.jjg.system.model.form;


import com.jjg.member.model.vo.EsGrowthValueStrategyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评价和收藏成长值配置
 * </p>
 */
@Data
@ApiModel
public class EsGrowthValueStrategyForm implements Serializable {


    private static final long serialVersionUID = -7066374469753933939L;

    /**
     * 评论及收藏成长值集合
     */
    @ApiModelProperty(value = "评论及收藏成长值集合")
    private List<EsGrowthValueStrategyVO> strategyVOList;

}
