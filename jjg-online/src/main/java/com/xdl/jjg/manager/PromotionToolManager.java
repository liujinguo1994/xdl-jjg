package com.xdl.jjg.manager;


import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.MathUtil;
import com.shopx.goods.api.model.domain.EsCategoryDO;
import com.shopx.goods.api.service.IEsCategoryService;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsMemberCouponDTO;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.member.api.service.IEsMemberCouponService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsFullDiscountDO;
import com.shopx.trade.api.model.domain.EsShipCompanyDetailsDO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.IEsFullDiscountService;
import com.shopx.trade.api.service.IEsShipCompanyDetailsService;
import com.shopx.trade.api.utils.CurrencyUtil;
import com.shopx.trade.web.constant.ApplicationContextHolder;
import com.shopx.trade.web.manager.event.PromotionEvent;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 促销工具实现类
 *
 * @author Snow create in 2018/3/21
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class PromotionToolManager {
    private static Logger logger = LoggerFactory.getLogger(PromotionToolManager.class);

    @Autowired
    private TradePriceManager tradePriceManager;

    @Autowired
    private CartManager cartManager;

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Autowired
    private ShippingManager shippingManager;

    @Autowired
    private JedisCluster jedisCluster;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCategoryService categoryService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iesMemberAddressService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShipCompanyDetailsService shipCompanyDetailsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFullDiscountService fullDiscountService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCouponService iEsMemberCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;



    @Value("${black.card.promotion.start}")
    private Long blackCardPromotionStart;
    @Value("${black.card.promotion.end}")
    private Long blackCardPromotionEnd;
    @Value("${black.card.money}")
    private Double money;
    @Value("${black.card.discount}")
    private Double discount;

    /**
     * 拷贝参数to活动列表
     * @param cartVO
     * @return
     */
    protected CartVO copySkuToPromotion(CartVO cartVO){

        /**
         * 购物车展示数据根据促销活动组合
         */
        List<GroupPromotionVO> promotionList = new ArrayList<>();

        List<Long> skuIds =new LinkedList<>();
        //遍历所有商品的集合，查找组合活动的商品，并组织好活动与商品的(一对多)关系
        for(CartItemsVO skuVO:cartVO.getCartItemsList()){
            if(skuVO.getChecked() == 0){
                continue;
            }

            //商品已参与的组合促销集合
            List<TradePromotionGoodsVO> groupList = skuVO.getGroupList();

            if(groupList!=null && !groupList.isEmpty()){

                TradePromotionGoodsVO promotionGoodsVO = groupList.get(0);
                //活动id
                Long activityId = promotionGoodsVO.getActivityId();
                //活动工具类型
                String promotionType = promotionGoodsVO.getPromotionType();

                //如果展示促销活动集合为空，则新建一个
                if(promotionList.isEmpty()){
                    GroupPromotionVO groupPromotionVo = new GroupPromotionVO();
                    List<CartItemsVO> skuVOList = new ArrayList<>();

                    skuVOList.add(skuVO);
                    groupPromotionVo.setSkuList(skuVOList);
                    groupPromotionVo.setPromotionType(promotionType);

                    DubboResult fullDiscountResult = this.fullDiscountService.getFullDiscountForCache(activityId);

                    if (!fullDiscountResult.isSuccess()){
                        throw new  ArgumentException(fullDiscountResult.getCode(),fullDiscountResult.getMsg());
                    }

                    groupPromotionVo.setActivityDetail(fullDiscountResult.getData());
                    groupPromotionVo.setIsGroup(1);
                    promotionList.add(groupPromotionVo);
                }else{

                    //用于判断当前商品参与的组合活动是否存在 展示促销活动集合中。
                    boolean flag = false;
                    //遍历展示促销活动集合
                    for (GroupPromotionVO groupPromotionVo : promotionList) {

                        //满优惠的Vo
                        EsFullDiscountVO discountVo = (EsFullDiscountVO) groupPromotionVo.getActivityDetail();

                        //判断当前商品参与的活动，是否存在于促销活动集合中。
                        if(activityId.equals(discountVo.getId()) && promotionType.equals(PromotionTypeEnum.FULL_DISCOUNT.name())){
                            groupPromotionVo.getSkuList().add(skuVO);
                            flag=false;
                            break;	//结束循环遍历展示促销活动集合
                        }else{
                            flag = true;
                        }
                    }

                    //如果不存在
                    if(flag){
                        GroupPromotionVO groupPromotionVo = new GroupPromotionVO();
                        List<CartItemsVO> skuVOList = new ArrayList<>();
                        skuVOList.add(skuVO);
                        groupPromotionVo.setSkuList(skuVOList);
                        groupPromotionVo.setPromotionType(promotionType);
                        DubboResult fullDiscountResult = this.fullDiscountService.getFullDiscountForCache(activityId);
                        if (!fullDiscountResult.isSuccess()){
                            throw new  ArgumentException(fullDiscountResult.getCode(),fullDiscountResult.getMsg());
                        }
                        groupPromotionVo.setActivityDetail(fullDiscountResult.getData());
                        groupPromotionVo.setIsGroup(1);
                        promotionList.add(groupPromotionVo);
                    }

                }
                skuIds.add(skuVO.getSkuId());
                continue;
            }
        }


        //没有参与组合活动的商品
        List<CartItemsVO> singleProduct = new ArrayList<>();

        //遍历所有商品的集合
        for(CartItemsVO skuVO:cartVO.getCartItemsList()){
            //如果不属于组合活动的商品，则进入
            if(!skuIds.contains(skuVO.getSkuId())){
                singleProduct.add(skuVO);
            }
        }

        if(!singleProduct.isEmpty()){
            GroupPromotionVO groupPromotionVo = new GroupPromotionVO();
            groupPromotionVo.setSkuList(singleProduct);
            groupPromotionVo.setIsGroup(2);
            promotionList.add(groupPromotionVo);
        }
        cartVO.setPromotionList(promotionList);
        this.countPromotionPrice(cartVO);
        return cartVO;
    }

    /**
     * 计算购物车内各满优惠活动中的商品小计总和<br>
     * 1、此处价格仅为展示购物车页时，计算满优惠活动的一些价格展示作用。
     * @param cartVO
     */
    protected void countPromotionPrice(CartVO cartVO){


        List<GroupPromotionVO> promotionList = cartVO.getPromotionList();
        for (GroupPromotionVO groupPromotionVo : promotionList) {

            if(cartVO.getPrice().getTotalPrice()<=0){
                groupPromotionVo.setSpreadPrice(0d);
                groupPromotionVo.setSubtotal(0d);
                groupPromotionVo.setDiscountPrice(0d);
                return;
            }

            //判断是组合活动 && 活动中是否有商品
            if(groupPromotionVo.getIsGroup()!=null && groupPromotionVo.getIsGroup().intValue()==1 && !groupPromotionVo.getSkuList().isEmpty()){

                EsFullDiscountDO discountDo = (EsFullDiscountDO) groupPromotionVo.getActivityDetail();

                for (CartItemsVO skuVO : groupPromotionVo.getSkuList()) {
                    groupPromotionVo.setSubtotal(MathUtil.add(groupPromotionVo.getSubtotal(), skuVO.getSubtotal()));
                }

                //满金额
                Double fullMoney = discountDo.getFullMoney();

                //活动中所有商品的总价
                Double totalSubtotal = groupPromotionVo.getSubtotal();

                //差额
                double spreadPrice = MathUtil.subtract(fullMoney, totalSubtotal);

                groupPromotionVo.setSpreadPrice(spreadPrice<0?0:spreadPrice);
                double discountPrice = 0.0;
                if(spreadPrice<0){
                    /**满减优惠计算*/
                    if(discountDo.getIsFullMinus()!=null && discountDo.getIsFullMinus()==1 && discountDo.getMinusValue() != null) {
                        discountPrice = discountDo.getMinusValue();
                    }
                    /**满折优惠计算*/
                    if(discountDo.getIsDiscount()!=null && discountDo.getIsDiscount()==1 && discountDo.getDiscountValue() != null) {
                        double percentage = MathUtil.multiply(discountDo.getDiscountValue(), 0.1);
                        discountPrice = MathUtil.subtract(totalSubtotal, MathUtil.multiply(totalSubtotal, percentage));
                    }
                    groupPromotionVo.setDiscountPrice(discountPrice);
                }
            }
        }
    }


    /**
     * 计算价格
     * @author: libw 981087977@qq.com
     * @date: 2019/06/25 13:35:55
     * @param cartList
     * @return: void
     */
//    public void countPrice(List<CartVO> cartList) {
//        // 循环carts，计算店铺活动
//        for (CartVO cart : cartList) {
//
//            // 每次进入活动处理插件之前，先把店铺的价格从新new对象
//            // 防止在购物车修改数量的时候，进入价格计算插件中，拿到的是上一次修改过的值。
//            cart.setPrice(new PriceDetailVO());
//            // 清空已使用的优惠券信息
//            cart.setCouponList(new ArrayList<OrderCouponVO>());
//            // 初始化赠送优惠券
//            cart.setGiftCouponList(new ArrayList<EsCouponVO>());
//            // 初始化赠送赠品
//            cart.setGiftList(new ArrayList<EsFullDiscountGiftVO>());
//            // 初始化赠送积分
//            cart.setGiftPoint(0);
//
//            // 循环购物车单品活动价格计算
//            List<String> pluginIdList = PromotionTypeEnum.getSingle();
//            for (String pluginId : pluginIdList) {
//                PromotionEvent promotion = (PromotionEvent) ApplicationContextHolder.getBean(pluginId);
//                 promotion.onPriceProcess(cart);
//            }
//            pluginIdList = PromotionTypeEnum.getGroup();
//            for (String pluginId : pluginIdList) {
//                PromotionEvent promotion = (PromotionEvent) ApplicationContextHolder.getBean(pluginId);
//                promotion.onPriceProcess(cart);
//            }
//
//        }
//        // 统计价格
//        countTotalPrice(cartList);
//    }

    /**
     * 计算店铺价格,将优惠等等信息汇总，存入redis
     * @param cartList
     */
    private void countTotalPrice(List<CartVO> cartList,String skey) {
        PriceDetailVO price = new PriceDetailVO();
        AtomicReference<Integer> goodsNum = new AtomicReference<>(0);
        cartList.forEach(cartVO -> {
            //店铺在活动插件处理后的价格
            PriceDetailVO cartPrice = cartVO.getPrice();
            logger.info("店铺购物车优惠价格"+cartPrice.getDiscountPrice());
            //店铺总商品重量
            cartVO.setWeight(0.0);
            Double companyDiscountPrice=0.0;
            Double xianDiscountPrice=0.0;

            DubboResult<EsCategoryDO> categoryDO = categoryService.getFirstByName("生鲜");
            if(!categoryDO.isSuccess() || categoryDO.getData() == null){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            List<Long> cateIds = new ArrayList<>();
            DubboPageResult<EsCategoryDO> EsCategoryDO =  categoryService.getCategoryChildren(categoryDO.getData().getId());
            List<EsCategoryDO> secondList = EsCategoryDO.getData().getList();
            for(EsCategoryDO secondCategory : secondList){
                List<EsCategoryDO> threeList = secondCategory.getChildren();
                for(EsCategoryDO threeCategory : threeList){
                    cateIds.add(threeCategory.getId());
                }
            }
            Double xianPrice = 0d;

            AtomicReference<Integer> isFreeFreight = new AtomicReference<>(2);
            List<CartItemsVO> collect = cartVO.getCartItemsList().stream()
                    .filter(cartSkuVO -> cartSkuVO.getChecked() == 1).collect(Collectors.toList());

            for (CartItemsVO cartSkuVO : collect){
                //单个商品原价小计
                Double originalTotalPrice = MathUtil.multiply(cartSkuVO.getNum(), cartSkuVO.getGoodsPrice());

                //如果是生鲜
                if(cateIds.contains(cartSkuVO.getCategoryId())){
                    //生鲜价格
                    xianPrice=MathUtil.add(xianPrice,originalTotalPrice);
                }

                // 商品数量累加
                goodsNum.updateAndGet(v -> v + cartSkuVO.getNum());
                //店铺商品总价累计
                cartPrice.setGoodsPrice(MathUtil.add(cartPrice.getGoodsPrice(), originalTotalPrice));
                cartPrice.setXianTotalPrice(MathUtil.subtract(xianPrice,xianDiscountPrice));

                // TODO 公司折扣
                cartPrice.setCompanyDiscountPrice(companyDiscountPrice);
                // 设置店铺商品总重量
                Double weight = MathUtil.multiply(cartSkuVO.getGoodsWeight(), cartSkuVO.getNum());
                cartVO.setWeight(MathUtil.add(cartVO.getWeight(), weight));
                Integer isFreeFreight1 = cartSkuVO.getIsFreeFreight();
                // 如果存在 不包邮商品 计算不包邮商品运费
                if (isFreeFreight1 == 1){
                    isFreeFreight.set(1);
                }
            }
            cartPrice.setIsFreeFreight(isFreeFreight.get());
            //调用价格计算
            cartPrice.countPrice();
            cartVO.setPrice(cartPrice);
            // 累计每个购物车的 优惠金额、商品金额、积分等信息
            price.plus(cartPrice);

        });
        // 设置商品数量
        price.setGoodsNum(goodsNum.get());

        // 写入redis 购物车价格
        this.tradePriceManager.pushPrice(price,skey);



    }
    public void countShippingPrice(List<CartVO> cartList,String skey) {
//        countPrice(cartList);

        // V_2 start
        countPromotion(cartList,skey);
        // V_2 end
        PriceDetailVO price = this.tradePriceManager.getTradePrice(skey);
        double totalFreightPrice = 0;
        Double xian=0d;
        Double normal=0d;
        CheckoutParamVO param = checkoutParamManager.getParam(skey);
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.getMemberAddress(param.getAddressId());
        if(!result.isSuccess()){
            throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        EsMemberAddressDO address = result.getData();
        EsConsigneeVO consignee = new EsConsigneeVO();
        consignee.setCityId(address.getCountyId().intValue());
        //循环购物车商品
        for (CartVO cart:cartList) {
            Double aDouble1 = 0d;
            Double aDouble2 = 0d;
            Double xianSum=0d;
            Double normalSum=0d;
            List<CartItemsVO> skuList = cart.getCartItemsList();
            for (CartItemsVO sku:skuList) {
                //选中商品且买家承担运费
                if(sku.getChecked()!= null && sku.getChecked()==1
                   && sku.getIsFreeFreight() != null && sku.getIsFreeFreight() ==1
                       && sku.getIsDelivery() != null && sku.getIsDelivery() == 2){
                    //生鲜
                    if(sku.getIsFresh()==1){
                        xianSum = CurrencyUtil.add(xianSum,CurrencyUtil.mul(sku.getGoodsWeight(),sku.getNum()));
                    }else{
                        //Math.round(normalSum)
                        normalSum =CurrencyUtil.add(normalSum,CurrencyUtil.mul(sku.getGoodsWeight(),sku.getNum()));
                    }
                }
            }

            if(xianSum >0){
                aDouble1 = countFreight(consignee, price,  xianSum, 1, cart.getShopId(), null);
                xian = CurrencyUtil.add(xian,aDouble1);//生鲜运费
                cart.getPrice().setFreshFreightPrice(aDouble1);
            }
            if (normalSum>0){
                aDouble2 = countFreight(consignee, price,  normalSum, 2, cart.getShopId(), null);
                normal = CurrencyUtil.add(normal,aDouble2);//非生鲜
                cart.getPrice().setCommonFreightPrice(aDouble2);
            }
            cart.getPrice().setFreightPrice(MathUtil.add(aDouble1,aDouble2));
        }
        //两种运费合计
        totalFreightPrice=CurrencyUtil.add(xian,normal);
        logger.info("运费合计："+totalFreightPrice);
        price.setCommonFreightPrice(normal);
        price.setFreshFreightPrice(xian);
        price.setFreightPrice(totalFreightPrice);
        price.countPrice();
        // 写入redis 购物车价格 包含
        this.tradePriceManager.pushPrice(price,skey);
    }
    private Double countFreight(EsConsigneeVO consignee, PriceDetailVO paymentDetail, Double normalSum, Integer type,Long shopId,Long templateId) {
        if (type==1){
            //生鲜先判断配送区域
            boolean byAreaId = shipCompanyDetailsService.getByAreaId(type, String.valueOf(consignee.getCityId())).getData();
            if (!byAreaId){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), "生鲜暂配送杭州部分地区");
            }
        }
        Double price;
        Double money;
        Double afterCompanyPrice = paymentDetail.getAfterCompanyPrice();
        Double xianTotalPrice = paymentDetail.getXianTotalPrice();
        Double normal= CurrencyUtil.sub(afterCompanyPrice,xianTotalPrice);
        if (type==1){
            price=xianTotalPrice;
        }else{
            price=normal;
        }
        DubboResult<EsShipCompanyDetailsDO> result = shipCompanyDetailsService.getPrice(type, String.valueOf(consignee.getCityId()),shopId,templateId,2L);
        if(!result.isSuccess()||result.getData().getFirstCompany()==null){
            throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), "不在配送范围");
        }
        EsShipCompanyDetailsDO shipCompanyDetailsDO= result.getData();
        if (normalSum > shipCompanyDetailsDO.getFirstCompany()) {
            //运费计算
            Double continueW=CurrencyUtil.sub(normalSum,shipCompanyDetailsDO.getFirstCompany());//续重
            Double aa=CurrencyUtil.add(shipCompanyDetailsDO.getFirstPrice(),CurrencyUtil.mul(Math.ceil(continueW),shipCompanyDetailsDO.getContinuedPrice()));
            money= aa;
        } else {
            money=shipCompanyDetailsDO.getFirstPrice();
        }
        return money;
    }
    public void countCouponPrice(CartVO cartVO, OrderCouponVO orderCouponVO, int isUse,String skey) {
        //所有店铺的总交易金额
        PriceDetailVO price = this.tradePriceManager.getTradePrice(skey);
        //当前店铺的交易金额
        PriceDetailVO curCartPrice = cartVO.getPrice();

        List<OrderCouponVO> couponList = cartVO.getCouponList();

        //切换优惠券之前 已使用的优惠券的金额
        double beforeCouponPrice = 0.0;

        //将要使用的优惠券金额
        double couponPrice = 0.0;

        //获取会员id
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        //如果是使用优惠券
        if(isUse==1){
            EsMemberCouponDTO memberCouponDTO = new EsMemberCouponDTO();
            memberCouponDTO.setCouponId(orderCouponVO.getCouponId());
            memberCouponDTO.setId(orderCouponVO.getMemberCouponId());
            memberCouponDTO.setShopId(orderCouponVO.getShopId());
            memberCouponDTO.setMemberId(memberId);

            //如果是空则为使用优惠券
            if(couponList.isEmpty()){
                couponList.add(orderCouponVO);

            }else{	//切换优惠券
                beforeCouponPrice = couponList.get(0).getCouponMoney();
                couponList.clear();
                couponList.add(orderCouponVO);
            }
            couponPrice = orderCouponVO.getCouponMoney();


            // 设置会员优惠券的选中状态
            iEsMemberCouponService.updateMemberCouponIsCheck(memberCouponDTO);

        }else{
            EsMemberCouponDTO memberCouponDTO = new EsMemberCouponDTO();
            memberCouponDTO.setShopId(orderCouponVO.getShopId());
            memberCouponDTO.setMemberId(memberId);

            //如果是空则为使用优惠券
            if(couponList.isEmpty()){
                beforeCouponPrice = 0;
            }else{
                beforeCouponPrice = couponList.get(0).getCouponMoney();
                //取消使用，清空优惠券
                couponList.clear();
            }
            // 设置会员优惠券为未选中状态
            iEsMemberCouponService.updateMemberCouponIsNotCheck(memberCouponDTO);
        }


        // 计算店铺优惠金额
        Double discountPrice1 = curCartPrice.getDiscountPrice();
        //店铺总优惠金额 = 总优惠金额 - 之前使用的优惠券金额 + 将要使用的优惠券金额
        discountPrice1 = MathUtil.add(MathUtil.subtract(discountPrice1, beforeCouponPrice), couponPrice);
        curCartPrice.setDiscountPrice(discountPrice1);
        curCartPrice.countPrice_coupon();

        //计算总优惠金额
        double discountPrice =  price.getDiscountPrice();
        //总优惠金额 = 总优惠金额 - 之前使用的优惠券金额 + 将要使用的优惠券金额
        discountPrice = MathUtil.add(MathUtil.subtract(discountPrice, beforeCouponPrice), couponPrice);
        price.setDiscountPrice(discountPrice);
        price.countPrice();

        cartVO.setPrice(curCartPrice);
        cartVO.setCouponList(couponList);
        // 写入redis 购物车价格 包含
        this.tradePriceManager.pushPrice(price,skey);
    }
    /**
     * Version_2 start
     */
    public void countPromotion(List<CartVO> cacheCartList,String skey) {
        // 循环carts，计算店铺活动
        for (CartVO cart : cacheCartList) {

            // 每次进入活动处理插件之前，先把店铺的价格从新new对象
            // 防止在购物车修改数量的时候，进入价格计算插件中，拿到的是上一次修改过的值。
            cart.setPrice(new PriceDetailVO());
            // 清空已使用的优惠券信息
            cart.setCouponList(new ArrayList<OrderCouponVO>());
            // 初始化赠送优惠券
            cart.setGiftCouponList(new ArrayList<EsCouponVO>());
            // 初始化赠送赠品
            cart.setGiftList(new ArrayList<EsFullDiscountGiftVO>());
            // 初始化赠送积分
            cart.setGiftPoint(0);

            //获取会员id
            Long memberId = null;
            if (StringUtils.isBlank(skey)){
                memberId = ShiroKit.getUser().getId();
            }else {
                //小程序获取当前用户ID
                memberId = this.getMemberIdApplet(skey);
            }
            DubboResult<EsMemberDO> memberById = memberService.getMemberById(memberId);
            if (memberById.isSuccess()){
                List<String> pluginIdList = PromotionTypeEnum.getBlackCard();
                long timeMillis = System.currentTimeMillis();
                if (!"null".equals(memberById.getData().getBlackCard()) &&
                        memberById.getData().getBlackCard() == 1 &&
                        blackCardPromotionStart < timeMillis &&
                        timeMillis < blackCardPromotionEnd){
                    for (String pluginId : pluginIdList) {
                        PromotionEvent promotion = (PromotionEvent) ApplicationContextHolder.getBean(pluginId);
                        // 赋值金卡折扣
                        cart.setBlackCard(1);
                        cart.setBlackCardDiscount(memberById.getData().getBlackDiscount());
                        promotion.onPromotionPriceProcess(cart);
                    }
                }else {
                    // 循环购物车单品活动价格计算
                    pluginIdList = PromotionTypeEnum.getSingle();
                    for (String pluginId : pluginIdList) {
                        PromotionEvent promotion = (PromotionEvent) ApplicationContextHolder.getBean(pluginId);
                        promotion.onPromotionPriceProcess(cart);
                    }
                    pluginIdList = PromotionTypeEnum.getGroup();
                    for (String pluginId : pluginIdList) {
                        PromotionEvent promotion = (PromotionEvent) ApplicationContextHolder.getBean(pluginId);
                        promotion.onPromotionPriceProcess(cart);
                    }
                }

            }



        }
        // 统计价格
        logger.info("计算价格");
        countTotalPrice(cacheCartList,skey);

    }
    /**
     * Version_2 end
     */

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
