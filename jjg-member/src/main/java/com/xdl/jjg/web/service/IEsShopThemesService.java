package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsShopThemesDO;
import com.jjg.member.model.dto.EsShopThemesDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺模版 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-01
 */
public interface IEsShopThemesService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 14:39:30
     * @param shopThemesDTO    店铺模版DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    DubboResult insertShopThemes(EsShopThemesDTO shopThemesDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 13:10:10
     * @param shopThemesDTO    店铺模版DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    DubboResult updateShopThemes(EsShopThemesDTO shopThemesDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 14:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    DubboResult<EsShopThemesDO> getShopThemes(Long id);

    /**
     * 根据查询条件查询列表-后台
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 13:42:53
     * @param shopThemesDTO  店铺模版DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopThemesDO>
     */
    DubboPageResult<EsShopThemesDO> getShopThemesList(EsShopThemesDTO shopThemesDTO, int pageSize, int pageNum);

    /**
     * 根据查询条件查询列表-卖家端
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 13:42:53
     * @param shopThemesDTO  店铺模版DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopThemesDO>
     */
    DubboPageResult<EsShopThemesDO> getSellerShopThemesList(EsShopThemesDTO shopThemesDTO, Long shopId, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    DubboResult deleteShopThemes(Long id);


    /**
     * 切换店铺模板
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-04 16:03:44
     * @param themesId    主键id
     *  @param shopId    店铺
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    DubboResult changeShopThemes(Long themesId, Long shopId);
}
