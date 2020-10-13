package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsDeliveryServiceDO;
import com.xdl.jjg.model.dto.EsDeliveryServiceDTO;
import com.xdl.jjg.model.form.EsDeliveryServiceForm;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.vo.EsDeliveryServiceVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsCompanyService;
import com.xdl.jjg.web.service.IEsDeliveryServiceService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器-自提点信息维护
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-06 15:09:47
 */
@Api(value = "/esDeliveryService", tags = "自提点信息维护")
@RestController
@RequestMapping("/esDeliveryService")
public class EsDeliveryServiceController {

    @Autowired
    private IEsDeliveryServiceService deliveryServiceService;

    @Autowired
    private IEsCompanyService companyService;

    @ApiOperation(value = "新增自提点")
    @PostMapping(value = "/insertDeliveryService")
    @ResponseBody
    public ApiResponse insertDeliveryService(@Valid @RequestBody @ApiParam(name = "自提点form对象", value = "form") EsDeliveryServiceForm form) {
        EsDeliveryServiceDTO deliveryServiceDTO = new EsDeliveryServiceDTO();
        BeanUtil.copyProperties(form, deliveryServiceDTO);
        String companyName = companyService.getCompany(form.getCompanyId()).getData().getCompanyName();
        String companyCode = companyService.getCompany(form.getCompanyId()).getData().getCompanyCode();
        deliveryServiceDTO.setCompanyName(companyName);
        deliveryServiceDTO.setCompanyCode(companyCode);
        DubboResult result = deliveryServiceService.insertDeliveryService(deliveryServiceDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除自提点")
    @DeleteMapping(value = "deleteDeliveryService/{id}")
    @ApiImplicitParam(name = "id", value = "自提点id", required = true, dataType = "long", example = "1", paramType = "path")
    @ResponseBody
    public ApiResponse deleteDeliveryService(@PathVariable Long id) {
        DubboResult result = deliveryServiceService.deleteDeliveryService(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改自提点")
    @PutMapping(value = "/updateDeliveryService/{id}")
    @ResponseBody
    public ApiResponse updateDeliveryService(@Valid @RequestBody @ApiParam(name = "自提点form对象", value = "form") EsDeliveryServiceForm form, @PathVariable Long id) {
        EsDeliveryServiceDTO deliveryServiceDTO = new EsDeliveryServiceDTO();
        BeanUtil.copyProperties(form, deliveryServiceDTO);
        String companyName = companyService.getCompany(form.getCompanyId()).getData().getCompanyName();
        String companyCode = companyService.getCompany(form.getCompanyId()).getData().getCompanyCode();
        deliveryServiceDTO.setCompanyName(companyName);
        deliveryServiceDTO.setCompanyCode(companyCode);
        DubboResult result = deliveryServiceService.updateDeliveryService(deliveryServiceDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询自提点列表", response = EsDeliveryServiceVO.class)
    @GetMapping(value = "/getDeliveryServiceList")
    @ResponseBody
    public ApiResponse getDeliveryServiceList(EsQueryPageForm form) {
        EsDeliveryServiceDTO deliveryServiceDTO = new EsDeliveryServiceDTO();
        DubboPageResult<EsDeliveryServiceDO> result = deliveryServiceService.getDeliveryServiceList(deliveryServiceDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsDeliveryServiceDO> data = result.getData().getList();
            List<EsDeliveryServiceVO> esDeliveryServiceVOList = BeanUtil.copyList(data, EsDeliveryServiceVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esDeliveryServiceVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据自提点id获取自提信息详情", response = EsDeliveryServiceVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "自提点id", required = true, dataType = "long", paramType = "path")})
    @GetMapping(value = "/getDeliveryService/{id}")
    @ResponseBody
    public ApiResponse getDeliveryService(@PathVariable Long id) {
        DubboResult<EsDeliveryServiceDO> result = deliveryServiceService.getDeliveryService(id);
        if (result.isSuccess()) {
            EsDeliveryServiceDO data = result.getData();
            EsDeliveryServiceVO esDeliveryServiceVO = new EsDeliveryServiceVO();
            BeanUtil.copyProperties(data, esDeliveryServiceVO);
            return ApiResponse.success(esDeliveryServiceVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}

