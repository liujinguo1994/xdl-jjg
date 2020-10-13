package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberCouponDO;
import com.shopx.member.api.model.domain.EsMemberCouponStatDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsMemberCouponDTO;
import com.shopx.member.api.model.domain.vo.EsMemberCouponStatVO;
import com.shopx.member.api.model.domain.vo.EsMemberCouponVO;
import com.shopx.member.api.service.IEsMemberCouponService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.dto.EsCouponDTO;
import com.shopx.trade.api.model.domain.vo.CartVO;
import com.shopx.trade.api.model.domain.vo.EsCouponVO;
import com.shopx.trade.api.model.domain.vo.EsWaitGetCouponVO;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.api.service.IEsSeckillApplyService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.manager.CartManager;
import com.shopx.trade.web.request.query.EsWapCouponQueryForm;
import com.shopx.trade.web.request.query.EsWapMemberCouponForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 移动端-优惠券 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Api(value = "/wap/member/coupon", tags = "移动端-优惠券")
@RestController
@RequestMapping("/wap/member/coupon")
public class EsWapMemberCouponController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService iesMemberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsSeckillApplyService esSeckillApplyService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iesMemberCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCouponService iesCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iEsMemberCouponService;
    @Autowired
    private CartManager cartManager;


    @ApiOperation(value = "结算页面 获取可使用的优惠券列表", notes = "根据会员id 和 店铺id 获取",response = EsMemberCouponVO.class)
    @GetMapping("/getMemberCouponList/{payMoney}")
    @ResponseBody
    public ApiResponse getMemberCouponList(@PathVariable("payMoney") Double payMoney) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }

        List<CartVO> checkedItems = cartManager.getCheckedGoodsItems(null);
        Map<Long, Double> shopIdPrice = new HashMap<>(12);
        checkedItems.forEach(cartVO -> {
            Long shopId = cartVO.getShopId();
            Double totalPrice = cartVO.getPrice().getGoodsPrice();
            shopIdPrice.put(shopId,totalPrice);
        });

        DubboResult memberCouponInOrder = iEsMemberCouponService.getMemberCouponInOrder(memberId,shopIdPrice);


        if (memberCouponInOrder.isSuccess()) {
            return ApiResponse.success(memberCouponInOrder.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(memberCouponInOrder));
    }

    @ApiOperation(value = "查询已领取优惠券数量", notes = "查询已领取优惠券数量")
    @GetMapping("/getMemberCouponNum")
    @ResponseBody
    public ApiResponse getMemberCouponNum() {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<Integer> result = this.iesMemberCouponService.getCouponNum(userId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "会员获取优惠券", notes = "会员获取优惠券")
    @ApiImplicitParam(name = "couponId", value = "优惠券id", required = true, dataType = "Long", paramType = "path", example = "1")
    @GetMapping("/addMemberCoupon/{couponId}")
    @ResponseBody
    public ApiResponse addMemberCoupon(@PathVariable("couponId") Long couponId ) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }

        EsMemberCouponDTO memberCouponDTO = new EsMemberCouponDTO();
        //设置优惠券信息
        DubboResult<EsCouponDO> resultEsCouponDO = iesCouponService.getCoupon(couponId);
        EsCouponDO esCouponDO = resultEsCouponDO.getData();
        if(esCouponDO == null){
            return ApiResponse.fail(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }

        //判断是否优惠券领取数是否已达上限
        DubboResult<Integer> couponNum = iesMemberCouponService.getCouponNum(userId,couponId);
        if(couponNum.getData() >= esCouponDO.getLimitNum()){
            return ApiResponse.fail(TradeErrorCode.RECEIVE_UPPER_LIMIT.getErrorCode(), TradeErrorCode.RECEIVE_UPPER_LIMIT.getErrorMsg());
        }

        memberCouponDTO.setCouponId(esCouponDO.getId());
        memberCouponDTO.setCouponMoney(esCouponDO.getCouponMoney());
        memberCouponDTO.setCouponThresholdPrice(esCouponDO.getCouponThresholdPrice());
        memberCouponDTO.setEndTime(esCouponDO.getEndTime());
        memberCouponDTO.setStartTime(esCouponDO.getStartTime());
        memberCouponDTO.setIsDel(esCouponDO.getIsDel());
        memberCouponDTO.setShopId(esCouponDO.getShopId());
        memberCouponDTO.setState(1);
        memberCouponDTO.setTitle(esCouponDO.getTitle());
        memberCouponDTO.setType(esCouponDO.getCouponType());

        //设置会员信息
        DubboResult<EsMemberDO> resultEsMemberDO = iesMemberService.getMember(userId);
        EsMemberDO esMemberDO = resultEsMemberDO.getData();
        if(esMemberDO == null){
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        memberCouponDTO.setMemberId(userId);
        memberCouponDTO.setMemberName(esMemberDO.getName());

        //插入数据
        DubboResult<Integer> result = this.iesMemberCouponService.insertMemberCoupon(memberCouponDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(DubboResult.success(result.getData()));
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "商品详情页 根据shopId 获取本店铺优惠券列表",response = EsCouponVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", required = true, dataType = "long", paramType = "query")})
    @GetMapping(value = "/getEsCouponList")
    @ResponseBody
    public ApiResponse getEsCouponList(Long shopId) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboPageResult<EsCouponDO> esCouponList = iesCouponService.getEsCouponListByShopId(shopId);
        DubboResult<List<EsMemberCouponDO>> byMemberId = iesMemberCouponService.getByMemberId(userId);
        Set<Long> hasCouponIds = new HashSet<>();
        if (byMemberId.isSuccess() && byMemberId.getData() != null) {
            hasCouponIds.addAll(byMemberId.getData().stream().map(EsMemberCouponDO::getCouponId).collect(Collectors.toSet()));
        }
        if (esCouponList.isSuccess()) {
            List<EsCouponVO> esCouponVOS = BeanUtil.copyList(esCouponList.getData().getList(), EsCouponVO.class);
            esCouponVOS.forEach(c -> {
                c.setIsReceived(hasCouponIds.contains(c.getId())?1:2);
            });
            return ApiResponse.success(esCouponVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(esCouponList));
        }
    }

    @ApiOperation(value = "查询会员优惠券列表", notes = "根据页码分页展示会员优惠券数据",response = EsMemberCouponVO.class)
    @GetMapping("/getMemberCouponList")
    @ResponseBody
    public ApiResponse getesMemberCouponList(EsWapMemberCouponForm form) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberCouponDTO esMemberCouponDTO = new EsMemberCouponDTO();
        esMemberCouponDTO.setMemberId(userId);
        esMemberCouponDTO.setState(form.getType());
        DubboPageResult<EsMemberCouponDO> result = iesMemberCouponService.getMemberCouponList(esMemberCouponDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsMemberCouponDO> esMemberCouponDOList = result.getData().getList();
            List<EsMemberCouponVO> esMemberCouponVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esMemberCouponDOList)) {
                esMemberCouponVOList = esMemberCouponDOList.stream().map(esMemberCollectionShop -> {
                    EsMemberCouponVO esMemberCouponVO = new EsMemberCouponVO();
                    BeanUtil.copyProperties(esMemberCollectionShop, esMemberCouponVO);
                    return esMemberCouponVO;
                }).collect(Collectors.toList());
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esMemberCouponVOList);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "获取优惠券列表", notes = "根据form表单数据查询",response = EsCouponVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponQueryForm", value = "优惠券查询Form表单"),
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    public ApiResponse<EsCouponVO> searchCouponList(@Valid EsWapCouponQueryForm couponQueryForm) {
        EsCouponDTO couponDTO = new EsCouponDTO();
        BeanUtil.copyProperties(couponQueryForm, couponDTO);
        DubboPageResult result = iesCouponService.getCouponList(couponDTO, couponQueryForm.getPageSize(), couponQueryForm.getPageNum());
        List<EsCouponVO> couponVOList = new ArrayList<>();
        if (result.isSuccess()) {
            List<EsCouponDO> couponDOList = result.getData().getList();
            if (org.apache.dubbo.common.utils.CollectionUtils.isNotEmpty(couponDOList)) {
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

    @ApiOperation(value = "获取待领取优惠券列表及数量",response = EsWaitGetCouponVO.class)
    @GetMapping(value = "/getWaitGetCouponList")
    @ResponseBody
    public ApiResponse getWaitGetCouponList() {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboPageResult<EsCouponDO> result = iesCouponService.getWaitGetCouponList(userId);
        if (result.isSuccess()) {
            List<EsCouponDO> list = result.getData().getList();
            EsWaitGetCouponVO waitGetCouponVO = new EsWaitGetCouponVO();
            if (list != null && list.size() > 0){
                List<EsCouponVO> esCouponVOS = BeanUtil.copyList(list, EsCouponVO.class);
                waitGetCouponVO.setCouponVOList(esCouponVOS);
                waitGetCouponVO.setCount(list.size());
            }
            return ApiResponse.success(waitGetCouponVO);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "优惠券数量统计", response =EsMemberCouponStatVO.class)
    @GetMapping(value = "/getCouponStat")
    @ResponseBody
    public ApiResponse getCouponStat() {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult<EsMemberCouponStatDO> result = iesMemberCouponService.getCouponStat(userId);
        if (result.isSuccess()){
            EsMemberCouponStatVO vo = new EsMemberCouponStatVO();
            BeanUtil.copyProperties(result.getData(),vo);
            return ApiResponse.success(vo);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}




