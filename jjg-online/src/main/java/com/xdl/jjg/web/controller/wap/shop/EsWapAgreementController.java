package com.xdl.jjg.web.controller.wap.shop;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.system.api.model.domain.EsAgreementDO;
import com.shopx.system.api.model.domain.vo.EsAgreementVO;
import com.shopx.system.api.service.IEsAgreementService;
import com.shopx.trade.web.constant.ApiStatus;
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
@RequestMapping("/esWapAgreement")
@Api(value="/esWapAgreement", tags="注册协议")
public class EsWapAgreementController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsAgreementService iEsAgreementService;

    @GetMapping(value = "/getWapEsAgreement")
    @ApiOperation(value = "获取注册协议",response = EsAgreementVO.class)
    @ResponseBody
    public ApiResponse getWapEsAgreement() {
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
