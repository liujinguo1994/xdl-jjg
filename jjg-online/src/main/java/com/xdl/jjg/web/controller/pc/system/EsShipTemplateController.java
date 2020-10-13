package com.xdl.jjg.web.controller.pc.system;/*
package com.shopx.trade.web.controller;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.EsShipTemplateDO;
import com.shopx.trade.api.model.domain.dto.EsShipTemplateDTO;
import com.shopx.trade.api.model.domain.vo.EsShipTemplateVO;
import com.shopx.trade.api.service.IEsShipTemplateService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsShipTemplateForm;
import com.shopx.trade.web.request.query.EsShipTemplateQueryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

*/

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.service.IEsShipTemplateService;
import com.shopx.trade.web.constant.ApiStatus;
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