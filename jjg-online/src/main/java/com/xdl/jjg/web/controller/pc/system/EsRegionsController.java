package com.xdl.jjg.web.controller.pc.system;

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
 *  前端控制器-地区
 * </p>
 *
 * @author liuJG
 * @since 2019-07-16
 */
@RestController
@RequestMapping("/esRegions")
@Api(value="/esRegions", tags="地区")
public class EsRegionsController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsRegionsService iEsRegionsService;

    @GetMapping(value = "/{id}/children")
    @ApiOperation(value = "获取某地区的子地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地区id", required = true, dataType = "long", paramType = "path",example = "1")
    })
    @ResponseBody
    public ApiResponse getChildrenById(@PathVariable Long id) {
        DubboResult<List<EsRegionsDO>> result = iEsRegionsService.getChildrenById(id);
        if (result.isSuccess()) {
            List<EsRegionsDO> data = result.getData();
            List<EsRegionsVO> esRegionsVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                esRegionsVOList = data.stream().map(esRegionsDO -> {
                    EsRegionsVO esRegionsVO = new EsRegionsVO();
                    BeanUtil.copyProperties(esRegionsDO, esRegionsVO);
                    return esRegionsVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(esRegionsVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
