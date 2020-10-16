package com.xdl.jjg.web.service;



import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.domain.EsBuyerGoodsDO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.domain.EsSalesRankingGoodsDO;
import com.jjg.shop.model.domain.EsSellerGoodsDO;
import com.jjg.shop.model.dto.EsGoodsDTO;
import com.jjg.shop.model.dto.EsGoodsQueryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  商品服务类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface IEsGoodsService {
    /**
     * 新增商品 卖家中心使用
     * @param esGoodsDTO
     * @return
     */
    DubboResult<EsGoodsDO>  insertEsGoods(EsGoodsDTO esGoodsDTO);
    DubboResult<EsGoodsDO>  insertGiftsEsGoods(EsGoodsDTO esGoodsDTO);
    /**
     * 根据商品DTO 查询商品集合
     * @param esGoodsDTO
     * @return
     */
    DubboPageResult<EsGoodsDO> sellerGetEsGoodsList(EsGoodsQueryDTO esGoodsDTO);

    DubboPageResult<EsGoodsDO> sellerDiscountEsGoodsList(EsGoodsQueryDTO esGoodsDTO);
    /**
     * 更新商品
     * @param esGoodsDTO
     * @param shopId 店铺ID
     * @return
     */
    DubboResult<EsGoodsDO>  sellerUpdateEsGoods(EsGoodsDTO esGoodsDTO, Long shopId);
    /**
     * 卖家中心商品下架 支持批量下架
     * @param ids
     * @return
     */
    DubboResult<EsGoodsDO> sellerUnderEsgoods(Integer[] ids, Long shopId);
    /**
     * 卖家中心商品分页查询(回收站\商品列表)
     * @param esGoodsDTO 商品查询DTO
     * @param pageSize 页数
     * @param pageNum 页码
     * @return
     */
    DubboPageResult<EsGoodsDO> sellerGetEsGoodsList(EsGoodsQueryDTO esGoodsDTO, Long shopId, int pageSize, int pageNum);

    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsCO> getEsGoods(Long id);
    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsCO> getEsBuyerGoods(Long id);
    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsSellerGoodsDO> getSellerEsGoods(Long id);

    /**
     * 根据商品ID获取商品信息
     * @param ids
     * @return
     */
    DubboPageResult<EsGoodsDO> getEsGoods(Long[] ids);

    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsDO> buyGetEsGoods(Long id);

    /**
     * 删除商品 支持批量删除
     * @param id
     * @param shopId 店铺ID
     * @return
     */
    DubboResult<EsGoodsDO>  deleteEsGoods(Integer[] id, Long shopId);
    DubboResult<EsGoodsDO>  deleteEsGoods(Long[] id);

    /**
     * 回收站商品还原 支持批量还原
     * @param ids 商品主键
     * @param shopId 店铺ID
     * @return
     */
    DubboResult<EsGoodsDO> revertEsGoods(Integer[] ids, Long shopId);

    /**
     * 放入回收站
     * @param ids 商品主键
     * @param shopId 店铺ID
     * @return
     */
    DubboResult<EsGoodsDO> inRecycleEsGoods(Integer[] ids, Long shopId);

    /**
     * 商品审核 支持批量审核
     * @param ids 商品id数组
     * @param status 审核状态
     * @param message 审核原因
     * @return
     */
    DubboResult<EsGoodsDO> authEsGoods(Integer[] ids, Integer status, String message);



    /**
     * 商品绑定店铺自定义分类
     * @param shopId 店铺ID
     * @param customId 自定义分类ID
     * @param goodsIds 商品id数组
     * @return
     */
    DubboResult<EsGoodsDO> bindGoodsCustom(Long shopId, Long customId, Integer[] goodsIds);
    /**
     * 商品取消绑定店铺自定义分类
     * @param shopId 店铺ID
     * @param goodsIds 商品id数组
     * @return
     */
    DubboResult<EsGoodsDO> unBindGoodsCustom(Long shopId, Integer[] goodsIds);

    /**
     * 管理后台商品下架 支持批量下架
     * @param ids
     * @return
     */
    DubboResult<EsGoodsDO> adminUnderEsGoods(Integer[] ids);
    /**
     * 店铺关闭时（店铺商品全部下架）g根据店铺ID
     * @param shopId
     * @return
     */
    DubboResult<EsGoodsDO> adminUnderEsGoods(Long shopId);
    /** 管理后台使用
     * 分页查询商品信息
     * @param esGoodsDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    DubboPageResult<EsGoodsDO> adminGetEsGoodsList(EsGoodsQueryDTO esGoodsDTO, int pageSize, int pageNum);

    /** 管理后台初始化索引使用
     * @param pageSize
     * @param pageNum
     * @return
     */
    DubboPageResult<EsGoodsDO> adminGetEsGoodsList(int pageSize, int pageNum);


    DubboResult adminGetEsGoodsCount();


    DubboResult<Map<String,String>> buyCheckGoods(Long[] goodsIds);

    /**
     *下架
     * @param ids
     * @return
     */
    DubboResult<List<Long>> getUnderGoods(Integer[] ids);

    /**
     * 获取标签商品
     * @return
     */
    DubboPageResult<EsGoodsDO> getTagGoods(Long shopId, Long tagId, int pageSize, int pageNum);

    /**
     * 获取自营商品集合
     * @param ids
     * @return
     */
    DubboPageResult<EsGoodsDO> getSelfGoods(List<Long> ids);

    /**
     * 修改商品购买数量
     * @param goodsId
     * @param goodsNum
     * @return
     */
    DubboResult<EsGoodsDO> updateBuyCount(Long goodsId, Integer goodsNum);

    /**
     * 使用mq修改商品销量
     * @param goodsId
     * @param goodsNum
     * @return
     */
    DubboResult<EsGoodsDO> updateBuyCountUseMq(Long goodsId, Integer goodsNum);


    /**
     * 修改商品浏览数量
     * @param goodsId
     * @return
     */
    DubboResult<EsGoodsDO> updateViewCount(Long goodsId);

    /**
     * 修改商品浏览数量
     * @param goodsId
     * @return
     */
    DubboResult<EsGoodsDO> updateCommenCount(Long goodsId);
    /**
     * 根据分类ID 获取商品
     * @param category
     * @return
     */
    DubboPageResult<EsGoodsDO> buyerGetGoodsByCategoryId(Long category);
    /**
     * 根据分类ID 获取商品
     * @param category
     * @return
     */
    DubboPageResult<EsGoodsDO> buyerGetGoodsByCategoryId(Long category, Long shopId, Long goodsId, int pageNum, int pageSize);
    /**
     * 根据商品IDList 查询猜你喜欢 商品
     */
    DubboPageResult<EsGoodsDO> getGuessYouLike(Long[] goodsIds, int pageNum, int pageSize);
    /**
     * 根据商品IDList 查询猜你想找 商品
     */
    DubboPageResult<EsGoodsDO> getGuessLook(Long[] cateIds, String keyword);
    /**
     * 根据销量排行获取分类前五条数据
     * @return
     */
    DubboPageResult<EsBuyerGoodsDO> getRecommendGoods();
    /**
     * 为你推荐
     * @return
     */
    DubboPageResult<EsGoodsDO> getRecommendForYouGoods(String[] goodsNames);

    /**
     * 根据分组ID获取商品信息
     * @return
     */
    DubboPageResult<EsGoodsDO> getCustomGoodsList(Long customId, int pageSize, int pageNum);


    DubboPageResult<EsGoodsDO> getGoodsList(Long templateId);

    DubboResult<EsGoodsDO> updateByTemplateId(Long newTemplateId, Long oldTemplateId);

    /**
     * app端热门榜单排行商品信息
     */
    DubboPageResult<EsSalesRankingGoodsDO> getByCategoryId(Long categoryId, Long goods);
}
