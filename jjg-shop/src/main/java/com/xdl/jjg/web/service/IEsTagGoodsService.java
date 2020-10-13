package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsGoodsDO;
import com.xdl.jjg.model.domain.EsTagGoodsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsTagGoodsService {

    /**
     * 商品标签关系表数据新增
     * @param tagIds
     * @param goodsId
     * @return
     */
    DubboResult<EsTagGoodsDO> insertTagGoods(Integer[] tagIds, Long goodsId);

    /**
     * 取消绑定商品
     * @param goodsId
     * @return
     */
    DubboResult<EsTagGoodsDO> deleteTagGoods(Long tagId, Long[] goodsId);

    /**
     * 绑定商品
     * @param tagId
     * @param goodsIds
     * @return
     */
    DubboResult<EsTagGoodsDO> insertTagGoods(Long tagId, Long[] goodsIds);

    /**
     *  删除
     * @param goodsId
     * @return
     */
    DubboResult<EsTagGoodsDO> deleteTagGoods(Long goodsId);

    DubboPageResult<EsGoodsDO> queryTagGoods(Integer shopId, Integer num, String mark);

    DubboPageResult<EsGoodsDO> queryTagGoodsById(Long tagId, Long shopId, int pageSize, int pageNum);

    DubboPageResult<EsTagGoodsDO> getTagList(Long goodsId);

    DubboResult deleteTagGoods(Integer[] goodsIds);

    DubboPageResult<EsGoodsDO> getBuyerAdminGoodsTags(Long shopId, Long tagId, Long type);
}
