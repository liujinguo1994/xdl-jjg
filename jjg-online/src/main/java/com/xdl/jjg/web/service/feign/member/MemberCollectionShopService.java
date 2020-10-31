package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberCollectionShopDO;
import com.jjg.member.model.domain.EsQueryCollectionShopDO;
import com.jjg.member.model.dto.EsMemberCollectionShopDTO;
import com.jjg.member.model.dto.EsQueryCollectShopDTO;
import com.jjg.member.model.dto.EsUpdateTopShopDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

public interface MemberCollectionShopService {

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
     * 删除会员收藏店铺
     *
     * @param memberId
     * @param shopId
     * @return
     */
    DubboResult<EsMemberCollectionShopDO> deleteMemberCollectionShop(Long memberId, Long shopId);



    /**
     * 添加备注
     *
     * @param esMemberCollectionShopDTO
     * @return
     */
    DubboResult insertRemarks(EsMemberCollectionShopDTO esMemberCollectionShopDTO);


    /**
     * 置顶操作
     * @param esUpdateTopShopDTO
     * @param memberId
     * @return
     */
    DubboResult updateShopTop(EsUpdateTopShopDTO esUpdateTopShopDTO, Long memberId);


}
