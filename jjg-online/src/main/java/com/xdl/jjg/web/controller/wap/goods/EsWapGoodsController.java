package com.xdl.jjg.web.controller.wap.goods;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.goods.api.model.domain.EsBuyerGoodsParamsDO;
import com.shopx.goods.api.model.domain.EsGoodsDO;
import com.shopx.goods.api.model.domain.EsGoodsIndexDO;
import com.shopx.goods.api.model.domain.cache.*;
import com.shopx.goods.api.model.domain.dto.EsGoodsIndexDTO;
import com.shopx.goods.api.model.domain.vo.EsBuyerGoodsParamsVO;
import com.shopx.goods.api.model.domain.vo.EsPromotionGoodsVO;
import com.shopx.goods.api.model.domain.vo.EsSpecValuesVO;
import com.shopx.goods.api.service.IEsGoodsIndexService;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsGoodsSkuService;
import com.shopx.member.api.model.domain.*;
import com.shopx.member.api.model.domain.vo.EsMemberGoodsVO;
import com.shopx.member.api.model.domain.vo.EsShopDetailVO;
import com.shopx.member.api.model.domain.vo.EsShopVO;
import com.shopx.member.api.service.IEsDiscountService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.member.api.service.IEsMyFootprintService;
import com.shopx.member.api.service.IEsShopService;
import com.shopx.trade.api.model.domain.*;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.*;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.manager.SeckillManager;
import com.shopx.trade.web.request.query.EsWapGoodsQueryForm;
import com.shopx.trade.web.request.query.EsWapGoodsSeeForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 移动端-商品 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Api(value = "/wap/goods/goods",tags = "移动端-商品")
@RestController
@RequestMapping("/wap/goods/goods")
public class EsWapGoodsController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService goodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsIndexService goodsIndexService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsPromotionGoodsService iEsPromotionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShopService shopService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMinusService iEsMinusService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFullDiscountService iEsFullDiscountService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFullDiscountGiftService iEsFullDiscountGiftService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsDiscountService iEsDiscountService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsHalfPriceService iEsHalfPriceService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsDiscountService iEsGoodsDiscountService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSeckillRangeService iEsSeckillRangeService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService iEsMemberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsSkuService skuService;



    @Autowired
    private SeckillManager seckillManager;
    @Reference(version = "${dubbo.application.version}", timeout = 10000)
    private IEsMyFootprintService myFootprintService;


    @ApiOperation(value = "查询商品列表", response = EsGoodsIndexVO.class)
    @GetMapping(value = "/getGoodsList")
    @ResponseBody
    public ApiResponse getGoodsList(EsWapGoodsQueryForm goodsQueryForm) {
        EsGoodsIndexDTO goodsIndexDTO = new EsGoodsIndexDTO();
        BeanUtil.copyProperties(goodsQueryForm, goodsIndexDTO);
        // 品牌转换为List
        goodsIndexDTO.setBrandList(goodsQueryForm.getBrandList());
        goodsIndexDTO.setCategoryIdList(goodsQueryForm.getCategoryIdList());
        DubboPageResult result = this.goodsIndexService.getEsGoodsIndex(goodsIndexDTO, goodsQueryForm.getPageSize(), goodsQueryForm.getPageNum());
        if (result.isSuccess()) {
            List<EsGoodsIndexDO> goodsIndexListDO = result.getData().getList();
            List<EsGoodsIndexVO> goodsIndexListVO = BeanUtil.copyList(goodsIndexListDO,
                    EsGoodsIndexVO.class);
            List<EsGoodsIndexDO> list = result.getData().getList();
            if (CollectionUtils.isNotEmpty(list)){
                // list 过滤出goodsID
                List<Long> goodsIdList = list.stream().map(EsGoodsIndexDO::getGoodsId).collect(Collectors.toList());
                DubboPageResult<EsPromotionGoodsDO> promotionByGoodsId = iEsPromotionGoodsService.getPromotionByGoodsId(goodsIdList);

                if (promotionByGoodsId.isSuccess()){
                    List<EsPromotionGoodsDO> promotionGoodsDOS = promotionByGoodsId.getData().getList();
                    Map<Long, List<EsPromotionGoodsDO>> listMap = promotionGoodsDOS.stream().collect(Collectors.groupingBy(EsPromotionGoodsDO::getGoodsId));
                    // 遍历商品集合
                    goodsIndexListVO = goodsIndexListVO.stream().map(esGoodsIndexVO -> {
                        listMap.forEach((k, v) ->{
                            if(k.equals(esGoodsIndexVO.getGoodsId())){
                                v.forEach(o -> o.setTitle(PromotionTypeEnum.match(o.getPromotionType()).getPromotionName()));
                                esGoodsIndexVO.setEsPromotionGoodsDOList(v);
                            }}
                        );
                        return esGoodsIndexVO;
                    }).collect(Collectors.toList());
                }
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), goodsIndexListVO);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping("/{goodsId}")
    @ResponseBody
    @ApiOperation(value = "查询商品详情", response = EsWapGoodsVO.class)
    @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "long", paramType = "path")
    public ApiResponse getGoodsSkuList(@PathVariable Long goodsId) {
        DubboResult result = this.goodsService.getEsGoods(goodsId);
        if (result.isSuccess()) {
            EsGoodsCO esGoodsCO = (EsGoodsCO) result.getData();
            DubboPageResult<EsPromotionGoodsDO> promotionByGoodsId = iEsPromotionGoodsService.getPromotionByGoodsId(Arrays.asList(goodsId));
            if(promotionByGoodsId.isSuccess()){
                List<EsPromotionGoodsVO> promotionGoodsVOList = BeanUtil.copyList(promotionByGoodsId.getData().getList(),EsPromotionGoodsVO.class);
                if (CollectionUtils.isNotEmpty(promotionGoodsVOList)){
                    // 封装活动信息
                    promotionGoodsVOList.stream().forEach(esPromotionGoodsVO -> {
                        String promotionType = esPromotionGoodsVO.getPromotionType();
                        if (PromotionTypeEnum.MINUS.name().equals(promotionType)){
                            // 获取单品立减活动
                            DubboResult<EsMinusDO> minus = iEsMinusService.getMinusForCache(esPromotionGoodsVO.getActivityId());
                            if (minus.isSuccess() && minus.getData() != null){
                                EsMinusCO esMinusCO = new EsMinusCO();
                                BeanUtil.copyProperties(minus.getData(), esMinusCO);
                                esPromotionGoodsVO.setName("单品立减");
                                esPromotionGoodsVO.setEsMinusCO(esMinusCO);

                            }
                        }else if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(promotionType)){
                            // 满减满赠活动
                            DubboResult<EsFullDiscountDO> fullDiscountForCache = iEsFullDiscountService.getFullDiscountForCache(esPromotionGoodsVO.getActivityId());
                            if (fullDiscountForCache.isSuccess() && fullDiscountForCache.getData() != null){
                                EsFullDiscountCO fullDiscountCO = new EsFullDiscountCO();
                                EsCouponCO esCouponCO = new EsCouponCO();
                                EsFullDiscountGiftCO esFullDiscountGiftCO = new EsFullDiscountGiftCO();
                                EsFullDiscountDO fullDiscountDO = fullDiscountForCache.getData();
                                // 验证活动赠优惠券
                                if(fullDiscountDO.getIsSendBonus() != null && fullDiscountDO.getIsSendBonus() == 1){
                                    DubboResult<EsSellerCouponDO> sellerCoupon = iEsCouponService.getSellerCoupon(fullDiscountDO.getBonusId());
                                    if (sellerCoupon.isSuccess()){
                                        BeanUtil.copyProperties(sellerCoupon.getData(),esCouponCO);
                                    }
                                }
                                //验证活动赠商品
                                if (fullDiscountDO.getIsSendGift() != null && fullDiscountDO.getIsSendGift() == 1){
                                    DubboResult<EsFullDiscountGiftDO> fullDiscountGift = iEsFullDiscountGiftService.getFullDiscountGift(fullDiscountDO.getGiftId());
                                    if (fullDiscountGift.isSuccess()){
                                        BeanUtil.copyProperties(fullDiscountGift.getData(),esFullDiscountGiftCO);
                                    }
                                }
                                BeanUtil.copyProperties(fullDiscountDO,fullDiscountCO);
                                fullDiscountCO.setEsCouponCO(esCouponCO);
                                fullDiscountCO.setEsFullDiscountGiftCO(esFullDiscountGiftCO);
                                esPromotionGoodsVO.setName("满减满赠");
                                esPromotionGoodsVO.setEsFullDiscountCO(fullDiscountCO);
                            }
                        }else if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(promotionType)){
                            // 打折活动
                            DubboResult goodsDiscountForCache = iEsGoodsDiscountService.getGoodsDiscountForCache(esPromotionGoodsVO.getActivityId());
                            if (goodsDiscountForCache.isSuccess() && goodsDiscountForCache.getData() != null){
                                EsGoodsDiscountCO goodsDiscountCO = new EsGoodsDiscountCO();
                                BeanUtil.copyProperties(goodsDiscountForCache.getData(),goodsDiscountCO);
                                esPromotionGoodsVO.setEsGoodsDiscountCO(goodsDiscountCO);
                            }
                        }else if (PromotionTypeEnum.HALF_PRICE.name().equals(promotionType)){
                            // 第二件板件活动
                            DubboResult halfPriceForCache = iEsHalfPriceService.getHalfPriceForCache(esPromotionGoodsVO.getActivityId());
                            if (halfPriceForCache.isSuccess() && halfPriceForCache.getData() != null){
                                EsHalfPriceCO halfPriceCO = new EsHalfPriceCO();
                                BeanUtil.copyProperties(halfPriceForCache.getData(),halfPriceCO);
                                esPromotionGoodsVO.setName("第二件半价");
                                esPromotionGoodsVO.setEsHalfPriceCO(halfPriceCO);
                            }
                        }else if (PromotionTypeEnum.SECKILL.name().equals(promotionType)){
                            // 秒杀商品
                            EsSeckillGoodsCO esSeckillGoodsCO = new EsSeckillGoodsCO();
                            SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(esPromotionGoodsVO.getGoodsId());
                            if (seckillGoodsVO != null){
                                BeanUtil.copyProperties(seckillGoodsVO,esSeckillGoodsCO);
                            }
                            esPromotionGoodsVO.setName("秒杀");
                            esPromotionGoodsVO.setSeckillGoodsCO(esSeckillGoodsCO);
                        }else if (PromotionTypeEnum.COMPANY_DISCOUNT.name().equals(promotionType)){
                            EsDiscountCO esDiscountCO = new EsDiscountCO();
                            // 公司签约用户
                            // 获取用户信息
                            if (ShiroKit.getUser() != null){
                                Long memberId = ShiroKit.getUser().getId();
                                DubboResult<EsMemberDO> member = iEsMemberService.getMember(memberId);
                                if (member.getData() != null){
                                    // 通过商品分类和公司标识符获取折扣
                                    DubboResult<EsDiscountDO> discount1 = iEsDiscountService.getDiscountByCompanyCodeAndCategoryId(member.getData().getCompanyCode(),
                                            esGoodsCO.getCategoryId());
                                    if (discount1.isSuccess() && discount1 != null){
                                        BeanUtil.copyProperties(discount1.getData(),esDiscountCO);
                                        esPromotionGoodsVO.setName("公司折扣");
                                        esPromotionGoodsVO.setEsDiscountCO(esDiscountCO);
                                    }
                                }
                            }
                        }

                    });
                }
                esGoodsCO.setPromotionList(promotionGoodsVOList);
            }
            List<EsGoodsSkuCO> skuList = esGoodsCO.getSkuList();
            EsWapGoodsVO wapGoodsVO=new EsWapGoodsVO();
            BeanUtil.copyProperties(esGoodsCO,wapGoodsVO);
            List<EsBuyerGoodsParamsVO> goodsParamsVOS=new ArrayList<>();
            List<EsBuyerGoodsParamsDO> paramsList = esGoodsCO.getParamsList();
            if (CollectionUtils.isNotEmpty(paramsList)){
                paramsList.forEach(goodsParams -> {
                    EsBuyerGoodsParamsVO esBuyerGoodsParamsVO = new EsBuyerGoodsParamsVO();
                    BeanUtil.copyProperties(goodsParams, esBuyerGoodsParamsVO);
                    goodsParamsVOS.add(esBuyerGoodsParamsVO);
                });
            }
            wapGoodsVO.setParamsList(goodsParamsVOS);
            List<EsWapGoodsSkuVO> list=new ArrayList<>();
            for (EsGoodsSkuCO sku:skuList) {
                EsWapGoodsSkuVO co=new EsWapGoodsSkuVO();
                String specs = sku.getSpecs();
                List<EsSpecValuesVO> esSpecValuesDOS = JsonUtil.jsonToList(specs, EsSpecValuesVO.class);
                BeanUtil.copyProperties(sku,co);
                co.setSpecList(esSpecValuesDOS);
                list.add(co);
            }
            wapGoodsVO.setSkuList(list);
            //插入我的足迹
