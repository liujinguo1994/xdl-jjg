package com.xdl.jjg.manager;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.util.SnowflakeIdWorker;
import com.shopx.goods.api.model.domain.cache.EsGoodsCO;
import com.shopx.goods.api.model.domain.cache.EsGoodsSkuCO;
import com.shopx.goods.api.model.domain.enums.GoodsCachePrefix;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsGoodsSkuService;
import com.shopx.member.api.model.domain.EsCompanyDO;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.IEsCompanyService;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.constant.cacheprefix.PromotionCacheKeys;
import com.shopx.trade.api.model.domain.EsDeliveryMessageDO;
import com.shopx.trade.api.model.domain.EsSeckillTimetableDO;
import com.shopx.trade.api.model.domain.dto.CartDTO;
import com.shopx.trade.api.model.domain.dto.CartItemsDTO;
import com.shopx.trade.api.model.domain.dto.EsOrderDTO;
import com.shopx.trade.api.model.domain.dto.EsTradeDTO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.*;
import com.shopx.trade.api.service.*;
import com.shopx.trade.web.manager.event.TradeIntoDbEvent;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.shopx.common.util.BeanUtil.copyList;

/**
 * @ClassName: TradeManager
 * @Description: 交易服务层
 * @Author: libw  981087977@qq.com
 * @Date: 6/11/2019 10:37
 * @Version: 1.0
 */
@Component
public class TradeManager {
    private static Logger logger = LoggerFactory.getLogger(TradeManager.class);

//    @Reference(version = "${dubbo.application.version}",timeout = 5000)

    @Reference(version = "${dubbo.application.version}",interfaceClass = IEsTradeService.class, retries = 3, timeout = 5000, parameters = {
            "insertEsTradeIntoDB.retries", "0", "insertEsTradeIntoDB.timeout", "5000"})
    private IEsTradeService iesTradeService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsGoodsService iesGoodsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsPromotionGoodsService iEsPromotionGoodsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsGoodsSkuService iEsGoodsSkuService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService memberAddressService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCompanyService companyService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsDeliveryServiceService esDeliveryServiceService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShipCompanyDetailsService shipCompanyDetailsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSeckillApplyService esSeckillApplyService;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private CartManager cartManager;

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Autowired
    private TradePriceManager tradePriceManager;

    @Autowired
    private TradeIntoDbEvent tradeIntoDbEvent;

