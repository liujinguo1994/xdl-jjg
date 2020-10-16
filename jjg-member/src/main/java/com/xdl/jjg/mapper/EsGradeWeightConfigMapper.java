package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsGradeWeightConfigDO;
import com.xdl.jjg.entity.EsGradeWeightConfig;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-29 14:18:11
 */
public interface EsGradeWeightConfigMapper extends BaseMapper<EsGradeWeightConfig> {

    /**
     * 查询配置列表
     * @return
     */
    List<EsGradeWeightConfigDO> getEsGradeConfigList();

    /**
     * 删除配置信息
     */
    void deleteEsGradeConfig();

}
