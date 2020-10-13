package com.xdl.jjg.web.controller.wap.system;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.system.api.model.domain.vo.ExpressDetailVO;
import com.shopx.system.api.service.IEsExpressPlatformService;
import com.shopx.trade.api.model.domain.EsOrderItemsDO;
import com.shopx.trade.api.model.domain.vo.ExpressDetailsVO;
import com.shopx.trade.api.service.IEsOrderItemsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * <p>
 * 移动端-物流轨迹查询
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-04-10 10:39:47
 */
@Api(value = "/wap/system/express", tags = "移动端-物流轨迹查询")
@RestController
@RequestMapping("/wap/system/express")
public class EsWapExpressController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsExpressPlatformService expressPlatformService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsOrderItemsService iEsOrderItemsService;


    @ApiOperation(value = "根据订单号查询物流轨迹",response = ExpressDetailsVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单号", dataType = "string", paramType = "query"),
    })
    @PostMapping("/getOrderExpress")
    public ApiResponse getOrderExpress(String orderSn) {
        //根据订单号查询查询详情里的快递公司id和单号
        DubboPageResult<EsOrderItemsDO> itemsByOrderSn = iEsOrderItemsService.getEsOrderItemsByOrderSn(orderSn);
        List<EsOrderItemsDO> list = itemsByOrderSn.getData().getList();
        //过滤出来已经发货的商品
        List<EsOrderItemsDO> collect1 = list.stream().filter(esOrderItemsDO -> esOrderItemsDO.getShipNo() != null).collect(Collectors.toList());
        //去重
        List<EsOrderItemsDO> collect = collect1.stream().filter(distinctByKey(o -> o.getShipNo())).collect(Collectors.toList());

        logger.info("去重后前集合大小"+collect1.size()+"集合"+collect1);
        logger.info("去重后集合大小"+collect.size()+"集合"+collect);
        List<ExpressDetailsVO> expressList = new ArrayList<>();
        collect.forEach(esOrderItemsDO -> {
            ExpressDetailsVO expressDetailsVO = new ExpressDetailsVO();
            DubboResult<ExpressDetailVO> expressFormDetail = this.expressPlatformService.getExpressFormDetail(esOrderItemsDO.getLogiId(), esOrderItemsDO.getShipNo());
            BeanUtil.copyProperties(expressFormDetail.getData(),expressDetailsVO);
            if(expressFormDetail.getData().getData() != null){
                // 获取每一个订单号对应的图片信息
                DubboPageResult<EsOrderItemsDO> orderItemsByOrderSnAndShipNo = iEsOrderItemsService.getEsOrderItemsByOrderSnAndShipNo(esOrderItemsDO.getOrderSn(), esOrderItemsDO.getShipNo());
                List<EsOrderItemsDO> list1 = orderItemsByOrderSnAndShipNo.getData().getList();
                List<String> imageList = list1.stream().map(EsOrderItemsDO::getImage).collect(Collectors.toList());
                expressDetailsVO.setImage(imageList);
                expressList.add(expressDetailsVO);
            }
        });

        return ApiResponse.success(expressList);
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

//    @ApiOperation(value = "物流轨迹" )
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "dateId", value = "日期id", required = true, dataType = "long", paramType = "path") })
//    @GetMapping(value = "/getSelfTime/{dateId}")
//    @ResponseBody
//    public ApiResponse<EsSelfTimeVO> getSelfTimeByDateId(@PathVariable Long dateId) {
//
//        DubboResult<ExpressDetailVO> expressResult = this.expressPlatformService.getExpressFormDetail(orderItemsDO.getLogiId(), orderItemsDO.getShipNo());
//
//        if (dubboPageResult.isSuccess()) {
//            List list = dubboPageResult.getData().getList();
//            List<EsSelfTimeVO> esSelfTimeVOS = BeanUtil.copyList(list, EsSelfTimeVO.class);
//            return ApiResponse.success(esSelfTimeVOS);
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(dubboPageResult));
//        }
//    }
}

