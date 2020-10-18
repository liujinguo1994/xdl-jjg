package com.xdl.jjg.web.controller;

import com.jjg.member.model.domain.EsAdminMemberCouponDO;
import com.jjg.member.model.domain.EsCouponManageMementDO;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.jjg.member.model.dto.QueryAdminCouponDTO;
import com.jjg.member.model.vo.EsAdminMemberCouponVO;
import com.jjg.member.model.vo.EsCouponManageMementVO;
import com.jjg.member.model.vo.EsMemberCouponVO;
import com.jjg.system.model.form.EsCouponQueryForm;
import com.jjg.system.model.form.EsMemberCouponQueryForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.member.MemberCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器-会员优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@Api(value = "/esMemberCoupon", tags = "会员优惠券")
@RequestMapping("/esMemberCoupon")
public class EsMemberCouponController {

    @Autowired
    private MemberCouponService iesMemberCouponService;

    @ApiOperation(value = "分页查询会员优惠券列表", response = EsAdminMemberCouponVO.class)
    @GetMapping(value = "/getCouponList")
    @ResponseBody
    public ApiResponse getCouponList(EsCouponQueryForm form) {
        QueryAdminCouponDTO dto = new QueryAdminCouponDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsAdminMemberCouponDO> result = iesMemberCouponService.getAdminMemberCouponList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsAdminMemberCouponDO> data = result.getData().getList();
            List<EsAdminMemberCouponVO> voList = BeanUtil.copyList(data, EsAdminMemberCouponVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @ApiOperation(value = "分页查询会员优惠券详情列表", response = EsMemberCouponVO.class)
    @GetMapping(value = "/getMemberCouponList")
    @ResponseBody
    public ApiResponse getMemberCouponList(@Valid EsMemberCouponQueryForm form) {
        EsMemberCouponDTO esMemberCouponDTO = new EsMemberCouponDTO();
        BeanUtil.copyProperties(form, esMemberCouponDTO);
        DubboPageResult<EsCouponManageMementDO> result = iesMemberCouponService.getCouponManageMentList(esMemberCouponDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsCouponManageMementDO> data = result.getData().getList();
            List<EsCouponManageMementVO> voList = BeanUtil.copyList(data, EsCouponManageMementVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

