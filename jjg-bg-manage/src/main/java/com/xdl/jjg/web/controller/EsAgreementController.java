package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsAgreementDO;
import com.xdl.jjg.model.dto.EsAgreementDTO;
import com.xdl.jjg.model.form.EsAgreementForm;
import com.xdl.jjg.model.vo.EsAgreementVO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器-注册协议
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esAgreement")
@Api(value="/esAgreement", tags="注册协议")
public class EsAgreementController {

    @Autowired
    private IEsAgreementService iEsAgreementService;


    @PutMapping(value = "/updateEsAgreement")
    @ApiOperation(value = "更新注册协议")
    @ResponseBody
    public ApiResponse updateEsAgreement(@Valid @RequestBody @ApiParam(name="注册协议form对象",value="form") EsAgreementForm form) {
        EsAgreementDTO esAgreementDTO = new EsAgreementDTO();
        BeanUtil.copyProperties(form,esAgreementDTO);
        DubboResult result = iEsAgreementService.updateAgreement(esAgreementDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getEsAgreement")
    @ApiOperation(value = "获取注册协议",response = EsAgreementVO.class)
    @ResponseBody
    public ApiResponse getEsAgreement() {
        DubboResult<EsAgreementDO> result = iEsAgreementService.getEsAgreement();
        if (result.isSuccess()) {
            EsAgreementDO data = result.getData();
            EsAgreementVO esAgreementVO = new EsAgreementVO();
            BeanUtil.copyProperties(data,esAgreementVO);
            return ApiResponse.success(esAgreementVO);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
