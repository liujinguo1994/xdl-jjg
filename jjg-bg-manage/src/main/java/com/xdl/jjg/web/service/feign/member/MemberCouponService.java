package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsAdminMemberCouponDO;
import com.jjg.member.model.domain.EsCouponManageMementDO;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.jjg.member.model.dto.QueryAdminCouponDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface MemberCouponService {

    /**
     * 后台-根据查询条件查询后台会员列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param queryAdminCouponDTO  会员优惠券DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @GetMapping("/getAdminMemberCouponList")
    DubboPageResult<EsAdminMemberCouponDO> getAdminMemberCouponList(@RequestBody QueryAdminCouponDTO queryAdminCouponDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 根据查询后台管理优惠券列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param esMemberCouponDTO  会员优惠券DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @GetMapping("/getCouponManageMentList")
    DubboPageResult<EsCouponManageMementDO> getCouponManageMentList(@RequestBody EsMemberCouponDTO esMemberCouponDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

}
