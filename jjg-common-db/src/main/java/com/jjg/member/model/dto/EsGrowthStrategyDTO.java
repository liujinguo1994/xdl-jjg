package com.jjg.member.model.dto;

import com.shopx.member.api.model.domain.vo.EsGrowthValueStrategyVO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评价和收藏成长值
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-20 15:51:03
 */
@Data
@ToString
public class EsGrowthStrategyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    List<EsGrowthValueStrategyVO> strategyVOList;
}
