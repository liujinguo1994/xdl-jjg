package com.xdl.jjg.web.controller.pc.system;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsShipTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 运费模板表 前端控制器
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */

@Api(value = "/shipTemplate",tags = "运费模板表模块接口")
@RestController
@RequestMapping("/shipTemplate")
public class EsShipTemplateController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 50000,retries = -1)
    private IEsShipTemplateService shipTemplateService;


    @DeleteMapping(value = "/{id}")
    @ResponseBody
    @ApiOperation(value = "删除运费模板表信息")
    @ApiImplicitParam(name = "id", value = "运费模板表主键id")
    public ApiResponse removeShipTemplate(@PathVariable Long id) {
        DubboResult result = shipTemplateService.deleteShipTemplate(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}