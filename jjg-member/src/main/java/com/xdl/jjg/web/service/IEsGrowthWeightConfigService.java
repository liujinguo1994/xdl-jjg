package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsGrowthWeightConfigDO;
import com.jjg.member.model.dto.EsGrowthWeightConfigDTO;
import com.jjg.member.model.dto.EsGrowthWeightConfigListDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  成长值权重配置服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 11:06:56
 */
public interface IEsGrowthWeightConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param growthWeightConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    DubboResult insertGrowthWeightConfig(EsGrowthWeightConfigListDTO growthWeightConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param growthWeightConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    DubboResult updateGrowthWeightConfig(EsGrowthWeightConfigDTO growthWeightConfigDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    DubboResult<EsGrowthWeightConfigDO> getGrowthWeightConfig(Long id);

    /**
     * 根据数据类型获取成长值权重
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param type    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    DubboResult<EsGrowthWeightConfigDO> getGrowthWeightConfigWeight(Integer type);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGrowthWeightConfigDO>
     */
    DubboPageResult<EsGrowthWeightConfigDO> getGrowthWeightConfigList();

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    DubboResult deleteGrowthWeightConfig(Long id);
}
