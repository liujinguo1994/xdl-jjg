package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsGradeWeightConfigDO;
import com.xdl.jjg.entity.EsMemberShopScore;


/**
 * <p>
 * 店铺评分 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsMemberShopScoreMapper extends BaseMapper<EsMemberShopScore> {

    /**
     * 查询权重值
     * @return
     */
    EsGradeWeightConfigDO getScoreWeightValue(Integer commentType);

}
