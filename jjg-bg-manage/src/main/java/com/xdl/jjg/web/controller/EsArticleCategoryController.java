package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsArticleCategoryDO;
import com.xdl.jjg.model.dto.EsArticleCategoryDTO;
import com.xdl.jjg.model.form.EsArticleCategoryForm;
import com.xdl.jjg.model.form.EsArticleCategoryQueryForm;
import com.xdl.jjg.model.vo.EsArticleCategoryVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsArticleCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器-文章分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esArticleCategory")
@Api(value="/esArticleCategory", tags="文章分类")
public class EsArticleCategoryController {

    @Autowired
    private IEsArticleCategoryService articleCategoryService;

    @ApiOperation(value = "新增文章分类")
    @ResponseBody
    @PostMapping(value = "/insertArticleCategory")
    public ApiResponse insertArticleCategory(@Valid @RequestBody @ApiParam(name="文章分类form对象",value="form") EsArticleCategoryForm form){
        EsArticleCategoryDTO dto = new EsArticleCategoryDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult result = articleCategoryService.insertArticleCategory(dto);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/updateArticleCategory")
    @ResponseBody
    @ApiOperation(value = "修改文章分类")
    public ApiResponse updateArticleCategory(@Valid @RequestBody @ApiParam(name="文章分类form对象",value="form") EsArticleCategoryForm form){
        EsArticleCategoryDTO dto = new EsArticleCategoryDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult result = articleCategoryService.updateArticleCategory(dto);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteArticleCategory/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "文章分类id", required = true, dataType = "long",example = "1", paramType = "path")
    public ApiResponse deleteArticleCategory(@PathVariable Long id){
        DubboResult result = articleCategoryService.deleteArticleCategory(id);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询一级文章分类列表",response = EsArticleCategoryVO.class)
    @GetMapping(value = "/getArticleCategoryList")
    @ResponseBody
    public ApiResponse getArticleCategoryList(EsArticleCategoryQueryForm form) {
        EsArticleCategoryDTO dto = new EsArticleCategoryDTO();
        BeanUtil.copyProperties(form,dto);
        DubboPageResult<EsArticleCategoryDO> result = articleCategoryService.getArticleCategoryList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsArticleCategoryDO> data = result.getData().getList();
            List<EsArticleCategoryVO>  list =BeanUtil.copyList(data,EsArticleCategoryVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询二级文章分类列表",response = EsArticleCategoryVO.class)
    @ApiImplicitParam(name = "id", value = "一级文章分类id", required = true, dataType = "long", paramType = "path")
    @GetMapping(value = "/getChildren/{id}")
    @ResponseBody
    public ApiResponse getChildren(@PathVariable Long id) {
        DubboPageResult<EsArticleCategoryDO> result = articleCategoryService.getChildren(id);
        if (result.isSuccess()) {
            List<EsArticleCategoryDO> data = result.getData().getList();
            List<EsArticleCategoryVO>  list = BeanUtil.copyList(data,EsArticleCategoryVO.class);
            return ApiPageResponse.pageSuccess(list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询文章分类树",response = EsArticleCategoryVO.class)
    @GetMapping("/getTree")
    @ResponseBody
    public ApiResponse getTree() {
        DubboPageResult<EsArticleCategoryDO> result = articleCategoryService.getTree();
        if (result.isSuccess()) {
            List<EsArticleCategoryDO> data = result.getData().getList();
            List<EsArticleCategoryVO>  list = (  List<EsArticleCategoryVO> ) BeanUtil.copyList(data,new EsArticleCategoryVO().getClass());
            return ApiPageResponse.pageSuccess(list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
