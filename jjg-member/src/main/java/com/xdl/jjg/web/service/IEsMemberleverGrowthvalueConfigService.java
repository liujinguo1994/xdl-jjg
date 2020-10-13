package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMemberleverGrowthvalueConfigDO;
import com.xdl.jjg.model.dto.EsMemberleverGrowthvalueConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  会员等级成长值配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-18 15:10:57
 */
public interface IEsMemberleverGrowthvalueConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberleverGrowthvalueConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberleverGrowthvalueConfigDO>
     */
    DubboResult insertMemberleverGrowthvalueConfig(EsMemberleverGrowthvalueConfigDTO memberleverGrowthvalueConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberleverGrowthvalueConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberleverGrowthvalueConfigDO>
     */
    DubboResult updateMemberleverGrowthvalueConfig(EsMemberleverGrowthvalueConfigDTO memberleverGrowthvalueConfigDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberleverGrowthvalueConfigDO>
     */
    DubboResult<EsMemberleverGrowthvalueConfigDO> getMemberleverGrowthvalueConfig(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberleverGrowthvalueConfigDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberleverGrowthvalueConfigDO>
     */
    DubboPageResult<EsMemberleverGrowthvalueConfigDO> getMemberleverGrowthvalueConfigList(EsMemberleverGrowthvalueConfigDTO memberleverGrowthvalueConfigDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberleverGrowthvalueConfigDO>
     */
    DubboResult deleteMemberleverGrowthvalueConfig(Long id);
}
