package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsArticleDO;
import com.xdl.jjg.model.dto.EsArticleDTO;
import com.xdl.jjg.model.form.EsArticleForm;
import com.xdl.jjg.model.form.EsArticleQueryForm;
import com.xdl.jjg.model.vo.EsArticleVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsArticleService;
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
 *  前端控制器-文章
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esArticle")
@Api(value="/esArticle", tags="文章")
public class EsArticleController {

    @Autowired
    private IEsArticleService articleService;

    @ApiOperation(value = "分页查询文章列表",response = EsArticleVO.class)
    @GetMapping(value = "/getArticleList")
    @ResponseBody
    public ApiResponse getArticleList(EsArticleQueryForm form) {
        EsArticleDTO dto = new EsArticleDTO();
        BeanUtil.copyProperties(form,dto);
        DubboPageResult<EsArticleDO> result = articleService.getArticleList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsArticleDO> data = result.getData().getList();
            List<EsArticleVO>  list = BeanUtil.copyList(data,EsArticleVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "新增文章")
    @ResponseBody
    @PostMapping(value = "/insertArticle")
    public ApiResponse insertArticle(@Valid @RequestBody @ApiParam(name="文章form对象",value="form") EsArticleForm form){
        EsArticleDTO dto = new EsArticleDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult<EsBrandDO> result = articleService.insertArticle(dto);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改文章")
    @PutMapping(value = "/updateEsBrand/{id}")
    @ResponseBody
    public ApiResponse updateEsBrand(@Valid @RequestBody @ApiParam(name="文章form对象",value="form") EsArticleForm form){
        EsArticleDTO dto = new EsArticleDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult<EsBrandDO> result = articleService.updateArticle(dto);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteArticle/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "文章主键id", required = true, dataType = "long",example = "1", paramType = "path")
    public ApiResponse deleteArticle(@PathVariable Long id){
        DubboResult result = articleService.deleteArticle(id);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getById/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id查询文章")
    @ApiImplicitParam(name = "id", value = "文章主键id", required = true, dataType = "long",example = "1", paramType = "path")
    public ApiResponse getById(@PathVariable Long id){
        DubboResult<EsArticleDO> result = articleService.getArticle(id);
        if(result.isSuccess()){
            EsArticleDO data = result.getData();
            EsArticleVO vo = new EsArticleVO();
            BeanUtil.copyProperties(data,vo);
            return ApiResponse.success(vo);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
