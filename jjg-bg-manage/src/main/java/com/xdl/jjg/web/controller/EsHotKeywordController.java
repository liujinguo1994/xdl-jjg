package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsHotKeywordDO;
import com.xdl.jjg.model.dto.EsHotKeywordDTO;
import com.xdl.jjg.model.form.EsHotKeywordForm;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.vo.EsHotKeywordVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsHotKeywordService;
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
 * 前端控制器-热门关键字
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-20 15:30:00
 */
@Api(value = "/esHotKeyword", tags = "热门关键字")
@RestController
@RequestMapping("/esHotKeyword")
public class EsHotKeywordController {

    @Autowired
    private IEsHotKeywordService iEsHotKeywordService;

    @ApiOperation(value = "新增热门关键字")
    @ResponseBody
    @PostMapping(value = "/insertHotKeyword")
    public ApiResponse insertHotKeyword(@Valid @RequestBody @ApiParam(name = "热门关键字form对象", value = "form") EsHotKeywordForm form) {
        EsHotKeywordDTO esHotKeywordDTO = new EsHotKeywordDTO();
        BeanUtil.copyProperties(form, esHotKeywordDTO);
        DubboResult result = iEsHotKeywordService.insertHotKeyword(esHotKeywordDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/updateHotKeyword")
    @ResponseBody
    @ApiOperation(value = "修改热门关键字")
    public ApiResponse updateHotKeyword(@Valid @RequestBody @ApiParam(name = "热门关键字form对象", value = "form") EsHotKeywordForm form) {
        EsHotKeywordDTO esHotKeywordDTO = new EsHotKeywordDTO();
        BeanUtil.copyProperties(form, esHotKeywordDTO);
        DubboResult result = iEsHotKeywordService.updateHotKeyword(esHotKeywordDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteHotKeyword/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "关键字id", required = true, dataType = "long", example = "1", paramType = "path")
    public ApiResponse deleteHotKeyword(@PathVariable Long id) {
        DubboResult result = iEsHotKeywordService.deleteHotKeyword(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页获取热门关键字展示列表", response = EsHotKeywordVO.class)
    @GetMapping(value = "/getHotKeywordList")
    @ResponseBody
    public ApiResponse getHotKeywordList(EsQueryPageForm form) {
        EsHotKeywordDTO esHotKeywordDTO = new EsHotKeywordDTO();
        DubboPageResult<EsHotKeywordDO> result = iEsHotKeywordService.getHotKeywordList(esHotKeywordDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsHotKeywordDO> data = result.getData().getList();
            List<EsHotKeywordVO> esHotKeywordVOList = BeanUtil.copyList(data, EsHotKeywordVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esHotKeywordVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
