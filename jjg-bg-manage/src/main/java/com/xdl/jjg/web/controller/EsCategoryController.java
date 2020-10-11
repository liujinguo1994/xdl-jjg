package com.xdl.jjg.web.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsCategoryForm;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 *  前端控制器-商品分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-20 15:30:00
 */
@Api(value = "/esCategory",tags = "商品分类")
@RestController
@RequestMapping("/esCategory")
public class EsCategoryController {

    @Autowired
    private IEsCategoryService iEsCategoryService;

    @Autowired
    private IEsCategoryBrandService iEsCategoryBrandService;

    @Autowired
    private IEsShipTemplateService iEsShipTemplateService;

    @Autowired
    private IEsParameterGroupService iEsParameterGroupService;

    @Autowired
    private IEsGoodsFreightService iEsGoodsFreightService;

    @Autowired
    private IEsCommentCategoryService iEsCommentCategoryService;


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

    @PostMapping(value = "/insertEsCategory")
    @ApiOperation(value = "新增商品分类")
    @ResponseBody
    public ApiResponse insertEsCategory(@Valid @RequestBody @ApiParam(name="商品分类form对象",value="form") EsCategoryForm form) {
        EsCategoryDTO categoryDTO = new EsCategoryDTO();
        BeanUtil.copyProperties(form, categoryDTO);
        DubboResult result = iEsCategoryService.insertCategory(categoryDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/updateCategory/{id}")
    @ApiOperation(value = "编辑商品分类")
    @ResponseBody
    public ApiResponse updateCategory(@Valid @RequestBody EsCategoryForm form, @PathVariable Long id) {
        EsCategoryDTO categoryDTO = new EsCategoryDTO();
        BeanUtil.copyProperties(form, categoryDTO);
        DubboResult result = iEsCategoryService.updateCategory(categoryDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteCategory/{id}")
    @ApiOperation(value = "删除商品分类")
    @ApiImplicitParam(name = "id", value = "商品分类id", required = true, dataType = "long", paramType = "path",example = "1")
    @ResponseBody
    public ApiResponse deleteCategory(@PathVariable Long id) {
        DubboResult result = iEsCategoryService.deleteCategory(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询某个分类绑定的品牌,包括未选中的品牌",response = EsBrandSelectVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = "long", paramType = "path") })
    @GetMapping(value = "/getCatBrands/{id}")
    @ResponseBody
    public ApiResponse getCatBrands(@PathVariable("id") Long id) {
        DubboPageResult<EsBrandSelectDO> result = iEsCategoryBrandService.getCategoryList(id);
        if (result.isSuccess()) {
            List<EsBrandSelectDO> data = result.getData().getList();
            List<EsBrandSelectVO> esBrandSelectVOList =  BeanUtil.copyList(data, EsBrandSelectVO.class);
            return ApiResponse.success(esBrandSelectVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @ApiOperation(value = "分类绑定品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分类id", required = true, paramType = "path", dataType = "long"),
            @ApiImplicitParam(name = "brandIds", value = "品牌id数组", required = true, paramType = "path", dataType = "int", allowMultiple = true) })
    @PutMapping(value = "/saveCatBrands/{id}/{brandIds}")
    @ResponseBody
    public ApiResponse saveCatBrands(@PathVariable Long id, @Size(message = "至少选中一个品牌",min = 1) @ApiIgnore @PathVariable Integer[] brandIds) {
        DubboResult result = iEsCategoryBrandService.saveCategoryBrand(id, brandIds);
        if(result.isSuccess()){
            return ApiResponse.success();
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
            List<ParameterGroupVO> parameterGroupVOList = BeanUtil.copyList(data,ParameterGroupVO.class);
            return ApiResponse.success(parameterGroupVOList);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getComboBox")
    @ResponseBody
    @ApiOperation(value = "获取运费模板下拉框",response = EsShipTemplateVO.class)
    public ApiResponse getComboBox() {
        DubboPageResult<EsShipTemplateDO> result = iEsShipTemplateService.getComboBox();
        if(result.isSuccess()){
            List<EsShipTemplateDO> data = result.getData().getList();
            List<EsShipTemplateVO> esShipTemplateVOList = BeanUtil.copyList(data, EsShipTemplateVO.class);
            return ApiResponse.success(esShipTemplateVOList);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分类绑定运费模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分类id", required = true, paramType = "path", dataType = "long"),
            @ApiImplicitParam(name = "shipTemplateId", value = "运费模板id", required = true, paramType = "path", dataType = "long") })
    @PutMapping(value = "/saveCatShipTemplate/{id}/{shipTemplateId}")
    @ResponseBody
    public ApiResponse saveCatShipTemplate(@PathVariable Long id, @PathVariable Long shipTemplateId) {
        DubboResult result = iEsGoodsFreightService.saveCatShipTemplate(id, shipTemplateId);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询某个分类绑定的商品评论标签,包括未选中的标签",response = EsCommentCategoryVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = "long", paramType = "path") })
    @GetMapping(value = "/getCatCommentLabels/{id}")
    @ResponseBody
    public ApiResponse getCatCommentLabels(@PathVariable("id") Long id) {
        DubboPageResult<EsCommentCategoryDO> result = iEsCommentCategoryService.getEsCommentCategoryBindList(id);
        if (result.isSuccess()) {
            List<EsCommentCategoryDO> data = result.getData().getList();
            List<EsCommentCategoryVO> esCommentCategoryVOList = BeanUtil.copyList(data, EsCommentCategoryVO.class);
            return ApiResponse.success(esCommentCategoryVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }

    }

    @ApiOperation(value = "分类绑定商品评论标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分类id", required = true, paramType = "path", dataType = "long"),
            @ApiImplicitParam(name = "labelIds", value = "评论标签id数组", required = true, paramType = "path", dataType = "int", allowMultiple = true) })
    @PutMapping(value = "/saveCatCommentLabels/{id}/{labelIds}")
    @ResponseBody
    public ApiResponse saveCatCommentLabels(@PathVariable Long id, @Size(message = "至少选中一个标签",min = 1) @ApiIgnore @PathVariable Integer[] labelIds) {
        DubboPageResult result = iEsCommentCategoryService.saveCommentCategory(id, labelIds);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询所有分类，父子关系",response = EsCategoryVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父id，顶级为0", required = true, dataType = "long", paramType = "path",example = "1")})
    @GetMapping(value = "getCategoryChildren/{parentId}")
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


}

