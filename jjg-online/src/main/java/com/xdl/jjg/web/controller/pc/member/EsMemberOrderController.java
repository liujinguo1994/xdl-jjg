package com.xdl.jjg.web.controller.pc.member;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.web.BaseController;
import com.shopx.system.api.model.domain.EsSettingsDO;
import com.shopx.system.api.model.domain.vo.EsOrderSettingVO;
import com.shopx.system.api.model.enums.SettingGroup;
import com.shopx.system.api.service.IEsSettingsService;
import com.shopx.trade.api.model.domain.EsBuyerOrderDO;
import com.shopx.trade.api.model.domain.EsOrderDO;
import com.shopx.trade.api.model.domain.dto.EsBuyerOrderQueryDTO;
import com.shopx.trade.api.model.domain.vo.EsOrderVO;
import com.shopx.trade.api.model.domain.vo.OrderOperateAllowable;
import com.shopx.trade.api.model.enums.OrderStatusEnum;
import com.shopx.trade.api.model.enums.OrderStatusEnum1;
import com.shopx.trade.api.model.enums.ServiceStatusEnum;
import com.shopx.trade.api.service.IEsOrderService;
import com.shopx.trade.api.service.IEsTradeService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.query.EsOrderQueryForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-17
 */
@Api(value = "/esOrder", tags = "订单查询接口")
@RestController
@RequestMapping("/esOrder")
public class EsMemberOrderController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOrderService iEsOrderService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsTradeService iEsTradeService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsSettingsService iEsSettingsService;


    @ApiOperation(value = "查询会员订单列表(已付款、已发货、待评价)", notes = "查询会员订单列表(已付款、已发货、待评价)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query", example = "10")
    })
    @GetMapping("/memberOrder")
    @ResponseBody
    public ApiResponse getOrderList(@NotEmpty int pageNum, @NotEmpty int pageSize, @Valid EsOrderQueryForm esOrderQueryForm) {
        EsBuyerOrderQueryDTO esBuyerOrderQueryDTO=new EsBuyerOrderQueryDTO();
        BeanUtil.copyProperties(esOrderQueryForm,esBuyerOrderQueryDTO);
        esBuyerOrderQueryDTO.setMemberId(ShiroKit.getUser().getId());
        DubboPageResult<EsBuyerOrderDO> result = iEsOrderService.getEsBuyerOrderList(esBuyerOrderQueryDTO, pageSize, pageNum);
        if (result.isSuccess()) {
            List<EsBuyerOrderDO> esOrderDOList = result.getData().getList();
            List<EsOrderVO> esOrderVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esOrderDOList)) {
                esOrderVOList = esOrderDOList.stream().map(esMemberAddress -> {
                    EsOrderVO esOrderVO = new EsOrderVO();
                    BeanUtil.copyProperties(esMemberAddress, esOrderVO);
                    esOrderVO.setOrderOperateAllowable(new OrderOperateAllowable(OrderStatusEnum.valueOf(esOrderVO.getOrderState()),
                            ServiceStatusEnum.valueOf(esOrderVO.getServiceState())));
                    return esOrderVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(DubboPageResult.success(esOrderVOList).getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "买家端根据订单号查询订单明细", notes = "查询某个订单明细",response = EsOrderVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderState", value = "订单状态", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/orderDetails")
    @ResponseBody
    public ApiResponse getOrderDetail(String orderSn,String orderState) {

        DubboResult<EsOrderDO> result = iEsOrderService.getEsBuyerOrderDetails(orderSn,orderState);
        if (result.isSuccess()) {
            EsOrderDO orderDO = result.getData();
            // 设置订单操作权限
            OrderOperateAllowable orderOperateAllowable = new OrderOperateAllowable(OrderStatusEnum.valueOf(orderDO.getOrderState()),
                    ServiceStatusEnum.valueOf(orderDO.getServiceState()));

            EsOrderVO esOrderVO=new EsOrderVO();
            if ("CONFIRM".equals(orderState)){
                // 系统配置 订单关闭时间
                DubboResult<EsSettingsDO> settingResult = iEsSettingsService.getByCfgGroup(SettingGroup.TRADE.name());
                EsSettingsDO data = settingResult.getData();
                String value = data.getCfgValue();
                EsOrderSettingVO esOrderSettingVO = JsonUtil.jsonToObject(value, EsOrderSettingVO.class);
                // 计算毫秒
                Integer closeOrderDay = esOrderSettingVO.getCloseOrderDay()*3600*1000;
                Long time = orderDO.getCreateTime()+closeOrderDay;
                esOrderVO.setCloseOrderTime(time - System.currentTimeMillis() < 0 ? 0 : time - System.currentTimeMillis());

            }
            esOrderVO.setOrderOperateAllowable(orderOperateAllowable);
            esOrderVO.setProcessStatusList(getProcess(orderDO.getOrderState()));
            BeanUtil.copyProperties(orderDO,esOrderVO);
            esOrderVO.setOrderMoney(orderDO.getOrderMoney());
            return ApiResponse.success(esOrderVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    public List<String> getProcess(String orderState){
        //获取操作类型
        String json = JsonUtil.objectToJson(OrderStatusEnum1.values());
        List<String> list= JSON.parseArray(json,String.class);
//        if ("APPLY_REFUND".equals(orderState)) {
//            list.remove("CANCELLED");
//            list.remove("SHIPPED");
//        }else
            if ("CANCELLED".equals(orderState)){
            list.remove("PAID_OFF");
            list.remove("APPLY_REFUND");
            list.remove("SHIPPED");
            list.remove("COMPLETE");
        }else {
            list.remove("APPLY_REFUND");
            list.remove("CANCELLED");
        }

        return list;

    }

}