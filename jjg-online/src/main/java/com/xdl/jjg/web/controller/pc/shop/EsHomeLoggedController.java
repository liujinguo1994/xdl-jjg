package com.xdl.jjg.web.controller.pc.shop;


import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.vo.EsGoodsVO;
import com.jjg.trade.model.domain.EsCouponDO;
import com.jjg.trade.model.vo.EsCouponVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsCouponService;
import com.xdl.jjg.web.service.feign.member.MemberCollectionGoodsService;
import com.xdl.jjg.web.service.feign.member.MemberCouponService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "/zhuox/homeLogged",tags = "商城首页登录后 商品接口管理")
@RestController
@RequestMapping("/zhuox/homeLogged")
public class EsHomeLoggedController {

    @Autowired
    private IEsCouponService iEsCouponService;

    @Autowired
    private MemberCollectionGoodsService esMemberCollectionGoodsService;

    @Autowired
    private GoodsService esGoodsService;

    @Autowired
    private MemberCouponService iesMemberCouponService;


    @ApiOperation(value = "商城首页 登录的情况 猜你喜欢列表",response = EsGoodsVO.class)
    @GetMapping(value = "/getGuessYouLikeList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query",defaultValue = "10")})
    @ResponseBody
    public ApiResponse getGuessYouLikeList(@ApiIgnore @NotEmpty(message = "页码不能为空") Integer pageNum,
                                       @ApiIgnore @NotEmpty(message = "每页数量不能为空") Integer pageSize) {
        // 登录的情况
        if(ShiroKit.getUser() != null){
            Long memberId  = ShiroKit.getUser().getId();
            // 调用会员收藏表接口 获取goodsId list
            DubboPageResult listByMemberId = esMemberCollectionGoodsService.getMemberCollectionGoodListByMemberId(memberId);
            List list1 = listByMemberId.getData().getList();
            Long[] goodsId = (Long[]) list1.toArray(new Long[list1.size()]);
            DubboPageResult<EsGoodsDO> guessYouLike = esGoodsService.getGuessYouLike(goodsId, pageNum, pageSize);
            if (guessYouLike.isSuccess()) {
                List<EsGoodsVO> esGoodsVOS = BeanUtil.copyList(guessYouLike.getData().getList(), EsGoodsVO.class);
                return ApiResponse.success(esGoodsVOS);
            }else{
                return ApiResponse.fail(ApiStatus.wrapperException(guessYouLike));
            }

        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "商城首页 登录的情况 会员优惠券列表",response = EsGoodsVO.class)
    @GetMapping(value = "/getEsCouponList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query",defaultValue = "10")})
    @ResponseBody
    public ApiResponse getEsCouponList(@ApiIgnore @NotEmpty(message = "页码不能为空") Integer pageNum,
                                           @ApiIgnore @NotEmpty(message = "每页数量不能为空") Integer pageSize) {
        // 登录的情况
        if(ShiroKit.getUser() == null){
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId  = ShiroKit.getUser().getId();
        // 调用会员优惠券列表接口 获取coupon list
        DubboPageResult<EsCouponDO> esCouponListAtHome = iEsCouponService.getEsCouponListAtHome(pageNum, pageSize);
        List<EsCouponDO> couponDOList = esCouponListAtHome.getData().getList();

        List<EsCouponVO> esCouponVOS = BeanUtil.copyList(couponDOList, EsCouponVO.class);

        List<Long> couponDOIds = esCouponVOS.stream().map(EsCouponVO::getId).collect(Collectors.toList());

        DubboResult result = iesMemberCouponService.getMemberWhetherCouponIds(memberId, couponDOIds);
        List<Long> list = (List<Long>)result.getData();
        if(null != list){
            esCouponVOS = esCouponVOS.stream().map(e -> {
                if(list.contains(e.getId())){
                    e.setIsReceive(1);
                    return e;
                }
                e.setIsReceive(0);
                return e;
            }).collect(Collectors.toList());
        }
        return ApiResponse.success(esCouponVOS);
    }

}
