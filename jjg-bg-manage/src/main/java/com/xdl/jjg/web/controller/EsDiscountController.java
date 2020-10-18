package com.xdl.jjg.web.controller;

import com.jjg.member.model.domain.EsDiscountDO;
import com.jjg.member.model.dto.EsDiscountDTO;
import com.jjg.member.model.vo.EsDiscountVO;
import com.jjg.system.model.form.EsDiscountForm;
import com.jjg.system.model.form.EsDiscountQueryForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.member.DiscountService;
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
 * 前端控制器-公司折扣
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esDiscount")
@Api(value = "/esDiscount", tags = "公司折扣")
public class EsDiscountController {

    @Autowired
    private DiscountService iesDiscountService;


    @ApiOperation(value = "分页查询公司折扣", response = EsDiscountVO.class)
    @GetMapping(value = "/getDiscountList")
    @ResponseBody
    public ApiResponse getDiscountList(@Valid EsDiscountQueryForm form) {
        EsDiscountDTO esDiscountDTO = new EsDiscountDTO();
        BeanUtil.copyProperties(form, esDiscountDTO);
        DubboPageResult<EsDiscountDO> result = iesDiscountService.getDiscountList(esDiscountDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsDiscountDO> data = result.getData().getList();
            List<EsDiscountVO> esDiscountVOList = BeanUtil.copyList(data, EsDiscountVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esDiscountVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加公司折扣")
    @PostMapping(value = "/insertCompany")
    @ResponseBody
    public ApiResponse insertCompany(@Valid @RequestBody @ApiParam(name = "公司折扣form对象", value = "form") EsDiscountForm form) {
        EsDiscountDTO esDiscountDTO = new EsDiscountDTO();
        BeanUtil.copyProperties(form, esDiscountDTO);
        DubboResult result = iesDiscountService.insertDiscount(esDiscountDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改公司折扣")
    @PutMapping(value = "/updateDiscount")
    @ResponseBody
    public ApiResponse updateDiscount(@Valid @RequestBody @ApiParam(name = "公司折扣form对象", value = "form") EsDiscountForm form) {
        EsDiscountDTO esDiscountDTO = new EsDiscountDTO();
        BeanUtil.copyProperties(form, esDiscountDTO);
        DubboResult result = iesDiscountService.updateDiscount(esDiscountDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ResponseBody
    @ApiOperation(value = "删除或批量删除公司折扣")
    @ApiImplicitParam(name = "ids", value = "公司折扣id数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    public ApiResponse batchDel(@PathVariable Integer[] ids) {
        DubboResult result = iesDiscountService.deleteDiscount(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

