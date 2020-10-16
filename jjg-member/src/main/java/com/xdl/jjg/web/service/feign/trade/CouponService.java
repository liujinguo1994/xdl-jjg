package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.domain.EsCouponDO;
import com.jjg.trade.model.dto.EsCouponDTO;
import com.jjg.trade.model.dto.EsMemberTradeCouponDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "jjg-online")
public interface CouponService {

    /**
     * 会员模块
     * 根据id获取数据
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    @GetMapping("/getCouponById")
    DubboResult<EsCouponDO> getCoupon(Long id);

    /***\
     *  注册赠券查询 优惠券
     * @param esCouponDTO
     * @return
     */
    @GetMapping("/getCoupons")
    DubboPageResult<EsCouponDO> getCoupons(EsCouponDTO esCouponDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param couponDTO 优惠券DTO
     * @param id        主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    @PostMapping("/updateCouponById")
    DubboResult updateCoupon(@RequestParam("couponDTO") EsCouponDTO couponDTO,@RequestParam("id") Long id);

    /**
     * 会员模块
     * 获取未领取的优惠券
     * LiuJG
     * @param esMemberCouponDTOList
     * @param pageSize
     * @param pageNum
     * @return
     */
//    @GetMapping("/getNotReceivedCouponList")
//    DubboPageResult<EsCouponDO> getNotReceivedCouponList(List<EsMemberTradeCouponDTO> esMemberCouponDTOList, int pageSize, int pageNum);

    /**
     * @Description: 商城 商品详情页 根据shopId 获取本店铺优惠券列表
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/29 13:19
     *
     */
    @GetMapping("/getEsCouponListByIds")
    DubboPageResult<EsCouponDO> getEsCouponListByIds(List<Long> ids);
}
