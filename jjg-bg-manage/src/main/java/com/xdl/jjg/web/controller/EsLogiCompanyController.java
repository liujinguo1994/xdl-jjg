package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsLogiCompanyDO;
import com.xdl.jjg.model.dto.EsLogiCompanyDTO;
import com.xdl.jjg.model.form.EsLogiCompanyForm;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.vo.EsLogiCompanyVO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsLogiCompanyService;
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
 * 前端控制器-物流公司
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esLogiCompany")
@Api(value = "/esLogiCompany", tags = "物流公司")
public class EsLogiCompanyController {

    @Autowired
    private IEsLogiCompanyService iEsLogiCompanyService;

    @ApiOperation(value = "分页查询物流公司列表", response = EsLogiCompanyVO.class)
    @GetMapping(value = "/getLogiCompanyList")
    @ResponseBody
    public ApiResponse getLogiCompanyList(EsQueryPageForm form) {
        EsLogiCompanyDTO esLogiCompanyDTO = new EsLogiCompanyDTO();
        DubboPageResult<EsLogiCompanyDO> result = iEsLogiCompanyService.getLogiCompanyList(esLogiCompanyDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsLogiCompanyDO> data = result.getData().getList();
            List<EsLogiCompanyVO> esLogiCompanyVOList = BeanUtil.copyList(data, EsLogiCompanyVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esLogiCompanyVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加物流公司")
    @PostMapping(value = "/insertLogiCompany")
    @ResponseBody
    public ApiResponse insertLogiCompany(@Valid @RequestBody @ApiParam(name = "物流公司form对象", value = "form") EsLogiCompanyForm form) {
        EsLogiCompanyDTO esLogiCompanyDTO = new EsLogiCompanyDTO();
        BeanUtil.copyProperties(form, esLogiCompanyDTO);
        DubboResult result = iEsLogiCompanyService.insertLogiCompany(esLogiCompanyDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改物流公司")
    @PutMapping(value = "/updateLogiCompany")
    @ResponseBody
    public ApiResponse updateSupplier(@Valid @RequestBody @ApiParam(name = "物流公司form对象", value = "form") EsLogiCompanyForm form) {
        EsLogiCompanyDTO esLogiCompanyDTO = new EsLogiCompanyDTO();
        BeanUtil.copyProperties(form, esLogiCompanyDTO);
        DubboResult result = iEsLogiCompanyService.updateLogiCompany(esLogiCompanyDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteLogiCompany/{id}")
    @ResponseBody
    @ApiOperation(value = "删除物流公司")
    @ApiImplicitParam(name = "id", value = "物流公司id", required = true, dataType = "long", example = "1", paramType = "path")
    public ApiResponse deleteLogiCompany(@PathVariable Long id) {
        DubboResult result = iEsLogiCompanyService.deleteLogiCompany(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
