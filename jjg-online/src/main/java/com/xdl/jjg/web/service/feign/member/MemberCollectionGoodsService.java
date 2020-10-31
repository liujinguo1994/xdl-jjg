package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberCollectionGoodsDO;
import com.jjg.member.model.domain.EsMemberCollectionGoodsSortStatisticsDO;
import com.jjg.member.model.dto.EsMemberCollectionGoodsDTO;
import com.jjg.member.model.dto.EsQueryMemberCollectionGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

public interface MemberCollectionGoodsService {

    /**
     * 根据会员ID查询会员收藏商品列表
     * @param memberId
     * @return
     */
    DubboPageResult getMemberCollectionGoodListByMemberId(Long memberId);

    /**
     * @Description: 查询商品是够被收藏
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/3/30 10:30
     * @param
     * @return
     * @exception
     *
     */
    DubboResult<Boolean> getIsMemberCollection(Long goodsId, Long memberId);

    /**
     * 买家-分页查询会员收藏商品列表
     *
     * @param esQueryMemberCollectionGoodsDTO
     * @return
     */
    DubboPageResult<EsMemberCollectionGoodsDO> getMemberCollectionGoodListBuyer(EsQueryMemberCollectionGoodsDTO esQueryMemberCollectionGoodsDTO);



    /**
     * 批量删除会员收藏商品
     *
     * @param ids
     * @return
     */
    DubboResult<EsMemberCollectionGoodsDO> deleteMemberCollectionGoodBatch(Long memberId, List<Long> ids);

    /**
     * 删除会员收藏商品
     *
     * @param memberId
     * @param goodsId
     * @return
     */
    DubboResult<EsMemberCollectionGoodsDO> deleteMemberCollectionGood(Long memberId, Long goodsId);

    /**
     * 添加商品收藏
     *
     * @param esMemberCollectionGoodsDTO
     * @return
     */
    DubboResult<EsMemberCollectionGoodsDO> insertMemberCollectionGood(EsMemberCollectionGoodsDTO esMemberCollectionGoodsDTO);


    /**
     * 修改降价提醒
     *
     * @param goodsId
     * @return
     */
    DubboResult updateRemind(Long goodsId, Long userId);

    /**
     * 买家-分页查询会员收藏商品列表
     *
     * @param memberId
     * @return
     */
    DubboResult<EsMemberCollectionGoodsSortStatisticsDO> getMemberCollectionGoodNumBuyer(Long memberId);

    /**
     * 取消降价提醒
     *
     * @param goodsId
     * @return
     */
    DubboResult deleteRemind(Long goodsId, Long userId);
}
