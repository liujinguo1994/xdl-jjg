package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsNavigationDO;
import com.xdl.jjg.model.dto.EsNavigationDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺导航管理 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-05
 */
public interface IEsNavigationService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 10:10:40
     * @param navigationDTO    店铺导航管理DTO
     * @return: com.shopx.common.model.result.DubboResult<EsNavigationDO>
     */
    DubboResult insertNavigation(EsNavigationDTO navigationDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 10:43:40
     * @param navigationDTO    店铺导航管理DTO
     * @return: com.shopx.common.model.result.DubboResult<EsNavigationDO>
     */
    DubboResult updateNavigation(EsNavigationDTO navigationDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 10:49:40
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsNavigationDO>
     */
    DubboResult<EsNavigationDO> getNavigation(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 10:48:40
     * @param navigationDTO  店铺导航管理DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsNavigationDO>
     */
    DubboPageResult<EsNavigationDO> getNavigationList(EsNavigationDTO navigationDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 11:05:20
     * @param id    主键id
     * @param shopId    店铺id
     * @return: com.shopx.common.model.result.DubboResult<EsNavigationDO>
     */
    DubboResult deleteNavigation(Long id, Long shopId);
}
