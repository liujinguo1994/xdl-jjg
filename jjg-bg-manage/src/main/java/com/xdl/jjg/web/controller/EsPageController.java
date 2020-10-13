package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsPageDO;
import com.xdl.jjg.model.dto.EsPageDTO;
import com.xdl.jjg.model.form.EsPageForm;
import com.xdl.jjg.model.vo.EsPageVO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器-楼层装修
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esPage")
@Api(value = "/esPage", tags = "楼层装修")
public class EsPageController {

    @Autowired
    private IEsPageService iEsPageService;

    @GetMapping(value = "/getByType/{clientType}/{pageType}")
    @ApiOperation(value = "使用客户端类型和页面类型查询一个楼层", response = EsPageVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientType", value = "要查询的客户端类型 APP/WAP/PC", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "pageType", value = "要查询的页面类型 INDEX 首页/SPECIAL 专题", required = true, dataType = "string", paramType = "path")
    })
    @ResponseBody
    public ApiResponse getByType(@PathVariable("clientType") String clientType, @PathVariable("pageType") String pageType) {
        DubboResult<EsPageDO> result = iEsPageService.getByType(clientType, pageType);
        if (result.isSuccess()) {
            EsPageDO data = result.getData();
            EsPageVO esPageVO = new EsPageVO();
            BeanUtil.copyProperties(data, esPageVO);
            return ApiResponse.success(esPageVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "使用客户端类型和页面类型修改楼层")
    @PutMapping(value = "/updatePage")
    @ResponseBody
    public ApiResponse updatePage(@Valid @RequestBody @ApiParam(name = "楼层信息form对象", value = "form") EsPageForm form) {
        EsPageDTO esPageDTO = new EsPageDTO();
        BeanUtil.copyProperties(form, esPageDTO);
        DubboResult result = iEsPageService.updatePage(esPageDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
