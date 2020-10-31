package com.xdl.jjg.web.controller.pc;


import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.dto.SynPaymentParameter;
import com.jjg.trade.model.enums.ClientType;
import com.jjg.trade.model.enums.PayMode;
import com.jjg.trade.model.enums.PayStatusEnum;
import com.jjg.trade.model.enums.TradeType;
import com.jjg.trade.model.form.PayParamForm;
import com.jjg.trade.model.vo.FormVO;
import com.jjg.trade.model.vo.PayBillVO;
import com.xdl.jjg.manager.DomainSettings;
import com.xdl.jjg.manager.OrderPayManager;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.utils.WeChatPayUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsOrderService;
import com.xdl.jjg.web.service.IEsPaymentMethodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description: 订单支付
 * @date 2018/4/1616:44
 * @since v7.0.0
 */
@Api(value = "/order/pay", description = "订单支付API")
@RestController
@RequestMapping("/order/pay")
@Validated
public class OrderPayBuyerController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderService orderService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsPaymentMethodService paymentMethodService;

    @Value("${server.address.serverName}")
    private String serverName;

    @Autowired
    private DomainSettings domainSettings;

    @Autowired
    private OrderPayManager orderPayManager;

    @ApiOperation(value = "订单检查 是否需要支付 为false代表不需要支付，出现支付金额为0，或者已经支付，为true代表需要支付")
    @GetMapping(value = "/need_pay/{sn}")
    public ApiResponse check(@PathVariable(name = "sn") String sn) {
        DubboResult result = orderService.getEsBuyerOrderInfo(sn);
        if (!result.isSuccess()) {
            return ApiResponse.fail(result.getCode(), result.getMsg());
        }
        EsOrderDO order = (EsOrderDO) result.getData();

        if(order.getPayState().equals(PayStatusEnum.PAY_YES.value())){
            return ApiResponse.success(false);
        }
        return ApiResponse.success(true);
    }



    @ApiOperation(value = "查询支持的支付方式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "client_type", value = "调用客户端PC,WAP,APP", required = true, dataType =
                    "String", paramType = "path", allowableValues = "PC,WAP,APP")
    })
    @GetMapping(value = "/{client_type}")
    public ApiResponse queryPayments(@PathVariable(name = "client_type") String clientType) {
        DubboPageResult result = paymentMethodService.getPaymentMethodListByClient(clientType);
        if (!result.isSuccess()){
            return ApiResponse.fail(result.getCode(), result.getMsg());
        }

        return ApiResponse.success(result.getData());
    }

    @ApiOperation(value = "对一个交易发起支付")
    @PostMapping(value = "/payTrade")
    @ResponseBody
    public ApiResponse payTrade(String sn,
                                String paymentPluginId,
                                String payMode,
                                String clientType,
                                String tradeType,
                                HttpServletRequest request) {
        try {
            PayParamForm param = new PayParamForm();
            param.setSn(sn);
            param.setPaymentPluginId(paymentPluginId);
            param.setTradeType(tradeType);
            param.setPayMode(payMode);
            param.setClientType(clientType);
            FormVO pay = orderPayManager.pay(param, request);
            return ApiResponse.success(pay.getGatewayUrl());
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }


    @ApiOperation(value = "接收支付同步回调")
    @GetMapping(value = "/return/{trade_type}/{pay_mode}/{plugin_id}")
    public String payReturn(@PathVariable(name = "trade_type") String tradeType, @PathVariable(name = "plugin_id") String paymentPluginId,
                            @PathVariable(name = "pay_mode") String payMode, HttpServletRequest request) {

        SynPaymentParameter parameter = new SynPaymentParameter();
        String outTradeNo = request.getParameter("out_trade_no");
        parameter.setOutTradeNo(outTradeNo);
        parameter.setTradeNo(request.getParameter("trade_no"));
        parameter.setTotalAmount(request.getParameter("total_amount"));
        parameter.setRequestParams(request.getParameterMap());
        logger.info("同步回调parameter信息"+parameter);
        DubboResult dubboResult = this.orderPayManager.payReturn(TradeType.valueOf(tradeType), paymentPluginId, parameter);

        String jumpHtml = "<script>";
        // 成功页面
        if (dubboResult.isSuccess()){

            String url = serverName + "/checkout/cashier-status-pay?payStatus=0&sn="+outTradeNo;
            final String httpPortal = "http://";
            if (!url.startsWith(httpPortal)){
                url=httpPortal+url;
            }

            //扫码支付
            if (PayMode.qr.name().equals(payMode)) {
                //二维码模式嵌在的iframe中的，要设置此相应允许被buyer域名的frame嵌套
                jumpHtml += "window.parent.location.href='" + url + "'";
            } else {
                jumpHtml += "location.href='" + url + "'";
            }

            jumpHtml += "</script>";
        }else {
            // 失败页面
                String url = serverName + "/checkout/cashier-status-pay?payStatus=1&sn="+outTradeNo;
                final String httpPortal = "http://";
                if (!url.startsWith(httpPortal)){
                    url=httpPortal+url;
                }
                //扫码支付
                if (PayMode.qr.name().equals(payMode)) {
                    //二维码模式嵌在的iframe中的，要设置此相应允许被buyer域名的frame嵌套
                    jumpHtml += "window.parent.location.href='" + url + "'";
                } else {
                    jumpHtml += "location.href='" + url + "'";
                }
                jumpHtml += "</script>";
        }
        return jumpHtml;
    }


    @ApiOperation(value = "接收支付异步回调")
    @PostMapping(value = "/callback/{trade_type}/{plugin_id}/{client_type}")
    public String payCallback(@PathVariable(name = "trade_type") String tradeType, @PathVariable(name = "plugin_id") String paymentPluginId,
                              @PathVariable(name = "client_type") String clientType, HttpServletRequest request) {
        SynPaymentParameter parameter = new SynPaymentParameter();
        parameter.setOutTradeNo(request.getParameter("out_trade_no"));
        parameter.setTradeNo(request.getParameter("trade_no"));
        parameter.setTotalAmount(request.getParameter("total_amount"));
        parameter.setTradeStatus(request.getParameter("trade_status"));

        parameter.setRequestParams(request.getParameterMap());
        logger.info("异步回调parameter信息"+parameter);
        String result = this.orderPayManager.payCallback(TradeType.valueOf(tradeType), paymentPluginId, ClientType.valueOf(clientType),parameter);

        return result;

    }

    @ApiOperation(value = "微信接收支付异步回调")
    @PostMapping(value = "/wxCallback/{trade_type}/{plugin_id}/{client_type}")
    public String wxCallback(@PathVariable(name = "trade_type") String tradeType, @PathVariable(name = "plugin_id") String paymentPluginId,
                             @PathVariable(name = "client_type") String clientType, HttpServletRequest request) {
        String result = "FAIL";
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(request.getInputStream());
            Map<String, String> params = WeChatPayUtil.xmlToMap(document);
            logger.info("异步回调params信息======"+params);
            result = this.orderPayManager.wxPayCallback(TradeType.valueOf(tradeType), paymentPluginId, ClientType.valueOf(clientType),params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        logger.info("异步回调result信息======"+result);
        return result;
    }

//    @ApiOperation(value = "主动查询支付结果")
//    @GetMapping(value = "/order/pay/query/{sn}")
//    public String query(@PathVariable(name = "trade_type") String tradeType, @Valid PayParam param,
//                        @PathVariable(name = "sn") String sn) {
//
//        param.setSn(sn);
//        param.setTradeType(tradeType);
//
//        String result = this.orderPayManager.queryResult(param);
//
//        return result;
//    }

    @ApiOperation(value = "获取支付的状态")
    @GetMapping("/status/{trade_sn}")
    public ApiResponse payStatus(@PathVariable(name = "trade_sn") String tradeSn,
                                 HttpServletRequest request) {

        PayBillVO bill = new PayBillVO();
        bill.setOutTradeNo(tradeSn);
        bill.setClientType(ClientType.PC);
        bill.setTradeType(TradeType.trade);
        bill.setPayMode(PayMode.qr.name());
        bill.setPaymentPluginId("alipayDirectPlugin");

        String s = orderPayManager.onQuery(bill, request);
        return ApiResponse.success(s);
    }
//
//    @ApiOperation(value = "APP对一个交易发起支付")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "sn", value = "要支付的交易sn", required = true, dataType = "String", paramType = "path"),
//            @ApiImplicitParam(name = "trade_type", value = "交易类型", required = true, dataType = "String", paramType = "path", allowableValues = "trade,order")
//    })
//    @GetMapping(value = "/app/{trade_type}/{sn}")
//    public String appPayTrade(@PathVariable(name = "sn") String sn, @PathVariable(name = "trade_type") String tradeType, @Valid PayParam param) {
//
//        param.setSn(sn);
//        param.setTradeType(tradeType);
//
//        Form form = orderPayManager.pay(param);
//
//        return form.getGatewayUrl();
//    }
}
