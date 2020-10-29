package com.xdl.jjg.web.controller.wap.trade;

import com.jjg.operateChecker.OrderOperateAllowable;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.vo.EsSpecValuesVO;
import com.jjg.system.model.domain.EsSettingsDO;
import com.jjg.system.model.enums.SettingGroup;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsOrderItemsDO;
import com.jjg.trade.model.domain.EsRefundDO;
import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.dto.EsTradeDTO;
import com.jjg.trade.model.enums.ProcessStatusEnum;
import com.jjg.trade.model.enums.RefundTypeEnum;
import com.jjg.trade.model.enums.ServiceStatusEnum;
import com.jjg.trade.model.form.query.EsWapMemberOrderForm;
import com.jjg.trade.model.form.query.EsWapOrderQueryForm;
import com.jjg.trade.model.vo.*;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器-移动端-我的订单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-14
 */
@RestController
@Api(value = "/wap/trade/order",tags = "移动端-我的订单")
@RequestMapping("/wap/trade/order")
public class EsWapOrderController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderService iesOrderService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsOrderItemsService iEsOrderItemsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsGoodsSkuService iEsGoodsSkuService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderOperateService iEsOrderOperateService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsTradeService iEsTradeService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsSettingsService iEsSettingsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOrderService iEsOrderService;

    @Autowired
    private JedisCluster jedisCluster;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService goodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsRefundService esRefundService;



    @GetMapping(value = "/getEsTradListV2")
    @ApiOperation(value = "根据订单状态查询订单",response = EsBuyerTradeVO.class)
    @ResponseBody
    public ApiResponse getEsTradeListV2(@Valid EsWapOrderQueryForm form){
    EsTradeDTO esTradeDTO = new EsTradeDTO();
        esTradeDTO.setMemberId(ShiroKit.getUser().getId());
        esTradeDTO.setMemberId(ShiroKit.getUser().getId());
        //赋值订单状态
        esTradeDTO.setTradeStatus(form.getOrderState());
    //删除状态
        esTradeDTO.setIsDel(0);

    DubboPageResult<EsTradeDO> esTradeInfoList = iEsTradeService.getEsTradeInfoList(esTradeDTO, form.getPageSize(), form.getPageNum());
    List<EsBuyerTradeVO> tradeVOList = new ArrayList<>();
        if (esTradeInfoList.isSuccess()){
        if(esTradeInfoList.getData() != null){
            List<EsTradeDO> tradeDOList = esTradeInfoList.getData().getList();
            //根据主订单列表中的订单SN检索订单商品明细表信息和商品SKU信息
            tradeVOList = tradeDOList.stream().map(esTradeDO -> {
                //  订单商品明细表信息
                DubboPageResult<EsOrderDO> esOrderInfoByTradeSn = iesOrderService.getEsOrderInfoByTradeSnAndState(esTradeDO.getTradeSn(),form.getOrderState());
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
                        // 由于该状态下订单已经拆分，查询订单详情时需要通过orderSn 查询
                        buyerItemsVO.setTradeSn(order.getOrderSn());
                        buyerItemsVO.setOrderState(order.getOrderState());
                        buyerItemsVO.setCreateTime(esTradeDO.getCreateTime());
                        buyerItemsVO.setShipAddr(esTradeDO.getConsigneeAddress());
                        buyerItemsVO.setShipCity(esTradeDO.getConsigneeCity());
                        buyerItemsVO.setShipProvince(esTradeDO.getConsigneeProvince());
                        buyerItemsVO.setShipCounty(esTradeDO.getConsigneeCounty());
                        buyerItemsVO.setShipMobile(esTradeDO.getConsigneeMobile());
                        buyerItemsVO.setShipName(esTradeDO.getConsigneeName());
                        buyerItemsVO.setOrderMoney(order.getOrderMoney()+order.getShippingMoney());
                        buyerItemsVO.setShippingMoney(order.getShippingMoney());
                        buyerItemsVO.setShipTown(esTradeDO.getConsigneeTown());
                        List<EsBuyerOrderItemsVO> buyerOrderItemsVOList = BeanUtil.copyList(esOrderItemsDO1,EsBuyerOrderItemsVO.class);

                        buyerOrderItemsVOList = buyerOrderItemsVOList.stream().map(orderItemsDo -> {
                            EsBuyerOrderItemsVO esOrderItemsVO = new EsBuyerOrderItemsVO();

                            BeanUtils.copyProperties(orderItemsDo,esOrderItemsVO);
                            //如果订单已申请售后
                            if (!"NOT_APPLY".equals(orderItemsDo.getState())){
                                DubboResult<EsRefundDO> refundSn = esRefundService.getRefundSn(orderItemsDo.getOrderSn(), orderItemsDo.getSkuId());
                                if (refundSn.isSuccess() && refundSn.getData() != null){
                                    EsRefundDO esRefundDO = refundSn.getData();
                                    esOrderItemsVO.setServiceHandleStatus(RefundTypeEnum.valueOf(esRefundDO.getRefundType()).description()+":"+
                                            ProcessStatusEnum.valueOf(esRefundDO.getProcessStatus()).description());
                                    esOrderItemsVO.setReFundSn(Long.valueOf(refundSn.getData().getSn()));
                                }
                            }
                            //获取规格参数
                            String specJson = orderItemsDo.getSpecJson();
                            List<EsSpecValuesVO> specValuesVOList = JsonUtil.jsonToList(specJson, EsSpecValuesVO.class);
                            //把商品SKU 封装到订单商品明细中
                            esOrderItemsVO.setSpecList(specValuesVOList);
                            Long goodsId = orderItemsDo.getGoodsId();
                            // 获取商品信息
                            DubboResult goodsResult = goodsService.getEsBuyerGoods(goodsId);
                            if (goodsResult.isSuccess()){
                                EsGoodsCO goods = (EsGoodsCO) goodsResult.getData();
                                Integer marketEnable = goods.getMarketEnable();
                                if (marketEnable == 2){
                                    esOrderItemsVO.setMarketEnable(2);
                                }
                            }

                            return esOrderItemsVO;
                        }).collect(Collectors.toList());
                        buyerItemsVO.setEsOrderItemsVOList(buyerOrderItemsVOList);
                        return  buyerItemsVO;
                    }).collect(Collectors.toList());

                    buyerTradeVO.setTradeList(itemsVOList);
                }else{
                    //CONFIRM 未付款

                    // 系统配置 订单关闭时间
                    DubboResult<EsSettingsDO> result = iEsSettingsService.getByCfgGroup(SettingGroup.TRADE.name());
                    EsSettingsDO data = result.getData();
                    String value = data.getCfgValue();
                    EsOrderSettingVO esOrderSettingVO = JsonUtil.jsonToObject(value, EsOrderSettingVO.class);

                    List<EsBuyerItemsVO> buyerItemsVOList = new ArrayList<>();
                    Long createTime = esTradeDO.getCreateTime();
                    // 计算毫秒
                    Integer closeOrderDay = esOrderSettingVO.getCloseOrderDay()*3600*1000;
                    EsBuyerItemsVO buyerItemsVO = new EsBuyerItemsVO();
                    buyerItemsVO.setTradeSn(esTradeDO.getTradeSn());
                    Long time = createTime+closeOrderDay;
                    if (time - System.currentTimeMillis() > 0){
                        buyerItemsVO.setCloseOrderTime(time - System.currentTimeMillis() < 0 ? 0 : time - System.currentTimeMillis());
                    }
                    buyerItemsVO.setOrderState(esTradeDO.getTradeStatus());
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
                    DubboPageResult<EsOrderItemsDO> orderItemsByOrderSnList = this.iEsOrderItemsService.getEsOrderItemsByTradeSn(esTradeDO.getTradeSn());
                    List<EsOrderItemsDO> esOrderItemsDO = orderItemsByOrderSnList.getData().getList();
                    // esOrderItemsDO 转换为 VO
                    List<EsBuyerOrderItemsVO> buyerOrderItemsVOList = BeanUtil.copyList(esOrderItemsDO,EsBuyerOrderItemsVO.class);

                    buyerOrderItemsVOList = buyerOrderItemsVOList.stream().map(orderItemsDo -> {
                        EsBuyerOrderItemsVO esOrderItemsVO = new EsBuyerOrderItemsVO();
                        BeanUtils.copyProperties(orderItemsDo,esOrderItemsVO);
                        //获取规格参数
                        String specJson = orderItemsDo.getSpecJson();
                        List<EsSpecValuesVO> specValuesVOList = JsonUtil.jsonToList(specJson, EsSpecValuesVO.class);
                        //把商品SKU 封装到订单商品明细中
                        esOrderItemsVO.setSpecList(specValuesVOList);

                        Long goodsId = orderItemsDo.getGoodsId();
                        // 获取商品信息
                        DubboResult goodsResult = goodsService.getEsBuyerGoods(goodsId);
                        if (goodsResult.isSuccess()){
                            EsGoodsCO goods = (EsGoodsCO) goodsResult.getData();
                            Integer marketEnable = goods.getMarketEnable();
                            if (marketEnable == 2){
                                esOrderItemsVO.setMarketEnable(2);
                            }
                        }
                        return esOrderItemsVO;
                    }).collect(Collectors.toList());

                    buyerItemsVO.setEsOrderItemsVOList(buyerOrderItemsVOList);
                    buyerItemsVO.setCreateTime(esTradeDO.getCreateTime());
                    buyerItemsVOList.add(buyerItemsVO);
                    buyerTradeVO.setTradeList(buyerItemsVOList);
                }
                return buyerTradeVO;
            }).collect(Collectors.toList());
        }
        return ApiPageResponse.pageSuccess((long)tradeVOList.size(), tradeVOList);
    }
        return ApiResponse.fail(ApiStatus.wrapperException(esTradeInfoList));
}



    @GetMapping(value = "/getEsTradList")
    @ApiOperation(value = "根据订单状态查询订单",response = EsBuyerTradeVO.class)
    @ResponseBody
    public ApiResponse getEsTradeList(@Valid EsWapOrderQueryForm form){
        EsTradeDTO esTradeDTO = new EsTradeDTO();
        esTradeDTO.setMemberId(ShiroKit.getUser().getId());
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
                DubboPageResult<EsOrderDO> esOrderInfoByTradeSn = iesOrderService.getEsOrderInfoByTradeSnAndState(esTradeDO.getTradeSn(),form.getOrderState());
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
    @GetMapping(value = "/statusNum")
    public ApiResponse<OrderStatusNumVO> getStatusNum() {
        Long id = ShiroKit.getUser().getId();
        DubboResult<OrderStatusNumVO> orderStatusNum = this.iesOrderService.getOrderStatusNum(id);
        return ApiResponse.success(orderStatusNum.getData());
    }


    @ApiOperation(value = "根据交易编号或者订单编号查询收银台数据",response = CashierVO.class)
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

    @ApiOperation(value = "根据订单号查询订单明细", notes = "查询某个订单明细",response = EsWapOrderVO.class)
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

