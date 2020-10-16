package com.xdl.jjg.web.service;



import com.jjg.shop.model.domain.EsAdminTagGoodsDO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-27 14:57:56
 */
public interface IEsAdminTagGoodsService {

    /**
     * 插入数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019-07-27 14:57:56
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagGoodsDO>
     */
    DubboResult insertAdminTagGoods(Long goodsId, Integer[] tagIds);

    /**
     * 插入数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019-07-27 14:57:56
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagGoodsDO>
     */
    DubboResult deleteAdminTagGoods(Long goodsId);
    /**
     * 插入数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019-07-27 14:57:56
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagGoodsDO>
     */
    DubboResult deleteAdminTagGoods(Integer[] goodsIds);

    DubboPageResult<EsAdminTagGoodsDO> getTagList(Long goodsId);

    /**
     * 根据shopId、tagId获取数据
     * @auther: xl 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param shopId    店铺id
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    DubboPageResult<EsAdminTagGoodsDO> getAdminGoodsTagsByShopId(Long shopId, Long tagId);

    /**
     * 根据标签获取商品数据
     * @param shopId
     * @param tagId
     * @param type 1 热门 2 新品
     * @return
     */
    DubboPageResult<EsGoodsDO> getBuyerAdminGoodsTags(Long shopId, Long tagId, Long type);
}
