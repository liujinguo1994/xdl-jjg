package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCommercelItemsDO;
import com.jjg.member.model.dto.EsCommercelItemsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-member")
public interface CommercelItemsService {

    /**
     * 根据查询会员id查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 11:12:53
     * @param memberId  会员id
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommercelItemsDO>
     */
    DubboPageResult<EsCommercelItemsDO> getCommercelItemsListByMemeberId(Long memberId);


    /**
     * 根据skuId获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:37:18
     * @param skuId    skuId
     * @param cartId    cartId
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    DubboResult<EsCommercelItemsDO> getItemsBySkuId(Long skuId, Long cartId);


    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:29:30
     * @param commercelItemsDTO    购物车项DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    DubboResult insertCommercelItems(EsCommercelItemsDTO commercelItemsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:40:10
     * @param commercelItemsDTO    购物车项DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    DubboResult updateCommercelItems(EsCommercelItemsDTO commercelItemsDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:37:18
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    DubboResult<EsCommercelItemsDO> getCommercelItems(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 13:42:53
     * @param commercelItemsDTO  购物车项DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommercelItemsDO>
     */
    DubboPageResult<EsCommercelItemsDO> getCommercelItemsList(EsCommercelItemsDTO commercelItemsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:40:44
     * @param ids    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    DubboResult deleteCommercelItems(Integer[] ids);

    /**
     * 根据skuId删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 09:40:44
     * @param skuIds    购物车id
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    DubboResult deleteByskuId(Long memberId, Integer[] skuIds);
}
