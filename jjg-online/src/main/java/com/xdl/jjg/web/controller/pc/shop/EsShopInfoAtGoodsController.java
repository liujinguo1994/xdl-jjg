package com.xdl.jjg.web.controller.pc.shop;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.model.domain.EsGoodsDO;
import com.shopx.goods.api.model.domain.vo.EsSearchGoodsVO;
import com.shopx.goods.api.service.IEsGoodsParamsService;
import com.shopx.goods.api.service.IEsTagGoodsService;
import com.shopx.member.api.model.domain.*;
import com.shopx.member.api.model.domain.cache.EsCustomCO;
import com.shopx.member.api.model.domain.vo.EsMemberGoodsVO;
import com.shopx.member.api.model.domain.vo.EsShopDetailVO;
import com.shopx.member.api.model.domain.vo.EsShopVO;
import com.shopx.member.api.service.IEsCustomService;
import com.shopx.member.api.service.IEsShopService;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.vo.EsCouponVO;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * @author xiaoLin
 * @ClassName EsShopInfoAtGoodsController
 * @Description 商品页面中店铺相关信息
 * @create 2019/11/1 16:22
 */
@Api(value = "/zhuox/goods/shop",tags = "商城商品详情页中店铺信息以及商品信息相关接口管理")
@RestController
@RequestMapping("/zhuox/goods/shop")
public class EsShopInfoAtGoodsController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCustomService customService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShopService shopService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsTagGoodsService iEsTagGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsParamsService iEsGoodsParamsService;


    @ApiOperation(value = "查询所有分类及子分类树形结构", notes = "查询所有分类及子分类树形结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", dataType = "long", paramType = "query", example = "1"),
    })
    @GetMapping("/getCategoryList")
    @ResponseBody
    public ApiResponse getCategoryList(Long shopId) {
        DubboPageResult<EsCustomCO> result = customService.getCategoryList(shopId);
        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "查询店铺详情", notes = "查询店铺详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", dataType = "long", paramType = "query", example = "1"),
    })
    @GetMapping("/getShopDetail")
    @ResponseBody
    public ApiResponse getShopDetail(Long shopId) {
        DubboResult<EsShopAndDetailDO> shop =  shopService.getShopDetail(shopId);
        if(shop.isSuccess()){
            return ApiResponse.success(shop.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(shop));
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

    @ApiOperation(value = "商品详情页面 左侧店铺热销，新品商品查询标签商品列表", response = EsSearchGoodsVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "卖家id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "mark", value = "hot热卖 new新品 recommend推荐", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "num", value = "查询数量", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/tags/{mark}/goods")
    public ApiResponse list(@ApiIgnore @NotNull(message = "店铺不能为空") Integer shopId, Integer num, @PathVariable String mark) {

        if (num == null) {
            num = 2;
        }
        DubboPageResult<EsGoodsDO> pageResult = iEsTagGoodsService.queryTagGoods(shopId, num, mark);
        if (pageResult.isSuccess()){
            List<EsSearchGoodsVO> esSearchGoodsVOS = BeanUtil.copyList(pageResult.getData().getList(), EsSearchGoodsVO.class);
            return ApiResponse.success(esSearchGoodsVOS);
        }
        return ApiResponse.success();
    }

//    @ApiOperation(value = "商城 商品详情页 商品ID 商品分类ID 获取该商品规格参数",response = EsBuyerGoodsParamsVO.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "long", paramType = "query"),
//            @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, dataType = "long", paramType = "query")
//    })
//    @GetMapping(value = "/getEsGoodsParamsList")
//    @ResponseBody
//    public ApiResponse getEsGoodsParamsList(Long categoryId, Long goodsId) {
//        DubboPageResult<EsBuyerGoodsParamsDO> pageResult = iEsGoodsParamsService.queryGoodsParams(categoryId, goodsId);
//        if (pageResult.isSuccess()) {
//            List<EsBuyerGoodsParamsVO> esBuyerGoodsParamsVOS = BeanUtil.copyList(pageResult.getData().getList(), EsBuyerGoodsParamsVO.class);
//            return ApiResponse.success(esBuyerGoodsParamsVOS);
//        }else{
//            return ApiResponse.fail(ApiStatus.wrapperException(pageResult));
//        }
//    }


    @ApiOperation(value = "商城 商品详情页 根据shopId 获取本店铺优惠券列表",response = EsCouponVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", required = true, dataType = "long", paramType = "query")})
    @GetMapping(value = "/getEsCouponList")
    @ResponseBody
    public ApiResponse getEsCouponList(Long shopId) {
        DubboPageResult<EsCouponDO> esCouponList = iEsCouponService.getEsCouponListByShopId(shopId);
        if (esCouponList.isSuccess()) {
            List<EsCouponVO> esCouponVOS = BeanUtil.copyList(esCouponList.getData().getList(), EsCouponVO.class);
            return ApiResponse.success(esCouponVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(esCouponList));
        }
    }

//    @ApiOperation(value = "热销、上新商品列表数据", notes = "收藏店铺列表数据", response = EsQueryCollectionShopVO.class)
//    @PostMapping("/getMemberCollectionShopListNew")
//    @ResponseBody
//    public ApiResponse getMemberCollectionShopListNew(EsCollectShopQueryForm form) {
//        ShiroUser user = ShiroKit.getUser();
//        if (null == user) {
//            return ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        Long memberId = user.getId();
//        EsQueryCollectShopDTO dto = new EsQueryCollectShopDTO();
//        BeanUtil.copyProperties(form, dto);
//        dto.setMemberId(memberId);
//        DubboPageResult result = memberCollectionShopService.getMemberCollectionShopListNew(form.getPageNum(), form.getPageSize(), dto);
//        if(result.isSuccess()){
//            if(result.getData().getList().size() == 0){
//                return ApiPageResponse.success(result);
//            }
//            List<EsQueryCollectionShopDO> shopDOList = result.getData().getList();
//            List<EsQueryCollectionShopVO> shopVOList = shopDOList.stream().map(e -> {
//                EsQueryCollectionShopVO queryCollectionShopVO = new EsQueryCollectionShopVO();
//                BeanUtil.copyProperties(e, queryCollectionShopVO);
//                List<EsMemberGoodsDO> hotGoodList = e.getHotGoodList();
//                List<EsMemberGoodsDO> newGoodList = e.getNewGoodList();
//                if(CollectionUtils.isNotEmpty(hotGoodList)){
//                    List<EsMemberGoodsVO> hotGoodsVOList = BeanUtil.copyList(hotGoodList, EsMemberGoodsVO.class);
//                    queryCollectionShopVO.setHotGoodList(hotGoodsVOList);
//                }
//                if(CollectionUtils.isNotEmpty(newGoodList)){
//                    List<EsMemberGoodsVO> newGoodsVOList = BeanUtil.copyList(newGoodList, EsMemberGoodsVO.class);
//                    queryCollectionShopVO.setNewGoodList(newGoodsVOList);
//                }
//                return queryCollectionShopVO;
//            }).collect(Collectors.toList());
//            return ApiPageResponse.pageSuccess(result.getData().getTotal(), shopVOList);
//        }
//        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
//    }
}
