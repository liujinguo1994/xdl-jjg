package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsCompanyForm;
import com.xdl.jjg.model.form.EsCompanyQueryForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器-签约公司
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esCompany",tags = "签约公司")
@RestController
@RequestMapping("/esCompany")
public class EsCompanyController {
    private static Logger logger = LoggerFactory.getLogger(EsCompanyController.class);

    @Autowired
    private IEsCompanyService iesCompanyService;

    @ApiOperation(value = "分页查询签约公司",response = EsCompanyVO.class)
    @GetMapping(value = "/getCompanyList")
    @ResponseBody
    public ApiResponse getCompanyList(EsCompanyQueryForm form) {
        EsCompanyDTO esCompanyDTO = new EsCompanyDTO();
        esCompanyDTO.setCompanyCode(form.getKeyword());
        esCompanyDTO.setCompanyName(form.getKeyword());
        DubboPageResult<EsCompanyDO> result = iesCompanyService.getCompanyList(esCompanyDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsCompanyDO> data = result.getData().getList();
            List<EsCompanyVO> esCompanyVOList = BeanUtil.copyList(data, EsCompanyVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),esCompanyVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加签约公司")
    @PostMapping(value = "/insertCompany")
    @ResponseBody
    public ApiResponse insertCompany(@Valid @RequestBody @ApiParam(name="签约公司form对象",value="form") EsCompanyForm form){
        EsCompanyDTO esCompanyDTO = new EsCompanyDTO();
        BeanUtil.copyProperties(form, esCompanyDTO);
        DubboResult result = iesCompanyService.insertCompany(esCompanyDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改签约公司")
    @PutMapping(value = "/updateCompany")
    @ResponseBody
    public ApiResponse updateCompany(@Valid @RequestBody @ApiParam(name="签约公司form对象",value="form") EsCompanyForm form){
        EsCompanyDTO esCompanyDTO = new EsCompanyDTO();
        BeanUtil.copyProperties(form, esCompanyDTO);
        DubboResult result = iesCompanyService.updateCompany(esCompanyDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ResponseBody
    @ApiOperation(value = "删除或批量删除签约公司")
    @ApiImplicitParam(name = "ids", value = "签约公司id数组", required = true, dataType = "int",example = "1", paramType = "path",allowMultiple = true)
    public ApiResponse batchDel(@PathVariable Integer[] ids){
        DubboResult result = iesCompanyService.deleteCompany(ids);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

