package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsShopSildeDO;
import com.jjg.member.model.dto.EsShopSildeDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 店铺幻灯片 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019/06/24
 */
public interface IEsShopSildeService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:39:30
     * @param shopSildeDTO    店铺幻灯片DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopSildeDO>
     */
    DubboResult<EsShopSildeDO> insertShopSilde(List<EsShopSildeDTO> shopSildeDTO, Long shopId);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:40:10
     * @param list    店铺幻灯片DTO集合
     * @return: com.shopx.common.model.result.DubboResult<EsShopSildeDO>
     */
    DubboResult updateShopSilde(List<EsShopSildeDTO> list);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopSildeDO>
     */
    DubboResult<EsShopSildeDO> getShopSilde(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 13:42:53
     * @param shopId  店铺id
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopSildeDO>
     */
    DubboPageResult<EsShopSildeDO> getShopSildeList(Long shopId);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopSildeDO>
     */
    DubboResult deleteShopSilde(Long id);

}
