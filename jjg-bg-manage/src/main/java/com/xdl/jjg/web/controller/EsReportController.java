package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsHandleReportForm;
import com.xdl.jjg.model.form.EsReportQueryForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
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
 * 前端控制器-会员举报信息管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esReport",tags = "会员举报信息管理")
@RestController
@RequestMapping("/esReport")
public class EsReportController {

    @Autowired
    private IEsReportService reportService;

    @ApiOperation(value = "分页查询举报信息",response = EsReportVO.class)
    @GetMapping(value = "/getReportList")
    @ResponseBody
    public ApiResponse getReportList(EsReportQueryForm form) {
        ReportQueryParam dto = new ReportQueryParam();
        BeanUtil.copyProperties(form,dto);
        DubboPageResult<EsReportDO> result = reportService.getReportList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsReportDO> data = result.getData().getList();
            List<EsReportVO> voList = BeanUtil.copyList(data, EsReportVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ApiOperation(value = "删除或批量删除")
    @ApiImplicitParam(name = "ids", value = "id数组", required = true, dataType = "int",example = "1", paramType = "path",allowMultiple = true)
    @ResponseBody
    public ApiResponse batchDel(@PathVariable Integer[] ids){
        DubboResult result = reportService.deleteReport(ids);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "处理举报")
    @PutMapping(value = "/handleReport")
    @ResponseBody
    public ApiResponse handleReport(@Valid @RequestBody @ApiParam(name="处理举报form对象",value="form") EsHandleReportForm form){
        String state = null;
        if (form.getOperationStatus() == 1){
            state = ReportEnum.APPLYING.value();
        }else if (form.getOperationStatus() == 2){
            state = ReportEnum.APPLYED.value();
        }
        DubboResult result = reportService.dealReport(form.getId(),state,form.getHandleContent());
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
