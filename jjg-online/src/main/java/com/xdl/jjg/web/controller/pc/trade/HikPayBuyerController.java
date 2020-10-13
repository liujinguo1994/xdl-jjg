package com.xdl.jjg.web.controller.pc.trade;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.shopx.system.api.model.enums.CachePrefix;
import com.shopx.trade.api.model.domain.vo.PayBillVO;
import com.shopx.trade.api.model.enums.ClientType;
import com.shopx.trade.api.model.enums.TradeType;
import com.shopx.trade.web.manager.OrderPayManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author yuanj 595831329
 * @version v2.0
 * @Description: 订单支付
 * @date 2020/05/13 16:44
 * @since v7.0.0
 */
@RestController
@RequestMapping("/order/pay")
@Api(value = "/order/pay", tags = "海康-二维码")
public class HikPayBuyerController {


    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private OrderPayManager orderPayManager;


    @ApiIgnore
    @ApiOperation(value = "显示一个海康二维码")
    @GetMapping(value = "/hik/qr/{hik_trade_sn}/{qr}")
    public byte[] qr(@PathVariable(name = "hik_trade_sn") String hikTradeSn, @PathVariable(name = "qr") String qr) throws IOException, WriterException {
        String qrCode = (String)jedisCluster.get(CachePrefix.QR_CODE.getPrefix() + hikTradeSn);
        if ("true".equals(qrCode)) {
            qrCode = "该订单已付款，请在我的订单里查询详情";
        } else if (StringUtils.isEmpty(qrCode)) {
            qrCode = "码已失效，请刷新后重新扫描";
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //图片的宽度
        int width = 200;
        //高度
        int height = 200;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix m = writer.encode(new String(qrCode.getBytes("UTF-8"),"iso-8859-1"), BarcodeFormat.QR_CODE, height, width);
        MatrixToImageWriter.writeToStream(m, "png", stream);

        return stream.toByteArray();
    }

    @ApiOperation(value = "获取海康扫描支付的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hik_trade_sn", value = "海康预付订单号", required = true, dataType = "String", paramType = "path"),
    })
    @GetMapping("/hik/status/{hik_trade_sn}")
    public String payStatus(@PathVariable(name = "hik_trade_sn") String hikTradeSn, HttpServletRequest request) {


        PayBillVO bill = new PayBillVO();
        bill.setOutTradeNo(hikTradeSn);
        bill.setClientType(ClientType.PC);
        bill.setTradeType(TradeType.trade);
        bill.setPaymentPluginId("hikDirectPlugin");
        String result = orderPayManager.onQuery(bill, request);
        return result;
    }
}
