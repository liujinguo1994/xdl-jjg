package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsComplaintForm;
import com.xdl.jjg.model.form.EsHandleComplaintForm;
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
 * 前端控制器-会员投诉信息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esComplaint", tags = "会员投诉信息")
@RestController
@RequestMapping("/esComplaint")
public class EsComplaintController {

    @Autowired
    private IEsComplaintService complaintService;

    @ApiOperation(value = "分页查询投诉信息", response = EsComplaintVO.class)
    @GetMapping(value = "/getComplaintList")
    @ResponseBody
    public ApiResponse getComplaintList(EsComplaintForm form) {
        ComplaintQueryParam dto = new ComplaintQueryParam();
        BeanUtil.copyProperties(form, dto);
        dto.setOrderSn(form.getKeyword());
        dto.setShopName(form.getKeyword());
        DubboPageResult<EsComplaintDO> result = complaintService.getComplaintListPage(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsComplaintDO> data = result.getData().getList();
            List<EsComplaintVO> voList = BeanUtil.copyList(data, EsComplaintVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ApiOperation(value = "删除或批量删除")
    @ApiImplicitParam(name = "ids", value = "id数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    @ResponseBody
    public ApiResponse batchDel(@PathVariable Integer[] ids) {
        DubboResult result = complaintService.deleteComplaint(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "处理投诉")
    @PutMapping(value = "/handleComplaint")
    @ResponseBody
    public ApiResponse handleComplaint(@Valid @RequestBody @ApiParam(name = "处理投诉form对象", value = "form") EsHandleComplaintForm form) {
        String state = null;
        if (form.getOperationStatus() == 1) {
            state = ComplaintEnumType.APPLYING.value();
        } else if (form.getOperationStatus() == 2) {
            state = ComplaintEnumType.APPLYED.value();
        }
        DubboResult result = complaintService.dealCompla(form.getId(), state, form.getHandleContent());
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
