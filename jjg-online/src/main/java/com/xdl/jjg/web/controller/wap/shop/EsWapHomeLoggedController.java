package com.xdl.jjg.web.controller.wap.shop;

import com.shopx.common.model.result.*;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.model.domain.EsGoodsDO;
import com.shopx.goods.api.model.domain.vo.EsGoodsVO;
import com.shopx.goods.api.model.domain.vo.EsPromotionGoodsVO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.member.api.service.IEsMemberCollectionGoodsService;
import com.shopx.member.api.service.IEsMemberCouponService;
import com.shopx.member.api.service.IEsSearchKeyWordService;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.EsPromotionGoodsDO;
import com.shopx.trade.api.model.domain.vo.EsCouponVO;
import com.shopx.trade.api.model.domain.vo.WapSeeGoodsVO;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.api.service.IEsPromotionGoodsService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.query.EsHomeForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(value = "/wap/homeLogged",tags = "移动端-商城首页登录后 商品接口管理")
@RestController
@RequestMapping("/wap/homeLogged")
public class EsWapHomeLoggedController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCollectionGoodsService esMemberCollectionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService esGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iesMemberCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSearchKeyWordService iEsSearchKeyWordService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsPromotionGoodsService iEsPromotionGoodsService;


    @ApiOperation(value = "商城首页 登录的情况 猜你喜欢列表",response = EsGoodsVO.class)
    @GetMapping(value = "/getGuessYouLikeList")
    @ResponseBody
    public ApiResponse getGuessYouLikeList(EsHomeForm form) {
        // 登录的情况
        if(ShiroKit.getUser() != null){
            Long memberId  = ShiroKit.getUser().getId();
            // 调用会员收藏表接口 获取goodsId list
            DubboPageResult listByMemberId = esMemberCollectionGoodsService.getMemberCollectionGoodListByMemberId(memberId);
            List list1 = listByMemberId.getData().getList();
            Long[] goodsId = (Long[]) list1.toArray(new Long[list1.size()]);
            DubboPageResult<EsGoodsDO> guessYouLike = esGoodsService.getGuessYouLike(goodsId, form.getPageNum(), form.getPageSize());
            if (guessYouLike.isSuccess()) {
                List<EsGoodsVO> esGoodsVOS = BeanUtil.copyList(guessYouLike.getData().getList(), EsGoodsVO.class);
                if(CollectionUtils.isNotEmpty(esGoodsVOS)){
                    // list 过滤出goodsID
                    List<Long> goodsIdList = esGoodsVOS.stream().map(EsGoodsVO::getId).collect(Collectors.toList());
                    DubboPageResult<EsPromotionGoodsDO> promotionByGoodsId = iEsPromotionGoodsService.getPromotionByGoodsId(goodsIdList);

                    if (promotionByGoodsId.isSuccess()){
                        List<EsPromotionGoodsDO> promotionGoodsDOS = promotionByGoodsId.getData().getList();
                        Map<Long, List<EsPromotionGoodsDO>> listMap = promotionGoodsDOS.stream().collect(Collectors.groupingBy(EsPromotionGoodsDO::getGoodsId));
                        // 遍历商品集合
                        esGoodsVOS = esGoodsVOS.stream().map(esGoodsVO -> {
                            listMap.forEach((k, v) ->{
                                if(k.equals(esGoodsVO.getId())){
                                    v.forEach(o -> o.setTitle(PromotionTypeEnum.match(o.getPromotionType()).getPromotionName()));
                                    esGoodsVO.setEsPromotionGoodsList(BeanUtil.copyList(v, EsPromotionGoodsVO.class));
                                }}
                            );
                            return esGoodsVO;
                        }).collect(Collectors.toList());
                    }
                }
                return ApiPageResponse.pageSuccess(guessYouLike.getData().getTotal(),esGoodsVOS);
            }else{
                return ApiPageResponse.fail(ApiStatus.wrapperException(guessYouLike));
            }

        }
        return ApiPageResponse.success();
    }

    @ApiOperation(value = "商城首页 会员优惠券列表(登录)",response = EsCouponVO.class)
    @GetMapping(value = "/getEsCouponList")
    @ResponseBody
    public ApiResponse getEsCouponList(EsHomeForm form) {
        Long memberId  = ShiroKit.getUser().getId();
        // 调用会员优惠券列表接口 获取coupon list
        DubboPageResult<EsCouponDO> esCouponListAtHome = iEsCouponService.getEsCouponListAtHome(form.getPageNum(), form.getPageSize());
        List<EsCouponDO> couponDOList = esCouponListAtHome.getData().getList();

        List<EsCouponVO> esCouponVOS = BeanUtil.copyList(couponDOList, EsCouponVO.class);

        List<Long> couponDOIds = esCouponVOS.stream().map(EsCouponVO::getId).collect(Collectors.toList());

        DubboResult result = iesMemberCouponService.getMemberWhetherCouponIds(memberId, couponDOIds);
        List<Long> list = (List<Long>)result.getData();
        if(null != list){
            esCouponVOS = esCouponVOS.stream().map(e -> {
                if(list.contains(e.getId())){
                    e.setIsReceive(1);
                    e.setIsReceived(1);
                    return e;
                }
                e.setIsReceive(0);
                e.setIsReceived(2);
                return e;
            }).collect(Collectors.toList());
        }
        return ApiPageResponse.pageSuccess(esCouponListAtHome.getData().getTotal(),esCouponVOS);
    }


    @ApiOperation(value = "支付页面猜你喜欢",response = WapSeeGoodsVO.class)
    @GetMapping(value = "/afterPay")
    @ResponseBody
    public ApiResponse getRecommendedForYouList() {
        // 调用搜索历史接口 获取结算页面 获取可使用的优惠券列表word list
        DubboPageResult searchKeyWord = iEsSearchKeyWordService.getSearchKeyWord(ShiroKit.getUser().getId());
        List list1 = searchKeyWord.getData().getList();
        String[] goodsName = (String[]) list1.toArray(new String[list1.size()]);
        DubboPageResult<EsGoodsDO> pageResult = esGoodsService.getRecommendForYouGoods(goodsName);
        if (pageResult.isSuccess()) {
            List<WapSeeGoodsVO> esGoodsVOS = BeanUtil.copyList(pageResult.getData().getList(), WapSeeGoodsVO.class);
            return ApiResponse.success(esGoodsVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(pageResult));
        }


    }



}
