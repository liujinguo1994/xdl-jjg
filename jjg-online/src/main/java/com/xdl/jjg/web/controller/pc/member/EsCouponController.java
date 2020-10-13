package com.xdl.jjg.web.controller.pc.member;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.dto.EsCouponDTO;
import com.shopx.trade.api.model.domain.vo.EsCouponVO;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsCouponForm;
import com.shopx.trade.web.request.query.EsCouponQueryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 优惠券 前端控制器
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 18:20:15
 */
@Api(value = "/coupon",tags = "会员优惠券模块接口")
@RestController
@RequestMapping("/coupon")
public class EsCouponController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService couponService;

    @PostMapping
    @ResponseBody
    @ApiOperation(value = "新增优惠券信息", notes = "根据form表单数据提交")
    @ApiImplicitParam(name = "couponForm", value = "优惠券Form表单")
    public ApiResponse addCoupon(@RequestBody @Valid EsCouponForm couponForm) {
        EsCouponDTO couponDTO = new EsCouponDTO();
        BeanUtil.copyProperties(couponForm, couponDTO);
        DubboResult result = couponService.insertCoupon(couponDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    @ApiOperation(value = "编辑优惠券信息", notes = "根据form表单数据提交")
    @ApiImplicitParam(name = "couponForm", value = "优惠券Form表单")
    public ApiResponse editCoupon(@RequestBody @Valid EsCouponForm couponForm, @PathVariable Long id) {
        EsCouponDTO couponDTO = new EsCouponDTO();
        BeanUtil.copyProperties(couponForm, couponDTO);
        DubboResult result = couponService.updateCoupon(couponDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/{ids}")
    @ResponseBody
    @ApiOperation(value = "删除优惠券信息")
    @ApiImplicitParam(name = "ids", value = "优惠券主键id")
    public ApiResponse removeCoupon(@PathVariable Integer[] ids) {
        DubboResult result = couponService.deleteCoupon(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    @ApiOperation(value = "获取优惠券信息", notes = "根据ID获取优惠券信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "会员优惠券主键id", required = true, dataType = "long", paramType = "query", example = "1")})
    public ApiResponse<EsCouponVO> searchCoupon(@PathVariable Long id) {
        DubboResult result = couponService.getCoupon(id);
        if (result.isSuccess()) {
            EsCouponVO couponVO = new EsCouponVO();
            BeanUtil.copyProperties(result.getData(), couponVO);
            return ApiResponse.success(couponVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "获取优惠券列表", notes = "根据form表单数据查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponQueryForm", value = "优惠券查询Form表单"),
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    public ApiResponse<EsCouponVO> searchCouponList(@Valid EsCouponQueryForm couponQueryForm,
                                                    @NotEmpty(message = "页数不能为空") int pageSize,
                                                    @NotEmpty(message = "页码不能为空") int pageNum) {
        EsCouponDTO couponDTO = new EsCouponDTO();
        BeanUtil.copyProperties(couponQueryForm, couponDTO);
        DubboPageResult result = couponService.getCouponList(couponDTO, pageSize, pageNum);
        List<EsCouponVO> couponVOList = new ArrayList<>();
        if (result.isSuccess()) {
            List<EsCouponDO> couponDOList = result.getData().getList();
            if (CollectionUtils.isNotEmpty(couponDOList)) {
                couponVOList = couponDOList.stream().map(couponDO -> {
                    EsCouponVO couponVO = new EsCouponVO();
                    BeanUtil.copyProperties(couponDO, couponVO);
                    return couponVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(couponVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    // TODO 会员领取优惠券，要和购物发放优惠券区分开
//    @GetMapping
    @ResponseBody
    @ApiOperation(value = "领取优惠券", notes = "会员领取优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponQueryForm", value = "优惠券查询Form表单"),
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    public ApiResponse<EsCouponVO> receiveCoupon(@Valid EsCouponQueryForm couponQueryForm,
                                                    @NotEmpty(message = "页数不能为空") int pageSize,
                                                    @NotEmpty(message = "页码不能为空") int pageNum) {
        return ApiResponse.success();
    }
}

