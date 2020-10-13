package com.xdl.jjg.web.controller.applet.trade;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.goods.api.model.domain.vo.EsSpecValuesVO;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.system.api.model.domain.EsSettingsDO;
import com.shopx.system.api.model.domain.vo.EsOrderSettingVO;
import com.shopx.system.api.model.enums.SettingGroup;
import com.shopx.system.api.service.IEsSettingsService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsOrderDO;
import com.shopx.trade.api.model.domain.EsOrderItemsDO;
import com.shopx.trade.api.model.domain.EsTradeDO;
import com.shopx.trade.api.model.domain.dto.EsTradeDTO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.OrderStatusEnum;
import com.shopx.trade.api.model.enums.ServiceStatusEnum;
import com.shopx.trade.api.service.IEsOrderItemsService;
import com.shopx.trade.api.service.IEsOrderService;
import com.shopx.trade.api.service.IEsTradeService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.query.EsAppletOrderQueryForm;
import com.shopx.trade.web.request.query.EsWapMemberOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器-小程序-我的订单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-02
 */
@RestController
@Api(value = "/applet/trade/order",tags = "小程序-我的订单")
@RequestMapping("/applet/trade/order")
public class EsAppletOrderController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderItemsService iEsOrderItemsService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsTradeService iEsTradeService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsSettingsService iEsSettingsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOrderService iEsOrderService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;


    @GetMapping(value = "/getEsTradList")
    @ApiOperation(value = "根据订单状态查询订单",response = EsBuyerTradeVO.class)
    @ResponseBody
    public ApiResponse getEsTradeList(@Valid EsAppletOrderQueryForm form){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsTradeDTO esTradeDTO = new EsTradeDTO();
        esTradeDTO.setMemberId(dubboResult.getData().getId());
        //赋值订单状态
        esTradeDTO.setTradeStatus(form.getOrderState());
        //删除状态
        esTradeDTO.setIsDel(0);
        DubboPageResult<EsTradeDO> esTradeInfoList = iEsTradeService.getEsTradeInfoList(esTradeDTO, form.getPageSize(), form.getPageNum());
        if (esTradeInfoList.isSuccess()){
            List<EsTradeDO> tradeDOList = esTradeInfoList.getData().getList();
            //根据主订单列表中的订单SN检索订单商品明细表信息和商品SKU信息
            List<EsBuyerTradeVO> tradeVOList = tradeDOList.stream().map(esTradeDO -> {
                //  订单商品明细表信息
                DubboPageResult<EsOrderDO> esOrderInfoByTradeSn = iEsOrderService.getEsOrderInfoByTradeSnAndState(esTradeDO.getTradeSn(),form.getOrderState());
                //所有状态订单集合
                List<EsOrderDO> list = esOrderInfoByTradeSn.getData().getList();
                EsBuyerTradeVO buyerTradeVO = new EsBuyerTradeVO();
                if (!"CONFIRM".equals(esTradeDO.getTradeStatus())){
                    //PAID_OFF:待发货,SHIPPED:待收货，COMPLETE:已完成 ，ROG:已收货
                   List<EsBuyerItemsVO> itemsVOList = list.stream().map(order->{
                        // 获取订单中的订单项数据
                        EsBuyerItemsVO buyerItemsVO = new EsBuyerItemsVO();

                        DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(order.getOrderSn());
                        List<EsOrderItemsDO> esOrderItemsDO1 = result.getData().getList();
                        buyerItemsVO.setShopId(order.getShopId());
                        buyerItemsVO.setShopName(order.getShopName());
                        buyerItemsVO.setTradeSn(order.getTradeSn());
                        buyerItemsVO.setOrderSn(order.getOrderSn());
                        buyerItemsVO.setOrderState(order.getOrderState());
//                        buyerItemsVO.setOrderState(esTradeDO.getTradeStatus());
                        buyerItemsVO.setCreateTime(esTradeDO.getCreateTime());
                        buyerItemsVO.setShipAddr(esTradeDO.getConsigneeAddress());
                        buyerItemsVO.setShipCity(esTradeDO.getConsigneeCity());
                        buyerItemsVO.setShipProvince(esTradeDO.getConsigneeProvince());
                        buyerItemsVO.setShipCounty(esTradeDO.getConsigneeCounty());
                        buyerItemsVO.setShipMobile(esTradeDO.getConsigneeMobile());
                        buyerItemsVO.setShipName(esTradeDO.getConsigneeName());
                        buyerItemsVO.setOrderMoney(order.getOrderMoney()+order.getShippingMoney());
                        buyerItemsVO.setShippingMoney(esTradeDO.getFreightMoney());
                        buyerItemsVO.setShipTown(esTradeDO.getConsigneeTown());
                        List<EsBuyerOrderItemsVO> buyerOrderItemsVOList = BeanUtil.copyList(esOrderItemsDO1,EsBuyerOrderItemsVO.class);

                       buyerOrderItemsVOList = buyerOrderItemsVOList.stream().map(orderItemsDo -> {
                           EsBuyerOrderItemsVO esOrderItemsVO = new EsBuyerOrderItemsVO();
                           BeanUtils.copyProperties(orderItemsDo,esOrderItemsVO);
                           //获取规格参数
                           String specJson = orderItemsDo.getSpecJson();
                           List<EsSpecValuesVO> specValuesVOList = JsonUtil.jsonToList(specJson, EsSpecValuesVO.class);
                           //把商品SKU 封装到订单商品明细中
                           esOrderItemsVO.setSpecList(specValuesVOList);
                           return esOrderItemsVO;
                       }).collect(Collectors.toList());
                        buyerItemsVO.setEsOrderItemsVOList(buyerOrderItemsVOList);
                        return  buyerItemsVO;
                    }).collect(Collectors.toList());

                    buyerTradeVO.setTradeList(itemsVOList);
                }else{
                    //CONFIRM 未付款

                    List<EsBuyerItemsVO> itemsVOList = list.stream().map(order->{
                        // 获取订单中的订单项数据
                        EsBuyerItemsVO buyerItemsVO = new EsBuyerItemsVO();

                        DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(order.getOrderSn());
                        List<EsOrderItemsDO> esOrderItemsDO1 = result.getData().getList();

                        // 系统配置 订单关闭时间
                        DubboResult<EsSettingsDO> resultSet = iEsSettingsService.getByCfgGroup(SettingGroup.TRADE.name());
                        EsSettingsDO data = resultSet.getData();
                        String value = data.getCfgValue();
                        EsOrderSettingVO esOrderSettingVO = JsonUtil.jsonToObject(value, EsOrderSettingVO.class);

                        List<EsBuyerItemsVO> buyerItemsVOList = new ArrayList<>();
                        Long createTime = esTradeDO.getCreateTime();
                        // 计算毫秒
                        Integer closeOrderDay = esOrderSettingVO.getCloseOrderDay()*3600*1000;
                        buyerItemsVO.setTradeSn(esTradeDO.getTradeSn());
                        Long time = createTime+closeOrderDay;
                        buyerItemsVO.setCloseOrderTime(time - System.currentTimeMillis() < 0 ? 0 : time - System.currentTimeMillis());

                        buyerItemsVO.setShopId(order.getShopId());
                        buyerItemsVO.setShopName(order.getShopName());
                        buyerItemsVO.setTradeSn(order.getTradeSn());
                        buyerItemsVO.setOrderSn(order.getOrderSn());
                        buyerItemsVO.setOrderState(order.getOrderState());
                        buyerItemsVO.setCreateTime(esTradeDO.getCreateTime());
                        buyerItemsVO.setShipAddr(esTradeDO.getConsigneeAddress());
                        buyerItemsVO.setShipCity(esTradeDO.getConsigneeCity());
                        buyerItemsVO.setShipProvince(esTradeDO.getConsigneeProvince());
                        buyerItemsVO.setShipCounty(esTradeDO.getConsigneeCounty());
                        buyerItemsVO.setShipMobile(esTradeDO.getConsigneeMobile());
                        buyerItemsVO.setShipName(esTradeDO.getConsigneeName());
                        buyerItemsVO.setOrderMoney(esTradeDO.getTotalMoney());
                        buyerItemsVO.setShippingMoney(esTradeDO.getFreightMoney());
                        buyerItemsVO.setShipTown(esTradeDO.getConsigneeTown());
//                        DubboPageResult<EsOrderItemsDO> orderItemsByOrderSnList = this.iEsOrderItemsService.getEsOrderItemsByTradeSn(esTradeDO.getTradeSn());
//                        List<EsOrderItemsDO> esOrderItemsDO = orderItemsByOrderSnList.getData().getList();
                        // esOrderItemsDO 转换为 VO
                        List<EsBuyerOrderItemsVO> buyerOrderItemsVOList = BeanUtil.copyList(esOrderItemsDO1,EsBuyerOrderItemsVO.class);

                        buyerOrderItemsVOList = buyerOrderItemsVOList.stream().map(orderItemsDo -> {
                            EsBuyerOrderItemsVO esOrderItemsVO = new EsBuyerOrderItemsVO();
                            BeanUtils.copyProperties(orderItemsDo,esOrderItemsVO);
                            //获取规格参数
                            String specJson = orderItemsDo.getSpecJson();
                            List<EsSpecValuesVO> specValuesVOList = JsonUtil.jsonToList(specJson, EsSpecValuesVO.class);
                            //把商品SKU 封装到订单商品明细中
                            esOrderItemsVO.setSpecList(specValuesVOList);
                            return esOrderItemsVO;
                        }).collect(Collectors.toList());

                        buyerItemsVO.setEsOrderItemsVOList(buyerOrderItemsVOList);
                        buyerItemsVOList.add(buyerItemsVO);
                        return  buyerItemsVO;
                    }).collect(Collectors.toList());
                    buyerTradeVO.setTradeList(itemsVOList);
                }
               return buyerTradeVO;
            }).collect(Collectors.toList());
            return ApiPageResponse.pageSuccess((long)tradeVOList.size(), tradeVOList);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(esTradeInfoList));
    }


    @ApiOperation(value = "查询订单状态的数量")
    @GetMapping(value = "/statusNum/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getStatusNum(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        DubboResult<OrderStatusNumVO> result = iEsOrderService.getOrderStatusNum(memberId);
        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @ApiOperation(value = "根据交易编号查询收银台数据",response = CashierVO.class)
    @ApiImplicitParam(name = "tradeSn", value = "交易编号", dataType = "string", paramType = "path")
    @GetMapping(value = "/cashier/{tradeSn}")
    @ResponseBody
    public ApiResponse getCashier(@PathVariable String tradeSn) {
        String sn = "";
        String shipMethod = "";
        String shipName = "";
        String orderState = "";
        String shipAddr = "";
        String shipMobile = "";
        String shipTel = "";
        String shipProvince = "";
        String shipCity = "";
        String shipCounty = "";
        String shipTown = "";
        String payTypeText = "";
        Double needPayPrice = 0.0;
        List<String> goodsName = null;

        if (tradeSn != null) {
            DubboResult<EsTradeDO> dubboResult = this.iEsTradeService.getEsTradeByTradeSn(tradeSn);

            if (dubboResult.isSuccess()){
                EsTradeDO tradeDO = dubboResult.getData();
                if (tradeDO != null){
                    // 获取商品名称
                    DubboPageResult<EsOrderItemsDO> orderSnList = this.iEsOrderItemsService.getEsOrderItemsByTradeSn(tradeSn);
                    List<EsOrderItemsDO> list1 = orderSnList.getData().getList();
                    List<String> goodsNames = list1.stream().map(EsOrderItemsDO::getName).collect(Collectors.toList());
                    goodsName = goodsNames;
                    sn = tradeDO.getTradeSn();
                    shipName = tradeDO.getConsigneeName();
                    orderState = tradeDO.getTradeStatus();
                    shipAddr = tradeDO.getConsigneeAddress();
                    shipMobile = tradeDO.getConsigneeMobile();
                    shipTel = tradeDO.getConsigneeTelephone();
                    shipProvince = tradeDO.getConsigneeProvince();
                    shipCity = tradeDO.getConsigneeCity();
                    shipCounty = tradeDO.getConsigneeCounty();
                    shipTown = tradeDO.getConsigneeTown();
                    needPayPrice = tradeDO.getPayMoney();
                    payTypeText = tradeDO.getPaymentType();
                    shipMethod = tradeDO.getShipMethod();
                }
            }
        } else {
            throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "参数错误");
        }

        CashierVO cashierVO = new CashierVO();
        cashierVO.setSn(sn);
        cashierVO.setShipMethod(shipMethod);
        cashierVO.setOrderState(orderState);
        cashierVO.setShipProvince(shipProvince);
        cashierVO.setShipCity(shipCity);
        cashierVO.setShipCounty(shipCounty);
        cashierVO.setShipTown(shipTown);
        cashierVO.setShipAddr(shipAddr);
        cashierVO.setShipMobile(shipMobile);
        cashierVO.setShipName(shipName);
        cashierVO.setNeedPayPrice(needPayPrice);
        cashierVO.setShipTel(shipTel);
        cashierVO.setPayTypeText(payTypeText);
        cashierVO.setGoodsName(goodsName);
        return ApiResponse.success(cashierVO);
    }

    @ApiOperation(value = "根据订单号查询订单明细",response = EsWapOrderVO.class)
    @GetMapping("/orderDetails")
    @ResponseBody
    public ApiResponse getOrderDetail(@Valid EsWapMemberOrderForm form) {

        String orderState = form.getOrderState();
        String orderSn = form.getOrderSn();
        DubboPageResult<EsOrderDO> result = iEsOrderService.getEsWapOrderDetails(orderSn, orderState);
        List<EsWapOrderVO> orderVOS=new ArrayList<>();
        if (result.isSuccess()) {
            List<EsOrderDO> orderDO = result.getData().getList();

            for (EsOrderDO order: orderDO) {
                // 设置订单操作权限
                OrderOperateAllowable orderOperateAllowable = new OrderOperateAllowable(OrderStatusEnum.valueOf(order.getOrderState()),
                        ServiceStatusEnum.valueOf(order.getServiceState()));
                EsWapOrderVO esWapOrderVO = new EsWapOrderVO();
                if ("CONFIRM".equals(orderState)) {
                    // 系统配置 订单关闭时间
                    DubboResult<EsSettingsDO> settingResult = iEsSettingsService.getByCfgGroup(SettingGroup.TRADE.name());
                    EsSettingsDO data = settingResult.getData();
                    String value = data.getCfgValue();
                    EsOrderSettingVO esOrderSettingVO = JsonUtil.jsonToObject(value, EsOrderSettingVO.class);
                    // 计算毫秒
                    Integer closeOrderDay = esOrderSettingVO.getCloseOrderDay() * 3600 * 1000;
                    Long time = order.getCreateTime() + closeOrderDay;
                    esWapOrderVO.setCloseOrderTime(time - System.currentTimeMillis() < 0 ? 0 : time - System.currentTimeMillis());

                }
                esWapOrderVO.setOrderOperateAllowable(orderOperateAllowable);
                BeanUtil.copyProperties(order, esWapOrderVO);
                orderVOS.add(esWapOrderVO);
            }
            return ApiResponse.success(orderVOS);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

}