    public static final  SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0,0);


    public static EsTradeSnMoneyVO esTradeSnMoneyVO = new EsTradeSnMoneyVO();

    /**
     * 创建交易
     * @auther: LIUJG 344009799@qq.com
     * @date: 2019/06/11 10:38:52
     * @return: void
     */
    public ApiResponse createTrade(EsTradeDTO tradeDTO) {
        try {
            //小程序登录态标识
            String skey = tradeDTO.getSkey();

            if (tradeDTO == null){
                throw new ArgumentException(TradeErrorCode.TRADE_DATE_ERROR_CODE.getErrorCode(),TradeErrorCode.TRADE_DATE_ERROR_CODE.getErrorMsg());
            }
            // 结算价格信息
            PriceDetailVO tradePrice = tradePriceManager.getTradePrice(skey);

            // 存入数据库（存入前，判断商品库存，判断自提时间可用次数。判断余额不能大于订单金额）
            DubboResult dubboResult = iesTradeService.insertEsTradeIntoDB(tradeDTO, tradePrice);
            if (!dubboResult.isSuccess()) {
               throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
            }
            // 清除购物车选中的商品
            this.cartManager.cleanChecked(skey);
            // 清除价格
            this.tradePriceManager.cleanPrice(skey);
            // 清除自提信息
            this.checkoutParamManager.deleteDelivery(skey);
            // 清除备注信息
            this.checkoutParamManager.deleteRemark(skey);

            return ApiResponse.success();
        } catch (ArgumentException ae) {
            return ApiResponse.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("系统异常", th);
            return ApiResponse.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "下单失败");
        }

    }

    public String addressMessage(EsTradeDTO tradeDTO) {
        StringBuffer address = new StringBuffer();
        if (!StringUtils.isEmpty(tradeDTO.getConsigneeProvince())){
            address.append(tradeDTO.getConsigneeProvince());
        }
        if (!StringUtils.isEmpty(tradeDTO.getConsigneeCity())){
            address.append(tradeDTO.getConsigneeCityId());
        }
        if (!StringUtils.isEmpty(tradeDTO.getConsigneeCounty())){
            address.append(tradeDTO.getConsigneeCounty());
        }
        if (!StringUtils.isEmpty(tradeDTO.getConsigneeAddress())){
            address.append(tradeDTO.getConsigneeAddress());
        }
        if (!StringUtils.isEmpty(tradeDTO.getConsigneeTelephone())){
            address.append(tradeDTO.getConsigneeTelephone());
        }
        return address.toString();
    }

    /**
     * 封装交易信息
     * @return EsTradeDTO
     */
    private EsTradeDTO innerCreateTrade(String skey) {

        ShiroUser user = null;
        // 获取当前用户
        if (StringUtils.isBlank(skey)){
            user = ShiroKit.getUser();
        }else {
            //小程序获取当前用户
            user = this.getMemberApplet(skey);
        }

        EsCompanyDO esCompany = null;
        if (user.getCompanyCode() != null){
            DubboResult<EsCompanyDO> companyByCode = companyService.getCompanyByCode(user.getCompanyCode());
            if (companyByCode != null){
                esCompany = companyByCode.getData();
            }
        }
        // 参数缓存
        CheckoutParamVO param = checkoutParamManager.getParam(skey);
        Map<Integer, String> deliveryMessageVOMap = param.getDeliveryMessageVOMap();

        // 收货地址
        DubboResult<EsMemberAddressDO> addressDODubboResult = this.memberAddressService.getMemberAddress(param.getAddressId());
        EsMemberAddressDO address = addressDODubboResult.getData();
        // 获取购物车列表
        List<CartVO> cartList2 = this.cartManager.getCheckedGoodsItems(skey);

        // 结算价格信息
        PriceDetailVO tradePrice = tradePriceManager.getTradePrice(skey);

        //创建一个交易信息 并组装
        EsTradeDTO esTradeDTO = new EsTradeDTO();
        esTradeDTO.setId(snowflakeIdWorker.nextId());
        esTradeDTO.setTradeSn(String.valueOf(snowflakeIdWorker.nextId()));
        //会员信息
        esTradeDTO.setMemberId(user.getId());
        esTradeDTO.setMemberName(user.getName());
        //价格信息
        esTradeDTO.setTotalMoney(tradePrice.getTotalPrice());
        esTradeDTO.setGoodsMoney(tradePrice.getGoodsPrice());
        esTradeDTO.setFreightMoney(tradePrice.getFreightPrice());
        esTradeDTO.setDiscountMoney(tradePrice.getDiscountPrice());
        //收货人信息
        esTradeDTO.setConsigneeId(address.getId());
        esTradeDTO.setConsigneeName(address.getName());
        esTradeDTO.setConsigneeCountry(address.getCountry());
        esTradeDTO.setConsigneeCountryId(address.getCountyId());
        esTradeDTO.setConsigneeProvince(address.getProvince());
        esTradeDTO.setConsigneeProvinceId(address.getProvinceId());
        esTradeDTO.setConsigneeCity(address.getCity());
        esTradeDTO.setConsigneeCityId(address.getCityId());
        esTradeDTO.setConsigneeTown(address.getTown());
        esTradeDTO.setConsigneeTownId(address.getTownId());
        esTradeDTO.setConsigneeAddress(address.getAddress());
        esTradeDTO.setConsigneeMobile(address.getMobile());
        esTradeDTO.setCreateTime(System.currentTimeMillis());
        //交易状态
        esTradeDTO.setTradeStatus(OrderStatusEnum.CONFIRM.value());
        esTradeDTO.setIsDel(0);
        // 支付类型
        esTradeDTO.setPaymentType(param.getPaymentType().value());
        //余额支付金额
        esTradeDTO.setUseBalance(tradePrice.getBalance());

        List<EsOrderDTO> orderList = new ArrayList<EsOrderDTO>();

        //遍历购物车集合 封装订单对象
        EsCompanyDO finalEsCompany = esCompany;

        for (CartVO cartVO : cartList2) {
            CartDTO cartDTO = new CartDTO();
            // 设置自提信息
            String finalDeliveryText = "";
            String finalDeliveryTime = "";
            if (deliveryMessageVOMap != null){
                String message = deliveryMessageVOMap.get(new Integer(cartVO.getShopId().intValue()));
                if (message != null){
                    EsDeliveryMessageDO esDeliveryMessageDO = JsonUtil.jsonToObject(message, EsDeliveryMessageDO.class);
                    finalDeliveryText = this.deliveryText(esDeliveryMessageDO);
                    finalDeliveryTime = this.deliveryTime(esDeliveryMessageDO);
                }
            }
            List<CartItemsVO> itemsList2 = cartVO.getCartItemsList();
            List<CartItemsDTO> cartItemsDTO = copyList(itemsList2, CartItemsDTO.class);

            BeanUtils.copyProperties(cartVO, cartDTO);
            // 赋值 DTO
            cartDTO.setCartItemsList(cartItemsDTO);

            List<CartItemsDTO> cartItemsList = cartDTO.getCartItemsList();
            // 封装订单信息
            EsOrderDTO esOrderDTO = new EsOrderDTO();
            esOrderDTO.setCartItemsList(cartItemsList);
            esOrderDTO.setTradeSn(esTradeDTO.getTradeSn());
            esOrderDTO.setOrderSn(String.valueOf(snowflakeIdWorker.nextId()));
            //店铺信息
            esOrderDTO.setShopId(cartDTO.getShopId());
            esOrderDTO.setShopName(cartDTO.getShopName());
            //  会员信息
            esOrderDTO.setMemberId(user.getId());
            esOrderDTO.setMemberName(user.getName());
            esOrderDTO.setMobile(address.getMobile());
            //  收货人信息
            esOrderDTO.setShipName(address.getName());
            esOrderDTO.setShipAddr(address.getAddress());
            esOrderDTO.setShipCity(address.getCity());
            esOrderDTO.setShipCityId(address.getCityId());
            esOrderDTO.setShipCounty(address.getCounty());
            esOrderDTO.setShipCountyId(address.getCountyId());
            esOrderDTO.setShipMobile(address.getMobile());
            esOrderDTO.setShipProvince(address.getProvince());
            esOrderDTO.setShipProvinceId(address.getProvinceId());
            esOrderDTO.setShipTel(address.getTel());
            esOrderDTO.setShipMobile(address.getMobile());
            esOrderDTO.setShipTown(address.getTown());
            esOrderDTO.setShipTownId(address.getTownId());
            esOrderDTO.setReceiveTime(param.getReceiveTime());
            esOrderDTO.setDeliveryText(finalDeliveryText);
            esOrderDTO.setDeliveryTime(finalDeliveryTime);
            //  配送信息
            esOrderDTO.setShipMethod(cartDTO.getShippingTypeName());
            // 支付类型
            esOrderDTO.setPaymentType(param.getPaymentType().value());
            // 发票信息
            esOrderDTO.setNeedReceipt(0);
            esOrderDTO.setAddressId(param.getAddressId());
            if (param.getReceipt() != null) {
                esOrderDTO.setNeedReceipt(1);
            }
            // 0 在合作 1 未合作
            if (finalEsCompany != null && finalEsCompany.getIsDel() == 0) {
                esOrderDTO.setCompanyId(finalEsCompany.getId());
            }
            // 发票信息
            esOrderDTO.setReceiptVO(param.getReceipt());
            // 收货时间
            esOrderDTO.setReceiveTime(param.getReceiveTime());
            // 订单备注
            if (param.getRemark() != null) {
                esOrderDTO.setRemark(param.getRemark().get(cartVO.getShopId().intValue()));
            }
            // 订单来源
            esOrderDTO.setClientType(param.getClientType());
            //订单，付款，售后，货运状态
            esOrderDTO.setOrderState(OrderStatusEnum.CONFIRM.value());
            esOrderDTO.setPayState(PayStatusEnum.PAY_NO.value());
            esOrderDTO.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
            esOrderDTO.setShipState(ShipStatusEnum.SHIP_NO.value());
            esOrderDTO.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
            esOrderDTO.setCreateTime(System.currentTimeMillis());
            // 订单价格信息
            esOrderDTO.setGoodsMoney(cartDTO.getPrice().getGoodsPrice());
            logger.info("订单总金额：" + cartDTO.getPrice().getTotalPrice());
            esOrderDTO.setOrderMoney(cartDTO.getPrice().getTotalPrice());
            esOrderDTO.setDiscountMoney(cartDTO.getPrice().getDiscountPrice());

            esOrderDTO.setShippingMoney(cartDTO.getPrice().getFreightPrice());
            esOrderDTO.setFreshFreightPrice(cartDTO.getPrice().getFreshFreightPrice());
            esOrderDTO.setCommonFreightPrice(cartDTO.getPrice().getCommonFreightPrice());

            esOrderDTO.setGoodsNum(cartItemsList.size());
            esOrderDTO.setPrice(cartDTO.getPrice());
            esOrderDTO.setGiftCouponList(cartDTO.getGiftCouponList());
            esOrderDTO.setCouponList(cartDTO.getCouponList());
            esOrderDTO.setGiftList(cartDTO.getGiftList());
            esOrderDTO.setGiftPoint(cartDTO.getGiftPoint());
            orderList.add(esOrderDTO);

            esTradeDTO.setOrderList(orderList);
        }

        return esTradeDTO;
    }


    private EsTradeDTO innerCreateTradeYC(List<CartVO> cartList2) {
        // 获取会员的签约公司字符串
        ShiroUser user = ShiroKit.getUser();

        EsCompanyDO esCompany = null;
        if (user.getCompanyCode() != null){
            DubboResult<EsCompanyDO> companyByCode = companyService.getCompanyByCode(user.getCompanyCode());
            if (companyByCode != null){
                esCompany = companyByCode.getData();
            }
        }
        // 参数缓存
        CheckoutParamVO param = checkoutParamManager.getParam(null);
        Map<Integer, String> deliveryMessageVOMap = param.getDeliveryMessageVOMap();

        // 收货地址
        DubboResult<EsMemberAddressDO> addressDODubboResult = this.memberAddressService.getMemberAddress(param.getAddressId());
        EsMemberAddressDO address = addressDODubboResult.getData();
        // 获取购物车列表
//        List<CartVO> cartList2 = this.cartManager.getCheckedGoodsItems();

        // 结算价格信息
        PriceDetailVO tradePrice = tradePriceManager.getTradePrice(null);

        //创建一个交易信息 并组装
        EsTradeDTO esTradeDTO = new EsTradeDTO();
        esTradeDTO.setId(snowflakeIdWorker.nextId());
        esTradeDTO.setTradeSn(String.valueOf(snowflakeIdWorker.nextId()));
        //会员信息
        esTradeDTO.setMemberId(user.getId());
        esTradeDTO.setMemberName(user.getName());
        //价格信息
        esTradeDTO.setTotalMoney(tradePrice.getTotalPrice());
        esTradeDTO.setGoodsMoney(tradePrice.getGoodsPrice());
        esTradeDTO.setFreightMoney(tradePrice.getFreightPrice());
        esTradeDTO.setDiscountMoney(tradePrice.getDiscountPrice());
        //收货人信息
        esTradeDTO.setConsigneeId(address.getId());
        esTradeDTO.setConsigneeName(address.getName());
        esTradeDTO.setConsigneeCountry(address.getCountry());
        esTradeDTO.setConsigneeCountryId(address.getCountyId());
        esTradeDTO.setConsigneeProvince(address.getProvince());
        esTradeDTO.setConsigneeProvinceId(address.getProvinceId());
        esTradeDTO.setConsigneeCity(address.getCity());
        esTradeDTO.setConsigneeCityId(address.getCityId());
        esTradeDTO.setConsigneeTown(address.getTown());
        esTradeDTO.setConsigneeTownId(address.getTownId());
        esTradeDTO.setConsigneeAddress(address.getAddress());
        esTradeDTO.setConsigneeMobile(address.getMobile());
        esTradeDTO.setCreateTime(System.currentTimeMillis());
        //交易状态
        esTradeDTO.setTradeStatus(OrderStatusEnum.CONFIRM.value());
        esTradeDTO.setIsDel(0);
        // 支付类型
        esTradeDTO.setPaymentType(param.getPaymentType().value());
        //余额支付金额
        esTradeDTO.setUseBalance(tradePrice.getBalance());

        List<EsOrderDTO> orderList = new ArrayList<EsOrderDTO>();

        //遍历购物车集合 封装订单对象
        EsCompanyDO finalEsCompany = esCompany;

        for (CartVO cartVO : cartList2) {
            CartDTO cartDTO = new CartDTO();
            // 设置自提信息
            String finalDeliveryText = "";
            String finalDeliveryTime = "";
            if (deliveryMessageVOMap != null){
                String message = deliveryMessageVOMap.get(new Integer(cartVO.getShopId().intValue()));
                if (message != null){
                    EsDeliveryMessageDO esDeliveryMessageDO = JsonUtil.jsonToObject(message, EsDeliveryMessageDO.class);
                    finalDeliveryText = this.deliveryText(esDeliveryMessageDO);
                    finalDeliveryTime = this.deliveryTime(esDeliveryMessageDO);
                }
            }
            List<CartItemsVO> itemsList2 = cartVO.getCartItemsList();
            List<CartItemsDTO> cartItemsDTO = copyList(itemsList2, CartItemsDTO.class);

            BeanUtils.copyProperties(cartVO, cartDTO);
            // 赋值 DTO
            cartDTO.setCartItemsList(cartItemsDTO);

            List<CartItemsDTO> cartItemsList = cartDTO.getCartItemsList();
            // 封装订单信息
            EsOrderDTO esOrderDTO = new EsOrderDTO();
            esOrderDTO.setCartItemsList(cartItemsList);
            esOrderDTO.setTradeSn(esTradeDTO.getTradeSn());
            esOrderDTO.setOrderSn(String.valueOf(snowflakeIdWorker.nextId()));
            //店铺信息
            esOrderDTO.setShopId(cartDTO.getShopId());
            esOrderDTO.setShopName(cartDTO.getShopName());
            //  会员信息
            esOrderDTO.setMemberId(user.getId());
            esOrderDTO.setMemberName(user.getName());
            esOrderDTO.setMobile(address.getMobile());
            //  收货人信息
            esOrderDTO.setShipName(address.getName());
            esOrderDTO.setShipAddr(address.getAddress());
            esOrderDTO.setShipCity(address.getCity());
            esOrderDTO.setShipCityId(address.getCityId());
            esOrderDTO.setShipCounty(address.getCounty());
            esOrderDTO.setShipCountyId(address.getCountyId());
            esOrderDTO.setShipMobile(address.getMobile());
            esOrderDTO.setShipProvince(address.getProvince());
            esOrderDTO.setShipProvinceId(address.getProvinceId());
            esOrderDTO.setShipTel(address.getTel());
            esOrderDTO.setShipMobile(address.getMobile());
            esOrderDTO.setShipTown(address.getTown());
            esOrderDTO.setShipTownId(address.getTownId());
            esOrderDTO.setReceiveTime(param.getReceiveTime());
            esOrderDTO.setDeliveryText(finalDeliveryText);
            esOrderDTO.setDeliveryTime(finalDeliveryTime);
            //  配送信息
            esOrderDTO.setShipMethod(cartDTO.getShippingTypeName());
            // 支付类型
            esOrderDTO.setPaymentType(param.getPaymentType().value());
            // 发票信息
            esOrderDTO.setNeedReceipt(0);
            esOrderDTO.setAddressId(param.getAddressId());
            if (param.getReceipt() != null) {
                esOrderDTO.setNeedReceipt(1);
            }
            // 0 在合作 1 未合作
            if (finalEsCompany != null && finalEsCompany.getIsDel() == 0) {
                esOrderDTO.setCompanyId(finalEsCompany.getId());
            }
            // 发票信息
            esOrderDTO.setReceiptVO(param.getReceipt());
            // 收货时间
            esOrderDTO.setReceiveTime(param.getReceiveTime());
            // 订单备注
            if (param.getRemark() != null) {
                esOrderDTO.setRemark(param.getRemark().get(cartVO.getShopId().intValue()));
            }
            // 订单来源
            esOrderDTO.setClientType(param.getClientType());
            //订单，付款，售后，货运状态
            esOrderDTO.setOrderState(OrderStatusEnum.CONFIRM.value());
            esOrderDTO.setPayState(PayStatusEnum.PAY_NO.value());
            esOrderDTO.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
            esOrderDTO.setShipState(ShipStatusEnum.SHIP_NO.value());
            esOrderDTO.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
            esOrderDTO.setCreateTime(System.currentTimeMillis());
            // 订单价格信息
            esOrderDTO.setGoodsMoney(cartDTO.getPrice().getGoodsPrice());
            logger.info("订单总金额：" + cartDTO.getPrice().getTotalPrice());
            esOrderDTO.setOrderMoney(cartDTO.getPrice().getTotalPrice());
            esOrderDTO.setDiscountMoney(cartDTO.getPrice().getDiscountPrice());

            esOrderDTO.setShippingMoney(cartDTO.getPrice().getFreightPrice());
            esOrderDTO.setFreshFreightPrice(cartDTO.getPrice().getFreshFreightPrice());
            esOrderDTO.setCommonFreightPrice(cartDTO.getPrice().getCommonFreightPrice());

            esOrderDTO.setGoodsNum(cartItemsList.size());
            esOrderDTO.setPrice(cartDTO.getPrice());
            esOrderDTO.setGiftCouponList(cartDTO.getGiftCouponList());
            esOrderDTO.setCouponList(cartDTO.getCouponList());
            esOrderDTO.setGiftList(cartDTO.getGiftList());
            esOrderDTO.setGiftPoint(cartDTO.getGiftPoint());
            orderList.add(esOrderDTO);

            esTradeDTO.setOrderList(orderList);
        }

        return esTradeDTO;
    }

    private String deliveryText(EsDeliveryMessageDO esDeliveryMessageDO) {
        // 拼接自提信息文本
        String deliveryText = "";
        if (esDeliveryMessageDO != null){
            deliveryText = "自提点：" + esDeliveryMessageDO.getDeliveryName() + "，地址："
                    + esDeliveryMessageDO.getAddress();
        }
        return deliveryText;
    }

    private String deliveryTime(EsDeliveryMessageDO esDeliveryMessageDO) {
        // 拼接自提信息文本
        String deliveryTime = "";
        if (esDeliveryMessageDO != null){

            Long selfDate = esDeliveryMessageDO.getSelfDate();
            if (selfDate != null){
                String NYR = new SimpleDateFormat("yyyy-MM-dd").format(selfDate);
                Long startTime = esDeliveryMessageDO.getStartTime();
                Long endTime = esDeliveryMessageDO.getEndTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Date date1 = new Date(startTime);//将时间调整为yyyy-MM-dd HH:mm:ss时间样式
                Date date2 = new Date(endTime);
                String SFStart = simpleDateFormat.format(date1);
                String SFEnd = simpleDateFormat.format(date2);
                deliveryTime = NYR + " " + SFStart + "-" + SFEnd;
            }

        }
        return deliveryTime;
    }


    // todo
    private String shippingMethod(List<CartItemsVO> itemsList2) {

        AtomicReference<String> method = new AtomicReference<>("");

            List<CartItemsVO> cartItemsList = null;

            cartItemsList = itemsList2.stream()
                    .filter(cartItems -> !"notInScope".equals(cartItems.getDeliveryMethod())).collect(Collectors.toList());

            Map<String, List<CartItemsVO>> collect = cartItemsList.stream().collect(Collectors.groupingBy(CartItemsVO::getDeliveryMethod));

            Iterator it=collect.keySet().iterator();
            while(it.hasNext()){
                //取出key
                String key=it.next().toString();
                if ("express".equals(key)){
                    method.set("快递");
                    continue;
                }
                method.set("自提");
            }

        return method.get();
    }

    /**
     * 检测购物车信息
     */
