package com.xdl.jjg.web.service;

import com.jjg.member.model.domain.EsCollectionShopInfoDO;
import com.jjg.member.model.domain.EsMemberCollectionShopDO;
import com.jjg.member.model.domain.EsQueryCollectionShopDO;
import com.jjg.member.model.dto.EsMemberCollectionShopDTO;
import com.jjg.member.model.dto.EsQueryCollectShopDTO;
import com.jjg.member.model.dto.EsUpdateTopShopDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 会员收藏店铺表 服务类
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface IEsMemberCollectionShopService {

    /**
     * 分页查询会员收藏店铺列表
     *
     * @return
     */
    DubboPageResult<EsQueryCollectionShopDO> getMemberCollectionShopList(int page, int pageSize, EsQueryCollectShopDTO dto);


    /**
     * 分页查询店铺热销、上新商品
     *
     * @return
     */
    DubboPageResult<EsQueryCollectionShopDO> getMemberCollectionShopListNew(int page, int pageSize, EsQueryCollectShopDTO dto);

    /**
     * 添加会员收藏店铺
     *
     * @param shopId
     * @param memberId
     * @return
     */
    DubboResult<EsMemberCollectionShopDO> insertMemberCollectionShop(Long shopId, Long memberId);

    /**
     * 删除会员收藏店铺
     *
     * @param memberId
     * @param shopId
     * @return
     */
    DubboResult<EsMemberCollectionShopDO> deleteMemberCollectionShop(Long memberId, Long shopId);

    /**
     * 批量删除会员收藏店铺
     *
     * @param memberId
     * @param shopIds
     * @return
     */
    DubboResult<EsMemberCollectionShopDO> deleteMemberCollectionShopBatch(Long memberId, Long[] shopIds);

    /**
     * 设置店铺置顶
     * @return
     */
    DubboResult shopTop(Long id);


    /**
     * 添加备注
     *
     * @param esMemberCollectionShopDTO
     * @return
     */
    DubboResult insertRemarks(EsMemberCollectionShopDTO esMemberCollectionShopDTO);

    /**
     * 查询店铺备注
     *
     * @param id
     * @return
     */
    DubboResult<EsMemberCollectionShopDO> getEsMemberCollectionMarks(Long id);

    /**
     * 置顶操作
     * @param esUpdateTopShopDTO
     * @param memberId
     * @return
     */
    DubboResult updateShopTop(EsUpdateTopShopDTO esUpdateTopShopDTO, Long memberId);

    /**
     * 统计收藏店铺个数
     * @param memberId
     * @return
     */
    DubboResult<Integer> getCountShopNum(Long memberId);

    /**
     * 判断用户是否收藏该店铺
     * @return
     */
    DubboResult<Boolean> hasCollection(Long shopId, Long memberId);

    /**
     * 分页查询收藏店铺列表
     */
    DubboPageResult<EsCollectionShopInfoDO> getCollectionShopList(Long memberId, int pageSize, int pageNum);
}
