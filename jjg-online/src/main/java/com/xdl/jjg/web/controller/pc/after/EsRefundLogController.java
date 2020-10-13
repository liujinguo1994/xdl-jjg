package com.xdl.jjg.web.controller.pc.after;/*
package com.shopx.trade.web.controller;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.EsRefundLogDO;
import com.shopx.trade.api.model.domain.dto.EsRefundLogDTO;
import com.shopx.trade.api.model.domain.vo.EsRefundLogVO;
import com.shopx.trade.api.service.IEsRefundLogService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsRefundLogForm;
import com.shopx.trade.web.request.query.EsRefundLogQueryForm;
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
/**
 * <p>
 *  退货日志表 前端控制器
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 *//*

@Api(value = "/refundLog",tags = "退货日志模块接口")
@RestController
@RequestMapping("/refundLog")
public class EsRefundLogController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsRefundLogService refundLogService;

    @PostMapping
    @ResponseBody
    @ApiOperation(value = "新增信息", notes = "根据form表单数据提交")
    @ApiImplicitParam(name = "refundLogForm", value = "Form表单")
    public ApiResponse addRefundLog(@Valid EsRefundLogForm refundLogForm) {
        EsRefundLogDTO refundLogDTO = new EsRefundLogDTO();
        BeanUtil.copyProperties(refundLogForm, refundLogDTO);
        DubboResult result = refundLogService.insertRefundLog(refundLogDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping
    @ResponseBody
    @ApiOperation(value = "编辑信息", notes = "根据form表单数据提交")
    @ApiImplicitParam(name = "refundLogForm", value = "Form表单")
    public ApiResponse updateRefundLog(@Valid EsRefundLogForm refundLogForm, @PathVariable Long id) {
        EsRefundLogDTO refundLogDTO = new EsRefundLogDTO();
        BeanUtil.copyProperties(refundLogForm, refundLogDTO);
        DubboResult result = refundLogService.updateRefundLog(refundLogDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    @ApiOperation(value = "删除信息")
    @ApiImplicitParam(name = "id", value = "主键id")
    public ApiResponse removeRefundLog(@PathVariable Long id) {
        DubboResult result = refundLogService.deleteRefundLog(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    @ApiOperation(value = "获取信息", notes = "根据ID获取信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "query", example = "1")})
    public ApiResponse<EsRefundLogVO> searchRefundLog(@PathVariable Long id) {
        DubboResult result = refundLogService.getRefundLog(id);
        if (result.isSuccess()) {
            EsRefundLogVO refundLogVO = new EsRefundLogVO();
            BeanUtil.copyProperties(result.getData(), refundLogVO);
            return ApiResponse.success(refundLogVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "获取列表", notes = "根据form表单数据查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refundLogQueryForm", value = "查询Form表单"),
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    public ApiResponse<EsRefundLogVO> searchRefundLogList(@Valid EsRefundLogQueryForm refundLogQueryForm,
                                                          @NotEmpty(message = "页数不能为空") int pageSize,
                                                          @NotEmpty(message = "页码不能为空") int pageNum) {
        EsRefundLogDTO refundLogDTO = new EsRefundLogDTO();
        BeanUtil.copyProperties(refundLogQueryForm, refundLogDTO);
        DubboPageResult result = refundLogService.getRefundLogList(refundLogDTO, pageSize, pageNum);
        List<EsRefundLogVO> refundLogVOList = new ArrayList<>();
        if (result.isSuccess()) {
            List<EsRefundLogDO> refundLogDOList = result.getData().getList();
            if (CollectionUtils.isNotEmpty(refundLogDOList)) {
                refundLogVOList = refundLogDOList.stream().map(refundLogDO -> {
                    EsRefundLogVO refundLogVO = new EsRefundLogVO();
                    BeanUtil.copyProperties(refundLogDO, refundLogVO);
                    return refundLogVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(refundLogVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
*/
