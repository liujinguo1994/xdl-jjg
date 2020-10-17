package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.domain.EsSalesRankingGoodsDO;
import com.jjg.shop.model.dto.EsGoodsDTO;
import com.jjg.shop.model.dto.EsGoodsQueryDTO;
import com.xdl.jjg.entity.EsGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface EsGoodsMapper extends BaseMapper<EsGoods> {

    /**
     * 回收站商品删除
     * @param goods_ids
     * @param shopId
     */
    void deleteEsGoods(@Param("goods_ids") Integer[] goods_ids, @Param("shopId") Long shopId);

    EsGoodsDO buyGetEsGoods(@Param("goodsId") Long goodsId);

    void  updateBuyCount(@Param("goodsId") Long goodsId, @Param("goodsNum") Integer goodsNum);

    void  updateViewCount(@Param("goodsId") Long goodsId);

    void  updateCommenCount(@Param("goodsId") Long goodsId);
    /**
     * 批量获取商品信息
     * @param esGoodsDTO
     * @param shopId
     * @return
     */
    List<EsGoods> getEsGoodsList(@Param("esGoodsDTO") EsGoodsDTO esGoodsDTO, @Param("shopId") Long shopId);

    /**
     * 卖家中心 商品分页查询 关联SKU表 isDel 0 商品表信息 isdel 1 商品回收站信息
     * @param page
     * @param esGoodsQueryDTO
     * @return
     */
    IPage<EsGoodsDO> getEsGoodsPageList(Page page, @Param("esGoodsQueryDTO") EsGoodsQueryDTO esGoodsQueryDTO, @Param("cateIds") Long[] categoryId);

    /**
     * 管理后台 商品分页查询 关联SKU表 isDel 0 商品表信息 isdel 1 商品回收站信息
     * @param page
     * @param esGoodsQueryDTO
     * @param cateIds
     * @return
     */
    IPage<EsGoodsDO> adminGetEsGoodsPageList(Page page, @Param("esGoodsQueryDTO") EsGoodsQueryDTO esGoodsQueryDTO, @Param("cateIds") Long[] cateIds);

    /**
     * 获取推荐排行商品信息
     * @return
     */
    List<Long> getRecommendGoods();

    void updateBathByCustom(@Param("customId") Long customId);

    List<EsGoodsDO> getBuyerAdminGoods(@Param("shopId") Long shopId, @Param("tagId") Long tagId, @Param("type") Long type);

    List<EsGoodsDO> getRecommendForYouGoods(@Param("bindSql") String bindSql);

    int updateByTemplateId(@Param("newTemplateId") Long newTemplateId, @Param("oldTemplateId") Long oldTemplateId);

    /**
     * 热门榜单排行商品信息
     */
    List<EsSalesRankingGoodsDO> getByCategoryId(@Param("categoryId") Long categoryId);
}
