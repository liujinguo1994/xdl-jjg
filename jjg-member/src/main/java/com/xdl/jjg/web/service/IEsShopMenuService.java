package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsShopMenuDO;
import com.xdl.jjg.model.domain.EsShopMenuListDO;
import com.xdl.jjg.model.dto.EsShopMenuDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺菜单管理 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsShopMenuService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param shopMenuDTO    店铺菜单管理DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    DubboResult insertShopMenu(EsShopMenuDTO shopMenuDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param shopMenuDTO    店铺菜单管理DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    DubboResult updateShopMenu(EsShopMenuDTO shopMenuDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    DubboResult<EsShopMenuDO> getShopMenu(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param shopMenuDTO  店铺菜单管理DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopMenuDO>
     */
    DubboPageResult<EsShopMenuDO> getShopMenuList(EsShopMenuDTO shopMenuDTO, int pageSize, int pageNum);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 10:06:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopMenuDO>
     */
    DubboPageResult<EsShopMenuDO> getMenuList();

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    DubboResult deleteShopMenu(Long id);

    /**
     * 查询店铺菜单
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/5/19 14:55:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
     DubboPageResult<EsShopMenuListDO> getMenuTree(Long id);
}