//            EsMyFootprintDTO myFootprintDTO = new EsMyFootprintDTO();
//            myFootprintDTO.setMemberId(ShiroKit.getUser().getId());
//            myFootprintDTO.setGoodsId(goodsId);
//            myFootprintDTO.setShopId(esGoodsCO.getShopId());
//            myFootprintDTO.setImg(esGoodsCO.getOriginal());
//            myFootprintDTO.setMoney(esGoodsCO.getMoney());
//            myFootprintService.insertMyFootprint(myFootprintDTO);

            return ApiResponse.success(wapGoodsVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询店铺详情(热销、上新商品)", notes = "查询店铺详情(热销、上新商品)", response = EsShopVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", dataType = "long", paramType = "query", example = "1"),
    })
    @GetMapping("/getBuyerShopDetail")
    @ResponseBody
    public ApiResponse getBuyerShopDetail(Long shopId) {
        DubboResult<EsShopDO> shop =  shopService.getBuyerShopDetail(shopId);
        if(shop.isSuccess()){
            EsShopDO shopDO = shop.getData();
            if(shopDO == null){
                return ApiResponse.success(shop);
            }
            EsShopVO shopVO = new EsShopVO();
            BeanUtil.copyProperties(shopDO, shopVO);
            //详细VO
            EsShopDetailDO shopDetailDO =  shopDO.getShopDetailDO();
            EsShopDetailVO shopDetailVO = new EsShopDetailVO();
            if(shopDetailDO != null){
                BeanUtil.copyProperties(shopDetailDO, shopDetailVO);
            }
            shopVO.setShopDetailVO(shopDetailVO);

            //热销VO
            List<EsMemberGoodsVO> hotList = new ArrayList<>();
            List<EsMemberGoodsDO> hotGoodsDOList = shopDO.getHotGoodList();
            if(CollectionUtils.isNotEmpty(hotGoodsDOList)){
                hotList = BeanUtil.copyList(hotGoodsDOList, EsMemberGoodsVO.class);
            }
            shopVO.setHotGoodList(hotList);

            //上新
            List<EsMemberGoodsVO> newList = new ArrayList<>();
            List<EsMemberGoodsDO> newGoodsDOList = shopDO.getNewGoodList();
            if(CollectionUtils.isNotEmpty(newGoodsDOList)){
                newList = BeanUtil.copyList(newGoodsDOList, EsMemberGoodsVO.class);
            }
            shopVO.setNewGoodList(newList);

            return ApiResponse.success(shopVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(shop));
    }


    @ApiOperation(value = "看了又看,根据商品分类查询",response = WapSeeGoodsVO.class)
    @GetMapping(value = "/getLookAndLook")
    @ResponseBody
    public ApiResponse getRecommendedForYouList(EsWapGoodsSeeForm form) {

        // 根据商品分类查询商品
        DubboPageResult<EsGoodsDO> pageResult = goodsService.buyerGetGoodsByCategoryId(form.getCategoryId().longValue(),form.getShopId().longValue(),form.getGoodsId(),form.getPageNum(),form.getPageSize());
        if (pageResult.isSuccess()) {
            List<WapSeeGoodsVO> esGoodsVOS = BeanUtil.copyList(pageResult.getData().getList(), WapSeeGoodsVO.class);
            return ApiPageResponse.pageSuccess(pageResult.getData().getTotal(), esGoodsVOS);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(pageResult));
        }

    }


}