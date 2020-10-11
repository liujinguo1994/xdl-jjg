package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.vo.EsSmsPlatformVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.web.service.IEsSmsPlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器-短信网关设置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esSmsPlatform")
@Api(value="/esSmsPlatform", tags="短信网关设置")
public class EsSmsPlatformController {

    @Autowired
    private IEsSmsPlatformService iEsSmsPlatformService;

    @ApiOperation(value = "查询短信平台列表",response = EsSmsPlatformVO.class)
    @GetMapping(value = "/getSmsPlatformList")
    @ResponseBody
    public ApiResponse getSmsPlatformList(EsQueryPageForm form) {
        DubboPageResult<EsSmsPlatformVO> result = iEsSmsPlatformService.getSmsPlatformList(form.getPageSize(),form.getPageNum());
        if (result.isSuccess()) {
            List<EsSmsPlatformVO> data = result.getData().getList();
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),data);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改短信网关")
    @PutMapping(value = "/updateSmsPlatform")
    @ResponseBody
    public ApiResponse updateSmsPlatform(@RequestBody @ApiParam(name="短信网关form对象",value="form") EsSmsPlatformVO form){
        DubboResult result = iEsSmsPlatformService.updateSmsPlatform(form);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "开启某个短信网关")
    @PutMapping("/openPlatform/{bean}")
    @ApiImplicitParam(name = "bean", value = "bean", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse openPlatform(@PathVariable String bean) {
        DubboResult result = iEsSmsPlatformService.openPlatform(bean);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
