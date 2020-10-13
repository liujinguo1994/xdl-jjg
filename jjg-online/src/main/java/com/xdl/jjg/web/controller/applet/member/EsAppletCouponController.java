package com.xdl.jjg.web.controller.applet.member;

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
import com.shopx.trade.api.model.domain.vo.CartVO;
import com.shopx.trade.api.model.domain.vo.EsCouponVO;
import com.shopx.trade.api.model.domain.vo.EsWaitGetCouponVO;
import com.shopx.trade.api.model.domain.vo.ReturnCouponMsgVO;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.api.service.IEsSeckillApplyService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.manager.CartManager;
import com.shopx.trade.web.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序-优惠券 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-02
 */
@Api(value = "/applet/member/coupon", tags = "小程序-优惠券")
@RestController
@RequestMapping("/applet/member/coupon")
public class EsAppletCouponController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSeckillApplyService esSeckillApplyService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCouponService iesMemberCouponService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iesCouponService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;
    @Autowired
    private CartManager cartManager;

    @ApiOperation(value = "结算页面-获取可使用的优惠券列表",response = EsMemberCouponVO.class)
    @GetMapping("/getCouponList")
    @ResponseBody
    public ApiResponse getMemberCouponList(@Valid EsAppletEnabledCouponForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        List<CartVO> checkedItems = cartManager.getCheckedGoodsItems(null);
        Map<Long, Double> shopIdPrice = new HashMap<>(12);
        checkedItems.forEach(cartVO -> {
            Long shopId = cartVO.getShopId();
            Double totalPrice = cartVO.getPrice().getGoodsPrice();
            shopIdPrice.put(shopId,totalPrice);
        });
        DubboResult result = iesMemberCouponService.getMemberCouponInOrder(memberId,shopIdPrice);

        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "查询已领取优惠券数量")
    @GetMapping("/getMemberCouponNum/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getMemberCouponNum(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        DubboResult<Integer> result = this.iesMemberCouponService.getCouponNum(memberId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "会员领取优惠券")
    @GetMapping("/addMemberCoupon")
    @ResponseBody
    public ApiResponse addMemberCoupon(@Valid EsAppletAddMemberCouponForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();

        EsMemberCouponDTO memberCouponDTO = new EsMemberCouponDTO();
        //设置优惠券信息
        DubboResult<EsCouponDO> resultEsCouponDO = iesCouponService.getCoupon(form.getCouponId());
        EsCouponDO esCouponDO = resultEsCouponDO.getData();
        if(esCouponDO == null){
            return ApiResponse.fail(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }

        //判断是否优惠券领取数是否已达上限
        DubboResult<Integer> couponNum = iesMemberCouponService.getCouponNum(memberId,form.getCouponId());
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
        memberCouponDTO.setMemberId(memberId);
        memberCouponDTO.setMemberName(dubboResult.getData().getName());

        //插入数据
        DubboResult<Integer> result = iesMemberCouponService.insertMemberCoupon(memberCouponDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(DubboResult.success(result.getData()));
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "商品详情页-根据shopId获取本店铺优惠券列表",response = EsCouponVO.class)
    @ApiImplicitParam(name = "shopId", value = "店铺ID", required = true, dataType = "long", paramType = "path")
    @GetMapping(value = "/getEsCouponList/{shopId}")
    @ResponseBody
    public ApiResponse getEsCouponList(@PathVariable Long shopId) {
        DubboPageResult<EsCouponDO> result = iesCouponService.getEsCouponListByShopId(shopId);
        if (result.isSuccess()) {
            List<EsCouponVO> esCouponVOS = BeanUtil.copyList(result.getData().getList(), EsCouponVO.class);
            return ApiResponse.success(esCouponVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询会员优惠券列表",response = EsMemberCouponVO.class)
    @GetMapping("/getMemberCouponList")
    @ResponseBody
    public ApiResponse getesMemberCouponList(@Valid EsAppletMemberCouponForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        EsMemberCouponDTO esMemberCouponDTO = new EsMemberCouponDTO();
        esMemberCouponDTO.setMemberId(memberId);
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


    @ApiOperation(value = "获取待领取优惠券列表及数量",response = EsWaitGetCouponVO.class)
    @GetMapping(value = "/getWaitGetCouponList/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getWaitGetCouponList(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        DubboPageResult<EsCouponDO> result = iesCouponService.getWaitGetCouponList(memberId);
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
    @GetMapping(value = "/getCouponStat/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getCouponStat(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        DubboResult<EsMemberCouponStatDO> result = iesMemberCouponService.getCouponStat(memberId);
        if (result.isSuccess()){
            EsMemberCouponStatVO vo = new EsMemberCouponStatVO();
            BeanUtil.copyProperties(result.getData(),vo);
            return ApiResponse.success(vo);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "设置优惠券", notes = "使用优惠券的时候分为三种情况：前2种情况couponId 不为0,不为空。第3种情况couponId为0," +
            "1、使用优惠券:在刚进入订单结算页，为使用任何优惠券之前。" +
            "2、切换优惠券:在1、情况之后，当用户切换优惠券的时候。" +
            "3、取消已使用的优惠券:用户不想使用优惠券的时候。",response = ReturnCouponMsgVO.class)
    @PostMapping(value = "/setCoupon")
    public ApiResponse setCoupon(@RequestBody @Valid EsAppletSetCouponForm form) {
        ReturnCouponMsgVO returnCouponMsgVO = this.cartManager.userCoupon(form.getCouponId(), form.getShopId(),form.getSkey());
        return ApiResponse.success(returnCouponMsgVO);
    }
}
