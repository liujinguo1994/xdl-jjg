package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsAdvertisingDO;
import com.xdl.jjg.model.dto.EsAdvertisingDTO;
import com.xdl.jjg.model.form.EsAdvertisingForm;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.vo.EsAdvertisingVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 广告位 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Api(value = "/esAdvertising",tags = "广告位")
@RestController
@RequestMapping("/esAdvertising")
public class EsAdvertisingController {

    @Autowired
    private IEsAdvertisingService iesAdvertisingService;

    @ApiOperation(value = "添加")
    @PostMapping(value = "/insertEsAdvertising")
    @ResponseBody
    public ApiResponse insertEsAdvertising(@RequestBody @ApiParam(name="广告位form对象",value="form") @Valid EsAdvertisingForm form){
        EsAdvertisingDTO dto = new EsAdvertisingDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult result = iesAdvertisingService.insertAdvertising(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/editEsAdvertising")
    @ResponseBody
    public ApiResponse editEsAdvertising(@RequestBody @ApiParam(name="广告位form对象",value="form") @Valid EsAdvertisingForm form){
        EsAdvertisingDTO dto = new EsAdvertisingDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult result = iesAdvertisingService.updateAdvertising(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "path",example = "1")
    })
    @DeleteMapping(value = "/deleteEsAdvertising/{id}")
    @ResponseBody
    public ApiResponse deleteEsAdvertising(@PathVariable Long id) {
        DubboResult result = iesAdvertisingService.deleteAdvertising(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询列表",response = EsAdvertisingVO.class)
    @GetMapping(value = "/getEsAdvertising")
    @ResponseBody
    public ApiResponse getEsAdvertising(EsQueryPageForm form) {
        EsAdvertisingDTO dto = new EsAdvertisingDTO();
        DubboPageResult<EsAdvertisingDO> result = iesAdvertisingService.getAdvertisingList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsAdvertisingDO> data = result.getData().getList();
            List<EsAdvertisingVO> voList = BeanUtil.copyList(data, EsAdvertisingVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

