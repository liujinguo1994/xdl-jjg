package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsZoneDO;
import com.xdl.jjg.model.dto.EsZoneDTO;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.form.EsZoneForm;
import com.xdl.jjg.model.vo.EsZoneVO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsZoneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 专区管理 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Api(value = "/esZone",tags = "专区管理")
@RestController
@RequestMapping("/esZone")
public class EsZoneController {

    @Autowired
    private IEsZoneService iesZoneService;


    /*@ApiOperation(value = "添加")
    @PostMapping(value = "/insertEsZone")
    @ResponseBody
    public ApiResponse insertEsZone(@RequestBody @ApiParam(name="专区form对象",value="form") EsZoneForm form){
        EsZoneDTO dto = new EsZoneDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult result = iesZoneService.insertZone(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }*/

    @ApiOperation(value = "修改")
    @PutMapping(value = "/editEsZone")
    @ResponseBody
    public ApiResponse editEsZone(@RequestBody @ApiParam(name="专区form对象",value="form") @Valid EsZoneForm form){
        EsZoneDTO dto = new EsZoneDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult result = iesZoneService.updateZone(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询列表",response = EsZoneVO.class)
    @GetMapping(value = "/getZoneList")
    @ResponseBody
    public ApiResponse getZoneList(EsQueryPageForm form) {
        EsZoneDTO dto = new EsZoneDTO();
        DubboPageResult<EsZoneDO> result = iesZoneService.getZoneList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsZoneDO> data = result.getData().getList();
            List<EsZoneVO> voList = BeanUtil.copyList(data, EsZoneVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

