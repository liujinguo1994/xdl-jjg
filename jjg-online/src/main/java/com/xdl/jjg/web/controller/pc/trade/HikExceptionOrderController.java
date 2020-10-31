package com.xdl.jjg.web.controller.pc.trade;


import com.jjg.trade.model.dto.EsHikExceptionOrderDTO;
import com.jjg.trade.model.form.query.EsHikExceptionOrderForm;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.web.service.IEsHikExceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: ExceptionOrderController
 * @Description: 异常订单控制器
 * @Author: libw
 * @Date: 5/22/2019 19:10
 * @Version: 1.0
 */
@RestController
@Validated
@Api(description = "异常订单管理API")
@RequestMapping("/order/trade/exceptionOrder")
public class HikExceptionOrderController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsHikExceptionService hikExceptionService;

    @PostMapping
    @ApiOperation(value = "添加异常订单信息")
    public ApiResponse add(EsHikExceptionOrderForm hikExceptionOrderForm) {
        EsHikExceptionOrderDTO hikExceptionOrderDTO=new EsHikExceptionOrderDTO();
        BeanUtils.copyProperties(hikExceptionOrderForm,hikExceptionOrderDTO);
        hikExceptionOrderDTO.setCreateTime(System.currentTimeMillis());

        DubboResult result=this.hikExceptionService.insertHikException(hikExceptionOrderDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(TradeErrorCode.HIK_PAY_EOORE_BACK.getErrorCode(), TradeErrorCode.HIK_PAY_EOORE_BACK.getErrorMsg());
    }
}
