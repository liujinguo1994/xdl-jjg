package com.xdl.jjg.web.controller.pc;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.vo.PayBillVO;
import com.shopx.trade.api.model.enums.ClientType;
import com.shopx.trade.api.model.enums.PayMode;
import com.shopx.trade.api.model.enums.TradeType;
import com.shopx.trade.api.service.IPayService;
import com.shopx.trade.web.manager.DomainSettings;
import com.shopx.trade.web.manager.OrderPayManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

/**
 * @author fk
 * @version v2.0
 * @Description: 订单支付
 * @date 2018/4/1616:44
 * @since v7.0.0
 */
@Api(value = "/order/pay/wechat", tags = "微信支付相关")
@RestController
@RequestMapping("/order/pay/wechat")
@Validated
public class WeChatPayBuyerController extends BaseController {

    private final static String HTTP_PROTOCOL = "http://";
    @Autowired
    private OrderPayManager orderPayManager;
    @Autowired
    private DomainSettings domainSettings;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IPayService payService;

    @ApiOperation(value = "微信二维码显示页")
    @GetMapping("/qrpage/{weixin_trade_sn}/{pr}")
    public String qrPage(Model model, @PathVariable(name = "weixin_trade_sn") String weixinTradeSn, @PathVariable(name = "pr") String pr, HttpServletResponse response, HttpServletRequest request) {

        String buyerDomain = domainSettings.getBuyer();
        if (!buyerDomain.startsWith(HTTP_PROTOCOL)) {
            buyerDomain = HTTP_PROTOCOL + buyerDomain;
        }

        buyerDomain = HTTP_PROTOCOL + buyerDomain;
        //获取虚拟目录
        String cxt = request.getContextPath();
        if ("/".equals(cxt)) {
            cxt = "";
        }
        String paySuccessUrl = buyerDomain + "/payment-complete";

        model.addAttribute("pr", pr);
        model.addAttribute("weixinTradeSn", weixinTradeSn);
        model.addAttribute("paySuccessUrl", paySuccessUrl);

        model.addAttribute("jquery_path", request.getContextPath() + "/jquery.min.js");
        model.addAttribute("default_gateway_url", cxt);

        //二维码模式嵌在的iframe中的，要设置此相应允许被buyer域名的frame嵌套
        response.setHeader("Content-Security-Policy", "frame-ancestors " + buyerDomain);
        return "wechat_qr";
    }

    @ApiOperation(value = "显示一个微信二维码")
    @GetMapping(value = "/qr/{pr}")
    public byte[] qr(@PathVariable(name = "pr") String pr) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //图片的宽度
        int width = 200;
        //高度
        int height = 200;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix m = writer.encode("weixin://wxpay/bizpayurl?pr=" + pr, BarcodeFormat.QR_CODE, height, width);
        MatrixToImageWriter.writeToStream(m, "png", stream);

        return stream.toByteArray();
    }


    @ApiOperation(value = "获取微信扫描支付的状态")
    @GetMapping("/status/{wechat_trade_sn}")
    public ApiResponse payStatus(@PathVariable(name = "wechat_trade_sn") String weChatTradeSn,
                                 HttpServletRequest request) {

        PayBillVO bill = new PayBillVO();
        bill.setOutTradeNo(weChatTradeSn);
        bill.setClientType(ClientType.PC);
        bill.setTradeType(TradeType.trade);
        bill.setPayMode(PayMode.qr.name());
        bill.setPaymentPluginId("weixinPayPlugin");

        String s = orderPayManager.onQuery(bill, request);
        return ApiResponse.success(s);
    }



}
