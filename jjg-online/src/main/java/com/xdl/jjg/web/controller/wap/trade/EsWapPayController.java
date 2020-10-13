package com.xdl.jjg.web.controller.wap.trade;

import com.alibaba.fastjson.JSONObject;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.MathUtil;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.dto.SynPaymentParameter;
import com.shopx.trade.api.model.domain.vo.FormVO;
import com.shopx.trade.api.model.domain.vo.PriceDetailVO;
import com.shopx.trade.api.model.enums.ClientType;
import com.shopx.trade.api.model.enums.TradeType;
import com.shopx.trade.api.service.IEsTradeService;
import com.shopx.trade.api.utils.WeChatPayUtil;
import com.shopx.trade.web.manager.OrderPayManager;
import com.shopx.trade.web.manager.TradePriceManager;
import com.shopx.trade.web.request.EsPayForm;
import com.shopx.trade.web.request.PayParamForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 *  前端控制器-移动端-支付接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-20
 */
@RestController
@Api(value = "/wap/trade/pay",tags = "移动端-支付接口")
@RequestMapping("/wap/trade/pay")
public class EsWapPayController {
    private static Logger logger = LoggerFactory.getLogger(EsWapPayController.class);

    @Autowired
    private OrderPayManager orderPayManager;

    @Autowired
    private TradePriceManager tradePriceManager;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsTradeService tradeService;

    @ApiOperation(value = "支付宝，微信发起支付")
    @PostMapping(value = "/payTrade")
    @ResponseBody
    public ApiResponse payTrade(@RequestBody @Valid EsPayForm form, HttpServletRequest request) {
        try {
            logger.info("***************支付开始，参数:" + JSONObject.toJSONString(form));
            PayParamForm payParamForm = new PayParamForm();
            BeanUtil.copyProperties(form,payParamForm);
            FormVO formVO = orderPayManager.pay(payParamForm, request);
            logger.info("***************支付返回信息:" + JSONObject.toJSONString(formVO));
            return ApiResponse.success(formVO);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }


    @ApiOperation(value = "支付宝异步回调")
    @PostMapping(value = "/callback/{trade_type}/{plugin_id}/{client_type}")
    public String payCallback(@PathVariable(name = "trade_type") String tradeType, @PathVariable(name = "plugin_id") String paymentPluginId,
                              @PathVariable(name = "client_type") String clientType, HttpServletRequest request){
        SynPaymentParameter parameter = new SynPaymentParameter();
        parameter.setOutTradeNo(request.getParameter("out_trade_no"));
        parameter.setTradeNo(request.getParameter("trade_no"));
        parameter.setTotalAmount(request.getParameter("total_amount"));
        parameter.setTradeStatus(request.getParameter("trade_status"));
        parameter.setRequestParams(request.getParameterMap());
        logger.info("***************接收支付异步回调信息:" + JSONObject.toJSONString(parameter));
        String result = orderPayManager.payCallback(TradeType.valueOf(tradeType), paymentPluginId, ClientType.valueOf(clientType),parameter);
        logger.info("***************异步回调返回信息:" + result);
        return result;
    }

    @ApiOperation(value = "微信异步回调")
    @PostMapping(value = "/wxCallback/{trade_type}/{plugin_id}/{client_type}")
    public String wxCallback(@PathVariable(name = "trade_type") String tradeType, @PathVariable(name = "plugin_id") String paymentPluginId,
                             @PathVariable(name = "client_type") String clientType, HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(request.getInputStream());
            params = WeChatPayUtil.xmlToMap(document);
            logger.info("异步回调params信息======"+params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = orderPayManager.wxPayCallback(TradeType.valueOf(tradeType), paymentPluginId, ClientType.valueOf(clientType), params);
        logger.info("异步回调result信息======"+result);
        return result;
    }

    @ApiOperation(value = "设置余额", response = PriceDetailVO.class)
    @ResponseBody
    @PostMapping(value = "/balance")
    public ApiResponse setBalance(Double balance) {
        Double needPayMoney;
        PriceDetailVO priceDetailVO = tradePriceManager.getTradePrice(null);
        needPayMoney = MathUtil.subtract(priceDetailVO.getTotalPrice(), balance);
        priceDetailVO.setBalance(balance);
        if (needPayMoney < 0 ) {
            return ApiResponse.fail(TradeErrorCode.OVERPAYMENT_OF_BALANCE.getErrorCode(),
                    TradeErrorCode.OVERPAYMENT_OF_BALANCE.getErrorMsg());
        }

        priceDetailVO.setNeedPayMoney(needPayMoney);
        tradePriceManager.pushPrice(priceDetailVO,null);
        return ApiResponse.success(priceDetailVO);
    }


}
