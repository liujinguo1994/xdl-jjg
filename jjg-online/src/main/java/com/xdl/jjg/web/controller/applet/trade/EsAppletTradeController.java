package com.xdl.jjg.web.controller.applet.trade;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.model.domain.vo.EsTradeSnMoneyVO;
import com.shopx.trade.api.service.IEsTradeService;
import com.shopx.trade.web.manager.CheckoutParamManager;
import com.shopx.trade.web.manager.TradeManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *小程序-交易 前端控制器
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-10
 */
@Api(value = "/applet/trade/esTrade",tags = "小程序-交易")
@RestController
@RequestMapping("/applet/trade/esTrade")
public class EsAppletTradeController {
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsTradeService iesTradeService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @Autowired
    private TradeManager tradeManager;
    @Autowired
    private CheckoutParamManager checkoutParamManager;


    @ApiOperation(value = "创建交易信息",response = EsTradeSnMoneyVO.class)
    @PostMapping("/create/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse create(@PathVariable String skey) {
        this.checkoutParamManager.setClientType("APPLET",skey);
        ApiResponse trade = this.tradeManager.createStockReduceMQ(skey);
        return trade;
    }

//    @ApiOperation(value = "会员未付款取消整个交易单")
//    @PostMapping("/cancelTrade")
//    @ResponseBody
//    public ApiResponse cancelTrade(@RequestBody @Valid EsAppletCancelTradeForm form) {
//        // 获取操作人id（会员id）
//        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
//        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
//            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        Long memberId = dubboResult.getData().getId();
//        DubboResult<EsTradeDO> result = iesTradeService.cancelTrade(memberId, form.getTradeSn());
//        if (result.isSuccess()) {
//            return ApiResponse.success();
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }
}

