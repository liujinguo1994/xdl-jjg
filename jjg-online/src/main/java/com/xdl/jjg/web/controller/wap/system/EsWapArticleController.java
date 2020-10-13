package com.xdl.jjg.web.controller.wap.system;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.system.api.model.domain.EsArticleCategoryDO;
import com.shopx.system.api.model.domain.EsArticleDO;
import com.shopx.system.api.model.domain.vo.EsArticleCategoryVO;
import com.shopx.system.api.model.domain.vo.EsArticleVO;
import com.shopx.system.api.service.IEsArticleCategoryService;
import com.shopx.system.api.service.IEsArticleService;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器-移动端-帮助中心
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/wap/system/article")
@Api(value = "/wap/system/article", tags = "移动端-帮助中心")
public class EsWapArticleController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsArticleService articleService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsArticleCategoryService articleCategoryService;


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

    @ApiOperation(value = "根据分类id查询文章列表",response = EsArticleVO.class)
    @ApiImplicitParam(name = "categoryId", value = "分类id", required = true, dataType = "long", paramType = "path")
    @GetMapping(value = "/geByCategoryId/{categoryId}")
    @ResponseBody
    public ApiResponse geByCategoryId(@PathVariable Long categoryId) {
        DubboPageResult<EsArticleDO> result = articleService.getByCategoryId(categoryId);
        if (result.isSuccess()) {
            List<EsArticleDO> data = result.getData().getList();
            List<EsArticleVO>  list = BeanUtil.copyList(data,EsArticleVO.class);
            return ApiPageResponse.pageSuccess(list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
