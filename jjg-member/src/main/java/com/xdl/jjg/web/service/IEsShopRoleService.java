package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsShopMenuDO;
import com.xdl.jjg.model.domain.EsShopRoleDO;
import com.xdl.jjg.model.dto.EsShopRoleDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 店铺角色 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsShopRoleService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param shopRoleDTO    店铺角色DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopRoleDO>
     */
    DubboResult insertShopRole(EsShopRoleDTO shopRoleDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param shopRoleDTO    店铺角色DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopRoleDO>
     */
    DubboResult updateShopRole(EsShopRoleDTO shopRoleDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopRoleDO>
     */
    DubboResult<EsShopRoleDO> getShopRole(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param shopRoleDTO  店铺角色DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopRoleDO>
     */
    DubboPageResult<EsShopRoleDO> getShopRoleList(EsShopRoleDTO shopRoleDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     *  @param shopId  店铺id
     * @return: com.shopx.common.model.result.DubboResult<EsShopRoleDO>
     */
    DubboResult deleteShopRole(Long id, Long shopId);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param roleId    角色id
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    DubboPageResult<EsShopMenuDO> getMenuByRoleBy(Long roleId);

    DubboResult<List<String>> getRoleMenu(Long id, Long shopId);
}
