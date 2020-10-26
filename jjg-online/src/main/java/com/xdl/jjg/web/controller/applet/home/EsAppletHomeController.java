package com.xdl.jjg.web.controller.applet.home;

import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.shop.model.domain.EsGoodsIndexDO;
import com.jjg.shop.model.domain.EsGoodsWordsDO;
import com.jjg.shop.model.dto.EsGoodsIndexDTO;
import com.jjg.shop.model.vo.EsGoodsWordsVO;
import com.jjg.system.model.domain.EsFocusPictureDO;
import com.jjg.system.model.domain.EsHotKeywordDO;
import com.jjg.system.model.domain.EsSiteNavigationDO;
import com.jjg.system.model.vo.EsFocusPictureVO;
import com.jjg.system.model.vo.EsHotKeywordVO;
import com.jjg.system.model.vo.EsSiteNavigationVO;
import com.jjg.trade.model.domain.EsCouponDO;
import com.jjg.trade.model.domain.EsPromotionGoodsDO;
import com.jjg.trade.model.form.query.EsAppletCouponQueryForm;
import com.jjg.trade.model.form.query.EsSearchQueryForm;
import com.jjg.trade.model.vo.EsCouponVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsCouponService;
import com.xdl.jjg.web.service.IEsPromotionGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序-首页相关接口管理 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-22 09:28:26
 */
@Api(value = "/applet/home",tags = "小程序-首页相关接口管理")
@RestController
@RequestMapping("/applet/home")
public class EsAppletHomeController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsHotKeywordService iEsHotKeywordService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsIndexService iEsGoodsIndexService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsPromotionGoodsService iEsPromotionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsWordsService iEsGoodsWordsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFocusPictureService iEsFocusPictureService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSiteNavigationService iEsSiteNavigationService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCouponService iesMemberCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;


    @ApiOperation(value = "热门关键词列表",response = EsHotKeywordVO.class)
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

    @ApiOperation(value = "通过搜索栏关键字查询商品列表")
    @GetMapping("/getGoodsList")
    @ResponseBody
    public ApiResponse getGoodsList(EsSearchQueryForm form){
        EsGoodsIndexDTO esGoodsIndexDTO = new EsGoodsIndexDTO();
        // 品牌转换为List
        if (form.getBrandList() != null){
            String replace = form.getBrandList().replace("[", "").replace("]","");

            List<String> brandList1 = Arrays.asList(replace.split(","));
            esGoodsIndexDTO.setBrandList(brandList1);
        }
        // 分类转换为List
        if (form.getCategoryIdList() != null){
            String replace = form.getCategoryIdList().replace("[", "").replace("]", "");

            List<String> categoryIdList1 = Arrays.asList(replace.split(","));
            esGoodsIndexDTO.setCategoryIdList(categoryIdList1);
        }

        esGoodsIndexDTO.setKeyword(form.getKeyword());
        esGoodsIndexDTO.setSort(form.getSort());
        DubboPageResult<EsGoodsIndexDO> esGoodsIndex = iEsGoodsIndexService.getEsGoodsIndex(esGoodsIndexDTO, form.getPageSize(), form.getPageNum());
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

    @ApiOperation(value = "查询商品分词以及对应数量" ,response = EsGoodsWordsVO.class)
    @ApiImplicitParam(name = "keyword", value = "搜索关键字", required = true, dataType = "String", paramType = "path")
    @GetMapping("/searchGoodsWords/{keyword}")
    public ApiResponse searchGoodsWords(@PathVariable String keyword){
        DubboPageResult<EsGoodsWordsDO> result = iEsGoodsWordsService.getGoodsWords(keyword);
        if (result.isSuccess()) {
            List<EsGoodsWordsVO> esGoodsWordsVOS = BeanUtil.copyList(result.getData().getList(), EsGoodsWordsVO.class);
            return ApiResponse.success(esGoodsWordsVOS);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "获取轮播图列表")
    @GetMapping("/getPictures")
    @ResponseBody
    public ApiResponse getFocusPictures() {
        DubboPageResult<EsFocusPictureDO> result = iEsFocusPictureService.getList("WAP");
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
        }else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "导航栏列表",response = EsSiteNavigationVO.class)
    @GetMapping(value = "/getNavigationList")
    @ResponseBody
    public ApiResponse getNavigationList() {
        DubboPageResult<EsSiteNavigationDO> result = iEsSiteNavigationService.getByClientType("MOBILE");
        if (result.isSuccess()){
            List<EsSiteNavigationVO> esSiteNavigationVOS = BeanUtil.copyList(result.getData().getList(), EsSiteNavigationVO.class);
            return ApiResponse.success(esSiteNavigationVOS);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "商城首页 领券中心(登录)",response = EsCouponVO.class)
    @GetMapping(value = "/getEsCouponList")
    @ResponseBody
    public ApiResponse getEsCouponList(EsAppletCouponQueryForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        //获取优惠券列表
        DubboPageResult<EsCouponDO> esCouponListAtHome = iEsCouponService.getEsCouponListAtHome(form.getPageNum(), form.getPageSize());
        List<EsCouponDO> couponDOList = esCouponListAtHome.getData().getList();
        List<EsCouponVO> esCouponVOS = BeanUtil.copyList(couponDOList, EsCouponVO.class);
        List<Long> couponDOIds = esCouponVOS.stream().map(EsCouponVO::getId).collect(Collectors.toList());
        //获取已领取的优惠券
        DubboResult result = iesMemberCouponService.getMemberWhetherCouponIds(dubboResult.getData().getId(), couponDOIds);
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

}
