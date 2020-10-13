package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.EsSellerCouponDO;
import com.shopx.trade.api.model.domain.dto.EsCouponDTO;
import com.shopx.trade.api.model.domain.dto.EsMemberTradeCouponDTO;
import com.shopx.trade.api.model.domain.dto.EsOrderDTO;
import com.shopx.trade.api.model.domain.dto.EsSellerCouponDTO;
import com.shopx.trade.api.model.domain.vo.EsCouponVO;

import java.util.List;

/**
 * <p>
 * 优惠券 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsCouponService {

    /**
     * 插入数据
     *
     * @param couponDTO 优惠券DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    DubboResult insertCoupon(EsCouponDTO couponDTO);
    DubboResult sellerInsertCoupon(EsSellerCouponDTO couponDTO);
    /**
     * 根据条件更新更新数据
     *
     * @param couponDTO 优惠券DTO
     * @param id        主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    DubboResult updateCoupon(EsCouponDTO couponDTO, Long id);
    /**
     * 根据条件更新更新数据
     *
     * @param couponDTO 优惠券DTO
     * @param id        主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    DubboResult sellerUpdateCoupon(EsSellerCouponDTO couponDTO, Long id);
    /**
     * 会员模块
     * 根据id获取数据
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    DubboResult<EsCouponDO> getCoupon(Long id);

    /***\
     *  注册赠券查询 优惠券
     * @param esCouponDTO
     * @return
     */
    DubboPageResult<EsCouponDO> getCoupons(EsCouponDTO esCouponDTO);

    DubboResult<EsSellerCouponDO> getSellerCoupon(Long id);
    /**
     * 根据查询条件查询列表
     *
     * @param couponDTO 优惠券DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponDO>
     */
    DubboPageResult<EsCouponDO> getCouponList(EsCouponDTO couponDTO, int pageSize, int pageNum);

    /**
     * 卖家端
     * 查询优惠券列表
     * @param couponDTO 优惠券DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponDO>
     */
    DubboPageResult<EsSellerCouponDO> getSellerCouponList(EsCouponDTO couponDTO, int pageSize, int pageNum);


    /**
     * 根据主键删除数据
     *
     * @param ids 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    DubboResult deleteCoupon(Integer[] ids);

    /**
     * 下架优惠券
     *
     * @param id 主键id
     * @auther: liujg
     * @date: 2019/08/12 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    DubboResult downCoupon(Long id);

    /**
     * 会员模块
     * 获取未领取的优惠券
     * LiuJG
     * @param esMemberCouponDTOList
     * @param pageSize
     * @param pageNum
     * @return
     */
    DubboPageResult<EsCouponDO> getNotReceivedCouponList(List<EsMemberTradeCouponDTO> esMemberCouponDTOList, int pageSize, int pageNum);

    /**
     * 增加优惠券使用数量
     * @param id
     * LJG
     */
    DubboResult updateUsedNum(Long id);

    /**
     * 会员模块 查询会员列表list
     *
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponDO>
     */
    DubboResult getMemberCouponList();

    /**
     * @Description: 消息处理 赠送优惠券，修改优惠券领取数量，增加会员优惠券表
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/28 13:19
     * @param
     * @return       void
     * @exception
     *
     */
    DubboResult addMemberCoupon(EsCouponVO esCouponVO, EsOrderDTO esOrderDTO);

    /**
     * @Description: 消息处理 修改优惠券领取数量，删除会员优惠券
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/28 13:19
     * @param
     * @return       void
     * @exception
     *
     */
    DubboResult redMemberCoupon(EsCouponVO esCouponVO, EsOrderDTO esOrderDTO);

    /**
     * @Description: 定时任务处理优惠券过期状态
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/29 13:19
     * @param
     * @return       void
     * @exception
     *
     */
    DubboResult updateCouponByTask(EsCouponDTO esCouponDTO);
    /**
     * @Description: 领券中心 获取最新发布的优惠券列表
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/29 13:19
     * @param
     * @return       void
     * @exception
     *
     */
    DubboPageResult<EsCouponDO> getEsCouponListAtHome(Integer pageNo, Integer pageSize);

    /**
     * @Description: 商城 商品详情页 根据shopId 获取本店铺优惠券列表
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/29 13:19
     *
     */
    DubboPageResult<EsCouponDO> getEsCouponListByShopId(Long shopId);

    /**
     * @Description: 商城 商品详情页 根据shopId 获取本店铺优惠券列表
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/29 13:19
     *
     */
    DubboPageResult<EsCouponDO> getEsCouponListByIds(List<Long> ids);

    //app端待领取优惠券list
    DubboPageResult<EsCouponDO> getWaitGetCouponList(Long userId);
}
