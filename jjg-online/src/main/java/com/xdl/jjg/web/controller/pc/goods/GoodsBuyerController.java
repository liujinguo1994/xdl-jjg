package com.xdl.jjg.web.controller.pc.goods;

import com.jjg.member.model.domain.EsDiscountDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.shop.model.co.*;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.domain.EsGoodsIndexDO;
import com.jjg.shop.model.dto.EsGoodsIndexDTO;
import com.jjg.shop.model.vo.EsCategoryVO;
import com.jjg.shop.model.vo.EsGoodsVO;
import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.form.query.GoodsQueryForm;
import com.jjg.trade.model.vo.EsFreightTemplateDetailVO;
import com.jjg.trade.model.vo.SeckillGoodsVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.ErrorDate;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.manager.SeckillManager;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: GoodsBuyerController
 * @Description: 商品（买家）前端控制器
 * @Author: libw  981087977@qq.com
 * @Date: 6/10/2019 13:27
 * @Version: 1.0
 */
@Api(value = "/goods",tags = "商品（买家）接口")
@RestController
@RequestMapping("/goods")
public class GoodsBuyerController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService iEsGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSearchKeyWordService iEsSearchKeyWordService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService goodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsIndexService goodsIndexService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsSkuService goodsSkuService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCategoryService categoryService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsPromotionGoodsService iEsPromotionGoodsService;

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
    private IEsSeckillService iEsSeckillService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService iEsMemberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFreightTemplateDetailService freightTemplateDetailService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShipCompanyDetailsService shipCompanyDetailsService;

    @Autowired
    private SeckillManager seckillManager;

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "查询商品列表", notes = "根据查询条件查询商品", response = EsGoodsVO.class)
    public ApiResponse getGoodsList(@Valid GoodsQueryForm goodsQueryForm) {
        EsGoodsIndexDTO goodsIndexDTO = new EsGoodsIndexDTO();
        BeanUtil.copyProperties(goodsQueryForm, goodsIndexDTO);
        // 品牌转换为List
        goodsIndexDTO.setCategoryId(goodsQueryForm.getCat());
        goodsIndexDTO.setBrandList(goodsQueryForm.getBrand());
        if ("null".equals(goodsQueryForm.getCat())){
            String [] arr = {goodsQueryForm.getCat().toString()};
            goodsIndexDTO.setCategoryIdList(Arrays.asList(arr));
        }
        goodsIndexDTO.setGoodsList(goodsQueryForm.getGoodsList());
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
    @ApiOperation(value = "查询商品详情", notes = "根据查询条件查询商品", response = EsGoodsCO.class)
    @ApiImplicitParam(name = "goodsId" , value = "商品列表查询Form表单", paramType = "path")
    public ApiResponse getGoodsDetail(@PathVariable Long goodsId) {
        DubboResult result = this.goodsService.getEsBuyerGoods(goodsId);
        if (result.isSuccess()) {
            EsGoodsCO esGoodsCO = (EsGoodsCO) result.getData();
            if(esGoodsCO == null || esGoodsCO.getIsDel() == 1|| esGoodsCO.getIsAuth() != 1 || esGoodsCO.getMarketEnable() == 2 || esGoodsCO.getIsGifts() == 1){
                esGoodsCO.setIsMarketEnable(2);
            }else {
                List<EsPromotionGoodsVO> promotionGoodsVOList = new ArrayList<>();
                // 查询当前时刻满减满赠的活动数据

                DubboResult<EsFullDiscountDO> fullDiscountByTime = iEsFullDiscountService.getFullDiscountByTime(esGoodsCO.getShopId());
                if (fullDiscountByTime.isSuccess() && fullDiscountByTime.getData().getId() != null && fullDiscountByTime.getData().getRangeType() == 1) {
                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                    EsFullDiscountCO fullDiscountCO = new EsFullDiscountCO();
                    EsCouponCO esCouponCO = new EsCouponCO();
                    EsFullDiscountGiftCO esFullDiscountGiftCO = new EsFullDiscountGiftCO();
                    EsFullDiscountDO fullDiscountDO = fullDiscountByTime.getData();
                    // 验证活动赠优惠券
                    if (fullDiscountDO.getIsSendBonus() != null && fullDiscountDO.getIsSendBonus() == 1) {
                        DubboResult<EsSellerCouponDO> sellerCoupon = iEsCouponService.getSellerCoupon(fullDiscountDO.getBonusId());
                        if (sellerCoupon.isSuccess()) {
                            BeanUtil.copyProperties(sellerCoupon.getData(), esCouponCO);
                        }
                    }
                    //验证活动赠商品
                    if (fullDiscountDO.getIsSendGift() != null && fullDiscountDO.getIsSendGift() == 1) {
                        DubboResult<EsFullDiscountGiftDO> fullDiscountGift = iEsFullDiscountGiftService.getFullDiscountGift(fullDiscountDO.getGiftId());
                        if (fullDiscountGift.isSuccess()) {
                            BeanUtil.copyProperties(fullDiscountGift.getData(), esFullDiscountGiftCO);
                        }
                    }
                    BeanUtil.copyProperties(fullDiscountDO, fullDiscountCO);
                    fullDiscountCO.setEsCouponCO(esCouponCO);
                    fullDiscountCO.setEsFullDiscountGiftCO(esFullDiscountGiftCO);
                    esPromotionGoodsVO.setEsFullDiscountCO(fullDiscountCO);
                    esPromotionGoodsVO.setPromotionType("FULL_DISCOUNT");
                    promotionGoodsVOList.add(esPromotionGoodsVO);
                } else {

                    DubboPageResult<EsPromotionGoodsDO> promotionByGoodsId = iEsPromotionGoodsService.getPromotionByGoodsId(Arrays.asList(goodsId));
                    if (promotionByGoodsId.isSuccess()) {
                        List<EsPromotionGoodsVO> promotionGoodsVO = BeanUtil.copyList(promotionByGoodsId.getData().getList(), EsPromotionGoodsVO.class);
                        if (CollectionUtils.isNotEmpty(promotionGoodsVO)) {
                            // 封装活动信息
                            promotionGoodsVO.stream().forEach(esPromotionGoods -> {
                                String promotionType = esPromotionGoods.getPromotionType();
                                if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(promotionType)) {
                                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                                    // 满减满赠活动
                                    DubboResult<EsFullDiscountDO> fullDiscountForCache = iEsFullDiscountService.getFullDiscountForCache(esPromotionGoods.getActivityId());
                                    if (fullDiscountForCache.isSuccess() && fullDiscountForCache.getData() != null) {
                                        EsFullDiscountCO fullDiscountCO = new EsFullDiscountCO();
                                        EsCouponCO esCouponCO = new EsCouponCO();
                                        EsFullDiscountGiftCO esFullDiscountGiftCO = new EsFullDiscountGiftCO();
                                        EsFullDiscountDO fullDiscountDO = fullDiscountForCache.getData();
                                        // 验证活动赠优惠券
                                        if (fullDiscountDO.getIsSendBonus() != null && fullDiscountDO.getIsSendBonus() == 1) {
                                            DubboResult<EsSellerCouponDO> sellerCoupon = iEsCouponService.getSellerCoupon(fullDiscountDO.getBonusId());
                                            if (sellerCoupon.isSuccess()) {
                                                BeanUtil.copyProperties(sellerCoupon.getData(), esCouponCO);
                                            }
                                        }
                                        //验证活动赠商品
                                        if (fullDiscountDO.getIsSendGift() != null && fullDiscountDO.getIsSendGift() == 1) {
                                            DubboResult<EsFullDiscountGiftDO> fullDiscountGift = iEsFullDiscountGiftService.getFullDiscountGift(fullDiscountDO.getGiftId());
                                            if (fullDiscountGift.isSuccess()) {
                                                BeanUtil.copyProperties(fullDiscountGift.getData(), esFullDiscountGiftCO);
                                            }
                                        }
                                        BeanUtil.copyProperties(fullDiscountDO, fullDiscountCO);
                                        fullDiscountCO.setEsCouponCO(esCouponCO);
                                        fullDiscountCO.setEsFullDiscountGiftCO(esFullDiscountGiftCO);
                                        esPromotionGoodsVO.setEsFullDiscountCO(fullDiscountCO);
                                        esPromotionGoodsVO.setPromotionType("FULL_DISCOUNT");
                                        promotionGoodsVOList.add(esPromotionGoodsVO);
                                    }
                                }
                            });
                        }
                    }
                }
                // 查询单品立减有效活动
                DubboResult<EsMinusDO> minusByTime = iEsMinusService.getMinusByTime(esGoodsCO.getShopId());
                if ((minusByTime.isSuccess() && minusByTime.getData().getId() != null) && minusByTime.getData().getRangeType() == 1) {
                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                    EsMinusCO esMinusCO = new EsMinusCO();
                    BeanUtil.copyProperties(minusByTime.getData(), esMinusCO);
                    esPromotionGoodsVO.setEsMinusCO(esMinusCO);
                    esPromotionGoodsVO.setPromotionType("MINUS");
                    promotionGoodsVOList.add(esPromotionGoodsVO);

                } else {

                    DubboPageResult<EsPromotionGoodsDO> promotionByGoodsId = iEsPromotionGoodsService.getPromotionByGoodsId(Arrays.asList(goodsId));
                    if (promotionByGoodsId.isSuccess()) {
                        List<EsPromotionGoodsVO> promotionGoodsVO = BeanUtil.copyList(promotionByGoodsId.getData().getList(), EsPromotionGoodsVO.class);
                        if (CollectionUtils.isNotEmpty(promotionGoodsVO)) {
                            // 封装活动信息
                            promotionGoodsVO.stream().forEach(esPromotionGoods -> {
                                String promotionType = esPromotionGoods.getPromotionType();
                                if (PromotionTypeEnum.MINUS.name().equals(promotionType)) {
                                    // 获取单品立减活动
                                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                                    DubboResult<EsMinusDO> minus = iEsMinusService.getMinusForCache(esPromotionGoods.getActivityId());
                                    if ((minus.isSuccess() && minus.getData() != null) && minus.getData().getRangeType() == 2) {
                                        EsMinusCO esMinusCO = new EsMinusCO();
                                        BeanUtil.copyProperties(minus.getData(), esMinusCO);
                                        esPromotionGoodsVO.setEsMinusCO(esMinusCO);
                                        esPromotionGoodsVO.setPromotionType("MINUS");
                                        promotionGoodsVOList.add(esPromotionGoodsVO);
                                    }
                                }
                            });
                        }
                    }
                }
                // 查询打折活动
                DubboResult<EsGoodsDiscountDO> goodsDiscountByTime = iEsGoodsDiscountService.getGoodsDiscountByTime(esGoodsCO.getShopId());
                if (goodsDiscountByTime.isSuccess() && goodsDiscountByTime.getData().getId() != null && goodsDiscountByTime.getData().getRangeType() == 1) {
                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                    EsGoodsDiscountCO goodsDiscountCO = new EsGoodsDiscountCO();
                    BeanUtil.copyProperties(goodsDiscountByTime.getData(), goodsDiscountCO);
                    esPromotionGoodsVO.setEsGoodsDiscountCO(goodsDiscountCO);
                    esPromotionGoodsVO.setPromotionType("GOODS_DISCOUNT");
                    promotionGoodsVOList.add(esPromotionGoodsVO);
                } else {

                    DubboPageResult<EsPromotionGoodsDO> promotionByGoodsId = iEsPromotionGoodsService.getPromotionByGoodsId(Arrays.asList(goodsId));
                    if (promotionByGoodsId.isSuccess()) {
                        List<EsPromotionGoodsVO> promotionGoodsVO = BeanUtil.copyList(promotionByGoodsId.getData().getList(), EsPromotionGoodsVO.class);
                        if (CollectionUtils.isNotEmpty(promotionGoodsVO)) {
                            // 封装活动信息
                            promotionGoodsVO.stream().forEach(esPromotionGoods -> {
                                String promotionType = esPromotionGoods.getPromotionType();
                                if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(promotionType)) {
                                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                                    // 打折活动
                                    DubboResult goodsDiscountForCache = iEsGoodsDiscountService.getGoodsDiscountForCache(esPromotionGoods.getActivityId());
                                    if (goodsDiscountForCache.isSuccess() && goodsDiscountForCache.getData() != null) {
                                        EsGoodsDiscountCO goodsDiscountCO = new EsGoodsDiscountCO();
                                        BeanUtil.copyProperties(goodsDiscountForCache.getData(), goodsDiscountCO);
                                        esPromotionGoodsVO.setEsGoodsDiscountCO(goodsDiscountCO);
                                        esPromotionGoodsVO.setPromotionType("GOODS_DISCOUNT");
                                        promotionGoodsVOList.add(esPromotionGoodsVO);
                                    }
                                }
                            });
                        }
                    }

                }
                // 第二件半价活动
                DubboResult<EsHalfPriceDO> halfPriceByTime = iEsHalfPriceService.getHalfPriceByTime(esGoodsCO.getShopId());
                if (halfPriceByTime.isSuccess() && halfPriceByTime.getData().getId() != null && halfPriceByTime.getData().getRangeType() == 1) {
                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                    EsHalfPriceCO halfPriceCO = new EsHalfPriceCO();
                    BeanUtil.copyProperties(halfPriceByTime.getData(), halfPriceCO);
                    esPromotionGoodsVO.setEsHalfPriceCO(halfPriceCO);
                    esPromotionGoodsVO.setPromotionType("HALF_PRICE");
                    promotionGoodsVOList.add(esPromotionGoodsVO);
                } else {

                    DubboPageResult<EsPromotionGoodsDO> promotionByGoodsId = iEsPromotionGoodsService.getPromotionByGoodsId(Arrays.asList(goodsId));
                    if (promotionByGoodsId.isSuccess()) {
                        List<EsPromotionGoodsVO> promotionGoodsVO = BeanUtil.copyList(promotionByGoodsId.getData().getList(), EsPromotionGoodsVO.class);
                        if (CollectionUtils.isNotEmpty(promotionGoodsVO)) {
                            // 封装活动信息
                            promotionGoodsVO.stream().forEach(esPromotionGoods -> {
                                String promotionType = esPromotionGoods.getPromotionType();
                                if (PromotionTypeEnum.HALF_PRICE.name().equals(promotionType)) {
                                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                                    // 第二件板件活动
                                    DubboResult halfPriceForCache = iEsHalfPriceService.getHalfPriceForCache(esPromotionGoods.getActivityId());
                                    if (halfPriceForCache.isSuccess() && halfPriceForCache.getData() != null) {
                                        EsHalfPriceCO halfPriceCO = new EsHalfPriceCO();
                                        BeanUtil.copyProperties(halfPriceForCache.getData(), halfPriceCO);
                                        esPromotionGoodsVO.setEsHalfPriceCO(halfPriceCO);
                                        esPromotionGoodsVO.setPromotionType("HALF_PRICE");
                                        promotionGoodsVOList.add(esPromotionGoodsVO);
                                    }
                                }
                            });
                        }
                    }

                }
                // 秒杀商品
                SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(goodsId);
                if (seckillGoodsVO != null) {
                    EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                    EsSeckillGoodsCO esSeckillGoodsCO = new EsSeckillGoodsCO();
                    BeanUtil.copyProperties(seckillGoodsVO, esSeckillGoodsCO);
                    esPromotionGoodsVO.setSeckillGoodsCO(esSeckillGoodsCO);
                    esPromotionGoodsVO.setPromotionType("SECKILL");
                    promotionGoodsVOList.add(esPromotionGoodsVO);
                }
                EsDiscountCO esDiscountCO = new EsDiscountCO();
                // 公司签约用户
                // 获取用户信息
                if (ShiroKit.getUser() != null) {
                    Long memberId = ShiroKit.getUser().getId();
                    DubboResult<EsMemberDO> member = iEsMemberService.getMember(memberId);
                    if (member.getData() != null) {
                        // 通过商品分类和公司标识符获取折扣
                        DubboResult<EsDiscountDO> discount1 = iEsDiscountService.getDiscountByCompanyCodeAndCategoryId(member.getData().getCompanyCode(),
                                esGoodsCO.getCategoryId());
                        if (discount1.isSuccess() && discount1.getData().getId() != null) {
                            EsPromotionGoodsVO esPromotionGoodsVO = new EsPromotionGoodsVO();
                            BeanUtil.copyProperties(discount1.getData(), esDiscountCO);
                            esPromotionGoodsVO.setEsDiscountCO(esDiscountCO);
                            esPromotionGoodsVO.setPromotionType("COMPANY_DISCOUNT");
                            promotionGoodsVOList.add(esPromotionGoodsVO);
                        }
                    }

                }
                esGoodsCO.setPromotionList(promotionGoodsVOList);
                esGoodsCO.setIsMarketEnable(1);
            }
            return ApiResponse.success(esGoodsCO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @GetMapping("/{goodsId}/skuList")
    @ResponseBody
    @ApiOperation(value = "查询商品详情", notes = "根据查询条件查询商品", response = EsGoodsCO.class)
    @ApiImplicitParam(name = "goodsId" , value = "商品列表查询Form表单", paramType = "path")
    public ApiResponse getGoodsSkuList(@PathVariable Long goodsId) {
        DubboResult result = this.goodsService.getEsGoods(goodsId);
        if (result.isSuccess()) {
            EsGoodsCO goods = (EsGoodsCO) result.getData();
            return ApiResponse.success(goods.getSkuList());
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @GetMapping("/category/{parentId}/children")
    @ResponseBody
    @ApiOperation(value = "查询商品分类父子关系", notes = "根据查询条件查询商品", response = EsCategoryVO.class)
    @ApiImplicitParam(name = "parentId", value = "父ID", paramType = "path")
    public ApiResponse getCategory(@PathVariable Long parentId) {
        DubboPageResult result = this.categoryService.getCategoryChildren(parentId);
        if (result.isSuccess()) {
            List<EsCategoryVO> categoryList = BeanUtil.copyList(result.getData().getList(), EsCategoryVO.class);
            return ApiPageResponse.success(categoryList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping("/area/{areaId}/{goodsId}")
    @ResponseBody
    @ApiOperation(value = "查询是否在配送范围", notes = "根据地区ID查询配送范围")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "areaId", value = "地区ID", paramType = "path",dataType = "long"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", paramType = "path",dataType = "long")
    })
    public ApiResponse getArea(@PathVariable Long areaId, @PathVariable Long goodsId) {
        ErrorDate errorDate = new ErrorDate();
        try {
            // 通过商品ID 查询该商品绑定的运费模板ID
            DubboResult<EsGoodsCO> esBuyerGoods = goodsService.getEsBuyerGoods(goodsId);
            if (esBuyerGoods.isSuccess() && esBuyerGoods.getData() != null && esBuyerGoods.getData().getGoodsTransfeeCharge() == 1) {
                Long templateId = esBuyerGoods.getData().getTemplateId();
                //运费模版映射
                Map<Long, EsFreightTemplateDetailVO> shipMap = new HashMap<>(16);
                // 先验证该商品运费模板是否存在
                DubboPageResult<EsFreightTemplateDetailDO> freightTemplateDetailListByModeId = this.freightTemplateDetailService.getFreightTemplateDetailListByModeId(templateId);

                if (freightTemplateDetailListByModeId.isSuccess()){
                    List<EsFreightTemplateDetailDO> shipDetail = freightTemplateDetailListByModeId.getData().getList();

                    if (shipDetail.isEmpty()) {
                        throw new ArgumentException(TradeErrorCode.GOODS_NOT_IN_AREA.getErrorCode(),TradeErrorCode.GOODS_NOT_IN_AREA.getErrorMsg());
                    }else {
                        for (EsFreightTemplateDetailDO child : shipDetail) {
                            if (!StringUtils.isEmpty(child.getArea())) {
                                // 校验地区
                                if (child.getAreaId().contains(areaId)) {
                                    EsFreightTemplateDetailVO freightTemplateDetailVO = new EsFreightTemplateDetailVO();
                                    BeanUtil.copyProperties(child, freightTemplateDetailVO);
                                    shipMap.put(goodsId, freightTemplateDetailVO);
                                }
                            }
                        }

                        // 如果没有匹配 则当前地区无货
                        if (!shipMap.containsKey(goodsId)) {
                            throw new ArgumentException(TradeErrorCode.GOODS_NOT_IN_AREA.getErrorCode(),TradeErrorCode.GOODS_NOT_IN_AREA.getErrorMsg());
                        }
                    }
                }

            }
            return ApiResponse.success();
        } catch (ArgumentException e) {
            errorDate.setErrorCode(e.getExceptionCode());
            errorDate.setErrotMessage(e.getMessage());
            return ApiResponse.success(errorDate);
        }
    }

    @ApiOperation(value = "看了又看,根据商品分类查询",response = EsGoodsVO.class)
    @GetMapping(value = "/getLookAndLook/{shopId}/{categoryId}/{goodsId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", paramType = "path",dataType = "Long"),
            @ApiImplicitParam(name = "categoryId", value = "分类ID", paramType = "path",dataType = "Long"),
            @ApiImplicitParam(name = "goodsId", value = "分类ID", paramType = "path",dataType = "Long")
    })
    @ResponseBody
    public ApiResponse getRecommendedForYouList(@PathVariable("shopId") Long shopId, @PathVariable("categoryId") Long categoryId, @PathVariable("goodsId") Long goodsId,
                                                Integer pageSize, Integer pageNum) {

        // 根据商品分类查询商品
        DubboPageResult<EsGoodsDO> pageResult = goodsService.buyerGetGoodsByCategoryId(categoryId,shopId,goodsId,pageNum,pageSize);

        if (pageResult.isSuccess()) {
            List<EsGoodsVO> esGoodsVOS = BeanUtil.copyList(pageResult.getData().getList(), EsGoodsVO.class);
            return ApiPageResponse.pageSuccess(pageResult.getData().getTotal(), esGoodsVOS);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(pageResult));
        }

    }

    @GetMapping(value = "/{goodsId}/visit")
    @ResponseBody
    @ApiOperation(value = "记录浏览商品次数", notes = "记录浏览器商品次数")
    @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "path", dataType = "int")
    public ApiResponse visitGoods(@PathVariable("goodsId") Integer goodsId) {
        // TODO 需要商品模块提供增加商品浏览次数功能
//        DubboResult result = this.goodsService.(goodsId);
//        if (result.isSuccess()) {
//            return ApiResponse.success();
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
        return null;
    }


}