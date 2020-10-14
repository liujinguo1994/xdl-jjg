package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsAdminMemberCouponDO;
import com.jjg.member.model.domain.EsCouponManageMementDO;
import com.jjg.member.model.domain.EsMemberCouponDO;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.jjg.member.model.dto.QueryAdminCouponDTO;
import com.jjg.member.model.dto.QuerySellerCouponDTO;
import com.xdl.jjg.model.domain.EsMemberCouponStatDO;
import com.xdl.jjg.model.domain.EsSellerMemberCouponDO;
import com.xdl.jjg.model.domain.EsTradeCouponDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员优惠券 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberCouponService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberCouponDTO    会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    DubboResult insertMemberCoupon(EsMemberCouponDTO memberCouponDTO);

 
    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberCouponDTO    会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    DubboResult updateMemberCoupon(EsMemberCouponDTO memberCouponDTO);

    /**
     * 根据优惠券id和会员id修改优惠券状态
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberCouponDTO    会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    DubboResult updateMemberCouponByCouponIdAndMemId(EsMemberCouponDTO memberCouponDTO);

    /**
     * 根据优惠券id和会员id删除
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberCouponDTO    会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    DubboResult deleteMemberCouponByCouponIdAndMemId(EsMemberCouponDTO memberCouponDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    DubboResult<EsMemberCouponDO> getMemberCoupon(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboPageResult<EsMemberCouponDO> getMemberCouponList(EsMemberCouponDTO memberCouponDTO, int pageSize, int pageNum);

    /**
     * 后台-根据查询条件查询后台会员列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param queryAdminCouponDTO  会员优惠券DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboPageResult<EsAdminMemberCouponDO> getAdminMemberCouponList(QueryAdminCouponDTO queryAdminCouponDTO, int pageSize, int pageNum);

    /**
     * 买家-根据查询条件查询后台会员列表
     * @auther: 下xl 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboResult<EsAdminMemberCouponDO> getBuyerMemberCouponList(Long memberId);

    /**
     * 买家-根据查询条件查询后台会员列表
     * @auther: 下xl 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboResult<EsAdminMemberCouponDO> getBuyerMemberCouponNumList(Long memberId);

    /**
     * 根据查询条件查询卖家会员列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param querySellerCouponDTO  会员优惠券DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboPageResult<EsSellerMemberCouponDO> getSellerMemberCouponList(QuerySellerCouponDTO querySellerCouponDTO, int pageSize, int pageNum);

    /**
     * 根据查询推荐优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberId  会员ID
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboPageResult<EsTradeCouponDO> getMemberCouponRecommendList(Long memberId, int pageSize, int pageNum);


    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    DubboResult deleteMemberCoupon(Long id);

    /**
     * 统计已领取优惠券数目
     * @return
     */
    DubboResult<Integer> getCouponNum(Long memberId);

    DubboResult<Integer> getCouponId(Long memberId, Integer isCheck);

    /**
     * 查询该优惠券是否已领取
     * @param memberId
     * @param couponId
     * @return
     */
    DubboResult<Integer> getCouponNum(Long memberId, Long couponId);
    /**
     * 根据查询后台管理优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param esMemberCouponDTO  会员优惠券DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboPageResult<EsCouponManageMementDO> getCouponManageMentList(EsMemberCouponDTO esMemberCouponDTO, int pageSize, int pageNum);

    /**
     * 根据查询后台管理优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboResult<Integer> getCountByMemberIdAndCouponId(EsMemberCouponDTO memberCouponDTO);

    /**
     * 根据优惠券id和店铺id修改优惠券状态 mvn deploy
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboResult updateStateByShopIdAndCouponId(EsMemberCouponDTO memberCouponDTO);

    /**
     * 根据优惠券id和会员id查询优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCouponDTO  会员优惠券DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    DubboPageResult<EsMemberCouponDO> getByMemberIdAndCouponIdList(EsMemberCouponDTO memberCouponDTO);


    /**
     * @Author xiaoLin
     * @Description
     * @Param [memberId, couponIdList]
     * @return java.util.List<java.lang.Long>
     */
    DubboResult getMemberWhetherCouponIds(Long memberId, List<Long> couponIdList);

    /**
     * @Author xiaoLin
     * @Description
     * @Param [memberId, shopIdList]
     * @return java.util.List<java.lang.Long>
     */
    DubboResult getMemberCouponInOrder(Long memberId, Double money, List<Long> shopIdList);
    /**
     * @Description: 通过店铺以及店铺的价格活动符合条件的优惠券
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/6/16 15:43
     * @param
     * @return       com.shopx.common.model.result.DubboResult
     * @exception
     *
     */
    DubboResult getMemberCouponInOrder(Long memberId, Map<Long, Double> shopIdPrice);

    DubboResult updateMemberCouponIsCheck(EsMemberCouponDTO memberCouponDTO);

    DubboResult updateMemberCouponIsNotCheck(EsMemberCouponDTO memberCouponDTO);

    DubboResult<EsMemberCouponDO> getMemberCouponByOrder(String orderSn);


    DubboResult updateIsCheckByMemberIdAndShopId(Long memberId, List<Long> shopIdList);

    /**
     * app端查询优惠券领取数量
     */
    DubboResult<Integer> getCouponCount(Long memberId, Long couponId);

    /**
     * app端优惠券数量统计
     */
    DubboResult<EsMemberCouponStatDO> getCouponStat(Long memberId);

    DubboResult<List<EsMemberCouponDO>> getByMemberId(Long memberId);

}
