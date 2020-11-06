package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsAdminMemberCouponDO;
import com.jjg.member.model.domain.EsMemberCouponDO;
import com.jjg.member.model.domain.EsTradeCouponDO;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@FeignClient(value = "jjg-member")
public interface MemberCouponService {

    @PostMapping("/updateMemberCouponIsNotCheck")
    DubboResult updateMemberCouponIsNotCheck(@RequestBody EsMemberCouponDTO memberCouponDTO);

    @PostMapping("/updateMemberCouponIsCheck")
    DubboResult updateMemberCouponIsCheck(@RequestBody EsMemberCouponDTO memberCouponDTO);

    /**
     * @Author xiaoLin
     * @Description
     * @Param [memberId, couponIdList]
     * @return java.util.List<java.lang.Long>
     */
    @GetMapping("/getMemberWhetherCouponIds")
    DubboResult getMemberWhetherCouponIds(@RequestParam("memberId") Long memberId, @RequestParam("couponIdList") List<Long> couponIdList);

    /**
     * @Description: 通过店铺以及店铺的价格活动符合条件的优惠券
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/6/16 15:43
     * @param
     * @return       com.shopx.common.model.result.DubboResult
     * @exception
     *
     */
    @GetMapping("/getMemberCouponInOrder")
    DubboResult getMemberCouponInOrder(@RequestParam("memberId") Long memberId, @RequestParam("shopIdPrice") Map<Long, Double> shopIdPrice);

    /**
     * 根据优惠券id和会员id查询优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @GetMapping("/getByMemberIdAndCouponIdList")
    DubboPageResult<EsMemberCouponDO> getByMemberIdAndCouponIdList(@RequestBody EsMemberCouponDTO memberCouponDTO);

    @PostMapping("/updateIsCheckByMemberIdAndShopId")
    DubboResult updateIsCheckByMemberIdAndShopId(@RequestParam("memberId") Long memberId, @RequestParam("shopIdList") List<Long> shopIdList);

    /**
     * 根据查询后台管理优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @GetMapping("/getCountByMemberIdAndCouponId")
    DubboResult<Integer> getCountByMemberIdAndCouponId(@RequestBody EsMemberCouponDTO memberCouponDTO);

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberCouponDTO    会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    @PostMapping("/insertMemberCoupon")
    DubboResult insertMemberCoupon(@RequestBody EsMemberCouponDTO memberCouponDTO);

    /**
     * 根据优惠券id和会员id删除
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberCouponDTO    会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    @DeleteMapping("/deleteMemberCouponByCouponIdAndMemId")
    DubboResult deleteMemberCouponByCouponIdAndMemId(@RequestBody EsMemberCouponDTO memberCouponDTO);


    /**
     * 根据优惠券id和店铺id修改优惠券状态 mvn deploy
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @PostMapping("/updateStateByShopIdAndCouponId")
    DubboResult updateStateByShopIdAndCouponId(@RequestBody EsMemberCouponDTO memberCouponDTO);

    /**
     * app端查询优惠券领取数量
     */
    @GetMapping("/getCouponCount")
    DubboResult<Integer> getCouponCount(@RequestParam("") Long memberId, @RequestParam("couponId") Long couponId);


    /**
     * 买家-根据查询条件查询后台会员列表
     * @auther: 下xl 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @GetMapping("/getBuyerMemberCouponList")
    DubboResult<EsAdminMemberCouponDO> getBuyerMemberCouponList(@RequestParam("memberId") Long memberId);

    /**
     * 买家-根据查询条件查询后台会员列表
     * @auther: 下xl 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @GetMapping("/getBuyerMemberCouponNumList")
    DubboResult<EsAdminMemberCouponDO> getBuyerMemberCouponNumList(@RequestParam("memberId") Long memberId);

    /**
     * 根据查询推荐优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberId  会员ID
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @GetMapping("/getMemberCouponRecommendList")
    DubboPageResult<EsTradeCouponDO> getMemberCouponRecommendList(@RequestParam("memberId") Long memberId, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);


    /**
     * 统计已领取优惠券数目
     * @return
     */
    @GetMapping("/getCouponNum")
    DubboResult<Integer> getCouponNum(@RequestParam("memberId") Long memberId);


    /**
     * 查询该优惠券是否已领取
     * @param memberId
     * @param couponId
     * @return
     */
    @GetMapping("/getCouponNum")
    DubboResult<Integer> getCouponNum(@RequestParam("memberId") Long memberId, @RequestParam("couponId") Long couponId);





}
