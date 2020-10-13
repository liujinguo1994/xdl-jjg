package com.xdl.jjg.web.controller.wap.goods;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.model.domain.*;
import com.shopx.goods.api.model.domain.vo.*;
import com.shopx.goods.api.service.IEsBrandService;
import com.shopx.goods.api.service.IEsCategoryService;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 移动端-商品分类 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Api(value = "/wap/goods/category",tags = "移动端-商品分类")
@RestController
@RequestMapping("/wap/goods/category")
public class EsWapCategoryController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCategoryService iEsCategoryService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsBrandService esBrandService;

    @ApiOperation(value = "查询所有分类，父子关系",response = EsCategoryVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父id，顶级为0", required = true, dataType = "long", paramType = "path",example = "1")})
    @GetMapping(value = "/getCategoryChildren/{parentId}")
    @ResponseBody
    public ApiResponse getCategoryChildren(@PathVariable("parentId") Long parentId) {
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

    @ApiOperation(value = "获取分类关联的品牌")
    @ApiImplicitParam(name = "categoryId", value = "分类id", required = true, dataType = "long", paramType = "path")
    @GetMapping(value = "/getBrandsByCategory/{categoryId}")
    @ResponseBody
    public ApiResponse getBrandsByCategory(@PathVariable Long categoryId) {
        DubboPageResult<EsBrandDO> result = esBrandService.getBrandsByCategory(categoryId);
        if(result.isSuccess()){
            List<EsBrandVO>  brandVOList = BeanUtil.copyList(result.getData().getList(), EsBrandVO.class);
            return ApiResponse.success(brandVOList);
        } else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
