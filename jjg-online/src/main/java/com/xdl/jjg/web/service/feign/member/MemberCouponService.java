package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberCouponDO;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

public interface MemberCouponService {

    /**
     * @Author xiaoLin
     * @Description
     * @Param [memberId, couponIdList]
     * @return java.util.List<java.lang.Long>
     */
    DubboResult getMemberWhetherCouponIds(Long memberId, List<Long> couponIdList);

    /**
     * 根据优惠券id和会员id查询优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboPageResult<EsMemberCouponDO> getByMemberIdAndCouponIdList(EsMemberCouponDTO memberCouponDTO);

    /**
     * 根据查询后台管理优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboResult<Integer> getCountByMemberIdAndCouponId(EsMemberCouponDTO memberCouponDTO);

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberCouponDTO    会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    DubboResult insertMemberCoupon(EsMemberCouponDTO memberCouponDTO);

    /**
     * 根据优惠券id和会员id删除
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberCouponDTO    会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    DubboResult deleteMemberCouponByCouponIdAndMemId(EsMemberCouponDTO memberCouponDTO);


    /**
     * 根据优惠券id和店铺id修改优惠券状态 mvn deploy
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboResult updateStateByShopIdAndCouponId(EsMemberCouponDTO memberCouponDTO);

    /**
     * app端查询优惠券领取数量
     */
    DubboResult<Integer> getCouponCount(Long memberId, Long couponId);


}
