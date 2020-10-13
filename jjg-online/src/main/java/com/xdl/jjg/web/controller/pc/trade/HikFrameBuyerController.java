package com.xdl.jjg.web.controller.pc.trade;

import com.shopx.trade.web.manager.DomainSettings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kingapex on 2018/7/20.
 *
 * @author Li bw
 * @version 1.0
 * @since 7.0.0
 * 2018/7/20
 */
@Api(description = "海康-订单支付API")
@Controller
@RequestMapping("/order/pay")
@Validated
public class HikFrameBuyerController {
    private final static String HTTP_PROTOCOL="http://";

    @Autowired
    private DomainSettings domainSettings;
    @ApiIgnore
    @ApiOperation(value = "海康二维码显示页")
    @PostMapping("/hik/qrpage/{hik_trade_sn}/{pr}")
    public String qrPage(Model model, @PathVariable(name = "hik_trade_sn") String hikTradeSn, @PathVariable(name = "pr") String pr, HttpServletResponse response, HttpServletRequest request) {

        String buyerDomain = domainSettings.getBuyer();
        if (!buyerDomain.startsWith(HTTP_PROTOCOL)) {
            buyerDomain= HTTP_PROTOCOL+buyerDomain;
        }
        //获取虚拟目录
        String cxt = request.getContextPath();
        if("/".equals(cxt)){
            cxt = "";
        }
        String paySuccessUrl = buyerDomain + "/payment-complete";

        model.addAttribute("qr", pr);
        model.addAttribute("hikTradeSn", hikTradeSn);
        model.addAttribute("paySuccessUrl", paySuccessUrl);

        model.addAttribute("jquery_path", request.getContextPath()+"/jquery.min.js");
        model.addAttribute("default_gateway_url", cxt);
        //二维码模式嵌在的iframe中的，要设置此相应允许被buyer域名的frame嵌套

        response.setHeader("Content-Security-Policy","frame-ancestors "+ buyerDomain);
        return "hik_qr";
    }

}
