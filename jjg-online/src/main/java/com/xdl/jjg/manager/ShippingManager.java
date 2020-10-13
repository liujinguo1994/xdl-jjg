package com.xdl.jjg.manager;

import com.alibaba.fastjson.JSON;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.MathUtil;
import com.shopx.goods.api.model.domain.vo.EsGoodsVO;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.constant.cacheprefix.TradeCachePrefix;
import com.shopx.trade.api.model.domain.EsFreightTemplateDetailDO;
import com.shopx.trade.api.model.domain.EsShipCompanyDetailsDO;
import com.shopx.trade.api.model.domain.ShippingPrice;
import com.shopx.trade.api.model.domain.vo.CartItemsVO;
import com.shopx.trade.api.model.domain.vo.CartVO;
import com.shopx.trade.api.model.domain.vo.EsBuyerOrderVO;
import com.shopx.trade.api.model.domain.vo.EsFreightTemplateDetailVO;
import com.shopx.trade.api.service.IEsFreightTemplateDetailService;
import com.shopx.trade.api.service.IEsShipCompanyDetailsService;
import com.shopx.trade.api.service.IEsShipTemplateService;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运费计算业务层实现类
 *
 * @author Snow create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class ShippingManager {

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService memberAddressService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShipTemplateService shipTemplateService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFreightTemplateDetailService freightTemplateDetailService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShipCompanyDetailsService shipCompanyDetailsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iesMemberAddressService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @Autowired
    private TradePriceManager tradePriceManager;
    @Autowired
    private PromotionToolManager promotionToolManager;

    @Autowired
    private JedisCluster jedisCluster;
    /**
     * 获取购物车价格
     * （原）
     * @param cartList 购物车
     * @return
     */
    public Double getShipPrice(List<CartVO> cartList) {
        // 最终运费
        double finalShip = 0;
        for (CartVO cartVO : cartList) {
            List<CartItemsVO> cartSkuList = cartVO.getCartItemsList();
            for (CartItemsVO cartSkuVO : cartSkuList) {
                // 购物车商品没有被选中或者 购物车商品是否包邮（2 包邮，1 不包邮），是否自提,（1：是 2：否），是否参加活动
                boolean check =
                        cartSkuVO.getChecked() == 0 || cartSkuVO.getIsFreeFreight() == 2 || cartSkuVO.getIsDelivery() == 1
                                || !cartSkuVO.getSingleList().isEmpty() || !cartSkuVO.getGroupList().isEmpty();
                if (check) {
                    continue;
                }

                // 获取购物车 运费模版 映射
                Map<Long, EsFreightTemplateDetailVO> map = cartVO.getShipTemplateChildMap();
                // 获取当前商品的运费模版
                EsFreightTemplateDetailVO temp = map.get(cartSkuVO.getSkuId());
                if (temp == null) {
                    cartSkuVO.setShipPrice(0.0);
                    continue;
                }

                // 运费金额
                double shipPrice = temp.getFirstTip();
                // 计算基数  重量/数量
                Double purchase;
                //  重量算运费
                purchase = MathUtil.multiply(cartSkuVO.getGoodsWeight(), cartSkuVO.getNum());

                // 是否需要计算 续重/续件
                if (temp.getFirstWeight() < purchase) {
                    //重量 / 续重=续重金额的倍数
                    double count = (purchase - temp.getFirstWeight()) / temp.getSequelWeight();
                    //向上取整计算为运费续重倍数
                    count = Math.ceil(count);
                    // 运费 = 首重价格 + 续重倍数 * 续重费用
                    shipPrice = MathUtil.add(shipPrice,
                            MathUtil.multiply(count, temp.getSequelTip()));
                }
                cartSkuVO.setShipPrice(shipPrice);
                finalShip = MathUtil.add(finalShip, shipPrice);
            }
            // 加上活动非生鲜运费
            finalShip = MathUtil.add(finalShip, cartVO.getNotFreshFreightPrice());
            // 加上活动生鲜运费
            finalShip = MathUtil.add(finalShip, cartVO.getFreshFreightPrice());
        }
        return finalShip;
    }

    /**
     * 计算单店铺 的购物车价格
     * @param cartVo 购物车
     * @return
     */
    public ShippingPrice getSingleShipPrice(CartVO cartVo, List<Long> cateIds) {
        // 生鲜非活动总量
        Double xianSum=0d;
        // 非生鲜非活动重量
        Double normalSum=0d;

        // 生鲜活动总量
        Double xianHdSum=0d;
        // 非生鲜活动重量
        Double normalHdSum=0d;
        // 最终运费
        Double finalShip = 0.0;
        // 生鲜总费用
        Double freshFreightPrice = 0.0;
        // 非生鲜总费用
        Double notFreshFreightPrice = 0.0;
        // 非活动普通运费
        Double nothdNormalShip = 0.0;
        // 非活动生鲜运费
        Double nothdxianShip = 0.0;
        // 活动普通运费
        Double hdNormalShip = 0.0;
        // 活动生鲜运费
        Double hdxianShip = 0.0;

        // 生鲜非活动
        Double  xianpurchase = 0.0;
        // 非生鲜非活动
        Double  normalpurchase = 0.0;


        Double xian=0d;
        Double normal=0d;

            List<CartItemsVO> cartSkuList = cartVo.getCartItemsList();
        List<CartItemsVO> collect1 = cartSkuList.stream().filter(cartItemsVO -> (cartItemsVO.getChecked() != 0)).collect(Collectors.toList());
        for (CartItemsVO cartSkuVO : collect1) {

                // 购物车商品没有被选中或者 购物车商品是否包邮（2 包邮，1 不包邮），是否自提,（1：是 2：否），是否参加活动
                boolean check =
                        cartSkuVO.getChecked() == 0 || cartSkuVO.getIsFreeFreight() == 2 || cartSkuVO.getIsDelivery() == 1
                                || cartSkuVO.getSingleList().size() > 1|| !cartSkuVO.getGroupList().isEmpty();
                if (check) {
                    continue;
                }

                // 获取购物车 运费模版 映射
                Map<Long, EsFreightTemplateDetailVO> map = cartVo.getShipTemplateChildMap();
                // 获取当前商品的运费模版
                EsFreightTemplateDetailVO temp = map.get(cartSkuVO.getSkuId());
                if (temp == null) {
                    cartSkuVO.setShipPrice(0.0);
                    continue;
                }

                // 非活动商品重量
                if (cateIds.contains(cartSkuVO.getCategoryId()) ){
                    freshFreightPrice = freshFreightPrice + cartSkuVO.getCartPrice();

                    xianSum =MathUtil.add(xianSum,MathUtil.multiply(cartSkuVO.getGoodsWeight(),cartSkuVO.getNum()));

                    // 运费金额
                    Double shipPrice = temp.getFirstTip();

                    // 是否需要计算 续重/续件
                    if (temp.getFirstWeight() < xianSum) {
                        //重量 / 续重=续重金额的倍数
                        Double weight = temp.getSequelWeight() == 0 ? 1: temp.getSequelWeight();
                        Double tip = temp.getSequelTip() == 0 ? 1: temp.getSequelTip();
                        Double count = (xianSum - temp.getFirstWeight()) / weight;
                        //向上取整计算为运费续重倍数
                        count = Math.ceil(count);
                        // 运费 = 首重价格 + 续重倍数 * 续重费用
                        shipPrice = MathUtil.add(shipPrice,
                                MathUtil.multiply(count, tip));
                        nothdxianShip = shipPrice;
                    }else {
                        nothdxianShip = shipPrice;
                    }
                }else{
                    notFreshFreightPrice = notFreshFreightPrice + cartSkuVO.getCartPrice();
                    normalSum =MathUtil.add(normalSum,MathUtil.multiply(cartSkuVO.getGoodsWeight(),cartSkuVO.getNum()));

                    // 运费金额
                    Double shipPrice = temp.getFirstTip();

                    // 是否需要计算 续重/续件
                    if (temp.getFirstWeight() < normalSum) {
                        //重量 / 续重=续重金额的倍数
                        Double weight = temp.getSequelWeight() == 0 ? 1: temp.getSequelWeight();
                        Double tip = temp.getSequelTip() == 0 ? 1: temp.getSequelTip();
                        Double count = (normalSum - temp.getFirstWeight()) / weight;
                        //向上取整计算为运费续重倍数
                        count = Math.ceil(count);
                        // 运费 = 首重价格 + 续重倍数 * 续重费用
                        shipPrice = MathUtil.add(shipPrice,
                                MathUtil.multiply(count, tip));
                        nothdNormalShip = shipPrice;
                    }else {
                        nothdNormalShip = shipPrice;
                    }

                }

            }
          /*  //------
            CheckoutParamVO param = checkoutParamManager.getParam();
            DubboResult<EsMemberAddressDO> memberAddressDubbo = iesMemberAddressService.getMemberAddress(param.getAddressId());
            if(!memberAddressDubbo.isSuccess() || memberAddressDubbo.getData() == null){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

            // 购物车总重
            Double weight = cartVo.getWeight();
            //活动商品重量 = 总重 - 生鲜非活动重量 - 非生鲜非活动重量
            Double subtract = MathUtil.add(xianSum, normalSum);
            Double hdWeight = MathUtil.subtract(weight,subtract);

            // 筛选出来 活动的生鲜商品项

            List<CartItemsVO> collect = cartSkuList.stream()
                    // 购物车商品被选中并且不免运费的场合
                    .filter(sku -> sku.getChecked() == 1 && sku.getIsFreeFreight() == 1 && sku.getIsFresh() == 1
                            && !sku.getSingleList().isEmpty() && !sku.getGroupList().isEmpty()).collect(Collectors.toList());

            for (CartItemsVO cartItemsVO : collect) {
                xianHdSum = xianHdSum +cartItemsVO.getGoodsWeight();
            }
            normalHdSum =MathUtil.subtract(hdWeight,xianHdSum);

            //收货人
            EsBuyerOrderVO buyerOrder = new EsBuyerOrderVO();
            EsMemberAddressDO data = memberAddressDubbo.getData();
            buyerOrder.setShipCityId(data.getCountyId());

            if(xianHdSum >0){
                xian= xian + countFreight(buyerOrder,freshFreightPrice,xianHdSum,1,cartVo.getShopId());//生鲜运费
            }
            if (normalHdSum>0){
                normal= normal + countFreight(buyerOrder,notFreshFreightPrice,normalHdSum,2,cartVo.getShopId());//非生鲜
            }*/
            //两种运费合计,活动和非活动的也要计算进去
            // 普通总运费
            Double ptShipTotal = MathUtil.add(normal, nothdNormalShip);
            // 生鲜总运费
            Double sxShipTotal = MathUtil.add(xian, nothdxianShip);
            finalShip = MathUtil.add(ptShipTotal,sxShipTotal);
            // 封装生鲜和非生鲜 总运费
            ShippingPrice shippingPrice = new ShippingPrice();
            shippingPrice.setFreightPrice(finalShip);
            shippingPrice.setCommonFreightPrice(ptShipTotal);
            shippingPrice.setFreshFreightPrice(sxShipTotal);
            return shippingPrice;
    }

    public Double countFreight(EsBuyerOrderVO buyerOrder, Double price, Double normalSum, Integer type,Long shopId) {
        //1 生鲜 2 非生鲜
        if (type == 1){
            //生鲜先判断配送区域
            DubboResult<Boolean> byAreaId = shipCompanyDetailsService.getByAreaId(type, String.valueOf(buyerOrder.getShipCityId()));
            if (!byAreaId.getData()){
                throw  new ArgumentException(TradeErrorCode.AREA_NOT_SUPPORTED.getErrorCode(),TradeErrorCode.AREA_NOT_SUPPORTED.getErrorMsg());
            }
        }

        Double money = 0d;

        DubboResult<EsShipCompanyDetailsDO> shipCompanyDetailsDODubboResult = shipCompanyDetailsService.getByTypeAndPrice(type, String.valueOf(buyerOrder.getShipCityId()),
                price,shopId);
        if(!shipCompanyDetailsDODubboResult.isSuccess()){
            throw  new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        EsShipCompanyDetailsDO shipCompanyDetailsDO = shipCompanyDetailsDODubboResult.getData();
        if(shipCompanyDetailsDO.getFirstCompany() !=null){
            if (normalSum > shipCompanyDetailsDO.getFirstCompany()) {
                //运费计算
                Double continueW=MathUtil.subtract(normalSum,shipCompanyDetailsDO.getFirstCompany());//续重
                Double aa=MathUtil.add(shipCompanyDetailsDO.getFirstPrice(),MathUtil.multiply(Math.ceil(continueW),shipCompanyDetailsDO.getContinuedPrice()));
                money= aa;
            } else {
                money=shipCompanyDetailsDO.getFirstPrice();
            }
        }

        return money;
    }

    /**
     * 设置配送方式及价格
     *
     * @param cartList 购物车
     * @author: libw 981087977@qq.com
     * @date: 2019/07/02 17:01:26
     * @return: void
     */
    public void setShippingPrice(List<CartVO> cartList, String where,Integer isDelivery,String skey) {
        Long addressId = checkoutParamManager.getParam(skey).getAddressId();
        DubboResult result = memberAddressService.getMemberAddress(addressId);
        if (!result.isSuccess()) {
            throw new ArgumentException(result.getCode(), result.getMsg());
        }

        EsMemberAddressDO address = (EsMemberAddressDO) result.getData();
        // 获取操作人id
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        if (address == null || !address.getMemberId().equals(memberId)) {
            throw new ArgumentException(TradeErrorCode.ADDRESS_NOT_EXIST.getErrorCode(),
                    TradeErrorCode.ADDRESS_NOT_EXIST.getErrorMsg());
        }
        Long areaId = this.actualAddress(address);
        this.checkAreaNotInScope(cartList, areaId,where,isDelivery,skey);

    }

    /**
     * 校验地区
     *
     * @param cartList 购物车
     * @param areaId   地区
     * @return
     */
    public void checkAreaNotInScope(List<CartVO> cartList, Long areaId,String where,Integer isDelivery,String skey) {

        cartList.stream().forEach(cartVO -> {
            //运费模版映射
            Map<Long, EsFreightTemplateDetailVO> shipMap = new HashMap<>(16);
            List<CartItemsVO> cartItemList = cartVO.getCartItemsList();

            // 设置非活动商品的运费模板
            List<CartItemsVO> collect = cartItemList.stream()
                    // 购物车商品被选中并且不免运费的场合
                    .filter(sku -> sku.getChecked() == 1/* && sku.getIsFreeFreight() == 1*/
                            /*&& (sku.getSingleList().isEmpty() || sku.getGroupList().isEmpty())*/).collect(Collectors.toList());

            collect.forEach(sku -> {
                        // 获取运费模板信息 没有运费模版的话 记录错误的商品，禁止下单
                        // 判断商品是否包邮
                        // 获取模板详情
                        DubboPageResult shipDetailResult =
                                this.freightTemplateDetailService.getFreightTemplateDetailListByModeId(sku.getTemplateId());
                        if (!shipDetailResult.isSuccess()) {
                            throw new ArgumentException(shipDetailResult.getCode(), shipDetailResult.getMsg());
                        }
                        List<EsFreightTemplateDetailDO> shipDetail = shipDetailResult.getData().getList();
                        // 如果是设置自提方式
                        if ("delivery".equals(where)){
                            // 设置默认配送方式 配送方式 notInScope 不在配送范围，express 快递(默认)，selfMention 自提
                            if (isDelivery == 1){
                                // 判断该商品是否支持自提
                                if (sku.getIsDelivery() == 1){
                                    sku.setDeliveryMethod("selfMention");

                                }else if (sku.getIsDelivery() == 2){
                                    sku.setDeliveryMethod("express");

                                }
                            }else if (isDelivery == 2 ){
                                sku.setDeliveryMethod("express");
                                sku.setIsDelivery(2);
                            }
                        }else if ("address".equals(where)){
                            DubboResult<Boolean> byAreaId = shipCompanyDetailsService.getByAreaId(2, String.valueOf(areaId));
                            if (byAreaId.getData().booleanValue()){
                                if (!"selfMention".equals(sku.getDeliveryMethod())){
                                    sku.setDeliveryMethod("express");
                                }
                            }
                            if (isDelivery == 2 ){
                                sku.setDeliveryMethod("express");
                                sku.setIsDelivery(2);
//                                checkoutParamManager.deleteDelivery();
                            }
                            if (shipDetail.isEmpty() && sku.getTemplateId() != 0) {
                                // 设置默认配送方式 配送方式 notInScope 不在配送范围，express 快递(默认)，selfMention 自提
                                sku.setDeliveryMethod("notInScope");
                            } else {
                                for (EsFreightTemplateDetailDO child : shipDetail) {
                                    if (!StringUtils.isEmpty(child.getArea())) {
                                        shipDetail.stream().map(EsFreightTemplateDetailDO::getId).collect(Collectors.toList());
                                        // 校验地区
                                        if (child.getAreaId().contains(areaId)) {
                                            EsFreightTemplateDetailVO freightTemplateDetailVO = new EsFreightTemplateDetailVO();
                                            BeanUtil.copyProperties(child, freightTemplateDetailVO);
                                            shipMap.put(sku.getSkuId(), freightTemplateDetailVO);
                                        }
                                    }
                                }

                                // 如果没有匹配 则当前地区无货
                                if (!shipMap.containsKey(sku.getSkuId()) && sku.getTemplateId() != 0) {
                                    sku.setDeliveryMethod("notInScope");

                                }
                            }
                        }
                    });
//            cartVO.setShipTemplateChildMap(shipMap);
        });
        // 存到缓存里
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        this.putCache(cacheKey, cartList);
    }

    private void putCache(String cacheKey, List<CartVO> itemList) {
        //重新压入缓存
        this.jedisCluster.set(cacheKey, JSON.toJSONString(itemList));
    }

    /**
     * 获取当前SessionKey
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/06/11 14:14:30
     * @return: java.lang.String
     */
    private String getSessionKey() {
        String cacheKey = "";
        // 获取用户信息
        ShiroUser buyer = ShiroKit.getUser();

        // 拼接购物车Key + 用户id
        if (buyer != null) {
            cacheKey = TradeCachePrefix.CART_MEMBER_ID_KEY.getPrefix() + buyer.getId();
        }

        return cacheKey;
    }

    /**
     * 小程序获取当前用户购物车的key
     */
    private String getSessionKeyApplet(String skey) {
        //获取当前用户
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            throw new ArgumentException(TradeErrorCode.NOT_LOGIN.getErrorCode(),TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        // 拼接购物车Key + 用户id
        String cacheKey = TradeCachePrefix.CART_MEMBER_ID_KEY.getPrefix() + memberId;
        return cacheKey;
    }
    /**
     * 校验地区
     *
     * @param cartList 购物车
     * @param areaId   地区
     * @return
     */
    public List<EsGoodsVO> checkArea(List<CartVO> cartList, Long areaId) {
        // 创建异常商品List
        List<EsGoodsVO> errorGoods = new ArrayList<>();
        for (CartVO cartVO : cartList) {
            //运费模版映射
            Map<Long, EsFreightTemplateDetailVO> shipMap = new HashMap<>(16);
            List<CartItemsVO> cartItemList = cartVO.getCartItemsList();

            // 设置非活动商品的运费模板
            cartItemList.stream()
                    // 购物车商品被选中并且不免运费的场合
                    .filter(sku -> sku.getChecked() == 1 && sku.getIsFreeFreight() == 1 && sku.getSingleList().isEmpty() && sku.getGroupList().isEmpty())
                    .forEach(sku -> {
                        // 获取运费模板信息 没有运费模版的话 记录错误的商品，禁止下单
                        // 获取模板详情
                        DubboPageResult shipDetailResult =
                                this.freightTemplateDetailService.getFreightTemplateDetailListByModeId(sku.getTemplateId());
                        if (!shipDetailResult.isSuccess()) {
                            throw new ArgumentException(shipDetailResult.getCode(), shipDetailResult.getMsg());
                        }
                        List<EsFreightTemplateDetailDO> shipDetail = shipDetailResult.getData().getList();

                        // 如果模版空
                        if (shipDetail.isEmpty()) {
                            EsGoodsVO goodsVO = new EsGoodsVO();
                            goodsVO.setGoodsName(sku.getName());
                            goodsVO.setOriginal(sku.getGoodsImage());
                            errorGoods.add(goodsVO);
                        } else {
                            for (EsFreightTemplateDetailDO child : shipDetail) {
                                if (!StringUtils.isEmpty(child.getArea())) {
                                    shipDetail.stream().map(EsFreightTemplateDetailDO::getId).collect(Collectors.toList());
                                    // 校验地区
                                    if (child.getAreaId().contains(areaId)) {
                                        EsFreightTemplateDetailVO freightTemplateDetailVO = new EsFreightTemplateDetailVO();
                                        BeanUtil.copyProperties(child, freightTemplateDetailVO);
                                        shipMap.put(sku.getSkuId(), freightTemplateDetailVO);
                                    }
                                }
                            }

                            // 如果没有匹配 则当前地区无货
                            if (!shipMap.containsKey(sku.getSkuId())) {
                                EsGoodsVO goodsVO = new EsGoodsVO();
                                goodsVO.setGoodsName(sku.getName());
                                goodsVO.setOriginal(sku.getGoodsImage());
                                errorGoods.add(goodsVO);
                            }
                        }
                    });
            cartVO.setShipTemplateChildMap(shipMap);
            // 设置活动非生鲜运费金额
            errorGoods.addAll(this.countNotFresh(cartVO, areaId));
            // 设置活动生鲜运费金额
            errorGoods.addAll(this.countFresh(cartVO, areaId));
        }
        return errorGoods;
    }

    /**
     * 计算非生鲜运费
     *
     * @param cartVO 购物车
     * @param areaId 地区
     * @author: libw 981087977@qq.com
     * @date: 2019/07/03 13:34:19
     * @return: List<EsGoodsVO>
     */
    private List<EsGoodsVO> countNotFresh(CartVO cartVO, Long areaId) {
        // 创建异常商品List
        List<EsGoodsVO> errorGoods = new ArrayList<>();

        // 活动非生鲜商品总金额
        Double totalPrice = 0.0;
        // 活动非生鲜商品总重量
        Double totalWeight = 0.0;
        List<CartItemsVO> cartItemList = cartVO.getCartItemsList();

        // 累加计算
        for (CartItemsVO cartItem : cartItemList) {
            boolean condition = cartItem.getChecked() == 1 && cartItem.getIsFreeFreight() != 2
                    && (!cartItem.getSingleList().isEmpty() || !cartItem.getGroupList().isEmpty()) && cartItem.getIsFresh() == 0;
            if (condition) {
                totalPrice += MathUtil.multiply(cartItem.getGoodsPrice(), cartItem.getNum());
                totalWeight += cartItem.getGoodsWeight();

                errorGoods.add(errorGoodsInfo(cartItem));
            }
        }

        // 初始化新的运费模板
        Double shipPrice = getActivityPrice(totalPrice, totalWeight, !errorGoods.isEmpty(), areaId);
        if (shipPrice == null) {
            return errorGoods;
        }

        cartVO.setNotFreshFreightPrice(shipPrice);
        return new ArrayList<>();
    }

    /**
     * 设置异常商品信息
     * @author: libw 981087977@qq.com
     * @date: 2019/07/03 16:10:44
     * @param cartItem  购物车项
     * @return: com.shopx.goods.api.model.domain.vo.EsGoodsVO
     */
    private EsGoodsVO errorGoodsInfo(CartItemsVO cartItem) {
        EsGoodsVO goodsVO = new EsGoodsVO();
        goodsVO.setGoodsName(cartItem.getName());
        goodsVO.setOriginal(cartItem.getGoodsImage());
        return goodsVO;
    }

    /**
     *
     * 计算活动生鲜商品商品运费
     *
     * @param cartVO 购物车
     * @param areaId 地区
     * @author: libw 981087977@qq.com
     * @date: 2019/07/03 13:34:19
     * @return: void
     */
    private List<EsGoodsVO> countFresh(CartVO cartVO, Long areaId) {
        // 创建异常商品List
        List<EsGoodsVO> errorGoods = new ArrayList<>();

        // 活动非生鲜商品总金额
        Double totalPrice = 0.0;
        // 活动非生鲜商品总重量
        Double totalWeight = 0.0;
        List<CartItemsVO> cartItemList = cartVO.getCartItemsList();

        // 累加计算
        for (CartItemsVO cartItem : cartItemList) {
            boolean condition = cartItem.getChecked() == 1 && cartItem.getIsFreeFreight() != 2
                    && (!cartItem.getSingleList().isEmpty() || !cartItem.getGroupList().isEmpty()) && cartItem.getIsFresh() == 1;
            if (condition) {
                totalPrice += MathUtil.multiply(cartItem.getGoodsPrice(), cartItem.getNum());
                totalWeight += cartItem.getGoodsWeight();

                errorGoods.add(errorGoodsInfo(cartItem));
            }
        }

        // 获取活动商品运费
        Double shipPrice = getActivityPrice(totalPrice, totalWeight, !errorGoods.isEmpty(), areaId);
        if (shipPrice == null) {
            return errorGoods;
        }

        cartVO.setFreshFreightPrice(shipPrice);
        return new ArrayList<>();
    }

    /**
     * 获取活动运费模板计算运费
     *
     *
     * @param totalPrice
     * @param totalWeight
     * @param exits  是否执行
     * @param areaId 地区ID
     * @author: libw 981087977@qq.com
     * @date: 2019/07/03 15:23:53
     * @return: com.shopx.trade.api.model.domain.vo.EsFreightTemplateDetailVO
     */
    private Double getActivityPrice(Double totalPrice, Double totalWeight, boolean exits, Long areaId) {
        // 是否构建模板
        boolean isBuild = false;

        // 初始化一个新的运费模板
        EsFreightTemplateDetailVO template = new EsFreightTemplateDetailVO();

        // 存在的话，查询运费模板
        if (exits) {
            // TODO trade 根据商品总金额和生鲜字段，获取模板详情
            DubboPageResult shipDetailResult = null;
//                                this.freightTemplateDetailService.getFreightTemplateDetailList();
            if (!shipDetailResult.isSuccess()) {
                throw new ArgumentException(shipDetailResult.getCode(), shipDetailResult.getMsg());
            }
            List<EsFreightTemplateDetailDO> shipDetail = (List<EsFreightTemplateDetailDO>) shipDetailResult;

            for (EsFreightTemplateDetailDO child : shipDetail) {
                if (!StringUtils.isEmpty(child.getArea())) {
                    // 校验地区
                    if (child.getArea().contains("," + areaId + ",")) {
                        // 取首重价格最高的模板
                        if (new BigDecimal(template.getFirstTip()).compareTo(new BigDecimal(child.getFirstTip())) < 0) {
                            template.setFirstTip(child.getFirstTip());
                            template.setFirstWeight(child.getFirstWeight());
                        }

                        // 取续重价格最高的模板
                        if (new BigDecimal(template.getSequelTip()).compareTo(new BigDecimal(child.getSequelTip())) < 0) {
                            template.setSequelTip(child.getSequelTip());
                            template.setSequelWeight(child.getSequelWeight());
                        }

                        isBuild = true;
                    }
                }
            }

        }

        // 没有构建的时候返回 null
        if (!isBuild) {
            return null;
        }

        // 运费金额
        double shipPrice = template.getFirstTip();
        // 是否需要计算 续重/续件
        if (template.getFirstWeight() < totalWeight) {
            //重量 / 续重=续重金额的倍数
            double count = (totalWeight - template.getFirstWeight()) / template.getSequelWeight();
            //向上取整计算为运费续重倍数
            count = Math.ceil(count);
            // 运费 = 首重价格 + 续重倍数 * 续重费用
            shipPrice = MathUtil.add(shipPrice,
                    MathUtil.multiply(count, template.getSequelTip()));
        }

        return shipPrice;
    }

    /**
     * 获取最低级地区
     *
     * @param address 收货地址实体类
     * @author: libw 981087977@qq.com
     * @date: 2019/07/02 17:26:47
     * @return: java.lang.Long
     */
    public Long actualAddress(EsMemberAddressDO address) {
        if (address.getCountyId() != null && address.getCountyId() != 0) {
            return address.getCountyId();
        }
        if (address.getCityId() != null && address.getCityId() != 0) {
            return address.getCityId();
        }
        return address.getProvinceId();
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
}
