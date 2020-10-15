package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsCollectCateryNumDO;
import com.jjg.member.model.domain.EsCutAndEffectDO;
import com.jjg.member.model.domain.EsMemberCollectionGoodsDO;
import com.jjg.member.model.dto.EsMemberCollectionGoodsDTO;
import com.jjg.member.model.dto.EsQueryMemberCollectionGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 会员商品收藏 服务类
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface IEsMemberCollectionGoodsService {

    /**
     * 分页查询会员收藏商品列表
     *
     * @param esQueryMemberCollectionGoodsDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    DubboPageResult<EsMemberCollectionGoodsDO> getMemberCollectionGoodList(int pageNo, int pageSize, EsQueryMemberCollectionGoodsDTO esQueryMemberCollectionGoodsDTO);

    /**
     * 分页查询会员收藏商品列表
     *
     * @param memberId
     * @return
     */
    DubboPageResult<EsCollectCateryNumDO> getGoodSort(Long memberId);

    /**
     * 统计降价和失效收藏数量
     *
     * @param memberId
     * @return
     */
     DubboResult<EsCutAndEffectDO> getCutAndEffetNum(Long memberId);

    /**
     * 分页查询会员收藏商品列表
     *
     * @param esQueryMemberCollectionGoodsDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    DubboPageResult<EsMemberCollectionGoodsDO> getMemberCollectionGoodListByType(int pageNo, int pageSize, EsQueryMemberCollectionGoodsDTO esQueryMemberCollectionGoodsDTO);

    /**
     * 统计收藏商品个数
     * @return
     */
    DubboResult<Integer> getCountGoodsNum(Long memberId);


    /**
     * 判断用户是否收藏该商品
     * @return
     */
    DubboResult<Boolean> hasCollection(Long goodsId, Long memberId);

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
     * 清空所有失效商品
     *
     * @param memberId
     * @return
     */
    DubboResult deleteAllEfect(Long memberId);

    /**
     * 修改降价提醒
     *
     * @param goodsId
     * @return
     */
    DubboResult updateRemind(Long goodsId, Long userId);

    /**
     * 取消降价提醒
     *
     * @param goodsId
     * @return
     */
    DubboResult deleteRemind(Long goodsId, Long userId);

    /**
     * 发送降价短信提醒
     * @param goodsId
     * @return
     */
    void  sendCutPriceSms(Long goodsId, Long shopId, Double money);


     void sendSMSCode(String mobile, String goodsName);


    /**
     * 根据会员ID查询会员收藏商品列表
     * @param memberId
     * @return
     */
    DubboPageResult getMemberCollectionGoodListByMemberId(Long memberId);

    /**
     * 买家-分页查询会员收藏商品列表
     *
     * @param esQueryMemberCollectionGoodsDTO
     * @return
     */
    DubboPageResult<EsMemberCollectionGoodsDO> getMemberCollectionGoodListBuyer(EsQueryMemberCollectionGoodsDTO esQueryMemberCollectionGoodsDTO);

    /**
     * 买家-分页查询会员收藏商品列表
     *
     * @param memberId
     * @return
     */
    DubboResult<com.xdl.jjg.model.domain.EsMemberCollectionGoodsSortStatisticsDO> getMemberCollectionGoodNumBuyer(Long memberId);

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


}