//    @SuppressWarnings("unchecked")
//    private ApiResponse checkTrade() {
//        boolean flag = true;
//        //读取结算参数
//         CheckoutParamVO param = checkoutParamManager.getParam();
//
//        // 会员信息CheckoutParamVO(addressId=286, paymentType=ONLINE, receipt=ReceiptVO(type=null, receiptType=null, receiptTitle=null, receiptContent=null, taxNo=null), receiveTime=任意时间, remark={}, clientType=PC, deliveryMessageVOMap=null, deliveryMessageVO=null)
//        Long memberId = ShiroKit.getUser().getId();
//          if (memberId == null) {
//               throw new ArgumentException(TradeErrorCode.LOGIN_INVALIDATION.getErrorCode(),TradeErrorCode.LOGIN_INVALIDATION.getErrorMsg());
//          }
//        //已选中结算的商品
//        List<CartVO> cartList1 = this.cartManager.getCheckedGoodsItems();
//
//        //1、检测购物车是否为空
//        if (cartList1 == null || cartList1.isEmpty()) {
//            throw new ArgumentException(TradeErrorCode.CART_EMPTY.getErrorCode(), TradeErrorCode.CART_EMPTY.getErrorMsg());
//        }
//
//        //验证后存在商品问题的集合
//        List<Map> goodsErrorList = new ArrayList();
//        //验证后存在运费模板问题的集合
//        List<Map> goodsNotInScopeList = new ArrayList();
//        //验证商品数量不足的集合
//        List<Map> goodsLowList = new ArrayList();
//        //验证后存在促销活动问题的集合
//        List<Map> promotionErrorList = new ArrayList();
//
//        //验证收货地址
//        DubboResult<EsMemberAddressDO> addressDODubboResult = this.memberAddressService.getMemberAddress(param.getAddressId());
//        if (!addressDODubboResult.isSuccess()) {
//            throw new ArgumentException(TradeErrorCode.PLEASE_FILL_ADDRESS.getErrorCode(), TradeErrorCode.PLEASE_FILL_ADDRESS.getErrorMsg());
//        }
//        EsMemberAddressDO addressDO = addressDODubboResult.getData();
//        // 遍历购物车列表
//        cartList1.stream().forEach(cartVO -> {
//
//            List<CartItemsVO> cartItemsListVO = cartVO.getCartItemsList();
//            // 遍历购物车中商品明细信息
//                for (CartItemsVO cartItemsVO : cartItemsListVO ) {
//
//                Map errorMap = new HashMap(16);
//                errorMap.put("name", cartItemsVO.getName());
//                errorMap.put("image", cartItemsVO.getGoodsImage());
//                Long goodsId = cartItemsVO.getGoodsId();
//                String goodsCache = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+goodsId);
//                EsGoodsCO goodsData = null;
//                if(!StringUtils.isBlank(goodsCache)){
//                    goodsData = JsonUtil.jsonToObject(goodsCache,EsGoodsCO.class);
//                }else{
//                    DubboResult<EsGoodsCO> esGoods = iesGoodsService.getEsGoods(goodsId);
//                    if(esGoods.isSuccess()){
//                        goodsData = esGoods.getData();
//                    }
//                }
//                if(cartItemsVO.getIsDelivery() == 1){
//                    //读取结算参数
//                    Map<Integer, String> deliveryMessageVOMap = param.getDeliveryMessageVOMap();
//                    if (deliveryMessageVOMap == null ){
//                        throw  new ArgumentException(TradeErrorCode.SAVE_DELIVERY.getErrorCode(),TradeErrorCode.SAVE_DELIVERY.getErrorMsg());
//                    }
//                }else {
//                    if("notInScope".equals(cartItemsVO.getDeliveryMethod())){
//                        goodsNotInScopeList.add(errorMap);
//                        break;
//                    }
//                }
//                //  判断该商品是否存在
//                if (goodsData == null ){
//                    goodsErrorList.add(errorMap);
//                    break;
//                }
//                //  判断该商品的审核状态0 待审核，1 审核通过 2 未通过
//                if ( goodsData.getIsAuth().intValue() == 0 || goodsData.getIsAuth().intValue() == 2){
//                    goodsErrorList.add(errorMap);
//                    break;
//                }
//                //  判断该商品的删除状态
//                //  判断该商品的上下架状态
//                if (goodsData.getIsDel().intValue() == 1 || goodsData.getMarketEnable().intValue() == 2){
//                    goodsErrorList.add(errorMap);
//                    break;
//                }
//
//                // 通过SkuId判断购买该商品SKU数量是否满足 购买数量
//                Long skuId = cartItemsVO.getSkuId() == null ? -1 : cartItemsVO.getSkuId();
//                // 根据SKUId 查询该商品sku 的可用库存 以及状态是否正常(是否启用0代表启用 1代表不启用/起购数量)
//
//                //获取该商品的sku 信息
//                String goodsSku = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + skuId);
//                EsGoodsSkuCO esGoodsSku = null;
//
//                //先看缓存中是否存在 ,不存在进行数据库查询
//                if (!StringUtils.isBlank(goodsSku)){
//                    esGoodsSku = JsonUtil.jsonToObject(goodsSku, EsGoodsSkuCO.class);
//                }else {
//                    DubboResult<EsGoodsSkuCO> skuCODubboResult = iEsGoodsSkuService.getGoodsSku(skuId);
//                    if (skuCODubboResult.isSuccess()){
//                        esGoodsSku = skuCODubboResult.getData();
//                    }
//                }
//
//                // 判断该sku 商品是否启用
//                if (esGoodsSku.getIsEnable() == 2){
//                    goodsErrorList.add(errorMap);
//                    break;
//                }
//                // 判断可购买数量
//                if (cartItemsVO.getNum() > esGoodsSku.getEnableQuantity()){
//                    goodsLowList.add(errorMap);
//                    break;
//                }
//
//                //此商品参与的活动（promotionList 是第二版产物，用于计算活动 isCheck会根据选中改变，第一版的singleList和groupList由于存在代码业务，暂时不清理）
//                List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
//                        for (TradePromotionGoodsVO promotionGoodsVO:promotionList) {
//                            if (promotionGoodsVO.getIsCheck().intValue() == 1){
//                                // 公司折扣没有时间
//                                if (!/*"COMPANY_DISCOUNT".equals(promotionGoodsVO.getPromotionType()) || */"NO".equals(promotionGoodsVO.getPromotionType())){
//                                    //获取当前活动的结束时间
//                                    Long endTime = promotionGoodsVO.getEndTime();
//                                    //获取当前时间
//                                    long currentTimeMillis = System.currentTimeMillis();
//                                    //比较时间 如果当前时间大于结束时间 则不能下单
//                                    if (currentTimeMillis > endTime){
//                                        promotionErrorList.add(errorMap);
//                                        break;
//                                    }
//                                }
//                            }
//                        }
////                //此商品参与的组合活动
////                List<TradePromotionGoodsVO> groupList = cartItemsVO.getGroupList();
////                    for (TradePromotionGoodsVO groupPromotionGoodsVO:groupList) {
////                        if (groupPromotionGoodsVO.getIsCheck().intValue() == 1){
////                            //获取当前活动的结束时间
////                            Long endTime = groupPromotionGoodsVO.getEndTime();
////                            //获取当前时间
////                            long currentTimeMillis = System.currentTimeMillis();
////                            //比较时间 如果当前时间大于结束时间 则不能下单
////                            if (currentTimeMillis > endTime){
////                                promotionErrorList.add(errorMap);
////                                break;
////                            }
////                        }
////                    }
//
//                    //1 生鲜 2 非生鲜
//                    if (cartItemsVO.getIsFresh() == 1){
//                        //生鲜先判断配送区域
//                        DubboResult<Boolean> byAreaId = shipCompanyDetailsService.getByAreaId(1, String.valueOf(addressDO.getCountyId()));
//                        if (!byAreaId.getData()){
//                            throw  new ArgumentException(TradeErrorCode.AREA_NOT_SUPPORTED.getErrorCode(),TradeErrorCode.AREA_NOT_SUPPORTED.getErrorMsg());
//                        }
//                    }
//                }
//
//        });
//        if (!goodsLowList.isEmpty()) {
//            return ApiResponse.fail(goodsLowList,TradeErrorCode.OVERSELL_ERROR.getErrorCode(),TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
//        }
//        if (!goodsErrorList.isEmpty()) {
//            return ApiResponse.fail(goodsErrorList,TradeErrorCode.NO_GOODS_ERROR.getErrorCode(),TradeErrorCode.NO_GOODS_ERROR.getErrorMsg());
//        }
//        if (!promotionErrorList.isEmpty()) {
//            return ApiResponse.fail(promotionErrorList,TradeErrorCode.PROMOTION_ERROR.getErrorCode(),TradeErrorCode.PROMOTION_ERROR.getErrorMsg());
//        }
//        if (!goodsNotInScopeList.isEmpty()) {
//            return ApiResponse.fail(goodsNotInScopeList,TradeErrorCode.GOODS_NOT_IN_AREA.getErrorCode(),TradeErrorCode.GOODS_NOT_IN_AREA.getErrorMsg());
//        }
//        return ApiResponse.success(flag);
//    }

    @SuppressWarnings("unchecked")
    private ApiResponse checkTrade(String skey) {
        //读取结算参数
        CheckoutParamVO param = checkoutParamManager.getParam(skey);
        // 获取操作人id（会员id）
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        if (memberId == null) {
            throw new ArgumentException(TradeErrorCode.LOGIN_INVALIDATION.getErrorCode(),TradeErrorCode.LOGIN_INVALIDATION.getErrorMsg());
        }
        //已选中结算的商品
        List<CartVO> cartList1 = this.cartManager.getCheckedGoodsItems(skey);

        //1、检测购物车是否为空
        if (cartList1 == null || cartList1.isEmpty()) {
            throw new ArgumentException(TradeErrorCode.CART_EMPTY.getErrorCode(), TradeErrorCode.CART_EMPTY.getErrorMsg());
        }

        //验证收货地址
        DubboResult<EsMemberAddressDO> addressDODubboResult = this.memberAddressService.getMemberAddress(param.getAddressId());
        if (!addressDODubboResult.isSuccess()) {
            throw new ArgumentException(TradeErrorCode.PLEASE_FILL_ADDRESS.getErrorCode(), TradeErrorCode.PLEASE_FILL_ADDRESS.getErrorMsg());
        }
        EsMemberAddressDO addressDO = addressDODubboResult.getData();
        // 遍历购物车列表
        cartList1.stream().forEach(cartVO -> {

            List<CartItemsVO> cartItemsListVO = cartVO.getCartItemsList();
            // 遍历购物车中商品明细信息
            for (CartItemsVO cartItemsVO : cartItemsListVO ) {

                Long goodsId = cartItemsVO.getGoodsId();
                String goodsCache = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+goodsId);
                EsGoodsCO goodsData = null;
                if(!StringUtils.isBlank(goodsCache)){
                    goodsData = JsonUtil.jsonToObject(goodsCache,EsGoodsCO.class);
                }else{
                    DubboResult<EsGoodsCO> esGoods = iesGoodsService.getEsGoods(goodsId);
                    if(esGoods.isSuccess()){
                        goodsData = esGoods.getData();
                    }
                }
                if(cartItemsVO.getIsDelivery() == 1){
                    //读取结算参数
                    Map<Integer, String> deliveryMessageVOMap = param.getDeliveryMessageVOMap();
                    if (deliveryMessageVOMap == null ){
                        throw new ArgumentException(TradeErrorCode.SAVE_DELIVERY.getErrorCode(),TradeErrorCode.SAVE_DELIVERY.getErrorMsg());
                    }
                }else {
                    if("notInScope".equals(cartItemsVO.getDeliveryMethod())){
                        throw new ArgumentException(TradeErrorCode.GOODS_NOT_IN_AREA.getErrorCode(),TradeErrorCode.GOODS_NOT_IN_AREA.getErrorMsg());
                    }
                }

                // 通过SkuId判断购买该商品SKU数量是否满足 购买数量
                Long skuId = cartItemsVO.getSkuId() == null ? -1 : cartItemsVO.getSkuId();

                //获取该商品的sku 信息
                String goodsSku = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + skuId);
                EsGoodsSkuCO esGoodsSku = null;

                //先看缓存中是否存在 ,不存在进行数据库查询
                if (!StringUtils.isBlank(goodsSku)){
                    esGoodsSku = JsonUtil.jsonToObject(goodsSku, EsGoodsSkuCO.class);
                }else {
                    DubboResult<EsGoodsSkuCO> skuCODubboResult = iEsGoodsSkuService.getGoodsSku(skuId);
                    if (skuCODubboResult.isSuccess()){
                        esGoodsSku = skuCODubboResult.getData();
                    }
                }

                //  判断该商品是否存在 ；判断该商品的审核状态0 待审核，1 审核通过 2 未通过；判断该商品的删除状态 判断该商品的上下架状态； 判断该sku 商品是否启用
                if (goodsData == null || goodsData.getIsAuth().intValue() != 1 || goodsData.getIsDel().intValue() == 1
                        || goodsData.getMarketEnable().intValue() == 2 || esGoodsSku.getIsEnable() == 2){
                    throw new ArgumentException(TradeErrorCode.NO_GOODS_ERROR.getErrorCode(),TradeErrorCode.NO_GOODS_ERROR.getErrorMsg());
                }

                // 判断可购买数量
                if (cartItemsVO.getNum() > esGoodsSku.getEnableQuantity()){
                    throw new ArgumentException(TradeErrorCode.OVERSELL_ERROR.getErrorCode(),TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
                }

                //此商品参与的活动（promotionList 是第二版产物，用于计算活动 isCheck会根据选中改变，第一版的singleList和groupList由于存在代码业务，暂时不清理）
                List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
                promotionList.forEach(promotionGoodsVO -> {
                    if (promotionGoodsVO.getIsCheck().intValue() == 1){
                        // 公司折扣没有时间
                        if (!"NO".equals(promotionGoodsVO.getPromotionType())){
                            //获取当前活动的结束时间
                            Long endTime = promotionGoodsVO.getEndTime();
                            //获取当前时间
                            long currentTimeMillis = System.currentTimeMillis();
                            //比较时间 如果当前时间大于结束时间 则不能下单
                            if (currentTimeMillis > endTime){
                                throw new ArgumentException(TradeErrorCode.PROMOTION_ERROR.getErrorCode(),TradeErrorCode.PROMOTION_ERROR.getErrorMsg());
                            }
                        }
                    }
                });
                //1 生鲜 2 非生鲜
                if (cartItemsVO.getIsFresh() == 1){
                    //生鲜先判断配送区域
                    DubboResult<Boolean> byAreaId = shipCompanyDetailsService.getByAreaId(1, String.valueOf(addressDO.getCountyId()));
                    if (!byAreaId.getData()){
                        throw  new ArgumentException(TradeErrorCode.AREA_NOT_SUPPORTED.getErrorCode(),TradeErrorCode.AREA_NOT_SUPPORTED.getErrorMsg());
                    }
                }
            }

        });

        return ApiResponse.success();
    }


    @SuppressWarnings("unchecked")
    private ApiResponse checkTradeYC(List<CartVO> cartList1) {
        //读取结算参数
        CheckoutParamVO param = checkoutParamManager.getParam(null);
        // 会员信息
        Long memberId = ShiroKit.getUser().getId();
        if (memberId == null) {
            throw new ArgumentException(TradeErrorCode.LOGIN_INVALIDATION.getErrorCode(),TradeErrorCode.LOGIN_INVALIDATION.getErrorMsg());
        }
        //已选中结算的商品
//        List<CartVO> cartList1 = this.cartManager.getCheckedGoodsItems();

        //1、检测购物车是否为空
        if (cartList1 == null || cartList1.isEmpty()) {
            throw new ArgumentException(TradeErrorCode.CART_EMPTY.getErrorCode(), TradeErrorCode.CART_EMPTY.getErrorMsg());
        }

        //验证收货地址
        DubboResult<EsMemberAddressDO> addressDODubboResult = this.memberAddressService.getMemberAddress(param.getAddressId());
        if (!addressDODubboResult.isSuccess()) {
            throw new ArgumentException(TradeErrorCode.PLEASE_FILL_ADDRESS.getErrorCode(), TradeErrorCode.PLEASE_FILL_ADDRESS.getErrorMsg());
        }
        EsMemberAddressDO addressDO = addressDODubboResult.getData();
        // 遍历购物车列表
        cartList1.stream().forEach(cartVO -> {

            List<CartItemsVO> cartItemsListVO = cartVO.getCartItemsList();
            // 遍历购物车中商品明细信息
            for (CartItemsVO cartItemsVO : cartItemsListVO ) {

                Long goodsId = cartItemsVO.getGoodsId();
                String goodsCache = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+goodsId);
                EsGoodsCO goodsData = null;
                if(!StringUtils.isBlank(goodsCache)){
                    goodsData = JsonUtil.jsonToObject(goodsCache,EsGoodsCO.class);
                }else{
                    DubboResult<EsGoodsCO> esGoods = iesGoodsService.getEsGoods(goodsId);
                    if(esGoods.isSuccess()){
                        goodsData = esGoods.getData();
                    }
                }
                if(cartItemsVO.getIsDelivery() == 1){
                    //读取结算参数
                    Map<Integer, String> deliveryMessageVOMap = param.getDeliveryMessageVOMap();
                    if (deliveryMessageVOMap == null ){
                        throw new ArgumentException(TradeErrorCode.SAVE_DELIVERY.getErrorCode(),TradeErrorCode.SAVE_DELIVERY.getErrorMsg());
                    }
                }else {
                    if("notInScope".equals(cartItemsVO.getDeliveryMethod())){
                        throw new ArgumentException(TradeErrorCode.GOODS_NOT_IN_AREA.getErrorCode(),TradeErrorCode.GOODS_NOT_IN_AREA.getErrorMsg());
                    }
                }

                // 通过SkuId判断购买该商品SKU数量是否满足 购买数量
                Long skuId = cartItemsVO.getSkuId() == null ? -1 : cartItemsVO.getSkuId();

                //获取该商品的sku 信息
                String goodsSku = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + skuId);
                EsGoodsSkuCO esGoodsSku = null;

                //先看缓存中是否存在 ,不存在进行数据库查询
                if (!StringUtils.isBlank(goodsSku)){
                    esGoodsSku = JsonUtil.jsonToObject(goodsSku, EsGoodsSkuCO.class);
                }else {
                    DubboResult<EsGoodsSkuCO> skuCODubboResult = iEsGoodsSkuService.getGoodsSku(skuId);
                    if (skuCODubboResult.isSuccess()){
                        esGoodsSku = skuCODubboResult.getData();
                    }
                }

                //  判断该商品是否存在 ；判断该商品的审核状态0 待审核，1 审核通过 2 未通过；判断该商品的删除状态 判断该商品的上下架状态； 判断该sku 商品是否启用
                if (goodsData == null || goodsData.getIsAuth().intValue() != 1 || goodsData.getIsDel().intValue() == 1
                        || goodsData.getMarketEnable().intValue() == 2 || esGoodsSku.getIsEnable() == 2){
                    throw new ArgumentException(TradeErrorCode.NO_GOODS_ERROR.getErrorCode(),TradeErrorCode.NO_GOODS_ERROR.getErrorMsg());
                }

                // 判断可购买数量
                if (cartItemsVO.getNum() > esGoodsSku.getEnableQuantity()){
                    throw new ArgumentException(TradeErrorCode.OVERSELL_ERROR.getErrorCode(),TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
                }

                //此商品参与的活动（promotionList 是第二版产物，用于计算活动 isCheck会根据选中改变，第一版的singleList和groupList由于存在代码业务，暂时不清理）
                List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
                promotionList.forEach(promotionGoodsVO -> {
                    if (promotionGoodsVO.getIsCheck().intValue() == 1){
                        // 公司折扣没有时间
                        if (!"NO".equals(promotionGoodsVO.getPromotionType())){
                            //获取当前活动的结束时间
                            Long endTime = promotionGoodsVO.getEndTime();
                            //获取当前时间
                            long currentTimeMillis = System.currentTimeMillis();
                            //比较时间 如果当前时间大于结束时间 则不能下单
                            if (currentTimeMillis > endTime){
                                throw new ArgumentException(TradeErrorCode.PROMOTION_ERROR.getErrorCode(),TradeErrorCode.PROMOTION_ERROR.getErrorMsg());
                            }
                            long timeMillis = System.currentTimeMillis();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                            String format = simpleDateFormat.format(timeMillis);
                            String redisKey = PromotionCacheKeys.getSeckillKey(format);
                            // 获取当前场次
                            int timeline = -1;
                            DubboResult<EsSeckillTimetableDO> inSeckillNow = esSeckillApplyService.getInSeckillNow();
                            if (inSeckillNow.isSuccess()) {
                                timeline = inSeckillNow.getData().getTimeline();
                            }

                            String hget = this.jedisCluster.hget(redisKey, timeline + "");
                            List<SeckillGoodsVO> seckillGoodsVOS = JsonUtil.jsonToList(hget, SeckillGoodsVO.class);
                            seckillGoodsVOS.forEach(seckillGoodsVO -> {
                                // 可售数量
                                Integer remainQuantity = seckillGoodsVO.getRemainQuantity();
                                if (remainQuantity - cartItemsVO.getNum() < 0 && cartItemsVO.getSkuId().intValue() == seckillGoodsVO.getSkuId().intValue()){
                                    throw new ArgumentException(TradeErrorCode.PROMOTION_KUCUN_ERROR.getErrorCode(),TradeErrorCode.PROMOTION_KUCUN_ERROR.getErrorMsg());
                                }

                            });

                        }
                    }
                });
                //1 生鲜 2 非生鲜
                if (cartItemsVO.getIsFresh() == 1){
                    //生鲜先判断配送区域
                    DubboResult<Boolean> byAreaId = shipCompanyDetailsService.getByAreaId(1, String.valueOf(addressDO.getCountyId()));
                    if (!byAreaId.getData()){
                        throw  new ArgumentException(TradeErrorCode.AREA_NOT_SUPPORTED.getErrorCode(),TradeErrorCode.AREA_NOT_SUPPORTED.getErrorMsg());
                    }
                }
            }

        });

        return ApiResponse.success();
    }

    public ApiResponse createStockReduceMQ(String skey) {
        try {
            // 下单前的检查
            long start = System.currentTimeMillis();
            logger.info("下单前的检查开始时间：[{}]",start);
            ApiResponse apiResponse = this.checkTrade(skey);
            long end = System.currentTimeMillis();
            logger.info("下单前的检查结束时间：[{}]；下单前的检查耗时--->[{}]",end,end-start);
            // 存在商品或者活动问题
            if (apiResponse.getStatus() != 0){
                return ApiResponse.fail(apiResponse.getData(),apiResponse.getStatus(),apiResponse.getError());
            }
            long zzStart = System.currentTimeMillis();
            logger.info("组装订单开始时间：[{}]",zzStart);
            // 组装主订单和子订单
            EsTradeDTO tradeDTO = this.innerCreateTrade(skey);
            long zzEnd = System.currentTimeMillis();
            logger.info("组装订单结束时间：[{}]；组装订单耗时--->[{}]",zzEnd,zzEnd-zzStart);
            // 所有减库存操作，以及发送商品减库存MQ操作
            long rkStart = System.currentTimeMillis();
            logger.info("订单入库开始时间：[{}]",zzStart);
            tradeDTO.setSkey(skey);
            DubboResult<EsTradeSnMoneyVO> dubboResult = this.tradeIntoDbEvent.onTradeIntoDb(tradeDTO);
            long rkEnd = System.currentTimeMillis();
            logger.info("订单入库结束时间：[{}]；订单入库耗时--->[{}]",rkEnd,rkEnd-rkStart);
            if (dubboResult.isSuccess()){
                EsTradeSnMoneyVO tradeSnMoneyVO = dubboResult.getData();
                return ApiResponse.success(tradeSnMoneyVO);
            }else {
                throw new ArgumentException(dubboResult.getCode(), dubboResult.getMsg());
            }
        } catch (ArgumentException ae) {
            return ApiResponse.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("系统异常", th);
            return ApiResponse.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "下单失败");
        }
    }

    public ApiResponse createStockReduceMQYC(List<CartVO> cartVOList) {
        try {
            // 下单前的检查
            long start = System.currentTimeMillis();
            logger.info("下单前的检查开始时间：[{}]",start);
            ApiResponse apiResponse = this.checkTradeYC(cartVOList);
            long end = System.currentTimeMillis();
            logger.info("下单前的检查结束时间：[{}]；下单前的检查耗时--->[{}]",end,end-start);
            // 存在商品或者活动问题
            if (apiResponse.getStatus() != 0){
                return ApiResponse.fail(apiResponse.getData(),apiResponse.getStatus(),apiResponse.getError());
            }
            long zzStart = System.currentTimeMillis();
            logger.info("组装订单开始时间：[{}]",zzStart);
            // 组装主订单和子订单
            EsTradeDTO tradeDTO = this.innerCreateTradeYC(cartVOList);
            long zzEnd = System.currentTimeMillis();
            logger.info("组装订单结束时间：[{}]；组装订单耗时--->[{}]",zzEnd,zzEnd-zzStart);
            // 所有减库存操作，以及发送商品减库存MQ操作
            long rkStart = System.currentTimeMillis();
            logger.info("订单入库开始时间：[{}]",zzStart);
            DubboResult<EsTradeSnMoneyVO> dubboResult = this.tradeIntoDbEvent.onTradeIntoDbYC(tradeDTO);
            long rkEnd = System.currentTimeMillis();
            logger.info("订单入库结束时间：[{}]；订单入库耗时--->[{}]",rkEnd,rkEnd-rkStart);
            if (dubboResult.isSuccess()){
                EsTradeSnMoneyVO tradeSnMoneyVO = dubboResult.getData();
                return ApiResponse.success(tradeSnMoneyVO);
            }else {
                throw new ArgumentException(dubboResult.getCode(), dubboResult.getMsg());
            }
        } catch (ArgumentException ae) {
            return ApiResponse.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("系统异常", th);
            return ApiResponse.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "下单失败");
        }
    }

    /**
     * 小程序获取当前用户ID
     */
    private Long getMemberIdApplet(String skey) {
        //获取当前用户
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            throw new ArgumentException(TradeErrorCode.NOT_LOGIN.getErrorCode(),TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        return memberId;
    }

    /**
     * 小程序获取当前用户
     */
    private ShiroUser getMemberApplet(String skey) {
        //获取当前用户
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            throw new ArgumentException(TradeErrorCode.NOT_LOGIN.getErrorCode(),TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(memberDO.getId());
        shiroUser.setName(memberDO.getName());
        shiroUser.setCompanyCode(memberDO.getCompanyCode());
        return shiroUser;
    }
}
