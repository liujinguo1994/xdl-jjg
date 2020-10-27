package com.xdl.jjg.web.controller.pc.shop;

import com.jjg.system.model.domain.EsAgreementDO;
import com.jjg.system.model.vo.EsAgreementVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.web.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsAgreementService iEsAgreementService;

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
