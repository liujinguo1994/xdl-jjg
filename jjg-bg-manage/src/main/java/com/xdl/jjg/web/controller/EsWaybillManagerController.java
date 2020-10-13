package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.vo.EsWaybillVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.web.service.IEsWaybillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器-电子面单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esWaybillManager")
@Api(value = "/esWaybillManager", tags = "电子面单")
public class EsWaybillManagerController {

    @Autowired
    private IEsWaybillService waybillService;


    @ApiOperation(value = "查询电子面单列表", response = EsWaybillVO.class)
    @GetMapping(value = "/getWaybillList")
    @ResponseBody
    public ApiResponse getWaybillList() {
        DubboPageResult<EsWaybillVO> result = waybillService.getWaybillList();
        if (result.isSuccess()) {
            List<EsWaybillVO> data = result.getData().getList();
            return ApiResponse.success(data);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改电子面单")
    @PutMapping(value = "/updateWaybill")
    @ResponseBody
    public ApiResponse updateWaybill(@RequestBody @ApiParam(name = "电子面单form对象", value = "form") EsWaybillVO form) {
        DubboResult result = waybillService.updateWaybill(form);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "开启某个电子面单")
    @PutMapping("/openWaybill/{bean}")
    @ApiImplicitParam(name = "bean", value = "bean", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse openWaybill(@PathVariable String bean) {
        DubboResult result = waybillService.openWaybill(bean);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
