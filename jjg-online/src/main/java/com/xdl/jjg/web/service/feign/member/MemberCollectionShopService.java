package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberCollectionShopDO;
import com.jjg.member.model.domain.EsQueryCollectionShopDO;
import com.jjg.member.model.dto.EsMemberCollectionShopDTO;
import com.jjg.member.model.dto.EsQueryCollectShopDTO;
import com.jjg.member.model.dto.EsUpdateTopShopDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface MemberCollectionShopService {

    /**
     * 分页查询会员收藏店铺列表
     *
     * @return
     */
    @GetMapping("/getMemberCollectionShopList")
    DubboPageResult<EsQueryCollectionShopDO> getMemberCollectionShopList(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,@RequestBody EsQueryCollectShopDTO dto);


    /**
     * 分页查询店铺热销、上新商品
     *
     * @return
     */
    @GetMapping("/getMemberCollectionShopListNew")
    DubboPageResult<EsQueryCollectionShopDO> getMemberCollectionShopListNew(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,@RequestBody EsQueryCollectShopDTO dto);



    /**
     * 删除会员收藏店铺
     *
     * @param memberId
     * @param shopId
     * @return
     */
    @DeleteMapping("/deleteMemberCollectionShop")
    DubboResult<EsMemberCollectionShopDO> deleteMemberCollectionShop(@RequestParam("memberId") Long memberId, @RequestParam("shopId") Long shopId);



    /**
     * 添加备注
     *
     * @param esMemberCollectionShopDTO
     * @return
     */
    @PostMapping("/insertRemarks")
    DubboResult insertRemarks(@RequestBody EsMemberCollectionShopDTO esMemberCollectionShopDTO);


    /**
     * 置顶操作
     * @param esUpdateTopShopDTO
     * @param memberId
     * @return
     */
    @PostMapping("/updateShopTop")
    DubboResult updateShopTop(@RequestBody EsUpdateTopShopDTO esUpdateTopShopDTO, @RequestParam("memberId") Long memberId);


}
