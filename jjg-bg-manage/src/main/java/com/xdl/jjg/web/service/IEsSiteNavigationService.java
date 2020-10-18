package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsSiteNavigationDO;
import com.jjg.system.model.dto.EsSiteNavigationDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsSiteNavigationService {

    /**
     * 插入数据
     *
     * @param siteNavigationDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSiteNavigationDO>
     * @since 2019-06-04
     */
    DubboResult insertSiteNavigation(EsSiteNavigationDTO siteNavigationDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param siteNavigationDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSiteNavigationDO>
     * @since 2019-06-04
     */
    DubboResult updateSiteNavigation(EsSiteNavigationDTO siteNavigationDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSiteNavigationDO>
     * @since 2019-06-04
     */
    DubboResult<EsSiteNavigationDO> getSiteNavigation(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param siteNavigationDTO DTO
     * @param pageSize          行数
     * @param pageNum           页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsSiteNavigationDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsSiteNavigationDO> getSiteNavigationList(EsSiteNavigationDTO siteNavigationDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSiteNavigationDO>
     * @since 2019-06-04
     */
    DubboResult deleteSiteNavigation(Long id);

    //根据客户端类型查询导航菜单(PC,MOBILE)
    DubboPageResult<EsSiteNavigationDO> getByClientType(String clientType);
}
