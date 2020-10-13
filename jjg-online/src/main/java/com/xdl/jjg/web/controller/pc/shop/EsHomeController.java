package com.xdl.jjg.web.controller.pc.shop;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.model.domain.*;
import com.shopx.goods.api.model.domain.cache.EsPcSelectSearchCO;
import com.shopx.goods.api.model.domain.cache.EsSelectSearchCO;
import com.shopx.goods.api.model.domain.dto.EsGoodsIndexDTO;
import com.shopx.goods.api.model.domain.vo.*;
import com.shopx.goods.api.service.*;
import com.shopx.member.api.model.domain.EsCommercelItemsDO;
import com.shopx.member.api.service.IEsCommercelItemsService;
import com.shopx.member.api.service.IEsMemberCollectionGoodsService;
import com.shopx.system.api.model.domain.EsFocusPictureDO;
import com.shopx.system.api.model.domain.EsGoodGoodsDO;
import com.shopx.system.api.model.domain.EsHotKeywordDO;
import com.shopx.system.api.model.domain.EsSiteNavigationDO;
import com.shopx.system.api.model.domain.vo.EsFocusPictureVO;
import com.shopx.system.api.model.domain.vo.EsGoodGoodsVO;
import com.shopx.system.api.model.domain.vo.EsHotKeywordVO;
import com.shopx.system.api.model.domain.vo.EsSiteNavigationVO;
import com.shopx.system.api.service.IEsFocusPictureService;
import com.shopx.system.api.service.IEsGoodGoodsService;
import com.shopx.system.api.service.IEsHotKeywordService;
import com.shopx.system.api.service.IEsSiteNavigationService;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.EsPromotionGoodsDO;
import com.shopx.trade.api.model.domain.vo.EsCouponVO;
import com.shopx.trade.api.model.domain.vo.EsGoodsIndexVO;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.api.service.IEsPromotionGoodsService;
import com.shopx.trade.web.constant.AgentTypeUtils;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.PcSelectorQueryForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(value = "/zhuox/home",tags = "商城首页商品接口管理")
@RestController
@RequestMapping("/zhuox/home")
public class EsHomeController {

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


    @ApiOperation(value = "商城首页 通过父类ID 获取分类",response = EsBuyerCategoryVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父id，顶级为0", required = true, dataType = "long", paramType = "path",example = "1")})
    @GetMapping(value = "/getCategoryChildrenList/{parentId}")
    @ResponseBody
    public ApiResponse getCategoryChildrenList(@PathVariable("parentId") Long parentId) {
        DubboPageResult<EsBuyerCategoryDO> buyCategoryChildren = iEsCategoryService.getBuyCategoryChildren(parentId);
        if (buyCategoryChildren.isSuccess()) {
            List<EsBuyerCategoryVO> esBuyerCategoryVOS = BeanUtil.copyList(buyCategoryChildren.getData().getList(), EsBuyerCategoryVO.class);
            return ApiResponse.success(esBuyerCategoryVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(buyCategoryChildren));
        }
    }


    /*
     * @Author xiaoLin
     * @Description
     * @Param [request]
     * @return com.shopx.common.model.result.ApiResponse
     **/
    @ApiOperation(value = "获取轮播图列表", notes = "获取轮播图列表")
    @GetMapping("/getPictures")
    @ResponseBody
    public ApiResponse getFocusPictures(HttpServletRequest request) {//IEsFocusPictureService
        String userAgent = request.getHeader("user-agent");
        String clientType = AgentTypeUtils.isMobileOrTablet(userAgent);
        if(StringUtil.isEmpty(clientType)){
            return null;
        }
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

    @ApiOperation(value = "商城首页 领券中心获取优惠券列表",response = EsCouponVO.class)
    @GetMapping(value = "/getEsCouponList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query")})
    @ResponseBody
    public ApiResponse getEsCouponList(@ApiIgnore @NotEmpty(message = "页码不能为空") Integer pageNum,
                                       @ApiIgnore @NotEmpty(message = "每页数量不能为空") Integer pageSize) {
        DubboPageResult<EsCouponDO> esCouponListAtHome = iEsCouponService.getEsCouponListAtHome(pageNum, pageSize);
        if (esCouponListAtHome.isSuccess()) {
            List<EsCouponVO> esCouponVOS = BeanUtil.copyList(esCouponListAtHome.getData().getList(), EsCouponVO.class);
            esCouponVOS.forEach(esCouponVO -> {
                esCouponVO.setIsReceive(0);
            });
            return ApiResponse.success(esCouponVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(esCouponListAtHome));
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
            return ApiResponse.success(esGoodsVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(guessYouLike));
        }

    }

