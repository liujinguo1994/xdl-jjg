package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsGrowthWeightConfig;

/**
 * <p>
 *  成长值权重配置Mapper 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 11:06:57
 */
public interface EsGrowthWeightConfigMapper extends BaseMapper<EsGrowthWeightConfig> {

    /**
     * 删除所有数据
     */
    void deleteAll();

}
