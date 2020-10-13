package com.xdl.jjg.web.controller.applet.system;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.system.api.model.domain.EsRegionsDO;
import com.shopx.system.api.model.domain.vo.EsRegionsVO;
import com.shopx.system.api.service.IEsRegionsService;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序-地区
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-20 09:28:26
 */
@Api(value = "/applet/system/regions", tags = "小程序-地区")
@RequestMapping("/applet/system/regions")
@RestController
public class EsAppletRegionsController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsRegionsService regionsService;

    @GetMapping(value = "/{id}/children")
    @ApiOperation(value = "获取某地区的子地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地区id", required = true, dataType = "long", paramType = "path",example = "1")
    })
    @ResponseBody
    public ApiResponse getChildrenById(@PathVariable Long id) {
        DubboResult<List<EsRegionsDO>> result = regionsService.getChildrenById(id);
        if (result.isSuccess()) {
            List<EsRegionsDO> data = result.getData();
            List<EsRegionsVO> voList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                voList = data.stream().map(esRegionsDO -> {
                    EsRegionsVO esRegionsVO = new EsRegionsVO();
                    BeanUtil.copyProperties(esRegionsDO, esRegionsVO);
                    return esRegionsVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(voList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
