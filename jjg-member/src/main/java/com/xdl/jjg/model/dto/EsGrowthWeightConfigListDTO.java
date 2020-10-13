package com.xdl.jjg.model.dto;

import com.shopx.member.api.model.domain.vo.EsGrowthWeightConfigVO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 权重配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 11:06:57
 */
@Data
@ToString
public class EsGrowthWeightConfigListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    List<EsGrowthWeightConfigVO> weightVoS;

}
