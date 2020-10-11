package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsSupplierForm;
import com.xdl.jjg.model.form.EsSupplierQueryForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsRegionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器-供应商
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esSupplier")
@Api(value="/esSupplier", tags="供应商")
public class EsSupplierController {

    @Autowired
    private IEsSupplierService iEsSupplierService;

    @Autowired
    private IEsRegionsService iEsRegionsService;

    @ApiOperation(value = "分页查询供应商列表",response = EsSupplierVO.class)
    @GetMapping(value = "/getSupplierList")
    @ResponseBody
    public ApiResponse getSupplierList(EsSupplierQueryForm form) {
        EsSupplierDTO esSupplierDTO = new EsSupplierDTO();
        esSupplierDTO.setKeyword(form.getKeyword());
        DubboPageResult<EsSupplierDO> result = iEsSupplierService.getSupplierList(esSupplierDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsSupplierDO> data = result.getData().getList();
            List<EsSupplierVO> esSupplierVOList = BeanUtil.copyList(data, EsSupplierVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),esSupplierVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加供应商")
    @PostMapping(value = "/insertSupplier")
    @ResponseBody
    public ApiResponse insertSupplier(@RequestBody @ApiParam(name="供应商form对象",value="form") EsSupplierForm form){
        EsSupplierDTO esSupplierDTO = new EsSupplierDTO();
        BeanUtil.copyProperties(form, esSupplierDTO);
        esSupplierDTO.setProvince(iEsRegionsService.getRegions(form.getProvinceId()).getData().getLocalName());
        esSupplierDTO.setCity(iEsRegionsService.getRegions(form.getCityId()).getData().getLocalName());
        esSupplierDTO.setCounty(iEsRegionsService.getRegions(form.getCountyId()).getData().getLocalName());
        DubboResult result = iEsSupplierService.insertSupplier(esSupplierDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改供应商")
    @PutMapping(value = "/updateSupplier/{id}")
    @ResponseBody
    public ApiResponse updateSupplier(@RequestBody @ApiParam(name="供应商form对象",value="form") EsSupplierForm form, @PathVariable Long id){
        EsSupplierDTO esSupplierDTO = new EsSupplierDTO();
        BeanUtil.copyProperties(form, esSupplierDTO);
        esSupplierDTO.setProvince(iEsRegionsService.getRegions(form.getProvinceId()).getData().getLocalName());
        esSupplierDTO.setCity(iEsRegionsService.getRegions(form.getCityId()).getData().getLocalName());
        esSupplierDTO.setCounty(iEsRegionsService.getRegions(form.getCountyId()).getData().getLocalName());
        DubboResult result = iEsSupplierService.updateSupplier(esSupplierDTO,id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ResponseBody
    @ApiOperation(value = "删除或批量删除")
    @ApiImplicitParam(name = "ids", value = "供应商id数组", required = true, dataType = "int",example = "1", paramType = "path",allowMultiple = true)
    public ApiResponse batchDel(@PathVariable Integer[] ids){
        DubboResult result = iEsSupplierService.deleteSupplier(ids);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据id查询供应商信息",response = EsSupplierVO.class)
    @GetMapping(value = "/getSupplier/{id}")
    @ApiImplicitParam(name = "id", value = "供应商id", required = true, dataType = "long",example = "1", paramType = "path")
    @ResponseBody
    public ApiResponse getSupplier(@PathVariable Long id) {
        DubboResult<EsSupplierDO> result = iEsSupplierService.getSupplier(id);
        if (result.isSuccess()) {
            EsSupplierDO data = result.getData();
            EsSupplierVO esSupplierVO = new EsSupplierVO();
            BeanUtil.copyProperties(data,esSupplierVO);
            return ApiResponse.success(esSupplierVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/revert/{ids}")
    @ResponseBody
    @ApiOperation(value = "启用供应商")
    @ApiImplicitParam(name = "ids", value = "供应商id数组", required = true, dataType = "int",example = "1", paramType = "path",allowMultiple = true)
    public ApiResponse revert(@PathVariable Integer[] ids){
        DubboResult result = iEsSupplierService.revertSupplier(ids);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/prohibitSupplier/{ids}")
    @ResponseBody
    @ApiOperation(value = "禁用供应商")
    @ApiImplicitParam(name = "ids", value = "供应商id数组", required = true, dataType = "int",example = "1", paramType = "path",allowMultiple = true)
    public ApiResponse prohibitSupplier(@PathVariable Integer[] ids){
        DubboResult result = iEsSupplierService.prohibitSupplier(ids);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