    @ApiOperation(value = "商城首页 推荐排行列表",response = EsBuyerGoodsVO.class)
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
    }

    @ApiOperation(value = "商城首页 导航栏列表",response = EsBuyerGoodsVO.class)
    @GetMapping(value = "/getNavigationList")
    @ResponseBody
    public ApiResponse getNavigationList(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        String clientType = AgentTypeUtils.isMobileOrTablet(userAgent);
        if ("WAP".equals(clientType)){
            clientType = "MOBILE";
        }
        if(StringUtil.isEmpty(clientType)){
            return null;
        }
        DubboPageResult<EsSiteNavigationDO> byClientType = iEsSiteNavigationService.getByClientType(clientType);
        if (byClientType.isSuccess()){
            List<EsSiteNavigationVO> esSiteNavigationVOS = BeanUtil.copyList(byClientType.getData().getList(), EsSiteNavigationVO.class);
            return ApiResponse.success(esSiteNavigationVOS);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(byClientType));
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

    @ApiOperation(value = "商城首页 品质好货列表",response = EsGoodGoodsVO.class)
    @GetMapping(value = "/getGoodGoodsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query",defaultValue = "10")})
    @ResponseBody
    public ApiResponse getGoodGoodsList(@ApiIgnore @NotEmpty(message = "页码不能为空") Integer pageNum,
                                           @ApiIgnore @NotEmpty(message = "每页数量不能为空") Integer pageSize) {
        DubboPageResult<EsGoodGoodsDO> list = iEsGoodGoodsService.getList(pageSize,pageNum);
        if (list.isSuccess()) {
            List<EsGoodGoodsVO> esGoodGoodsVOS = BeanUtil.copyList(list.getData().getList(), EsGoodGoodsVO.class);
            return ApiResponse.success(esGoodGoodsVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(list));
        }
    }

    @GetMapping("/selector")
    @ResponseBody
    @ApiOperation(value = "查询商品选择器",response = EsSelectSearchCO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "selectorQueryForm" , value = "查询Form表单")
            })
    public ApiResponse searchGoodsSelector( PcSelectorQueryForm selectorQueryForm){
        EsGoodsIndexDTO esGoodsIndexDTO = new EsGoodsIndexDTO();
        esGoodsIndexDTO.setKeyword(selectorQueryForm.getKeyword());
        esGoodsIndexDTO.setPrice(selectorQueryForm.getPrice());
        esGoodsIndexDTO.setPropList(selectorQueryForm.getProp());
        // 已选分类
        if (CollectionUtils.isNotEmpty(selectorQueryForm.getCat())) {
            esGoodsIndexDTO.setCategoryIdList(selectorQueryForm.getCat());
//            esGoodsIndexDTO.setCategoryId(Long.valueOf(selectorQueryForm.getCat().get(0)));
        }
        // 已选品牌
        if (CollectionUtils.isNotEmpty(selectorQueryForm.getBrand())) {
            esGoodsIndexDTO.setBrandList(selectorQueryForm.getBrand());
        }
        DubboPageResult<EsPcSelectSearchCO> pcSelector = iEsGoodsIndexService.getPcSelector(esGoodsIndexDTO);
        if (pcSelector.isSuccess()) {
            List<EsPcSelectSearchCO> list = pcSelector.getData().getList();
            return ApiResponse.success(list);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(pcSelector));
        }
    }

    @ApiOperation(value = "查询购物车的数量")
    @GetMapping(value = "/cartNum")
    public ApiResponse getCartNum() {

        ShiroUser buyer = ShiroKit.getUser();
        if (buyer != null) {
            Long memberId = ShiroKit.getUser().getId();
            // 通过会员ID 获取购物车数量
            DubboPageResult pageResult = commerceItemsService.getCommercelItemsListByMemeberId(memberId);
            if (!pageResult.isSuccess()) {
                throw new ArgumentException(pageResult.getCode(), pageResult.getMsg());
            }
            List<EsCommercelItemsDO> commerceItemsList = pageResult.getData().getList();

            return ApiResponse.success(commerceItemsList.size());
        }
        return ApiResponse.success(0);
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

}
