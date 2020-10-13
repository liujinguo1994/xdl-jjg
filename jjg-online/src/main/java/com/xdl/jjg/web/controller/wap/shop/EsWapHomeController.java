package com.xdl.jjg.web.controller.wap.shop;

import com.shopx.common.model.result.*;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.model.domain.*;
import com.shopx.goods.api.model.domain.cache.EsSelectSearchCO;
import com.shopx.goods.api.model.domain.dto.EsGoodsIndexDTO;
import com.shopx.goods.api.model.domain.vo.*;
import com.shopx.goods.api.service.*;
import com.shopx.member.api.service.IEsCommercelItemsService;
import com.shopx.member.api.service.IEsMemberCollectionGoodsService;
import com.shopx.system.api.model.domain.*;
import com.shopx.system.api.model.domain.dto.EsCustomCategoryDTO;
import com.shopx.system.api.model.domain.dto.EsGoodsRankingDTO;
import com.shopx.system.api.model.domain.dto.EsZoneDTO;
import com.shopx.system.api.model.domain.vo.*;
import com.shopx.system.api.service.*;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.EsPromotionGoodsDO;
import com.shopx.trade.api.model.domain.EsSeckillApplyDO;
import com.shopx.trade.api.model.domain.EsSeckillTimetableDO;
import com.shopx.trade.api.model.domain.dto.EsSeckillTimelineGoodsDTO;
import com.shopx.trade.api.model.domain.vo.EsCouponVO;
import com.shopx.trade.api.model.domain.vo.EsGoodsIndexVO;
import com.shopx.trade.api.model.domain.vo.EsSeckillApplyVO;
import com.shopx.trade.api.model.domain.vo.EsSeckillTimetableVO;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.api.service.IEsPromotionGoodsService;
import com.shopx.trade.api.service.IEsSeckillApplyService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.query.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(value = "/wap/home",tags = "移动端-商城首页商品接口管理")
@RestController
@RequestMapping("/wap/home")
public class EsWapHomeController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCategoryService esCategoryService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsParameterGroupService iEsParameterGroupService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCategoryService iEsCategoryService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsBrandService esBrandService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFocusPictureService iEsFocusPictureService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCollectionGoodsService esMemberCollectionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService esGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSiteNavigationService iEsSiteNavigationService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsHotKeywordService iEsHotKeywordService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodGoodsService iEsGoodGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsWordsService iEsGoodsWordsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsIndexService iEsGoodsIndexService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsPromotionGoodsService iEsPromotionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsBrandService iEsBrandService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCommercelItemsService commerceItemsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOftenGoodsService oftenGoodsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCustomCategoryService iesCustomCategoryService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsFindGoodsService findGoodsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsGoodsRankingService goodsRankingService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsAdvertisingService advertisingService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsZoneService iesZoneService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsSeckillApplyService esSeckillApplyService;



    @ApiOperation(value = "商城首页 获取父类列表",response = EsCategoryVO.class)
    @GetMapping(value = "/getCategoryParentList")
    @ResponseBody
    public ApiResponse getCategoryParentList() {
        DubboPageResult<EsCategoryDO> firstBrandList = iEsCategoryService.getFirstBrandList();
        if (firstBrandList.isSuccess()) {
            List<EsCategoryVO> esCategoryVOS = BeanUtil.copyList(firstBrandList.getData().getList(), EsCategoryVO.class);
            return ApiResponse.success(esCategoryVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(firstBrandList));
        }
    }

    @ApiOperation(value = "商城首页 热门关键词列表",response = EsHotKeywordVO.class)
    @GetMapping(value = "/getHotKeyWordList")
    @ResponseBody
    public ApiResponse getHotKeyWordList(){
        DubboPageResult<EsHotKeywordDO> list = iEsHotKeywordService.getList();
        if (list.isSuccess()){
            List<EsHotKeywordVO> esHotKeywordVOS = BeanUtil.copyList(list.getData().getList(), EsHotKeywordVO.class);
            return ApiResponse.success(esHotKeywordVOS);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(list));
        }
    }



    @ApiOperation(value = "获取轮播图列表", notes = "获取轮播图列表")
    @GetMapping("/getPictures/{clientType}")
    @ApiImplicitParam(name = "clientType", value = "客户端类型,传APP", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getFocusPictures(@PathVariable String clientType) {
        DubboPageResult<EsFocusPictureDO> result = iEsFocusPictureService.getList(clientType);
        if (result.isSuccess()) {
            List<EsFocusPictureDO> esFocusPictureDOList = result.getData().getList();
            if(CollectionUtils.isNotEmpty(esFocusPictureDOList)){
                esFocusPictureDOList.stream().map(esFocusPictureDO -> {
                    EsFocusPictureVO esFocusPictureVO = new EsFocusPictureVO();
                    BeanUtil.copyProperties(esFocusPictureDO, esFocusPictureVO);
                    return esFocusPictureVO;
                }).collect(Collectors.toList());
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), result.getData().getList());
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "商城首页 领券中心获取优惠券列表(未登录)",response = EsCouponVO.class)
    @GetMapping(value = "/getEsCouponList")
    @ResponseBody
    public ApiResponse getEsCouponList(EsHomeForm form) {
        DubboPageResult<EsCouponDO> esCouponListAtHome = iEsCouponService.getEsCouponListAtHome(form.getPageNum(), form.getPageSize());
        if (esCouponListAtHome.isSuccess()) {
            List<EsCouponVO> esCouponVOS = BeanUtil.copyList(esCouponListAtHome.getData().getList(), EsCouponVO.class);
            return ApiPageResponse.pageSuccess(esCouponListAtHome.getData().getTotal(),esCouponVOS);
        }else{
            return ApiPageResponse.fail(ApiStatus.wrapperException(esCouponListAtHome));
        }
    }

    @ApiOperation(value = "商城首页 未登录的情况 猜你喜欢列表",response = EsGoodsVO.class)
    @GetMapping(value = "/getGuessYouLikeList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query",defaultValue = "10")})
    @ResponseBody
    public ApiResponse getGuessYouLikeList(@ApiIgnore @NotEmpty(message = "页码不能为空") Integer pageNum,
                                           @ApiIgnore @NotEmpty(message = "每页数量不能为空") Integer pageSize) {

        // 未登录的情况
        Long[] goodsId = { };
        DubboPageResult<EsGoodsDO> guessYouLike = esGoodsService.getGuessYouLike(goodsId, pageNum, pageSize);
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

  /*  @ApiOperation(value = "商城首页 推荐排行列表",response = EsBuyerGoodsVO.class)
    @GetMapping(value = "/getRecommendGoodsList")
    @ResponseBody
    public ApiResponse getRecommendGoodsList() {
        DubboPageResult<EsBuyerGoodsDO> recommendGoods = esGoodsService.getRecommendGoods();
        if (recommendGoods.isSuccess()){
            List<EsBuyerGoodsVO> esBuyerGoodsVOS = BeanUtil.copyList(recommendGoods.getData().getList(), EsBuyerGoodsVO.class);
            return ApiResponse.success(esBuyerGoodsVOS);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(recommendGoods));
        }
    }*/

    @ApiOperation(value = "商城首页 导航栏列表",response = EsSiteNavigationVO.class)
    @GetMapping(value = "/getNavigationList")
    @ResponseBody
    public ApiResponse getNavigationList() {
        DubboPageResult<EsSiteNavigationDO> byClientType = iEsSiteNavigationService.getByClientType("MOBILE");
        if (byClientType.isSuccess()){
            List<EsSiteNavigationVO> esSiteNavigationVOS = BeanUtil.copyList(byClientType.getData().getList(), EsSiteNavigationVO.class);
            return ApiResponse.success(esSiteNavigationVOS);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(byClientType));
        }
    }



    @ApiOperation(value = "商城首页 查询商品分词以及对应数量" ,response = EsGoodsWordsVO.class)
    @ApiImplicitParam(name = "keyword", value = "搜索关键字", required = true, dataType = "string", paramType = "query")
    @GetMapping("/searchGoodsWords")
    public ApiResponse searchGoodsWords(String keyword){

        DubboPageResult<EsGoodsWordsDO> goodsWords = iEsGoodsWordsService.getGoodsWords(keyword);
        if (goodsWords.isSuccess()) {
            List<EsGoodsWordsVO> esGoodsWordsVOS = BeanUtil.copyList(goodsWords.getData().getList(), EsGoodsWordsVO.class);
            return ApiResponse.success(esGoodsWordsVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(goodsWords));
        }
    }



    @GetMapping("/selector")
    @ResponseBody
    @ApiOperation(value = "查询商品选择器",response = EsSelectSearchCO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "selectorQueryForm" , value = "查询Form表单")
    })
    public ApiResponse searchGoodsSelector(SelectorQueryForm selectorQueryForm){
        EsGoodsIndexDTO esGoodsIndexDTO = new EsGoodsIndexDTO();
        esGoodsIndexDTO.setKeyword(selectorQueryForm.getKeyword());
        // 已选分类
        if (CollectionUtils.isNotEmpty(selectorQueryForm.getCategoryIdList())) {
            esGoodsIndexDTO.setCategoryIdList(selectorQueryForm.getCategoryIdList());
        }
        // 已选品牌
        if (CollectionUtils.isNotEmpty(selectorQueryForm.getBrandList())) {
            esGoodsIndexDTO.setBrandList(selectorQueryForm.getBrandList());
        }
        DubboResult<EsSelectSearchCO> selector = iEsGoodsIndexService.getSelector(esGoodsIndexDTO);
        if (selector.isSuccess()) {
            EsSelectSearchCO searchCO = selector.getData();
            return ApiResponse.success(searchCO);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(selector));
        }
    }



    @ApiOperation(value = "通过搜索栏关键字查询商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "brandList", value = "品牌List", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "categoryIdList", value = "分类IdList",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码",  dataType = "int", paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量",dataType = "int", paramType = "query",defaultValue = "10")
    })
    @GetMapping("/getGoodsList")
    public ApiResponse getGoodsList(@ApiIgnore String keyword,
                                    @ApiIgnore String sort,
                                    @ApiIgnore String brandList,
                                    @ApiIgnore String categoryIdList,
                                    @ApiIgnore Integer pageNum,
                                    @ApiIgnore Integer pageSize){
        EsGoodsIndexDTO esGoodsIndexDTO = new EsGoodsIndexDTO();
        // 品牌转换为List
        if (brandList != null){
            String replace = brandList.replace("[", "").replace("]","");

            List<String> brandList1 = Arrays.asList(replace.split(","));
            esGoodsIndexDTO.setBrandList(brandList1);
        }
        // 分类转换为List
        if (categoryIdList != null){
            String replace = categoryIdList.replace("[", "").replace("]", "");

            List<String> categoryIdList1 = Arrays.asList(replace.split(","));
            esGoodsIndexDTO.setCategoryIdList(categoryIdList1);
        }

        esGoodsIndexDTO.setKeyword(keyword);
        esGoodsIndexDTO.setSort(sort);
        DubboPageResult<EsGoodsIndexDO> esGoodsIndex = iEsGoodsIndexService.getEsGoodsIndex(esGoodsIndexDTO, pageSize, pageNum);
        if (esGoodsIndex.isSuccess()) {

            List<EsGoodsIndexVO> esGoodsIndexVOS = BeanUtil.copyList(esGoodsIndex.getData().getList(), EsGoodsIndexVO.class);
            List<EsGoodsIndexDO> list = esGoodsIndex.getData().getList();
            if (CollectionUtils.isNotEmpty(list)){
                // list 过滤出goodsID
                List<Long> goodsIdList = list.stream().map(EsGoodsIndexDO::getGoodsId).collect(Collectors.toList());
                DubboPageResult<EsPromotionGoodsDO> promotionByGoodsId = iEsPromotionGoodsService.getPromotionByGoodsId(goodsIdList);

                if (promotionByGoodsId.isSuccess()){
                    List<EsPromotionGoodsDO> promotionGoodsDOS = promotionByGoodsId.getData().getList();
                    Map<Long, List<EsPromotionGoodsDO>> listMap = promotionGoodsDOS.stream().collect(Collectors.groupingBy(EsPromotionGoodsDO::getGoodsId));
                    // 遍历商品集合
                    esGoodsIndexVOS = esGoodsIndexVOS.stream().map(esGoodsIndexVO -> {
                        listMap.forEach((k, v) ->{
                            if(k.equals(esGoodsIndexVO.getGoodsId())){
                                esGoodsIndexVO.setEsPromotionGoodsDOList(v);
                            }}
                        );
                        return esGoodsIndexVO;
                    }).collect(Collectors.toList());
                }
            }
            return ApiResponse.success(esGoodsIndexVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(esGoodsIndex));
        }
    }

    @ApiOperation(value = "查询自定义分类列表",response = EsCustomCategoryVO.class)
    @GetMapping(value = "/getEsCustomCategoryList/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "2是常买清单，3发现好货", required = true, dataType = "int", paramType = "path")
    })
    @ResponseBody
    public ApiResponse getEsCustomCategoryList(@PathVariable Integer type) {
        EsCustomCategoryDTO dto = new EsCustomCategoryDTO();
        dto.setZoneId(type.longValue());
        DubboPageResult<EsCustomCategoryDO> result = iesCustomCategoryService.getCustomCategoryList(dto, 100, 1);
        if (result.isSuccess()) {
            List<EsCustomCategoryDO> data = result.getData().getList();
            List<EsCustomCategoryVO> voList = BeanUtil.copyList(data, EsCustomCategoryVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询常买清单列表",response = EsOftenGoodsVO.class)
    @GetMapping(value = "/getListByCustomCategoryId")
    @ResponseBody
    public ApiResponse getListByCustomCategoryId(@Valid EsAppOftenAndFindQueryForm form) {
        DubboPageResult<EsOftenGoodsDO> result = oftenGoodsService.getListByCustomCategoryId(form.getCustomCategoryId(), form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsOftenGoodsDO> data = result.getData().getList();
            List<EsOftenGoodsVO> voList = BeanUtil.copyList(data, EsOftenGoodsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询发现好货列表",response = EsFindGoodsVO.class)
    @GetMapping(value = "/getEsFindGoodsList")
    @ResponseBody
    public ApiResponse getEsFindGoodsList(@Valid EsAppOftenAndFindQueryForm form) {
        DubboPageResult<EsFindGoodsDO> result = findGoodsService.getListByCustomCategoryId(form.getCustomCategoryId(), form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsFindGoodsDO> doList = result.getData().getList();
            List<EsFindGoodsVO> voList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(doList)){
                doList.stream().forEach(esFindGoodsDO -> {
                    EsFindGoodsVO vo = new EsFindGoodsVO();
                    BeanUtil.copyProperties(esFindGoodsDO,vo);
                    if (CollectionUtils.isNotEmpty(esFindGoodsDO.getGalleryList())){
                        List<EsFindGoodsGalleryVO> galleryVOS = BeanUtil.copyList(esFindGoodsDO.getGalleryList(), EsFindGoodsGalleryVO.class);
                        vo.setGalleryList(galleryVOS);
                    }
                    voList.add(vo);
                });
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "首页热门榜单列表",response = EsGoodsRankingVO.class)
    @GetMapping(value = "/getEsGoodsRankingList")
    @ResponseBody
    public ApiResponse getEsGoodsRankingList() {
        EsGoodsRankingDTO dto = new EsGoodsRankingDTO();
        dto.setHomePage(1);
        DubboPageResult<EsGoodsRankingDO> result = goodsRankingService.getGoodsRankingList(dto ,3, 1);
        if (result.isSuccess()) {
            List<EsGoodsRankingDO> data = result.getData().getList();
            List<EsGoodsRankingVO> voList = BeanUtil.copyList(data, EsGoodsRankingVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询更多热门榜单列表",response = EsGoodsRankingVO.class)
    @GetMapping(value = "/selectGoodsRankingList")
    @ResponseBody
    public ApiResponse getEsGoodsRankingList(EsQueryPageForm form) {
        DubboPageResult<EsGoodsRankingDO> result = goodsRankingService.selectGoodsRankingList(form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsGoodsRankingDO> data = result.getData().getList();
            List<EsGoodsRankingVO> voList = BeanUtil.copyList(data, EsGoodsRankingVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "热门榜单排行商品列表",response = EsSalesRankingGoodsVO.class)
    @GetMapping(value = "/getGoodsRanking/getList")
    @ResponseBody
    public ApiResponse getGoodsRanking(@Valid EsGoodsRankForm form) {
        DubboPageResult<EsSalesRankingGoodsDO> result = esGoodsService.getByCategoryId(form.getCategoryId(),form.getGoodsId());
        if (result.isSuccess()) {
            List<EsSalesRankingGoodsDO> data = result.getData().getList();
            List<EsSalesRankingGoodsVO> voList = BeanUtil.copyList(data, EsSalesRankingGoodsVO.class);
            return ApiResponse.success(voList);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
    @ApiOperation(value = "首页限时抢购信息")
    @GetMapping(value = "/seckillInfo")
    public ApiResponse<EsSeckillTimetableVO> seckillInfo() {
        EsSeckillTimetableVO esSeckillTimetableVO = new EsSeckillTimetableVO();
        DubboResult<List<EsSeckillTimetableDO>> seckillTimetableToday = esSeckillApplyService.getSeckillTimetableToday();
        if(seckillTimetableToday.isSuccess() && seckillTimetableToday.getData()!= null && !seckillTimetableToday.getData().isEmpty()){
            EsSeckillTimetableDO esSeckillTimetableDO = seckillTimetableToday.getData().get(0);
            BeanUtil.copyProperties(esSeckillTimetableDO,esSeckillTimetableVO);
            EsSeckillTimelineGoodsDTO esSeckillTimelineGoodsDTO = new EsSeckillTimelineGoodsDTO();
            esSeckillTimelineGoodsDTO.setDay(esSeckillTimetableDO.getDay());
            esSeckillTimelineGoodsDTO.setTimeline(esSeckillTimetableDO.getTimeline());
            DubboPageResult<EsSeckillApplyDO> result = esSeckillApplyService.seckillTimelineGoodsList(esSeckillTimelineGoodsDTO, 1, 3);
            if(result.isSuccess() && result.getData() != null){
                esSeckillTimetableVO.setEsSeckillApplyList(BeanUtil.copyList(result.getData().getList(), EsSeckillApplyVO.class));
            }
        }
        return ApiResponse.success(esSeckillTimetableVO);
    }

    @ApiOperation(value = "获取广告",response = EsAdvertisingVO.class)
    @GetMapping(value = "/getEsAdvertising/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1首页，2常买清单", required = true, dataType = "int", paramType = "path")
    })
    @ResponseBody
    public ApiResponse getEsAdvertising(@PathVariable Integer type) {
        String location = "";
        if (type == 1){
            location = "首页";
        }else if (type == 2){
            location = "常购清单";
        }
        DubboResult<EsAdvertisingDO> result = advertisingService.getListByLocation(location);
        if (result.isSuccess()) {
            EsAdvertisingDO data = result.getData();
            EsAdvertisingVO vo = new EsAdvertisingVO();
            if (data != null){
                BeanUtil.copyProperties(data,vo);
            }
            return ApiResponse.success(vo);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "专区查询",response = EsZoneVO.class)
    @GetMapping(value = "/getZoneList")
    @ResponseBody
    public ApiResponse getZoneList() {
        EsZoneDTO dto = new EsZoneDTO();
        DubboPageResult<EsZoneDO> result = iesZoneService.getZoneList(dto, 100, 1);
        if (result.isSuccess()) {
            List<EsZoneDO> data = result.getData().getList();
            List<EsZoneVO> voList = BeanUtil.copyList(data, EsZoneVO.class);
            return ApiResponse.success(voList);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
