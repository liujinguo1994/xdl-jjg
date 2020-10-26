package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsPromotionGoodsDO;
import com.jjg.trade.model.dto.EsPromotionGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsPromotionGoodsService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsList            活动商品列表
     * @param promotionGoodsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsPromotionGoodsDO>
     */
    DubboResult insertPromotionGoods(List<EsPromotionGoodsDTO> goodsList, EsPromotionGoodsDTO promotionGoodsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsList            活动商品列表
     * @param promotionGoodsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsPromotionGoodsDO>
     */
    DubboResult updatePromotionGoods(List<EsPromotionGoodsDTO> goodsList, EsPromotionGoodsDTO promotionGoodsDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param activityId
     * @return: com.shopx.common.model.result.DubboResult<EsPromotionGoodsDO>
     */
    DubboResult<Boolean> getPromotionGoods(Long activityId);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param promotionGoodsDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsPromotionGoodsDO>
     */
    DubboPageResult getPromotionGoodsList(EsPromotionGoodsDTO promotionGoodsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @param promotionType
     * @return: com.shopx.common.model.result.DubboResult<EsPromotionGoodsDO>
     */
    DubboResult deletePromotionGoods(Long id, String promotionType);

    /**
     * 判断商品活动是否存在，是够过期
     * @param //商品 id
     * @return
     */
    DubboResult<Boolean> checkPromotionByGoodsId(Long id);

    /**
     * 根据商品SkuId获取该Sku参与的所有活动
     * @param goodsId
     * @param currTime
     * @param shopId
     * @return
     */
    DubboResult getAllPromotionGoods(Long goodsId, Long currTime, Long shopId, Long skuId);

    /**
     * 根据商品ID获取所有活动
     * @param goodsIdList
     */
    DubboPageResult<EsPromotionGoodsDO> getPromotionByGoodsId(List<Long> goodsIdList);

    /**
     * 根据SkuID获取所有活动
     * @param skuIdList
     */
    DubboPageResult<EsPromotionGoodsDO> getPromotionBySkuId(List<Long> skuIdList);

    /**
     * 根据活动ID和活动类型查出参与此活动的所有商品
     * @param activityId
     * @param promotionType
     * @return
     */
    DubboPageResult<EsPromotionGoodsDO> getPromotionGoods(Long activityId, String promotionType);

    DubboResult<Boolean> getPromotionGoodsIsOld(Long activityId, String promotionType);
}
