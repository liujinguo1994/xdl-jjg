package com.xdl.jjg.web.controller.applet.trade;

import com.alibaba.fastjson.JSONObject;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.MathUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.vo.FormVO;
import com.shopx.trade.api.model.domain.vo.PriceDetailVO;
import com.shopx.trade.api.model.enums.ClientType;
import com.shopx.trade.api.model.enums.TradeType;
import com.shopx.trade.api.utils.WeChatPayUtil;
import com.shopx.trade.web.manager.OrderPayManager;
import com.shopx.trade.web.manager.TradePriceManager;
import com.shopx.trade.web.request.EsAppletPayForm;
import com.shopx.trade.web.request.EsAppletSetBalanceForm;
import com.shopx.trade.web.request.PayParamForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.oas.annotations.responses.ApiResponse;
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
 *  前端控制器-小程序-支付接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-10
 */
@RestController
@Api(value = "/applet/trade/pay",tags = "小程序-支付接口")
@RequestMapping("/applet/trade/pay")
public class EsAppletPayController {
    private static Logger logger = LoggerFactory.getLogger(EsAppletPayController.class);

    @Autowired
    private OrderPayManager orderPayManager;
    @Autowired
    private TradePriceManager tradePriceManager;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;


    @ApiOperation(value = "发起支付")
    @PostMapping(value = "/payTrade")
    @ResponseBody
    public ApiResponse payTrade(@RequestBody @Valid EsAppletPayForm form, HttpServletRequest request) {
        try {
            logger.info("***************支付开始，参数:" + JSONObject.toJSONString(form));
            //获取openid
            DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
            if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
                return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
            }
            EsMemberDO memberDO = dubboResult.getData();
            String openid = memberDO.getOpenid();
            PayParamForm payParamForm = new PayParamForm();
            BeanUtil.copyProperties(form,payParamForm);
            payParamForm.setOpenid(openid);
            FormVO vo = orderPayManager.pay(payParamForm, request);
            logger.info("***************支付返回信息:" + JSONObject.toJSONString(vo));
            return ApiResponse.success(vo);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "异步回调")
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
    @PostMapping(value = "/balance")
    @ResponseBody
    public ApiResponse setBalance(@RequestBody @Valid EsAppletSetBalanceForm form) {
        Double needPayMoney;
        PriceDetailVO priceDetailVO = tradePriceManager.getTradePrice(form.getSkey());
        needPayMoney = MathUtil.subtract(priceDetailVO.getTotalPrice(), form.getBalance());
        priceDetailVO.setBalance(form.getBalance());
        if (needPayMoney < 0 ) {
            return ApiResponse.fail(TradeErrorCode.OVERPAYMENT_OF_BALANCE.getErrorCode(),
                    TradeErrorCode.OVERPAYMENT_OF_BALANCE.getErrorMsg());
        }
        priceDetailVO.setNeedPayMoney(needPayMoney);
        tradePriceManager.pushPrice(priceDetailVO,form.getSkey());
        return ApiResponse.success(priceDetailVO);
    }
}
