package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsShopLogiRelDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-17 14:59:13
 */
public interface IEsShopLogiRelService {

    /**
     * 插入数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019-07-17 14:59:13
     * @return: com.shopx.common.model.result.DubboResult<EsShopLogiRelDO>
     */
    DubboResult insertShopLogiRel(Long shopId, Long id);

    /**
     * 根据主键删除数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopLogiRelDO>
     */
    DubboResult adminDeleteShopLogiRel(Long id);

    /**
     * 根据主键删除数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopLogiRelDO>
     */
    DubboResult sellerDeleteShopLogiRel(Long id, Long shopId);

    /**
     *
     * @param shopId
     * @return
     */
    DubboPageResult<EsShopLogiRelDO> getShopLogiRelList(Long shopId);
    /**
     *
     * @param shopId
     * @return
     */
    DubboPageResult<EsShopLogiRelDO> getMyShopLogiRelList(Long shopId);
}
