package com.xdl.jjg.web.controller.pc.shop;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.system.api.model.domain.EsPageDO;
import com.shopx.system.api.service.IEsPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 楼层控制器
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-21 16:39:22
 */
@RestController
@RequestMapping("/zhuox/pages")
@Api(description = "楼层相关API")
public class PageDataBuyerController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsPageService pageService;

    @GetMapping(value = "/{client_type}/{page_type}")
    @ApiOperation(value = "查询楼层数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "client_type", value = "要查询的客户端类型 APP/WAP/PC", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "page_type", value = "要查询的页面类型 INDEX 首页/SPECIAL 专题", required = true, dataType = "string", paramType = "path")
    })
    public ApiResponse getPageDage(@PathVariable("client_type") String clientType, @PathVariable("page_type") String pageType) {

        DubboResult<EsPageDO> byType = this.pageService.getByType(clientType, pageType);
        if (!byType.isSuccess()){
            return ApiResponse.fail(byType.getCode(),byType.getMsg());
        }
        return ApiResponse.success(byType.getData());
    }

}