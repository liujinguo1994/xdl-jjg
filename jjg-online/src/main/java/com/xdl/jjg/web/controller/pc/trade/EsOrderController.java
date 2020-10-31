package com.xdl.jjg.web.controller.pc.trade;

import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.vo.EsSpecValuesVO;
import com.jjg.system.model.domain.EsSettingsDO;
import com.jjg.system.model.enums.SettingGroup;
import com.jjg.system.model.vo.ExpressDetailVO;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsOrderItemsDO;
import com.jjg.trade.model.domain.EsRefundDO;
import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.dto.EsTradeDTO;
import com.jjg.trade.model.enums.OrderStatusEnum;
import com.jjg.trade.model.enums.ProcessStatusEnum;
import com.jjg.trade.model.enums.RefundTypeEnum;
import com.jjg.trade.model.form.PayParamForm;
import com.jjg.trade.model.vo.*;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.manager.OrderPayManager;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.*;
import com.xdl.jjg.web.service.feign.member.ExpressPlatformService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuService;
import com.xdl.jjg.web.service.feign.system.SettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细表-es_order 前端控制器
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esOrder",tags = "订单明细接口")
@RestController
@RequestMapping("/esOrder")
public class EsOrderController extends BaseController {

    @Autowired
    private IEsOrderService iesOrderService;

    @Autowired
    private IEsOrderItemsService iEsOrderItemsService;

    @Autowired
    private GoodsSkuService iEsGoodsSkuService;

    @Autowired
    private IEsOrderOperateService iEsOrderOperateService;

    @Autowired
    private IEsTradeService iEsTradeService;

    @Autowired
    private SettingsService iEsSettingsService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private IEsRefundService esRefundService;
    @Autowired
    private ExpressPlatformService expressPlatformService;

    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private OrderPayManager orderPayManager;

