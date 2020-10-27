package com.xdl.jjg.web.controller.pc.member;

import com.jjg.member.model.domain.EsAdminMemberCouponDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.domain.EsTradeCouponDO;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.jjg.member.model.vo.EsMemberCouponVO;
import com.jjg.member.model.vo.EsTradeCouponVO;
import com.jjg.trade.model.domain.EsCouponDO;
import com.jjg.trade.model.vo.CartVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.manager.CartManager;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsCouponService;
import com.xdl.jjg.web.service.IEsSeckillApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员优惠券 前端控制器
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esMemberCoupon", tags = "会员优惠券")
@RestController
@RequestMapping("/esMemberCoupon")
public class EsMemberCouponController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iesMemberCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCouponService iesCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService iesMemberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iEsMemberCouponService;

    @Autowired
    private CartManager cartManager;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsSeckillApplyService esSeckillApplyService;

/*
    @ApiOperation(value = "查询会员优惠券列表", notes = "根据页码分页展示会员优惠券数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "type", value = "查询类型 0可用优惠券，1失效优惠券，2已用优惠券", required = true, dataType = "int", paramType = "query", example = "1"),
    })
    @GetMapping("/getesMemberCouponList")
    @ResponseBody
    public ApiResponse getesMemberCouponList(@ApiIgnore int pageNo, @ApiIgnore int pageSize, int type) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberCouponDTO esMemberCouponDTO = new EsMemberCouponDTO();
        esMemberCouponDTO.setMemberId(userId);
        esMemberCouponDTO.setState(type);
        DubboPageResult<EsMemberCouponDO> result = iesMemberCouponService.getMemberCouponList(esMemberCouponDTO, pageSize, pageNo);
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
            return ApiResponse.success(DubboPageResult.success(result.getData().getTotal(), esMemberCouponVOList));
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
*/

    @ApiOperation(value = "查询推荐优惠券列表", notes = "根据页码分页展示推荐优惠券数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = false, dataType = "int", paramType = "query", example = "10"),
    })
    @GetMapping("/getesMemberCouponListByTrade")
    @ResponseBody
    public ApiResponse getesMemberCouponListByTrade(Integer pageNum, Integer pageSize) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        if(pageNum == null || pageSize == null){
            pageNum = 1;
            pageSize = 10;
        }
        DubboPageResult<EsTradeCouponDO> result = iesMemberCouponService.getMemberCouponRecommendList(userId, pageSize, pageNum);
        if (result.isSuccess()) {
            List<EsTradeCouponDO> esTradeCouponDOList = result.getData().getList();
            List<EsTradeCouponVO> esTradeCouponVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esTradeCouponDOList)) {
                esTradeCouponVOList = esTradeCouponDOList.stream().map(esMemberCollectionShop -> {
                    EsTradeCouponVO esMemberCouponVO = new EsTradeCouponVO();
                    BeanUtil.copyProperties(esMemberCollectionShop, esMemberCouponVO);
                    return esMemberCouponVO;
                }).collect(Collectors.toList());
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esTradeCouponVOList);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "结算页面 重置优惠券选中状态", notes = "根据会员id 和 店铺id")
    @PostMapping("/updateMemberCouponList")
    @ResponseBody
    public ApiResponse updateMemberCouponList(){
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }

        List<CartVO> checkedItems = cartManager.getCheckedItems(null);
        List<Long> shopIdList = checkedItems.stream().map(CartVO::getShopId).distinct().collect(Collectors.toList());
        iEsMemberCouponService.updateIsCheckByMemberIdAndShopId(memberId, shopIdList);
        return ApiResponse.success();
    }

    @ApiOperation(value = "结算页面 获取可使用的优惠券列表", notes = "根据会员id 和 店铺id 获取",response = EsMemberCouponVO.class)
    @GetMapping("/getMemberCouponList")
    @ResponseBody
    public ApiResponse getMemberCouponList(){
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
            return ApiResponse.success(DubboResult.success(result.getData()));
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

    @ApiOperation(value = "买家端-优惠券分类列表", notes = "买家端-优惠券分类列表", response = EsAdminMemberCouponDO.class)
    @PostMapping("/getBuyerMemberCouponList")
    @ResponseBody
    public ApiResponse getBuyerMemberCouponList() {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult result = iesMemberCouponService.getBuyerMemberCouponList(userId);
        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "买家端-优惠券分类列表统计", notes = "买家端-优惠券分类列表统计", response = EsAdminMemberCouponDO.class)
    @PostMapping("/getBuyerMemberCouponNumList")
    @ResponseBody
    public ApiResponse getBuyerMemberCouponNumList() {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult result = iesMemberCouponService.getBuyerMemberCouponNumList(userId);
        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
}

