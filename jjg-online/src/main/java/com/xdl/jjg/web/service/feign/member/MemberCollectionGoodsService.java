package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberCollectionGoodsDO;
import com.jjg.member.model.domain.EsMemberCollectionGoodsSortStatisticsDO;
import com.jjg.member.model.dto.EsMemberCollectionGoodsDTO;
import com.jjg.member.model.dto.EsQueryMemberCollectionGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@FeignClient(value = "jjg-member")
public interface MemberCollectionGoodsService {

    /**
     * 根据会员ID查询会员收藏商品列表
     * @param memberId
     * @return
     */
    @GetMapping("/getMemberCollectionGoodListByMemberId")
    DubboPageResult getMemberCollectionGoodListByMemberId(@RequestParam("memberId") Long memberId);

    /**
     * @Description: 查询商品是够被收藏
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/3/30 10:30
     * @param
     * @return
     * @exception
     *
     */
    @GetMapping("/getIsMemberCollection")
    DubboResult<Boolean> getIsMemberCollection(@RequestParam("goodsId") Long goodsId,@RequestParam("memberId")  Long memberId);

    /**
     * 买家-分页查询会员收藏商品列表
     *
     * @param esQueryMemberCollectionGoodsDTO
     * @return
     */
    @GetMapping("/getMemberCollectionGoodListBuyer")
    DubboPageResult<EsMemberCollectionGoodsDO> getMemberCollectionGoodListBuyer(@RequestBody EsQueryMemberCollectionGoodsDTO esQueryMemberCollectionGoodsDTO);



    /**
     * 批量删除会员收藏商品
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteMemberCollectionGoodBatch")
    DubboResult<EsMemberCollectionGoodsDO> deleteMemberCollectionGoodBatch(@RequestParam("memberId") Long memberId,@RequestParam("ids")  List<Long> ids);

    /**
     * 删除会员收藏商品
     *
     * @param memberId
     * @param goodsId
     * @return
     */
    @DeleteMapping("/deleteMemberCollectionGood")
    DubboResult<EsMemberCollectionGoodsDO> deleteMemberCollectionGood(@RequestParam("memberId") Long memberId, @RequestParam("goodsId") Long goodsId);

    /**
     * 添加商品收藏
     *
     * @param esMemberCollectionGoodsDTO
     * @return
     */
    @PostMapping("/insertMemberCollectionGood")
    DubboResult<EsMemberCollectionGoodsDO> insertMemberCollectionGood(@RequestBody EsMemberCollectionGoodsDTO esMemberCollectionGoodsDTO);


    /**
     * 修改降价提醒
     *
     * @param goodsId
     * @return
     */
    @PostMapping("/updateRemind")
    DubboResult updateRemind(@RequestParam("goodsId") Long goodsId, @RequestParam("userId") Long userId);

    /**
     * 买家-分页查询会员收藏商品列表
     *
     * @param memberId
     * @return
     */
    @GetMapping("/getMemberCollectionGoodNumBuyer")
    DubboResult<EsMemberCollectionGoodsSortStatisticsDO> getMemberCollectionGoodNumBuyer(@RequestParam("memberId") Long memberId);

    /**
     * 取消降价提醒
     *
     * @param goodsId
     * @return
     */
    @DeleteMapping("/deleteRemind")
    DubboResult deleteRemind(@RequestParam("goodsId") Long goodsId, @RequestParam("userId") Long userId);
}
