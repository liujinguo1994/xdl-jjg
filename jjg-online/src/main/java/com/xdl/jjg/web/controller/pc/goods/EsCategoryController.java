package com.xdl.jjg.web.controller.pc.goods;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jjg.shop.model.domain.*;
import com.jjg.shop.model.vo.*;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "/seller/category",tags = "商品分类接口管理")
@RestController
@RequestMapping("/seller/category")
public class EsCategoryController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCategoryService esCategoryService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsParameterGroupService iEsParameterGroupService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCategoryService iEsCategoryService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsBrandService esBrandService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCollectionGoodsService esMemberCollectionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService esGoodsService;

    @ApiOperation(value = "查询所有分类，父子关系",response = EsCategoryVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父id，顶级为0", required = true, dataType = "long", paramType = "path",example = "1")})
    @GetMapping(value = "/getCategoryChildren/{parentId}")
    @ResponseBody
    public ApiResponse getCategoryChildren(@ApiIgnore @PathVariable("parentId") Long parentId) {
        DubboPageResult<EsCategoryDO> result = iEsCategoryService.getCategoryChildren(parentId);
        if (result.isSuccess()) {
            List<EsCategoryDO> data = result.getData().getList();
            List<EsCategoryVO> esCategoryVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                esCategoryVOList = data.stream().map(esCategoryDO -> {
                    EsCategoryVO esCategoryVO = new EsCategoryVO();
                    BeanUtil.copyProperties(esCategoryDO, esCategoryVO);
                    return esCategoryVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(esCategoryVOList);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
    @ApiOperation(value = "根据父ID获取分类下面的子类",response = EsCategoryVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父id，顶级为0", required = true, dataType = "long", paramType = "path",example = "1")})
    @GetMapping(value = "/{parentId}/children")
    @ResponseBody
    public ApiResponse getChildren(@PathVariable("parentId") Long parentId) {
        DubboPageResult<EsCategoryDO> result = iEsCategoryService.getCategoryParentList(parentId);
        if (result.isSuccess()) {
            List<EsCategoryDO> data = result.getData().getList();
            List<EsCategoryVO> esCategoryVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                esCategoryVOList = data.stream().map(esCategoryDO -> {
                    EsCategoryVO esCategoryVO = new EsCategoryVO();
                    BeanUtil.copyProperties(esCategoryDO, esCategoryVO);
                    return esCategoryVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(esCategoryVOList);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询分类绑定的参数，包括参数组和参数",response = ParameterGroupVO.class)
    @ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = "long", paramType = "path",example = "1")
    @GetMapping(value = "/getParamsByCategory/{id}")
    @ResponseBody
    public ApiResponse getParamsByCategory(@PathVariable("id") Long id) {
        DubboPageResult<ParameterGroupDO> result = iEsParameterGroupService.getParameterGroupList(id);
        if(result.isSuccess()){
            List<ParameterGroupDO> data = result.getData().getList();
            List<ParameterGroupVO> parameterGroupVOList = BeanUtil.copyList(data, ParameterGroupVO.class);
            return ApiResponse.success(parameterGroupVOList);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
    @ApiOperation(value = "分类关联的品牌信息")
    @ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path")
    @GetMapping(value = "/category/{category_id}/brands")
    public ApiResponse queryBrands(@PathVariable("category_id") Long categoryId) {
     DubboPageResult<EsBrandDO>   result = esBrandService.getBrandsByCategory(categoryId);
         if(result.isSuccess()){
             List<EsBrandVO>  brandVOList = BeanUtil.copyList(result.getData().getList(), EsBrandVO.class);
             return ApiResponse.success(brandVOList);
        } else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

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

    @ApiOperation(value = "商城首页 猜你喜欢列表",response = EsGoodsVO.class)
    @GetMapping(value = "/getGuessYouLikeList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query")})
    @ResponseBody
    public ApiResponse getGuessYouLikeList(@ApiIgnore @NotEmpty(message = "页码不能为空") Integer pageNum,
                                           @ApiIgnore @NotEmpty(message = "每页数量不能为空") Integer pageSize) {
        // 未登录的情况
        Long memberId = null;
        try {
            memberId = ShiroKit.getUser().getId();
        } catch (Exception e) {
            Long[] goodsId = {};
            DubboPageResult<EsGoodsDO> guessYouLike = esGoodsService.getGuessYouLike(goodsId, pageNum, pageSize);
            if (guessYouLike.isSuccess()) {
                List<EsGoodsVO> esGoodsVOS = BeanUtil.copyList(guessYouLike.getData().getList(), EsGoodsVO.class);
                return ApiResponse.success(esGoodsVOS);
            }else{
                return ApiResponse.fail(ApiStatus.wrapperException(guessYouLike));
            }
        }

        // 登录的情况
        // 调用会员收藏表接口 获取goodsId list
        DubboResult<Long> listByMemberId = esMemberCollectionGoodsService.getMemberCollectionGoodListByMemberId(memberId);

        List list = null;
        if (org.apache.dubbo.common.utils.CollectionUtils.isEmpty(list)){
            //如果该用户不存在收藏的商品
        }else {

        }


        return ApiResponse.success();

    }


//    public ApiResponse getBrandShow(){
//
//    }
}
