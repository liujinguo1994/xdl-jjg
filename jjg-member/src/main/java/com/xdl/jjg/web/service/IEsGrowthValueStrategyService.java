package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsGrowthValueStrategyDO;
import com.jjg.member.model.dto.EsGrowthStrategyDTO;
import com.jjg.member.model.dto.EsGrowthValueStrategyDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  评价和收藏成长值配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-20 15:51:00
 */
public interface IEsGrowthValueStrategyService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param comandcolleGrowthvalueConfigDTOlists    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    DubboResult insertGrowthValueStrategy(EsGrowthStrategyDTO esGrowthStrategyDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param comandcolleGrowthvalueConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    DubboResult updateComandcolleGrowthvalueConfig(EsGrowthValueStrategyDTO comandcolleGrowthvalueConfigDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    DubboResult<EsGrowthValueStrategyDO> getComandcolleGrowthvalueConfig(Long id);

    /**
     * 根据成长值类型计算获取到的成长值
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    DubboResult<EsGrowthValueStrategyDO> getComandcolleGrowthvalueConfigByType(Integer type);


    /**
     * 根据查询收藏商品和店铺查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGrowthValueStrategyDO>
     */
    DubboPageResult<EsGrowthValueStrategyDO> getGrowthValueStrategy();

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    DubboResult deleteComandcolleGrowthvalueConfig(Long id);
}
