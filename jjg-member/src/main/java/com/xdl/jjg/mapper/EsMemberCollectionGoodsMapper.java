package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsCollectCateryNumDO;
import com.xdl.jjg.entity.EsMemberCollectionGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员商品收藏 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsMemberCollectionGoodsMapper extends BaseMapper<EsMemberCollectionGoods> {

    /**
     * 查询该会员收藏的商品分类统计
     * @param memberId
     * @return
     */
    List<EsCollectCateryNumDO> getCateryNum(Long memberId);
    /**
     * 依据查询类型统计收藏的商品分类统计
     * @param memberId
     * @return
     */
    List<EsCollectCateryNumDO> getCateryNumByType(@Param("memberId") Long memberId, @Param("list") List<Long> list);
    /**
     * 查询店铺当天收藏次数
     * @param memberId
     * @param timesNow
     * @return
     */
    int getCollectGoodsNum(@Param("memberId") Long memberId, @Param("timesNow") String timesNow);

    /**
     * 根据会员查询收藏的商品列表
     * @param memberId
     * @return
     */
    List<Long> getMemberCollectionGoodListByMemberId(@Param("memberId") Long memberId);

    int getIsMemberCollection(@Param("goodsId") Long goodsId, @Param("memberId") Long memberId);

}
