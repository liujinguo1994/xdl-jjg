package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsShopPromiseDO;
import com.jjg.shop.model.dto.EsShopPromiseDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  卖家承诺服务类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-18 09:39:17
 */
public interface IEsShopPromiseService {

    /**
     * 插入数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @param shopPromiseDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopPromiseDO>
     */
    DubboResult insertShopPromise(EsShopPromiseDTO shopPromiseDTO);

    /**
     * 根据条件更新更新数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param shopPromiseDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopPromiseDO>
     */
    DubboResult updateShopPromise(EsShopPromiseDTO shopPromiseDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopPromiseDO>
     */
    DubboResult<EsShopPromiseDO> getShopPromise(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: WAF 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopPromiseDO>
     */
    DubboPageResult<EsShopPromiseDO> getShopPromiseList(Long shopId);

    /**
     * 根据主键删除数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @param shopId  店铺主键ID
     * @return: com.shopx.common.model.result.DubboResult<EsShopPromiseDO>
     */
    DubboResult deleteShopPromise(Long id, Long shopId);
}