    /**
     * 买家端 我的订单
     */
    @GetMapping(value = "/getEsTradList")
    @ApiOperation(value = "买家端 我的订单页面，全部商品2",notes = "根据订单状态和商品名称",response = EsBuyerTradeVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderState", value = "订单状态  CONFIRM:待付款,PAID_OFF:待发货,SHIPPED:待收货，COMPLETE:已完成 ，ROG:已收货", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "页数",dataType = "int",paramType = "query",required = true,example = "10"),
            @ApiImplicitParam(name = "pageNum",value = "页码",dataType = "int",paramType = "query",required = true,example = "1"),
            @ApiImplicitParam(name = "dateSearch",value = "时间检索",dataType = "String",paramType = "query",required = true,example = "oneM,threeM,sixM,thisY,lastY")
})
    @ResponseBody
    public ApiResponse getEsTradeList(@ApiIgnore String keyword, @ApiIgnore String orderState,
                                      @ApiIgnore @NotEmpty(message = "页数不能为空") int pageSize,
                                      @ApiIgnore @NotEmpty(message = "页码不能为空") int pageNum,
                                      @ApiIgnore String dateSearch
    ){
        EsTradeDTO esTradeDTO = new EsTradeDTO();
        esTradeDTO.setMemberId(ShiroKit.getUser().getId());
        //赋值商品名称
        esTradeDTO.setKeyword(keyword);
        //赋值订单状态
        esTradeDTO.setTradeStatus(orderState);
        //删除状态
        esTradeDTO.setIsDel(0);
        esTradeDTO.setDateSearch(dateSearch);

        long start = System.currentTimeMillis();
        logger.info("查询交易单所需开始时间[{}]，",start);
        DubboPageResult<EsTradeDO> esTradeInfoList = iEsTradeService.getEsTradeInfoList(esTradeDTO, pageSize, pageNum);
        long end = System.currentTimeMillis();
        logger.info("查询交易单结束时间[{}]，耗时[{}]",start,end - start);
        List<EsBuyerTradeVO> tradeVOList = new ArrayList<>();
        if (esTradeInfoList.isSuccess()){
            try {
                if(esTradeInfoList.getData() != null){
                    List<EsTradeDO> tradeDOList = esTradeInfoList.getData().getList();
                    //根据主订单列表中的订单SN检索订单商品明细表信息和商品SKU信息
                    long blstart = System.currentTimeMillis();
                    logger.info("遍历开始时间[{}]，",blstart);
                   tradeVOList = tradeDOList.stream().map(esTradeDO -> {
                        //  订单商品明细表信息
                        DubboPageResult<EsOrderDO> esOrderInfoByTradeSn = iesOrderService.getEsOrderInfoByTradeSnAndState(esTradeDO.getTradeSn(),orderState);
                        if (!esOrderInfoByTradeSn.isSuccess()){
                            throw  new ArgumentException(esOrderInfoByTradeSn.getCode(),esOrderInfoByTradeSn.getMsg());
                        }
                        //所有状态订单集合
                        List<EsOrderDO> list = esOrderInfoByTradeSn.getData().getList();
                        EsBuyerTradeVO buyerTradeVO = new EsBuyerTradeVO();
                        if (!"CONFIRM".equals(esTradeDO.getTradeStatus())){
                            long fukuanblstart = System.currentTimeMillis();
                            logger.info("已付款订单遍历开始时间[{}]，",fukuanblstart);
                            //PAID_OFF:待发货,SHIPPED:待收货，COMPLETE:已完成 ，ROG:已收货
                            List<EsBuyerItemsVO> itemsVOList = list.stream().map(order->{
                                // 获取订单中的订单项数据
                                EsBuyerItemsVO buyerItemsVO = new EsBuyerItemsVO();

                                DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(order.getOrderSn());
                                List<EsOrderItemsDO> esOrderItemsDO1 = result.getData().getList();

                                buyerItemsVO.setShopId(order.getShopId());
                                buyerItemsVO.setShopName(order.getShopName());
                                // 由于该状态下订单已经拆分，查询订单详情时需要通过orderSn 查询
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
                                buyerItemsVO.setOrderMoney(order.getOrderMoney()+order.getShippingMoney());
                                buyerItemsVO.setShippingMoney(order.getShippingMoney());
                                buyerItemsVO.setShipTown(esTradeDO.getConsigneeTown());
                                List<EsBuyerOrderItemsVO> buyerOrderItemsVOList = BeanUtil.copyList(esOrderItemsDO1,EsBuyerOrderItemsVO.class);

                                buyerOrderItemsVOList = buyerOrderItemsVOList.stream().map(orderItemsDo -> {
                                    EsBuyerOrderItemsVO esOrderItemsVO = new EsBuyerOrderItemsVO();

                                    BeanUtils.copyProperties(orderItemsDo,esOrderItemsVO);
                                    //如果订单已申请售后
                                    if (!"NOT_APPLY".equals(orderItemsDo.getState()) && orderItemsDo.getState() != null && !"EXPIRED".equals(orderItemsDo.getState())){
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
                                    if (specJson != null){
                                        List<EsSpecValuesVO> specValuesVOList = JsonUtil.jsonToList(specJson, EsSpecValuesVO.class);
                                        //把商品SKU 封装到订单商品明细中
                                        esOrderItemsVO.setSpecList(specValuesVOList);
                                    }

                                    Long goodsId = orderItemsDo.getGoodsId();
                                    // 获取商品信息
                                    DubboResult goodsResult = goodsService.getEsBuyerGoods(goodsId);
                                    if (goodsResult.isSuccess()){
                                        EsGoodsCO goods = (EsGoodsCO) goodsResult.getData();
                                        Integer marketEnable = goods.getMarketEnable();
                                        if (marketEnable == 2){
                                            esOrderItemsVO.setMarketEnable(2);
                                        }
                                        else {
                                            esOrderItemsVO.setMarketEnable(1);
                                        }
                                    }

                                    return esOrderItemsVO;
                                }).collect(Collectors.toList());
                                buyerItemsVO.setEsOrderItemsVOList(buyerOrderItemsVOList);

                                return  buyerItemsVO;
                            }).collect(Collectors.toList());
                            buyerTradeVO.setTradeSn(esTradeDO.getTradeSn());

                            buyerTradeVO.setCreateTime(esTradeDO.getCreateTime());
                            buyerTradeVO.setShipName(esTradeDO.getConsigneeName());
                            buyerTradeVO.setOrderMoney(esTradeDO.getTotalMoney());
                            buyerTradeVO.setTradeList(itemsVOList);
                            if (list.size() > 1){
                                buyerTradeVO.setCfState("已拆分");
                            }
                            long fukuanblend = System.currentTimeMillis();
                            logger.info("已付款订单遍历结束时间[{}]，耗时[{}]",fukuanblend,fukuanblend-fukuanblstart);
                        }else{
                            //CONFIRM 未付款
                            long weifukuanblstart = System.currentTimeMillis();
                            logger.info("未付款订单遍历开始时间[{}]",weifukuanblstart);
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
                            buyerItemsVO.setOrderSn(esTradeDO.getTradeSn());
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
                                    }else {
                                        esOrderItemsVO.setMarketEnable(1);
                                    }
                                }
                                return esOrderItemsVO;
                            }).collect(Collectors.toList());

                            buyerItemsVO.setEsOrderItemsVOList(buyerOrderItemsVOList);
                            buyerItemsVO.setCreateTime(esTradeDO.getCreateTime());
                            buyerItemsVOList.add(buyerItemsVO);
                            buyerTradeVO.setTradeSn(esTradeDO.getTradeSn());
                            buyerTradeVO.setCreateTime(esTradeDO.getCreateTime());
                            buyerTradeVO.setTradeList(buyerItemsVOList);
                            long weifukuanblend = System.currentTimeMillis();
                            logger.info("未付款订单遍历结束时间[{}]，耗时[{}]",weifukuanblend,weifukuanblend-weifukuanblstart);
                        }
                        return buyerTradeVO;
                    }).collect(Collectors.toList());
                    long blend = System.currentTimeMillis();
                    logger.info("遍历结束时间[{}]，耗时[{}]",blstart,blend - blstart);
                }
            }catch (ArgumentException e) {
                return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
            }catch (Exception e) {
                e.printStackTrace();
            }
            return ApiPageResponse.pageSuccess(esTradeInfoList.getData().getTotal(), tradeVOList);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(esTradeInfoList));
    }


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
            // 获取每一个订单号对应的图片信息
            DubboPageResult<EsOrderItemsDO> orderItemsByOrderSnAndShipNo = iEsOrderItemsService.getEsOrderItemsByOrderSnAndShipNo(esOrderItemsDO.getOrderSn(), esOrderItemsDO.getShipNo());
            List<EsOrderItemsDO> list1 = orderItemsByOrderSnAndShipNo.getData().getList();
            List<String> imageList = list1.stream().map(EsOrderItemsDO::getImage).collect(Collectors.toList());
            expressDetailsVO.setImage(imageList);
            expressList.add(expressDetailsVO);
        });

        return ApiResponse.success(expressList);
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @ApiOperation(value = "根据订单号查询物流轨迹",response = ExpressDetailsVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "skuId", value = "skuId", dataType = "long", paramType = "query"),
    })
    @GetMapping("/getExpressByOrderSnAndSkuId")
    public ApiResponse getExpressByOrderSnAndSkuId(String orderSn,Long skuId) {
        ExpressDetailsVO expressDetailsVO = null;
        try {
            //根据订单号查询查询详情里的快递公司id和单号
            DubboResult<EsOrderItemsDO> dubboResult = iEsOrderItemsService.getEsAfterSaleOrderItemsByOrderSnAndSkuId(orderSn, skuId);
            if (!dubboResult.isSuccess()){
                throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
            }
            EsOrderItemsDO orderItemsDO = dubboResult.getData();
            expressDetailsVO = new ExpressDetailsVO();
            DubboResult<ExpressDetailVO> expressFormDetail = this.expressPlatformService.getExpressFormDetail(orderItemsDO.getLogiId(), orderItemsDO.getShipNo());
            BeanUtil.copyProperties(expressFormDetail.getData(),expressDetailsVO);
            return ApiResponse.success(expressDetailsVO);
        } catch (ArgumentException ae) {
            ApiResponse.fail(ae.getExceptionCode(),ae.getMessage());
        }
        return ApiResponse.success(expressDetailsVO);
    }

    /**
     * 获取订单状态下拉别表
     * @param
     * @return
     */
    @PostMapping
    @ApiOperation(value = "获取订单状态下拉别表（订单查询时 共通的订单状态接口）")
    @ResponseBody
    public ApiResponse getOrderStatusList(){

        List<LabelValueBeanVO> list = new ArrayList<LabelValueBeanVO>();
        try{
            LabelValueBeanVO bean ;
            for(OrderStatusEnum e: OrderStatusEnum.values()){
                bean = new LabelValueBeanVO();
                if ("NEW".equals(e.value()) || "INTO_DB_ERROR".equals(e.value())){
                    continue;
                }
                if ("CONFIRM".equals(e.value())){
                    bean.setKey(e.value());
                    bean.setValue("待付款");
                }
                else if ("PAID_OFF".equals(e.value())){
                    bean.setKey(e.value());
                    bean.setValue("待发货");
                }
                else if ("SHIPPED".equals(e.value())){
                    bean.setKey(e.value());
                    bean.setValue("待收货");
                }
               else {
                    bean.setKey(e.value());
                    bean.setValue(e.description());
                }
                list.add(bean);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ApiResponse.success(list);
    }


    @ApiOperation(value = "查询订单状态的数量")
    @GetMapping(value = "/status-num")
    public ApiResponse getStatusNum() {
        Long id = ShiroKit.getUser().getId();
        DubboResult<OrderStatusNumVO> orderStatusNum = this.iesOrderService.getOrderStatusNum(id);
        return ApiResponse.success(orderStatusNum.getData());
    }


    @ApiOperation(value = "根据交易编号或者订单编号查询收银台数据",response = CashierVO.class)
    @ApiImplicitParam(name = "tradeSn", value = "交易编号", dataType = "string", paramType = "path")
    @GetMapping(value = "/cashier/{tradeSn}")
    @ResponseBody
    public ApiResponse getCashier(@PathVariable String tradeSn, HttpServletRequest request) {

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
        Double totalPrice = 0.0;
        Long closeOrderTime = 0l;
        String url = "";
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
                    totalPrice = MathUtil.add(tradeDO.getPayMoney(),tradeDO.getUseBalance());
                    payTypeText = tradeDO.getPaymentType();
                    shipMethod = tradeDO.getShipMethod();


                    String tradeStatus = tradeDO.getTradeStatus();
                    if (!"PAID_OFF".equals(tradeStatus)){
                        PayParamForm param = new PayParamForm();
                        param.setSn(tradeSn);
                        param.setPaymentPluginId("weixinPayPlugin");
                        param.setTradeType("trade");
                        param.setPayMode("qr");
                        param.setClientType("PC");
                        FormVO pay = orderPayManager.pay(param, request);
                        url = pay.getGatewayUrl();
                    }

                    // 系统配置 订单关闭时间
                    DubboResult<EsSettingsDO> result = iEsSettingsService.getByCfgGroup(SettingGroup.TRADE.name());
                    EsSettingsDO data = result.getData();
                    String value = data.getCfgValue();
                    EsOrderSettingVO esOrderSettingVO = JsonUtil.jsonToObject(value, EsOrderSettingVO.class);

                    Long createTime = tradeDO.getCreateTime();
                    // 计算毫秒
                    Integer closeOrderDay = esOrderSettingVO.getCloseOrderDay()*3600*1000;
                    Long time = createTime+closeOrderDay;
                    if (time - System.currentTimeMillis() > 0){
                        closeOrderTime = time - System.currentTimeMillis() < 0 ? 0 : time - System.currentTimeMillis();
                    }
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
        cashierVO.setTotalPrice(totalPrice);
        cashierVO.setShipTel(shipTel);
        cashierVO.setPayTypeText(payTypeText);
        cashierVO.setGoodsName(goodsName);
        cashierVO.setCloseOrderTime(closeOrderTime);
        cashierVO.setPayUrl(url);
        return ApiResponse.success(cashierVO);
    }
}

