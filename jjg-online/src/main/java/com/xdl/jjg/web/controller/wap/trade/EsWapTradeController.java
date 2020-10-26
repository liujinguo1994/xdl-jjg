package com.xdl.jjg.web.controller.wap.trade;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jjg.trade.model.domain.EsPaymentMethodDO;
import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.form.EsWapCouponForm;
import com.jjg.trade.model.vo.EsPaymentMethodVO;
import com.jjg.trade.model.vo.EsTradeSnMoneyVO;
import com.jjg.trade.model.vo.ReturnCouponMsgVO;
import com.xdl.jjg.constant.AgentTypeUtils;
import com.xdl.jjg.manager.CartManager;
import com.xdl.jjg.manager.CheckoutParamManager;
import com.xdl.jjg.manager.TradeManager;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.FormatValidateUtils;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsOrderOperateService;
import com.xdl.jjg.web.service.IEsPaymentMethodService;
import com.xdl.jjg.web.service.IEsTradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *移动端-交易 前端控制器
 *
 * @Author: yuanj  595831329@qq.com
 * @Date: 03/03/2020 10:40
 * @Version: 1.0
 */
@Api(value = "/wap/esTrade",tags = "移动端-交易")
@RestController
@RequestMapping("/wap/esTrade")
public class EsWapTradeController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(EsWapTradeController.class);

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsTradeService iesTradeService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsGoodsSkuService iEsGoodsSkuService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderOperateService iEsOrderOperateService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsPaymentMethodService paymentMethodService;

    @Autowired
    private TradeManager tradeManager;

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Autowired
    private CartManager cartManager;

    @ApiOperation(value = "创建交易信息", notes = "根据form表单数据提交",response = EsTradeSnMoneyVO.class)
    @PostMapping("/create")
    @ResponseBody
    public ApiResponse create(HttpServletRequest request) {
        //判断订单来源
        String userAgent = request.getHeader("user-agent");
        String clientType = AgentTypeUtils.isMobileOrTablet(userAgent);
        //设置订单交易信息
        this.checkoutParamManager.setClientType(clientType,null);
        ApiResponse trade = this.tradeManager.createStockReduceMQ(null);
        return trade;
    }

    @ApiOperation(value = "会员未付款取消交易订单，取消整个交易单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tradeSn", value = "交易编号", required = true, dataType = "string",paramType = "query"),
    })
    @PostMapping("/cancelTrade")
    @ResponseBody
    public ApiResponse cancelTrade(@ApiIgnore String tradeSn) {

        FormatValidateUtils.isMobile("");

        // 获取操作人id（会员id）
        Long memberId = ShiroKit.getUser().getId();
        DubboResult<EsTradeDO> result = this.iesTradeService.cancelTrade(memberId, tradeSn);
        return ApiResponse.success(result);
    }


    @ApiOperation(value = "设置优惠券", notes = "使用优惠券的时候分为三种情况：前2种情况couponId 不为0,不为空。第3种情况couponId为0," +
            "1、使用优惠券:在刚进入订单结算页，为使用任何优惠券之前。" +
            "2、切换优惠券:在1、情况之后，当用户切换优惠券的时候。" +
            "3、取消已使用的优惠券:用户不想使用优惠券的时候。",response = ReturnCouponMsgVO.class)
    @PostMapping(value = "seller/coupon")
    public ApiResponse setCoupon(@RequestBody EsWapCouponForm couponForm) {


        ReturnCouponMsgVO returnCouponMsgVO = this.cartManager.userCoupon(couponForm.getCouponId(), couponForm.getShopId(),null);
       
        return ApiResponse.success(returnCouponMsgVO);
    }

    @ApiOperation(value = "查询支持的支付方式",response = EsPaymentMethodVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "client_type", value = "调用客户端PC,WAP,APP", required = true, dataType =
                    "String", paramType = "path", allowableValues = "PC,WAP,APP")
    })
    @GetMapping(value = "/{client_type}")
    public ApiResponse queryPayments(@PathVariable(name = "client_type") String clientType) {
        DubboPageResult result = paymentMethodService.getPaymentMethodListByClient(clientType);
        if (result.isSuccess()) {
            List<EsPaymentMethodDO> data = result.getData().getList();
            List<EsPaymentMethodVO> esPaymentMethodVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                esPaymentMethodVOList = data.stream().map(esPaymentMethodDO -> {
                    EsPaymentMethodVO PaymentMethodVO = new EsPaymentMethodVO();
                    BeanUtil.copyProperties(esPaymentMethodDO, PaymentMethodVO);
                    return PaymentMethodVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(esPaymentMethodVOList);
        }else{
            return ApiResponse.fail(result.getCode(), result.getMsg());
        }
    }

}

