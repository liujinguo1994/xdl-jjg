package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评价和收藏成长值配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-20 15:51:03
 */
@Data
public class EsGrowthValueStrategyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
    private Long id;
    /**
     * 成长值类型(0收藏商品，1收藏店铺，2发表评论)
     */
    @ApiModelProperty(required = false,value = "成长值类型(0收藏商品，1收藏店铺，2发表评论)")
	private Integer growthType;

    /**
     * 成长值
     */
    @ApiModelProperty(required = false,value = "成长值")
    private Integer growthValue;
    /**
     * 每天限制次数
     */
    @ApiModelProperty(required = false,value = "每天限制次数",example = "1")
    private Integer limitNum;
    /**
     * 开关
     */
    @ApiModelProperty(required = false,value = "开关",example = "false")
    private Boolean configSwitch;


}
