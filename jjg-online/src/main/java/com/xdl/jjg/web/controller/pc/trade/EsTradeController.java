package com.xdl.jjg.web.controller.pc.trade;

import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.vo.CartItemsVO;
import com.jjg.trade.model.vo.CartVO;
import com.jjg.trade.model.vo.ReturnCouponMsgVO;
import com.xdl.jjg.constant.AgentTypeUtils;
import com.xdl.jjg.manager.CartManager;
import com.xdl.jjg.manager.CheckoutParamManager;
import com.xdl.jjg.manager.TradeManager;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.FormatValidateUtils;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsOrderOperateService;
import com.xdl.jjg.web.service.IEsTradeService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 订单主表-es_trade 前端控制器
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esTrade",tags = "订单接口模块")
@RestController
@RequestMapping("/esTrade")
public class EsTradeController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(EsTradeController.class);

    @Autowired
    private IEsTradeService iesTradeService;

    @Autowired
    private GoodsSkuService iEsGoodsSkuService;

    @Autowired
    private IEsOrderOperateService iEsOrderOperateService;

    @Autowired
    private TradeManager tradeManager;

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Autowired
    private CartManager cartManager;


    @ApiOperation(value = "立即购买", response = CartItemsVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId", value = "产品ID", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "此产品的购买数量", required = true, dataType = "int", paramType = "query"),
    })
    @ResponseBody
    @PostMapping("/buy")
    public String buy(@ApiIgnore @NotNull(message = "产品id不能为空") Long skuId, @ApiIgnore @NotNull(message = "购买数量不能为空") Integer num
                      ) {
        this.cartManager.add(skuId, num,null);
        return "";
    }


    @ApiOperation(value = "我的订单列表 再次购买", response = CartItemsVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", required = true, dataType = "String", paramType = "query"),
    })
    @ResponseBody
    @PostMapping("/buyAgain")
    public ApiResponse buyAgain(@ApiIgnore @NotNull(message = "不能为空") String orderSn
    ) {
        this.cartManager.buyAgainPC(orderSn,null);
        return ApiResponse.success();
    }

    @ApiOperation(value = "创建交易信息", notes = "根据form表单数据提交")
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


    @ApiOperation(value = "创建交易信息压测", notes = "根据form表单数据提交")
    @PostMapping("/createYC")
    @ResponseBody
    public ApiResponse createYC(@RequestBody @ApiParam(value = "赠品对象",name = "cartVOList")@Valid List<CartVO> cartVOList) {
        //判断订单来源
//        String userAgent = request.getHeader("user-agent");
//        String clientType = AgentTypeUtils.isMobileOrTablet(userAgent);
//        //设置订单交易信息
//        this.checkoutParamManager.setClientType(clientType);
        ApiResponse trade = this.tradeManager.createStockReduceMQYC(cartVOList);
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "couponId", value = "优惠券ID", required = true, dataType = "long", paramType = "path")
    })
    @PostMapping(value = "/{shopId}/seller/{couponId}/coupon")
    public ApiResponse setCoupon(@NotNull(message = "店铺id不能为空") @PathVariable("shopId") Long shopId,
                            @NotNull(message = "优惠卷id不能为空") @PathVariable("couponId") Long couponId) {

        try {
            ReturnCouponMsgVO returnCouponMsgVO = this.cartManager.userCoupon(couponId, shopId,null);
            return ApiResponse.success(returnCouponMsgVO);
        } catch (ArgumentException ae) {
            ae.printStackTrace();
            return ApiResponse.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(e.hashCode(),e.getMessage());
        }
    }

}

