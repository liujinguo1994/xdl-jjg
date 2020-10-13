package com.xdl.jjg.web.controller.pc.system;

import com.beust.jcommander.internal.Lists;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.model.domain.EsDeliveryServiceDO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.service.IEsDeliveryServiceService;
import com.shopx.trade.api.service.IEsSelfDateService;
import com.shopx.trade.api.service.IEsSelfTimeService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.manager.CartManager;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 自提点信息维护 前端控制器
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-06 15:09:47
 */
@Api(value = "/deliveryService", tags = "自提点信息维护模块接口")
@RestController
@RequestMapping("/deliveryService")
public class EsDeliveryServiceController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsDeliveryServiceService deliveryServiceService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSelfDateService iEsSelfDateService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSelfTimeService iEsSelfTimeService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService esMemberService;
    @Autowired
    private CartManager cartManager;

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "结算页面 获取自提点信息列表",response = EsDeliveryServiceVO.class)
    public ApiResponse<EsDeliveryServiceVO> getDeliveryServiceList() {
        // 查询是否支持自提
        List<CartVO> cartListCheck = cartManager.getCacheCartList(null);
        List<Long> skuIds = new ArrayList<>();
        // 获取所有已选中的商品SkuId
        cartListCheck.forEach(cartVO -> {
            // TODO 现在的业务是只有自营店铺 才支持自提 只考虑卓付商城的自提业务
            List<Long> collect = cartVO.getCartItemsList().stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1 &&cartItemsVO.getShopId().intValue() == 14).map(CartItemsVO::getSkuId).collect(Collectors.toList());
            skuIds.addAll(collect);
        });
        DubboResult<EsMemberDO> member = esMemberService.getMember(ShiroKit.getUser().getId());
        if(!member.isSuccess()){
            throw new ArgumentException(member.getCode(),member.getMsg());
        }
        String companyCode = member.getData().getCompanyCode();

        DubboResult<Boolean> support = deliveryServiceService.isSupport(skuIds, companyCode);

        // 不支持返回空
        if (!support.getData().booleanValue()) {
            return ApiResponse.success(Lists.newArrayList());
        }

        DubboPageResult<EsDeliveryServiceDO> result = deliveryServiceService.getDeliveryList(companyCode);

        if (result.isSuccess()) {
            List<EsDeliveryServiceDO> list = result.getData().getList();
            List<EsDeliveryServiceVO> esDeliveryServiceVOS = BeanUtil.copyList(list, EsDeliveryServiceVO.class);
            return ApiResponse.success(esDeliveryServiceVOS);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据自提点id获取自提日期列表",response = EsSelfDateVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliveryId", value = "自提点id", required = true, dataType = "long", paramType = "path") })
    @GetMapping(value = "/getSelfDate/{deliveryId}")
    @ResponseBody
    public ApiResponse<EsSelfDateVO> getSelfDateByDeliveryId(@PathVariable Long deliveryId) {
        DubboPageResult dubboPageResult = iEsSelfDateService.getSelfDateListByDeliveryId(deliveryId);
        if (dubboPageResult.isSuccess()) {
            List list = dubboPageResult.getData().getList();
            List<EsSelfDateVO> esSelfDateVOS = BeanUtil.copyList(list, EsSelfDateVO.class);
            return ApiResponse.success(esSelfDateVOS);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(dubboPageResult));
        }
    }

    @ApiOperation(value = "根据日期id获取自提时间点列表" ,response = EsSelfTimeVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateId", value = "日期id", required = true, dataType = "long", paramType = "path") })
    @GetMapping(value = "/getSelfTime/{dateId}")
    @ResponseBody
    public ApiResponse<EsSelfTimeVO> getSelfTimeByDateId(@PathVariable Long dateId) {
        DubboPageResult dubboPageResult = iEsSelfTimeService.getSelfTimeListByDateid(dateId);
        if (dubboPageResult.isSuccess()) {
            List list = dubboPageResult.getData().getList();
            List<EsSelfTimeVO> esSelfTimeVOS = BeanUtil.copyList(list, EsSelfTimeVO.class);
            return ApiResponse.success(esSelfTimeVOS);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(dubboPageResult));
        }
    }
}

