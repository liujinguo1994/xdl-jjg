package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsAdminMemberCouponDO;
import com.jjg.member.model.domain.EsCouponManageMementDO;
import com.jjg.member.model.domain.EsMemberCouponCountDO;
import com.jjg.member.model.domain.EsMemberCouponDO;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.jjg.member.model.dto.QueryAdminCouponDTO;
import com.jjg.member.model.dto.QuerySellerCouponDTO;
import com.xdl.jjg.entity.EsMemberCoupon;
import com.xdl.jjg.model.domain.EsSellerMemberCouponDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员优惠券 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsMemberCouponMapper extends BaseMapper<EsMemberCoupon> {

    /**
     * 查询会员优惠券明细
     * @param page
     * @param esMemberCouponDTO
     * @return
     */
    IPage<EsMemberCouponDO> getMemberCouponList(Page page, @Param("esMemberCoupon") EsMemberCouponDTO esMemberCouponDTO);

    /**
     * 查询卖家后台优惠券信息
     * @param page
     * @param esMemberCouponDTO
     * @return
     */
    IPage<EsSellerMemberCouponDO> getSellerMember(Page page, @Param("esMemberCoupon") QuerySellerCouponDTO esMemberCouponDTO);

    /**
     * 查询优惠券已经被一个人领了多少次
     * @param esMemberCouponDTO
     * @return
     */
    Integer getMemberCouponLimit(@Param("esMemberCoupon") EsMemberCouponDTO esMemberCouponDTO);

    /**
     * 查询优惠券和领取次数
     */
    List<EsMemberCouponCountDO> getMemberCouponNum(Long memberId);

    /**
     * 查询优惠券数量
     * @param memberId
     * @param times
     * @return
     */
    Integer getCouponNum(@Param("memberId") Long memberId, @Param("timesNow") Long times);
    /**
     * 查询优惠券数量
     * @param memberId
     * @param times
     * @return
     */
    Integer getCouponNumById(@Param("memberId") Long memberId, @Param("couponId") Long couponId, @Param("timesNow") Long times);

    /**
     * 查询后台管理优惠券
     */
    IPage<EsCouponManageMementDO> getAdminCouponList(Page page, @Param("es") EsMemberCouponDTO esMemberCouponDTO);

    /**
     * 删除优惠券
     * @param memberId
     * @param couponId
     */
    void deleteCoupontByMemberIdAndCouponId(@Param("memberId") Long memberId, @Param("couponId") Long couponId, @Param("times") Long times);

    /**
     * 新增优惠券接口
     * @param esMemberCoupon
     */
    void insertMemberCoupon(@Param("es") EsMemberCoupon esMemberCoupon);

    /**
     * 根据会员id判断是否领取优惠券
     * @param memberId
     * @param couponIdList
     */
    List<Long> getMemberWhetherCouponIds(@Param("memberId") Long memberId, @Param("couponIdList") List<Long> couponIdList);


    /**
     * 获取订单中会员店铺优惠券
     * @param memberId
     * @param shopIdList
     */
    List<EsMemberCouponDO> getMemberCouponInOrder(@Param("memberId") Long memberId, @Param("money") Double money, @Param("shopIdList") List<Long> shopIdList,
                                                  @Param("curTime") Long curTime);

    /**
     * 后台-查询会员后台优惠券
     * @param page
     * @param esMemberCouponDTO
     * @return
     */
    IPage<EsAdminMemberCouponDO> getAdminMemberCouponListGroupNum(Page page, @Param("esMemberCoupon") QueryAdminCouponDTO esMemberCouponDTO);

    Integer getCouponId(@Param("memberId") Long memberId, @Param("isCheck") Integer isCheck);

    void updateIsCheckByMemberId(@Param("memberId") Long memberId, @Param("shopIdList") List<Long> shopIdList);

}
