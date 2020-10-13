package com.xdl.jjg.manager;

import com.alibaba.fastjson.JSON;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.redisson.RedissonLock;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.DateUtils;
import com.shopx.common.util.MathUtil;
import com.shopx.goods.api.model.domain.cache.EsGoodsCO;
import com.shopx.goods.api.model.domain.cache.EsGoodsSkuCO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsGoodsSkuService;
import com.shopx.member.api.model.domain.*;
import com.shopx.member.api.model.domain.dto.EsCartDTO;
import com.shopx.member.api.model.domain.dto.EsCommercelItemsDTO;
import com.shopx.member.api.model.domain.dto.EsMemberCouponDTO;
import com.shopx.member.api.service.*;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.constant.cacheprefix.TradeCachePrefix;
import com.shopx.trade.api.converter.TradeGoodsConverter;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.EsFreightTemplateDetailDO;
import com.shopx.trade.api.model.domain.EsOrderItemsDO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.*;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisCluster;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @ClassName: CartManager
 * @Description: 购物车服务层
 * @Author: libw  981087977@qq.com
 * @Date: 6/10/2019 20:25
 * @Version: 1.0
 */
@Component
public class CartManager {
    private Logger logger = LoggerFactory.getLogger(PromotionGoodsManager.class);
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCartService cartService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCartConfigureService cartConfigureService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCommercelItemsService commerceItemsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsSkuService goodsSkuService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService goodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShopService shopService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCouponService iEsMemberCouponService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsPromotionGoodsService iEsPromotionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShipCompanyDetailsService shipCompanyDetailsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iEsMemberAddressService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFreightTemplateDetailService freightTemplateDetailService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOrderItemsService itemsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iesMemberAddressService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iesMemberCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    @Autowired
    private PromotionToolManager promotionToolManager;

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private SeckillManager seckillManager;

    @Autowired
    private TradePriceManager tradePriceManager;

    @Autowired
    private ShippingManager shippingManager;
    @Autowired
    private RedissonLock redissonLock;



    /**
     * 往购物车中添加商品
     *
     * @param skuId      商品skuID
     * @param num        商品数量
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/11 10:13:35
     * @return: void
     */
    public void add(Long skuId, Integer num,String skey) {

        // 获取操作人id（会员id）
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        Long cartId;
        Integer skuNum;

        // 验证商品SKU启用状态
        DubboResult<EsGoodsSkuCO> goodsSku1 = goodsSkuService.getGoodsSkuEnable(skuId);
        if (!goodsSku1.isSuccess()) {
            throw new ArgumentException(goodsSku1.getCode(), goodsSku1.getMsg());
        }
        // 验证商品是否状态
        DubboResult<EsGoodsCO> esGoods = this.goodsService.getEsGoods(goodsSku1.getData().getGoodsId());
        if (!esGoods.isSuccess()) {
            throw new ArgumentException(esGoods.getCode(), esGoods.getMsg());
        }
        // 根据会员id 查询购物车项数量及购物车id
        DubboResult cartCountResult = cartService.getByMemberId(memberId);
        if (!cartCountResult.isSuccess()) {
            throw new ArgumentException(cartCountResult.getCode(), cartCountResult.getMsg());
        }

        // 如果查询为空, 则新增购物车。设置购物车id，设置sku项数量
        EsCartNumDO cartNum = (EsCartNumDO) cartCountResult.getData();
        if (cartNum == null ||cartNum.getCartId() == null) {
            EsCartDTO cartDTO = new EsCartDTO();
            cartDTO.setMemberId(memberId);
            // 插入购物车
            DubboResult cartResult = cartService.insertCart(cartDTO);
            if (!cartResult.isSuccess()) {
                throw new ArgumentException(cartResult.getCode(), cartResult.getMsg());
            }

            cartId = (Long) cartResult.getData();
            skuNum = 0;
        } else {
            cartId = cartNum.getCartId();
            skuNum = cartNum.getSkuNum();
        }

        // 判断sku是否存在，存在的场合更新购物车项数量
        DubboResult cartItemResult = this.commerceItemsService.getItemsBySkuId(skuId, cartId);
        if (!cartItemResult.isSuccess()) {
            throw new ArgumentException(cartCountResult.getCode(), cartCountResult.getMsg());
        }
        if (cartItemResult.getData() == null) {
            // 取出配置的购物车数量
            DubboResult configCountResult = cartConfigureService.getCartConfigure();
            if (!configCountResult.isSuccess()) {
                throw new ArgumentException(configCountResult.getCode(), configCountResult.getMsg());
            }

            // 判断购物车数量是否大于后台配置的购物车数量
            if (skuNum >= ((EsCartConfigureDO) configCountResult.getData()).getQuantity()) {
                throw new ArgumentException(TradeErrorCode.CART_COUNT_MAX.getErrorCode(), TradeErrorCode.CART_COUNT_MAX.getErrorMsg());
            }

            // 查询商品sku信息 组装购物车项
            DubboResult goodsSkuResult = goodsSkuService.getGoodsSku(skuId);
            if (!goodsSkuResult.isSuccess()) {
                throw new ArgumentException(goodsSkuResult.getCode(), goodsSkuResult.getMsg());
            }

            EsGoodsSkuCO goodsSku = (EsGoodsSkuCO) goodsSkuResult.getData();
            EsCommercelItemsDTO cartItemDTO = new EsCommercelItemsDTO();
            cartItemDTO.setCartId(cartId);
            cartItemDTO.setGoodsSn(goodsSku.getGoodsSn());
            cartItemDTO.setPrice(goodsSku.getMoney());
            cartItemDTO.setShopId(goodsSku.getShopId());
            cartItemDTO.setProductId(goodsSku.getGoodsId());
            cartItemDTO.setSkuId(goodsSku.getId());
            cartItemDTO.setQuantity(num);
            cartItemDTO.setShopId(goodsSku.getShopId());
            // 存到数据库里
            DubboResult result = commerceItemsService.insertCommercelItems(cartItemDTO);
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }
            // 给购物车商品项赋值 start
            EsGoodsSkuCO goodsSkuCO = (EsGoodsSkuCO)goodsSkuResult.getData();
            CartItemsVO cartItem = new CartItemsVO(goodsSkuCO);
            cartItem.setNum(cartItemDTO.getQuantity());
            cartItem.setCartPrice(cartItemDTO.getPrice());
            cartItem.setSubtotal(MathUtil.multiply(cartItemDTO.getQuantity(),
                    ((EsGoodsSkuCO) goodsSkuResult.getData()).getMoney()));
            cartItem.setIsFreeFreight(esGoods.getData().getGoodsTransfeeCharge());
            cartItem.setIsFresh(esGoods.getData().getIsFresh());
            cartItem.setTemplateId(esGoods.getData().getTemplateId());
            cartItem.setGoodsImage(esGoods.getData().getOriginal());
            cartItem.setLastModify(System.currentTimeMillis());
            // 默认未选中
            cartItem.setChecked(1);
            cartItem.setIsSelf(goodsSkuCO.getIsSelf());
            // 给购物车商品项赋值 end

            // 获取缓存购物车数据
            List<CartVO> oldCacheCartList = getCacheCartListV2(skey);
            if (CollectionUtils.isEmpty(oldCacheCartList)){
                oldCacheCartList = this.assembleCart(skey);
                jedisCluster.del(cacheKey);
                oldCacheCartList.forEach(cartVO -> {
                    List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
                    cartItemsList.forEach(cartItemsVO -> {
                        cartItemsVO.setChecked(1);
                    });
                    cartVO.setChecked(1);
                });
                this.promotionToolManager.countPromotion(oldCacheCartList,skey);
                //重新压入内存
                this.putCache(cacheKey, oldCacheCartList);
            }else {
                jedisCluster.del(cacheKey);
                oldCacheCartList.forEach(cartVO -> {
                    if (cartVO.getShopId() == cartItem.getShopId()){
                        List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
                        // 过滤出当前要加入购物车的商品
                        List<CartItemsVO> collect = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getSkuId() == skuId.longValue()).collect(Collectors.toList());
                        // 如果购物车中存在该商品则 修改购物车数量
                        if (CollectionUtils.isNotEmpty(collect)){
                            collect.forEach(cartItemsVO -> {
                                cartItemsVO.setNum(cartItemsVO.getNum() + num);
                                cartItemsVO.setChecked(1);
                                this.getPromotionList(cartItemsVO,skey);
                            });
                        }else {
                            // 读取这个商品品参与的所有活动
                            cartItem.setChecked(1);
                            this.getPromotionList(cartItem,skey);
                            cartItemsList.add(cartItem);
                        }
                    }
                    Map<Long,Integer> goodsSkuCheckedMap = new HashedMap(16);
                    cartVO.getCartItemsList().forEach(cartItemsVO -> {
                        goodsSkuCheckedMap.put(cartItemsVO.getSkuId(),cartItemsVO.getChecked());

                    });

                    List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
//                    cartItemsList.forEach(cartItemsVO -> {
//                        // 设置默认选中
//                        cartItemsVO.setChecked(cartItemsVO.getSkuId().longValue() == skuId.longValue()?1:goodsSkuCheckedMap.get(cartItemsVO.getSkuId()));
//
//                        // 配送方式
//                        if(cartItemsVO.getIsFreeFreight() == 1){
//                            this.setShipMethod(cartItemsVO);
//                        }
//                    });
                    List<CartItemsVO> collect1 = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
                    if (cartItemsList.size() == collect1.size()){
                        // 设置店铺选中状态
                        cartVO.setChecked(1);
                    }else {
                        cartVO.setChecked(0);
                    }
                    List<CartItemsVO> collect = cartItemsList.stream().sorted(Comparator.comparing(CartItemsVO::getChecked).reversed()).collect(Collectors.toList());
                    cartVO.setCartItemsList(collect);
                });
            }



            this.promotionToolManager.countPromotion(oldCacheCartList,skey);
            //重新压入内存
            this.putCache(cacheKey, oldCacheCartList);
        } else {
            // sku存在的场合更新数量
            EsCommercelItemsDO cartItemsDO = (EsCommercelItemsDO) cartItemResult.getData();
            cartItemsDO.setSkuId(skuId);
            cartItemsDO.setQuantity(cartItemsDO.getQuantity() + num);

            EsCommercelItemsDTO commerceItemsDTO = new EsCommercelItemsDTO();
            BeanUtil.copyProperties(cartItemsDO, commerceItemsDTO);
            DubboResult result = commerceItemsService.updateCommercelItems(commerceItemsDTO);
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }
            List<CartVO> oldCacheCartList = getCacheCartList(skey);
            jedisCluster.del(cacheKey);
            oldCacheCartList.forEach(cartVO -> {
                if (cartVO.getShopId() == cartItemsDO.getShopId()){
                    List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
                    List<CartItemsVO> collect = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getSkuId() == skuId.longValue()).collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(collect)){
                        collect.forEach(cartItemsVO -> {
                            cartItemsVO.setNum(cartItemsVO.getNum() + num);
                            cartItemsVO.setChecked(1);
                        });
                    }
                }

                Map<Long,Integer> goodsSkuCheckedMap = new HashedMap(16);
                cartVO.getCartItemsList().forEach(cartItemsVO -> {
                    goodsSkuCheckedMap.put(cartItemsVO.getSkuId(),cartItemsVO.getChecked());

                });

                List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
//                cartItemsList.forEach(cartItemsVO -> {
//                    // 设置默认选中
//                    cartItemsVO.setChecked(cartItemsVO.getSkuId().longValue() == skuId.longValue()?1:goodsSkuCheckedMap.get(cartItemsVO.getSkuId()));
//
//                    // 配送方式
//                    if(cartItemsVO.getIsFreeFreight() == 1){
//                        this.setShipMethod(cartItemsVO);
//                    }
//                });
                List<CartItemsVO> collect1 = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
                if (cartItemsList.size() == collect1.size()){
                    // 设置店铺选中状态
                    cartVO.setChecked(1);
                }else {
                    cartVO.setChecked(0);
                }
                List<CartItemsVO> collect = cartItemsList.stream().sorted(Comparator.comparing(CartItemsVO::getChecked).reversed()).collect(Collectors.toList());
                cartVO.setCartItemsList(collect);
            });
            this.promotionToolManager.countPromotion(oldCacheCartList,skey);
            //重新压入内存
            this.putCache(cacheKey, oldCacheCartList);
        }
        long checkStart = System.currentTimeMillis();
        logger.info("保存原先商品的选中状态开始[{}]",checkStart);



        /**
         * _____________________________________________________________________________
         */
        // 保存原选中状态
//        List<CartVO> oldCacheCartList = getCacheCartList();
//
//        Map<Long,Integer> goodsSkuCheckedMap = new HashedMap(16);
//        oldCacheCartList.forEach(cartVO -> {
//            cartVO.getCartItemsList().forEach(cartItemsVO -> {
//                goodsSkuCheckedMap.put(cartItemsVO.getSkuId(),cartItemsVO.getChecked());
//            });
//        });
//        long checkEnd = System.currentTimeMillis();
//        logger.info("保存原先商品的选中状态开始[{}],耗时[{}]",checkStart,checkEnd-checkStart);
//
//        // 清除购物车缓存
//        jedisCluster.del(this.getSessionKey());
//        long start = System.currentTimeMillis();
//        logger.info("加入购物车重组购物车数据开始[{}]",start);
//        // 重组购物车
//        List<CartVO> cacheCartList = this.assembleCart();
//        long end1 = System.currentTimeMillis();
//        logger.info("加入购物车重组购物车数据结束[{}]，耗时[{}]",start,end1-start);
//
//        cacheCartList = cacheCartList.stream().filter(cartVO -> (cartVO.getCartItemsList().size()!=0)).collect(Collectors.toList());
//        // 默认选中当前sku，且取消之前的所有选中
//
//           cacheCartList.forEach(cartVO -> {
//            List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
//            cartItemsList.forEach(cartItemsVO -> {
//                // 设置默认选中
//                cartItemsVO.setChecked(cartItemsVO.getSkuId().longValue() == skuId.longValue()?1:goodsSkuCheckedMap.get(cartItemsVO.getSkuId()));
//
//                // 配送方式
//                if(cartItemsVO.getIsFreeFreight() == 1){
//                    this.setShipMethod(cartItemsVO);
//                }
//            });
//            List<CartItemsVO> collect1 = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
//            if (cartItemsList.size() == collect1.size()){
//                // 设置店铺选中状态
//                cartVO.setChecked(1);
//            }else {
//                cartVO.setChecked(0);
//            }
//            List<CartItemsVO> collect = cartItemsList.stream().sorted(Comparator.comparing(CartItemsVO::getChecked).reversed()).collect(Collectors.toList());
//            cartVO.setCartItemsList(collect);
//           });
//        long end2 = System.currentTimeMillis();
//        logger.info("赋值选中状态[{}]，耗时[{}]",end2,end2-end1);
        /**
         * _____________________________________________________________________________
         */
//        this.promotionToolManager.countPromotion(oldCacheCartList);
////        long end3 = System.currentTimeMillis();
////        logger.info("计算商品价格[{}]，耗时[{}]",end3,end3-end2);
//        //重新压入内存
//        this.putCache(getSessionKey(), oldCacheCartList);
    }


    /**
     * @Description: 购物车数据不存入数据库
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/5/21 11:59
     * @param
     * @return       void
     * @exception
     *
     */
    public void addV2(Long skuId, Integer num,String skey) {

        // 验证商品SKU启用状态
        DubboResult<EsGoodsSkuCO> goodsSku1 = goodsSkuService.getGoodsSkuEnable(skuId);
        if (!goodsSku1.isSuccess()) {
            throw new ArgumentException(goodsSku1.getCode(), goodsSku1.getMsg());
        }
        // 验证商品是否状态
        DubboResult<EsGoodsCO> esGoods = this.goodsService.getEsGoods(goodsSku1.getData().getGoodsId());
        if (!esGoods.isSuccess()) {
            throw new ArgumentException(esGoods.getCode(), esGoods.getMsg());
        }
        String sessionKey = "";
        if (StringUtils.isBlank(skey)){
            sessionKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            sessionKey = this.getSessionKeyApplet(skey);
        }
        String cart = jedisCluster.get(sessionKey);
        List<CartVO> cartList = JSON.parseArray(cart, CartVO.class);

        // 查询商品sku信息 组装购物车项
        DubboResult goodsSkuResult = goodsSkuService.getGoodsSku(skuId);
        if (!goodsSkuResult.isSuccess()) {
            throw new ArgumentException(goodsSkuResult.getCode(), goodsSkuResult.getMsg());
        }

        // 给购物车商品项赋值 start
        EsGoodsSkuCO goodsSkuCO = (EsGoodsSkuCO)goodsSkuResult.getData();
        CartItemsVO cartItem = new CartItemsVO(goodsSkuCO);
        cartItem.setNum(num);
        cartItem.setCartPrice(goodsSkuCO.getMoney());
        cartItem.setSubtotal(MathUtil.multiply(num,
                ((EsGoodsSkuCO) goodsSkuResult.getData()).getMoney()));
        cartItem.setThisGoodsPrice(MathUtil.multiply(num,
                ((EsGoodsSkuCO) goodsSkuResult.getData()).getMoney()));
        cartItem.setIsFreeFreight(esGoods.getData().getGoodsTransfeeCharge());
        cartItem.setIsFresh(esGoods.getData().getIsFresh());
        cartItem.setTemplateId(esGoods.getData().getTemplateId());
        cartItem.setGoodsImage(esGoods.getData().getOriginal());
        cartItem.setLastModify(System.currentTimeMillis());
        // 默认未选中
        cartItem.setChecked(1);
        cartItem.setIsSelf(goodsSkuCO.getIsSelf());

        long addStart = System.currentTimeMillis();
        logger.info("加入购物车开始时间[{}]",addStart);
        if (CollectionUtils.isEmpty(cartList)) {
            CartVO cartNew = new CartVO();
            List<CartItemsVO> cartItemsListNew = new ArrayList<>();
            // 店铺id不存在的场合, 新建一个购物车VO
            cartNew.setCartItemsList(cartItemsListNew);
            cartNew = new CartVO(esGoods.getData().getShopId(), esGoods.getData().getShopName());
            cartList.add(cartNew);
            cartList.forEach(cartVO -> {

                if (cartVO.getShopId().longValue() == cartItem.getShopId().longValue()){
                    List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
                    // 过滤出当前要加入购物车的商品
                    List<CartItemsVO> collect = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getSkuId() == skuId.longValue()).collect(Collectors.toList());
                    // 如果购物车中存在该商品则 修改购物车数量
                    if (CollectionUtils.isNotEmpty(collect)){
                        collect.forEach(cartItemsVO -> {
                            cartItemsVO.setNum(cartItemsVO.getNum() + num);
                            cartItemsVO.setChecked(1);
                            cartItemsVO.setLastModify(System.currentTimeMillis());
                            this.getPromotionList(cartItemsVO,skey);

                        });
                    }else {
                        // 读取这个商品品参与的所有活动
                        cartItem.setChecked(1);
                        this.getPromotionList(cartItem,skey);
                        cartItemsList.add(cartItem);
                    }
                }

                List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();

                List<CartItemsVO> collect1 = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
                if (cartItemsList.size() == collect1.size()){
                    // 设置店铺选中状态
                    cartVO.setChecked(1);
                }else {
                    cartVO.setChecked(0);
                }
                List<CartItemsVO> collect = cartItemsList.stream().sorted(Comparator.comparing(CartItemsVO::getChecked).reversed()).collect(Collectors.toList());
                cartVO.setCartItemsList(collect);
            });

        }else {
            List<CartVO> collect2 = cartList.stream().filter(cartVO -> cartVO.getShopId() == cartItem.getShopId().longValue()).collect(Collectors.toList());
            if (collect2.size() <= 0){
                CartVO cartNew = new CartVO();
                List<CartItemsVO> cartItemsListNew = new ArrayList<>();
                // 店铺id不存在的场合, 新建一个购物车VO
                cartNew.setCartItemsList(cartItemsListNew);
                cartNew = new CartVO(esGoods.getData().getShopId(), esGoods.getData().getShopName());
                cartList.add(cartNew);
            }
            cartList.forEach(cartVO -> {
                if (cartVO.getShopId() == cartItem.getShopId().longValue()){
                    List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
                    List<CartItemsVO> collect = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getSkuId() == skuId.longValue()).collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(collect)){
                        collect.forEach(cartItemsVO -> {
                            cartItemsVO.setNum(cartItemsVO.getNum() + num);
                            cartItemsVO.setLastModify(System.currentTimeMillis());
                            this.getPromotionList(cartItemsVO,skey);
                            cartItemsVO.setChecked(1);
                        });
                    }else {
                        // 读取这个商品品参与的所有活动
                        cartItem.setChecked(1);
                        this.getPromotionList(cartItem,skey);
                        cartItemsList.add(cartItem);
                    }
                }
                List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();

                List<CartItemsVO> collect1 = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
                if (cartItemsList.size() == collect1.size()){
                    // 设置店铺选中状态
                    cartVO.setChecked(1);
                }else {
                    cartVO.setChecked(0);
                }
                List<CartItemsVO> collect = cartItemsList.stream().sorted(Comparator.comparing(CartItemsVO::getChecked).reversed()).collect(Collectors.toList());
                cartVO.setCartItemsList(collect);
            });

        }

        promotionToolManager.countPromotion(cartList,skey);
        //重新压入内存
        this.putCache(sessionKey, cartList);

        long addEnd = System.currentTimeMillis();
        logger.info("加入购物车结束时间[{}]，[{}]",addEnd,addEnd-addStart);
    }

    /**
     * 更新购物车数量
     *
     * @param skuId skuId
     * @param num   商品数量
     * @author: libw 981087977@qq.com
     * @date: 2019/07/12 13:50:17
     * @return: java.util.List<com.shopx.trade.api.model.domain.vo.CartVO>
     */
    public List<CartVO> updateNum(Long skuId, Integer num,String skey) {
        // 获取操作人id
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        // 根据会员id 查询购物车项数量及购物车id
        DubboResult cartCountResult = cartService.getByMemberId(memberId);
        if (!cartCountResult.isSuccess()) {
            throw new ArgumentException(cartCountResult.getCode(), cartCountResult.getMsg());
        }

//        EsCartNumDO cartNum = (EsCartNumDO) cartCountResult.getData();
//        // 判断sku是否存在，存在的场合更新购物车项数量
//        DubboResult cartItemResult = this.commerceItemsService.getItemsBySkuId(skuId, cartNum.getCartId());
//        if (!cartItemResult.isSuccess()) {
//            throw new ArgumentException(cartCountResult.getCode(), cartCountResult.getMsg());
//        }
//
//        // 不存在的场合提示购物车商品不存在
//        if (cartItemResult.getData() == null) {
//            // 查询商品sku信息 组装购物车项
//            DubboResult goodsSkuResult = goodsSkuService.getGoodsSku(skuId);
//            if (!goodsSkuResult.isSuccess()) {
//                throw new ArgumentException(goodsSkuResult.getCode(), goodsSkuResult.getMsg());
//            }
//
//            EsGoodsSkuCO goodsSku = (EsGoodsSkuCO) goodsSkuResult.getData();
//            // sku 不存在的场合修改购物车数量（该情况一般是点击立即购买 修改数量引起）
//            EsCommercelItemsDTO cartItemDTO = new EsCommercelItemsDTO();
//            cartItemDTO.setCartId(cartNum.getCartId());
//            cartItemDTO.setGoodsSn(goodsSku.getGoodsSn());
//            cartItemDTO.setPrice(goodsSku.getMoney());
//            cartItemDTO.setShopId(goodsSku.getShopId());
//            cartItemDTO.setProductId(goodsSku.getGoodsId());
//            cartItemDTO.setSkuId(skuId);
//            cartItemDTO.setQuantity(num);
//            cartItemDTO.setShopId(goodsSku.getShopId());
//            // 存到数据库里
//            DubboResult result = commerceItemsService.insertCommercelItems(cartItemDTO);
//            if (!result.isSuccess()) {
//                throw new ArgumentException(result.getCode(), result.getMsg());
//            }
//
//        } else {
//            // sku存在的场合更新数量
//            EsCommercelItemsDO cartItemsDO = (EsCommercelItemsDO) cartItemResult.getData();
//            cartItemsDO.setSkuId(skuId);
//            cartItemsDO.setQuantity(num);
//
//            EsCommercelItemsDTO commerceItemsDTO = new EsCommercelItemsDTO();
//            BeanUtil.copyProperties(cartItemsDO, commerceItemsDTO);
//            DubboResult result = commerceItemsService.updateCommercelItems(commerceItemsDTO);
//            if (!result.isSuccess()) {
//                throw new ArgumentException(result.getCode(), result.getMsg());
//            }
//        }

        List<CartVO> cacheCartList = this.getCacheCartList(skey);
        long startTime = System.currentTimeMillis();
        for (CartVO cart : cacheCartList) {

            List<CartItemsVO> productList = cart.getCartItemsList();

            for (CartItemsVO sku : productList) {
                if (skuId.equals(sku.getSkuId())) {
                    sku.setNum(num);
                    sku.setSubtotal(MathUtil.multiply(sku.getNum(),sku.getGoodsPrice()));
                    sku.setThisGoodsPrice(MathUtil.multiply(sku.getNum(),sku.getGoodsPrice()));
                }
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("请求耗时:"+(endTime-startTime));
        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cacheCartList,skey);
        // V_2 end

        // 存到缓存里
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        this.putCache(cacheKey, cacheCartList);
        return getCacheCartList(skey);
    }

    /**
     * 修改购物车项选中状态
     *
     * @param skuIds  skuIdList
     * @param checked 选中状态
     * @author: libw 981087977@qq.com
     * @date: 2019/07/12 14:12:11
     * @return: java.util.List<com.shopx.trade.api.model.domain.vo.CartVO>
     */
    public List<CartVO> checked(Long[] skuIds, Integer checked,String skey) {
        Assert.notNull(skuIds, "参数skuIds不能为空");

        List<Long> skuList = Arrays.asList(skuIds);

        //不合法的参数，忽略掉
        if (checked != 1 && checked != 0) {
            return new ArrayList<>();
        }

        // 先读取出此用户的所有购物项
        List<CartVO> cartList = this.getCacheCartList(skey);

        for (CartVO cart : cartList) {

            cart.setChecked(0);
            List<CartItemsVO> cartItemsList = cart.getCartItemsList();

            for (CartItemsVO sku : cartItemsList) {
                if (skuList.contains(sku.getSkuId())) {
                    // 重组优惠列表
                    sku = getPromotionList(sku,skey);
                    sku.setChecked(checked);
                }
            }
            // 购物车中选中的商品数量
            List<CartItemsVO> collect1 = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
            // 购物车中未失效的商品数量
            List<CartItemsVO> collect = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getIsFailure() != 2).collect(Collectors.toList());
            // 如果购物车中选中的商品数量 = 购物车中未失效的商品数量 则 店铺选中状态为被选中
            if (collect1.size() == collect.size()){
                cart.setChecked(1);
            }
        }



        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cartList,skey);
        // V_2 end
        // 存到缓存里
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        this.putCache(cacheKey, cartList);
        return cartList;
    }

    /**
     * 批量删除购物车项
     *
     * @param skuIds skuIdList
     * @author: libw 981087977@qq.com
     * @date: 2019/07/12 14:16:46
     * @return: void
     */
    public List<CartVO> delete(Integer[] skuIds,String skey) {

        Assert.notNull(skuIds, "参数skuIds不能为空");

        // 获取操作人id（会员id）
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }

        DubboResult result = commerceItemsService.deleteByskuId(memberId, skuIds);
        if (!result.isSuccess()) {
            throw new ArgumentException(result.getCode(), result.getMsg());
        }

        List<CartVO> cacheCartList = this.getCacheCartList(skey);
        List<Integer> skuIdList = Arrays.asList(skuIds);
        for (CartVO cart : cacheCartList) {

            List<CartItemsVO> productList = cart.getCartItemsList();
            List<CartItemsVO> cartItemsList = productList.stream().
                    filter( cartItemsVO -> !(skuIdList.contains(cartItemsVO.getSkuId().intValue()))).collect(Collectors.toList());
            cart.setCartItemsList(cartItemsList);

        }

        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cacheCartList,skey);
        // V_2 end

        cacheCartList = cacheCartList.stream().filter(cartVO -> (cartVO.getCartItemsList().size()!=0)).collect(Collectors.toList());

        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        // 存到缓存里
        this.putCache(cacheKey, cacheCartList);

        return cacheCartList;
    }

    /**
     * 根据店铺批量修改选中状态
     *
     * @param shopId  店铺ID
     * @param checked 选中状态
     * @author: libw 981087977@qq.com
     * @date: 2019/07/12 14:22:17
     * @return: void
     */
    public void checkedShopAll(Long shopId, Integer checked,String tab,String skey) {
        // 不合法的参数，忽略掉
        if ((checked != 1 && checked != 0) || shopId == null) {
            return;
        }
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        // 先读取出此用户的缓存购物项
        List<CartVO> cartList = this.getCacheCartList(skey);
        if (tab == null){
            tab = "";
        }
        switch (tab){
            // 降价商品tab 入口
            case "priceDown":
                for (CartVO cart : cartList) {
                    // 店铺相同的情况
                    if (shopId.equals(cart.getShopId())) {
                        List<CartItemsVO> skuList = cart.getCartItemsList();
                        cart.setChecked(checked);
                        // 过滤未失效的商品
                        List<CartItemsVO> collect = skuList.stream().filter(cartItemsVO -> cartItemsVO.getIsFailure() == 1).collect(Collectors.toList());
                        for (CartItemsVO skuVO : collect) {
                            if (skuVO.getCartPrice() > skuVO.getGoodsPrice()){

                                skuVO.setChecked(checked);
                            }
                        }
                    }
                }
                break;
            // 库存紧缺商品tab 入口
            case "stockShortage":
                for (CartVO cart : cartList) {
                    // 店铺相同的情况
                    if (shopId.equals(cart.getShopId())) {
                        List<CartItemsVO> skuList = cart.getCartItemsList();
                        cart.setChecked(checked);
                        // 过滤未失效的商品
                        List<CartItemsVO> collect = skuList.stream().filter(cartItemsVO -> cartItemsVO.getIsFailure() == 1).collect(Collectors.toList());
                        for (CartItemsVO skuVO : collect) {
                            if ((skuVO.getWarningValue() == null ? 10 : skuVO.getWarningValue()) > skuVO.getEnableQuantity()){

                                skuVO.setChecked(checked);
                            }
                        }
                    }
                }
                break;
            // 全部商品入口
            default:
                for (CartVO cart : cartList) {
                    // 店铺相同的情况
                    if (shopId.equals(cart.getShopId())) {
                        List<CartItemsVO> skuList = cart.getCartItemsList();
                        cart.setChecked(checked);
                        // 过滤未失效的商品
                        List<CartItemsVO> collect = skuList.stream().filter(cartItemsVO -> cartItemsVO.getIsFailure() == 1).collect(Collectors.toList());
                        for (CartItemsVO skuVO : collect) {
                            skuVO.setChecked(checked);
                        }
                    }
                }
                break;
        }

        //调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cartList,skey);
        // V_2 end
        //重新压入缓存
        this.putCache(cacheKey, cartList);

    }

    /**
     * 设置选中状态
     * @author: libw 981087977@qq.com
     * @date: 2019/07/12 16:01:16
     * @param checked   选中状态
     * @return: void
     */
    public void checkedAll(Integer checked,String tab,String skey) {
        // 不合法的参数，忽略掉
        if (checked != 1 && checked != 0) {
            return;
        }

        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }

        // 先读取出此用户的所有购物项
        List<CartVO> cartList = this.getCartListV2(skey);
        if (tab == null){
            tab = "";
        }
        switch (tab){
            // 降价商品tab 入口
            case "priceDown":
                for (CartVO cart : cartList) {
                    // 店铺相同的情况
                    List<CartItemsVO> skuList = cart.getCartItemsList();
                    cart.setChecked(checked);
                    // 过滤未失效的商品
                    List<CartItemsVO> collect = skuList.stream().filter(cartItemsVO -> cartItemsVO.getIsFailure() == 1).collect(Collectors.toList());
                    for (CartItemsVO skuVO : collect) {
                        if (skuVO.getCartPrice() > skuVO.getGoodsPrice()){

                            skuVO.setChecked(checked);
                        }
                    }
                }
                break;
            // 库存紧缺商品tab 入口
            case "stockShortage":
                for (CartVO cart : cartList) {
                    // 店铺相同的情况
                    List<CartItemsVO> skuList = cart.getCartItemsList();
                    cart.setChecked(checked);
                    // 过滤未失效的商品
                    List<CartItemsVO> collect = skuList.stream().filter(cartItemsVO -> cartItemsVO.getIsFailure() == 1).collect(Collectors.toList());
                    for (CartItemsVO skuVO : collect) {
                        if ((skuVO.getWarningValue() == null ? 10 : skuVO.getWarningValue()) > skuVO.getEnableQuantity()){

                            skuVO.setChecked(checked);
                        }
                    }
                }
                break;
            // 全部商品入口
            default:
                for (CartVO cart : cartList) {
                    // 店铺相同的情况
                    List<CartItemsVO> skuList = cart.getCartItemsList();
                    cart.setChecked(checked);
                    // 过滤未失效的商品
                    List<CartItemsVO> collect = skuList.stream().filter(cartItemsVO -> cartItemsVO.getIsFailure() == 1).collect(Collectors.toList());
                    for (CartItemsVO skuVO : collect) {

                        skuVO.setChecked(checked);
                    }
                }
                break;
        }

        //调用促销价格计算的接口
//        this.promotionToolManager.countPrice(cartList);
        // V_2 start
        this.promotionToolManager.countPromotion(cartList,skey);
        // V_2 end

        //重新压入缓存
        this.putCache(cacheKey, cartList);

    }

    /**
     * 读取用户所有的购物车项
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/06/11 10:21:20
     * @return: List<EsCartVO>
     */
    public List<CartVO> getCartListCheck(String skey) {
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        // 获取购物车缓存
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartList1 = new ArrayList();
        List<CartVO> cartList = JSON.parseArray(itemListJson, CartVO.class);

        logger.info("缓存Key"+cacheKey+"=====缓存中购物车list"+cartList);

        // 如果不空查询数据库，重新生成缓存
        if (cartList != null && !cartList.isEmpty()) {

            cartList.stream().map(cartVO -> {
                CartVO cartVO1 = new CartVO();
                List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();

                cartItemsList.forEach(cartItemsVO -> {
                    Integer isFailure= cartItemsVO.getIsFailure();
                    if (isFailure == null || isFailure == 2 ){
                        cartItemsVO.setChecked(0);
                    }
                });

                // 调用会员优惠券列表接口 获取coupon list
                DubboPageResult<EsCouponDO> esCouponList = iEsCouponService.getEsCouponListByShopId(cartVO.getShopId());
                if (esCouponList.isSuccess()) {
                    List<EsCouponDO> couponDOList = esCouponList.getData().getList();
                    if (CollectionUtils.isNotEmpty(couponDOList)) {
                        List<EsCouponVO> esCouponVOS = BeanUtil.copyList(couponDOList, EsCouponVO.class);

                        List<Long> couponDOIds = esCouponVOS.stream().map(EsCouponVO::getId).collect(Collectors.toList());
                        Long memberId = null;
                        if (StringUtils.isBlank(skey)) {
                            memberId = ShiroKit.getUser().getId();
                        } else {
                            //小程序获取当前用户ID
                            memberId = this.getMemberIdApplet(skey);
                        }
                        DubboResult result = iesMemberCouponService.getMemberWhetherCouponIds(memberId, couponDOIds);
                        List<Long> list = (List<Long>) result.getData();
                        if (null != list) {
                            esCouponVOS = esCouponVOS.stream().map(e -> {
                                if (list.contains(e.getId())) {
                                    e.setIsReceive(1);
                                    return e;
                                }
                                e.setIsReceive(0);
                                return e;
                            }).collect(Collectors.toList());
                        }
                        cartVO.setEsCouponVO(esCouponVOS);
                    }
                }
                cartVO.setCartItemsList(cartItemsList);
                BeanUtil.copyProperties(cartVO, cartVO1);
                return cartVO1;
            }).collect(Collectors.toList());

            // 调用促销价格计算的接口
            // V_2 start
            this.promotionToolManager.countPromotion(cartList,skey);
            // V_2 end
            // 存到缓存里
            this.putCache(cacheKey, cartList);
            return cartList;

        }
        cartList = this.assembleCart(skey);
        logger.info("根据购物车数据重新生成购物车list"+cartList);
        cartList1 = cartList.stream().filter(cartVO -> (cartVO.getCartItemsList().size()!=0)).collect(Collectors.toList());
        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cartList1,skey);
        // V_2 end
        // 存到缓存里
        this.putCache(cacheKey, cartList1);
        logger.info("调用促销价格后返回的购物车list放到缓存中："+cartList1);
        return cartList1;
    }

    /**
     * 从数据库中获取初始的购物车数据
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/06/11 10:21:20
     * @return: List<EsCartVO>
     */
    /*public List<CartVO> getCartList() {
        // 从数据库中获取购物车信息
        List<CartVO> cartList = new ArrayList<>();

        cartList = this.assembleCart();

        cartList = cartList.stream().filter(cartVO -> (cartVO.getCartItemsList().size()!=0)).collect(Collectors.toList());

        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cartList);
        // V_2 end


        // 设置店铺全选状态
        for (CartVO cartVO : cartList) {
            //设置默认为店铺商品非全选
            cartVO.setChecked(0);
            List<CartItemsVO> itemsList = cartVO.getCartItemsList();
            for (CartItemsVO cartItems : itemsList) {
                if (cartItems.getChecked() == 0) {
                    cartItems.setChecked(0);
                    break;
                }
            }
        }

        // 存到缓存里
        this.putCache(this.getSessionKey(), cartList);

        return cartList;
    }*/

    public List<CartVO> getCartListV2(String skey) {
        // 从数据库中获取购物车信息
        List<CartVO> cartList = new ArrayList<>();

        cartList = this.getCacheCartList(skey);

        cartList = cartList.stream().filter(cartVO -> (cartVO.getCartItemsList().size()!=0)).collect(Collectors.toList());

        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cartList,skey);
        // V_2 end


        // 设置店铺全选状态
        for (CartVO cartVO : cartList) {
            //设置默认为店铺商品非全选
            cartVO.setChecked(0);
            List<CartItemsVO> itemsList = cartVO.getCartItemsList();
            for (CartItemsVO cartItems : itemsList) {
                if (cartItems.getChecked() == 0) {
                    cartItems.setChecked(0);
                    break;
                }
            }
        }

        // 存到缓存里
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        this.putCache(cacheKey, cartList);

        return cartList;
    }


    /**
     * 从缓存中获取到缓存购物车数据
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/06/11 10:21:20
     * @return: List<EsCartVO>
     */
    public List<CartVO> getCacheCartList(String skey) {
        // 获取购物车缓存
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartList = JSON.parseArray(itemListJson, CartVO.class);
        // 如果为空查询数据库，重新生成缓存
        if (cartList != null && !cartList.isEmpty()) {
            return cartList;
        }
        cartList = this.assembleCart(skey);

        cartList = cartList.stream().filter(cartVO -> (cartVO.getCartItemsList().size()!=0)).collect(Collectors.toList());
        // 设置店铺全选状态
        for (CartVO cartVO : cartList) {
            //设置默认为店铺商品非全选
            cartVO.setChecked(0);
            List<CartItemsVO> itemsList = cartVO.getCartItemsList();

            List<CartItemsVO> collect = itemsList.stream()
                    .filter(sku -> sku.getIsFreeFreight() == 1).collect(Collectors.toList());
            for (CartItemsVO cartItems : collect) {
                this.setShipMethod(cartItems,skey);
            }

        }

        // 调用促销价格计算的接口
//        this.promotionToolManager.countPrice(cartList);

        // V_2 start
        this.promotionToolManager.countPromotion(cartList,skey);
        // V_2 end
        // 存到缓存里
        this.putCache(cacheKey, cartList);
        return cartList;
    }



    /**
     * 从缓存中获取到缓存购物车数据
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/06/11 10:21:20
     * @return: List<EsCartVO>
     */
    public List<CartVO> getCacheCartListV2(String skey) {
        /// 获取购物车缓存
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartList = JSON.parseArray(itemListJson, CartVO.class);
        // 如果为空查询数据库，重新生成缓存
        if (cartList != null && !cartList.isEmpty()) {
            return cartList;
        }

        return cartList;
    }


    private void setShipMethod(CartItemsVO cartItems,String skey) {

        DubboPageResult shipDetailResult =
                this.freightTemplateDetailService.getFreightTemplateDetailListByModeId(cartItems.getTemplateId());
        if (!shipDetailResult.isSuccess()) {
            throw new ArgumentException(shipDetailResult.getCode(), shipDetailResult.getMsg());
        }
        List<EsFreightTemplateDetailDO> shipDetail = shipDetailResult.getData().getList();

        CheckoutParamVO param = checkoutParamManager.getParam(skey);
        Long addressId = param.getAddressId();
        DubboResult<EsMemberAddressDO> memberAddress = iEsMemberAddressService.getMemberAddress(addressId);
        if (memberAddress.isSuccess() && memberAddress.getData() != null){
            Long areaId = shippingManager.actualAddress(memberAddress.getData());
            if (cartItems.getIsFresh() == 2){
                DubboResult<Boolean> byAreaId = shipCompanyDetailsService.getByAreaId(2, String.valueOf(areaId));
                if (byAreaId.getData().booleanValue()){
                    cartItems.setDeliveryMethod("express");
                }else {
                    cartItems.setDeliveryMethod("notInScope");
                }
            }else if (cartItems.getIsFresh() == 1){
                DubboResult<Boolean> byAreaId = shipCompanyDetailsService.getByAreaId(1, String.valueOf(areaId));
                if (byAreaId.getData().booleanValue()){
                    cartItems.setDeliveryMethod("express");
                }else {
                    cartItems.setDeliveryMethod("notInScope");
                }
            }
            Map<Long, EsFreightTemplateDetailVO> shipMap = new HashMap<>(16);
            if (shipDetail.isEmpty()) {
                cartItems.setDeliveryMethod("notInScope");
            }else {
                for (EsFreightTemplateDetailDO child : shipDetail) {
                    if (!StringUtils.isEmpty(child.getArea())) {
                        shipDetail.stream().map(EsFreightTemplateDetailDO::getId).collect(Collectors.toList());
                        // 校验地区
                        if (child.getAreaId().contains(areaId)) {
                            EsFreightTemplateDetailVO freightTemplateDetailVO = new EsFreightTemplateDetailVO();
                            BeanUtil.copyProperties(child, freightTemplateDetailVO);
                            shipMap.put(cartItems.getSkuId(), freightTemplateDetailVO);
                        }
                    }
                }

                // 如果没有匹配 则当前地区无货
                if (!shipMap.containsKey(cartItems.getSkuId())) {
                    cartItems.setDeliveryMethod("notInScope");

                }
            }
        }
    }

    /**
     * 清空购物车
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/06/12 10:14:28
     * @return: void
     */
    public void clean(String skey) {
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        jedisCluster.del(cacheKey);
    }

    /**
     * 判断是否要刷新购物车
     *
     * @param
     * @author: libw 981087977@qq.com
     * @date: 2019/06/29 14:30:24
     * @return: void
     */
    public void reviewGoods(String skey) {

        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        String cartCache = jedisCluster.get(cacheKey);
        List<CartVO> itemList = JSON.parseArray(cartCache, CartVO.class);
        // 如果为空new一个 list返回
        if (itemList == null) {
            return;
        }

        // 商品价格是否有变动
        boolean flag = false;

        // 遍历我的购物车集合
        for (CartVO cartVO : itemList) {

            List<CartItemsVO> skuList = cartVO.getCartItemsList();
            // 遍历购物车中的商品
            for (CartItemsVO skuVO : skuList) {

                // 如果商品加入购物车后，商家修改了商品价格，购物车应变更相应的商品价格
                DubboResult<EsGoodsCO> goodsResult = this.goodsService.getEsGoods(skuVO.getGoodsId());
                EsGoodsCO goods = (EsGoodsCO) goodsResult.getData();
                if (goods == null || goods.getUpdateTime() == null || null == skuVO.getLastModify()) {
                    flag = true;
                    continue;

                }

                //判断最后修改时间是否相同，如果不同则说明商品信息有变动，需要从新将商品价格覆盖到购物车中
                if (goods.getUpdateTime().longValue() != skuVO.getLastModify().longValue()) {
                    flag = true;
                    break;
                }
                //判断活动时间是够过期，如果过期则重新刷新购物车价格
                List<TradePromotionGoodsVO> singleList = skuVO.getPromotionList().stream()
                        .filter(e -> null != e.getEndTime()).collect(Collectors.toList());
                for (TradePromotionGoodsVO tradePromotionGoodsVO : singleList) {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis > tradePromotionGoodsVO.getEndTime()){
                        flag = true;
                        continue;
                    }
                }
            }
        }

        // 如果价格有变动，则刷新购物车
        if (flag) {
            this.refreshCart(skey);
        }
    }

    /**
     * 刷新购物车价格
     *
     * @param
     * @author: libw 981087977@qq.com
     * @date: 2019/06/29 14:53:54
     * @return: void
     */
    public void refreshCart(String skey) {
        // 原购物车集合
        List<CartVO> cartList = this.getCacheCartList(skey);
        // 从缓存中从新读取商品的价格，覆盖到购物车中
        for (CartVO cartVO : cartList) {

            List<CartItemsVO> cartSkuList = cartVO.getCartItemsList();
            for (CartItemsVO skuVO : cartSkuList) {

                // 如果商品加入购物车后，商家修改了商品价格，购物车应变更相应的商品价格
                DubboResult<EsGoodsCO> goodsResult = this.goodsService.getEsGoods(skuVO.getGoodsId());
                EsGoodsCO goods = goodsResult.getData();

                // 商品不存在或者下架的场合
                if (goods == null) {
                    skuVO.setIsFailure(2);
                    continue;
                }else {
                    // 判断购物车商品是否失效
                    Integer isDel = goods.getIsDel();
                    Integer marketEnable = goods.getMarketEnable();
                    Integer isAuth = goods.getIsAuth();

                    Integer isFailures = 1;
                    // 判断条件
                    if (isDel == 1 || marketEnable == 2 || isAuth != 1){
                        isFailures = 2;
                        skuVO.setIsFailure(isFailures);
                        if (marketEnable == 2){
                            skuVO.setMarketEnable(2);
                        }
                    }else {
                        isFailures = 1;
                        skuVO.setIsFailure(isFailures);

                    }
                }

                //判断最后修改时间是否相同，如果不同将缓存中的商品价格覆盖到购物车中
                if (goods.getUpdateTime() != null || skuVO.getLastModify() != null || !goods.getUpdateTime().equals(skuVO.getLastModify())) {
                    List<EsGoodsSkuCO> skuList = goods.getSkuList();
                    for (EsGoodsSkuCO goodsSkuVo : skuList) {
                        if (goodsSkuVo.getId().intValue() == skuVO.getSkuId()) {
                            skuVO.setGoodsPrice(goodsSkuVo.getMoney());
                            skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(), skuVO.getNum()));
                            break;
                        }
                    }
                }

                //判断活动时间是够过期，如果过期则重新刷新购物车价格
                List<TradePromotionGoodsVO> list = skuVO.getPromotionList();

                for (TradePromotionGoodsVO tradePromotionGoodsVO : list) {
                    if(tradePromotionGoodsVO.getEndTime() != null){
                        long currentTimeMillis = System.currentTimeMillis();
                        if (currentTimeMillis > tradePromotionGoodsVO.getEndTime()){
                            List<EsGoodsSkuCO> skuList = goods.getSkuList();
                            for (EsGoodsSkuCO goodsSkuVo : skuList) {
                                if (goodsSkuVo.getId().intValue() == skuVO.getSkuId()) {
                                    skuVO.setCartPrice(goodsSkuVo.getMoney());
                                    skuVO.setGoodsPrice(goodsSkuVo.getMoney());
                                    skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(), skuVO.getNum()));
                                    break;
                                }
                            }
                            // 过滤掉时间为空的活动 防止空指针
                            List<TradePromotionGoodsVO> collectEndTime = list.stream()
                                    .filter(e -> null != e.getEndTime()).collect(Collectors.toList());
                            // 过滤掉过期的活动
                            List<TradePromotionGoodsVO> listEndTime = collectEndTime.stream()
                                    .filter(e -> System.currentTimeMillis() <= e.getEndTime()).collect(Collectors.toList());
                            // 过滤出时间为空的活动
                            List<TradePromotionGoodsVO> collectNoTime = list.stream()
                                    .filter(e -> null == e.getEndTime()).collect(Collectors.toList());
                            // 活动重新加到一块 用于价格计算以及页面活动列表展示
                            if (CollectionUtils.isNotEmpty(listEndTime)){
                                listEndTime.addAll(collectNoTime);
                            }
                            skuVO.setPromotionList(listEndTime);
                            skuVO.setPreferentialMessage(new PreferentialMessageVO());
                            skuVO.setPromotionType(PromotionTypeEnum.NO.name());

                        }
                    }
                }
            }
        }
        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cartList,skey);
        // V_2 end

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

    /**
     * 查询所有选中商品的购物车列表 如果活动价格发生变化 则重新封信活动列表，
     * 注 该接口会刷新缓存价格信息 为了规避在结算页设置余额被重置 现提供getCheckedGoodsItems()替代该接口
     * @author: libw 981087977@qq.com
     * @date: 2019/07/01 14:20:44
     * @return: java.util.List<com.shopx.trade.api.model.domain.vo.CartVO>
     */
    public List<CartVO> getCheckedItems(String skey) {
        // 获取购物车缓存
        List<CartVO> cartList = new ArrayList<>();
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartListCache = JSON.parseArray(itemListJson, CartVO.class);
        cartListCache.forEach(cartVO -> {
            List<CartItemsVO> cartItemsList = cartVO.getCartItemsList().stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
            cartItemsList.forEach(cartItemsVO -> {
                String promotionType = cartItemsVO.getPromotionType();
                List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
                List<TradePromotionGoodsVO> collect = promotionList.stream().filter(tradePromotionGoodsVO -> tradePromotionGoodsVO.getPromotionType().equals(promotionType)).collect(Collectors.toList());
                collect.forEach(tradePromotionGoodsVO -> {
                    Long activityId = tradePromotionGoodsVO.getActivityId();
                    String promotionType1 = tradePromotionGoodsVO.getPromotionType();
                    DubboResult<Boolean> isOld = iEsPromotionGoodsService.getPromotionGoodsIsOld(activityId, promotionType1);
                    if (isOld.getData()){
                        getPromotionList(cartItemsVO,skey);
                    }
                });
            });
        });
        //重新压入内存
        // V_2 start
        this.promotionToolManager.countPromotion(cartListCache,skey);
        CheckoutParamVO param = checkoutParamManager.getParam(skey);
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.getMemberAddress(param.getAddressId());
        if (result.isSuccess()){
            this.promotionToolManager.countShippingPrice(cartListCache,skey);
        }
        // V_2 end
        this.putCache(cacheKey, cartListCache);
        if (cartListCache == null) {
            return cartList;
        }
        cartListCache.forEach(cartCache -> {
            if (cartCache.getChecked() == 1) {
                // 取出所有被选中的购物车商品项
                List<CartItemsVO> itemsListCO = cartCache.getCartItemsList();
                List<CartItemsVO> itemsList = itemsListCO.stream()
                        .filter(item -> item.getChecked() == 1)
                        .collect(Collectors.toList());

                // 购物车里有数据的场合，添加到购物车
                if (itemsList.size() > 0) {
                    cartCache.setCartItemsList(itemsList);
                    cartList.add(cartCache);
                }
            } else {
                // 新建一个购物车，复制原有购物车属性
                CartVO cart = new CartVO();
                BeanUtil.copyProperties(cartCache, cart);

                // 取出所有被选中的购物车商品项
                List<CartItemsVO> itemsListCO = cartCache.getCartItemsList();
                List<CartItemsVO> itemsList = itemsListCO.stream()
                        .filter(item -> item.getChecked() == 1)
                        .collect(Collectors.toList());

                // 购物车里有数据的场合，添加到购物车
                if (itemsList.size() > 0) {
                    cart.setCartItemsList(itemsList);
                    cartList.add(cart);
                }
            }
            // 调用会员优惠券列表接口 获取coupon list
            DubboPageResult<EsCouponDO> esCouponList = iEsCouponService.getEsCouponListByShopId(cartCache.getShopId());
            List<EsCouponDO> couponDOList = esCouponList.getData().getList();

            List<EsCouponVO> esCouponVOS = BeanUtil.copyList(couponDOList, EsCouponVO.class);

            List<Long> couponDOIds = esCouponVOS.stream().map(EsCouponVO::getId).collect(Collectors.toList());
            Long memberId = null;
            if (StringUtils.isBlank(skey)){
                memberId = ShiroKit.getUser().getId();
            }else {
                //小程序获取当前用户ID
                memberId = this.getMemberIdApplet(skey);
            }
            DubboResult couponResult = iesMemberCouponService.getMemberWhetherCouponIds(memberId, couponDOIds);
            List<Long> couponList = (List<Long>)couponResult.getData();
            if(null != couponList){
                esCouponVOS = esCouponVOS.stream().map(e -> {
                    if(couponList.contains(e.getId())){
                        e.setIsReceive(1);
                        return e;
                    }
                    e.setIsReceive(0);
                    return e;
                }).collect(Collectors.toList());
            }

            cartCache.setEsCouponVO(esCouponVOS);
        });

        return cartList;
    }


    /**
     * @Description: 获取选中的购物车商品列表 不需要进行更新活动数据，
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/6/2 15:33
     * @param
     * @return       java.util.List<com.shopx.trade.api.model.domain.vo.CartVO>
     * @exception
     *
     */
    public List<CartVO> getCheckedGoodsItems(String skey) {
        // 获取购物车缓存
        List<CartVO> cartList = new ArrayList<>();
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartListCache = JSON.parseArray(itemListJson, CartVO.class);
        if (cartListCache == null) {
            return cartList;
        }
        cartListCache.forEach(cartCache -> {
            if (cartCache.getChecked() == 1) {
                // 取出所有被选中的购物车商品项
                List<CartItemsVO> itemsListCO = cartCache.getCartItemsList();
                List<CartItemsVO> itemsList = itemsListCO.stream()
                        .filter(item -> item.getChecked() == 1)
                        .collect(Collectors.toList());

                // 购物车里有数据的场合，添加到购物车
                if (itemsList.size() > 0) {
                    cartCache.setCartItemsList(itemsList);
                    cartList.add(cartCache);
                }
            } else {
                // 新建一个购物车，复制原有购物车属性
                CartVO cart = new CartVO();
                BeanUtil.copyProperties(cartCache, cart);

                // 取出所有被选中的购物车商品项
                List<CartItemsVO> itemsListCO = cartCache.getCartItemsList();
                List<CartItemsVO> itemsList = itemsListCO.stream()
                        .filter(item -> item.getChecked() == 1)
                        .collect(Collectors.toList());

                // 购物车里有数据的场合，添加到购物车
                if (itemsList.size() > 0) {
                    cart.setCartItemsList(itemsList);
                    cartList.add(cart);
                }
            }
        });

        return cartList;
    }

    /**
     *
     *
     * @param skuId skuId
     * @param num   商品数量
     * @author: libw 981087977@qq.com
     * @date: 2019/07/12 14:46:17
     * @return: void
     */
    public void buy(Long skuId, Integer num,String skey) {

        // 删除购物车中的此商品
        this.delete(new Integer[]{skuId.intValue()},skey);

        // 将购物车内原有的商品全部设置为未选中
        this.checkedAll(0,null,skey);

        // 将一键购的商品加入购物车
        this.addBuy(skuId, num,skey);
    }

    /**
     * 清除购物车中选中的商品
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/07/04 14:50:09
     * @return: void
     */
    public void cleanChecked(String skey) {
        // 原购物车集合
        List<CartVO> cartList = this.getCacheCartList(skey);
        // 新购物车集合
        List<CartVO> newCartList = new ArrayList<CartVO>();

        List<Integer> skuIds = new ArrayList<>();

        cartList.forEach(cartVO -> {
            // 原商品集合
            List<CartItemsVO> skuList = cartVO.getCartItemsList();
            // 新商品集合
            List<CartItemsVO> newSkuList = skuList.stream()
                    .filter(skuVO -> skuVO.getChecked() != 1)
                    .collect(Collectors.toList());

            List<Integer> cleanSkuId = skuList
                    .stream()
                    .filter(skuVO -> skuVO.getChecked() == 1)
                    .map(cartItemsVO -> cartItemsVO.getSkuId().intValue())
                    .collect(Collectors.toList());

            // 如果商品集合不为空,添加到 新购物车集合 中
            if (!newSkuList.isEmpty()) {
                cartVO.setCartItemsList(newSkuList);
                newCartList.add(cartVO);
            }
            skuIds.addAll(cleanSkuId);
        });

        Integer[] skuId = new Integer[skuIds.size()];
        skuId = skuIds.toArray(skuId);
        delete(skuId,skey);

        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(newCartList,skey);
        // V_2 end
        // 将新的购物车集合压入缓存中
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        this.putCache(cacheKey, newCartList);

    }

    /**
     * 设置配送价格
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/07/02 13:14:39
     * @return: void
     */
    public void setShipping(String where,Integer isDelivery,String skey) {
        // 获取操作人id（会员id）
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }

        String redissonLockName = TradePriceManager.TRADE_PRICE_CHANGE_REDISSON_LOCK_NAME_PREFIX+memberId.toString();
        try{
            // 获取锁 超时5秒 等待5秒
            redissonLock.lock(redissonLockName,5,5);
            // 读取所有已选中的购物车商品
            List<CartVO> cartList = this.getCacheCartList(skey);
            // 设置配送方式及价格
//            this.checkoutParamManager.setDeliveryV2(isDelivery,14l);
            this.shippingManager.setShippingPrice(cartList,where,isDelivery,skey);
            // 计算配送费用
            //this.promotionToolManager.countShippingPrice(cartList);
            logger.info("运费计算开始时间："+ DateUtils.format(new Date(),DateUtils.TIMESTAMP_PATTERN));
            this.promotionToolManager.countShippingPrice(cartList,skey);
            logger.info("运费计算结束时间："+ DateUtils.format(new Date(),DateUtils.TIMESTAMP_PATTERN));
            // 重新压入内存
            String cacheKey = "";
            if (StringUtils.isBlank(skey)){
                cacheKey = this.getSessionKey();
            }else {
                //小程序获取当前用户购物车的key
                cacheKey = this.getSessionKeyApplet(skey);
            }
            this.putCache(cacheKey, cartList);
        }finally {
            redissonLock.release(redissonLockName); // 释放锁
        }
    }

    private void putCache(String cacheKey, List<CartVO> itemList) {
        //重新压入缓存
        this.jedisCluster.set(cacheKey, JSON.toJSONString(itemList));
    }

    /**
     * 重新组装购物车，并压入缓存
     *
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/11 16:12:48
     * @return: void
     */
    private List<CartVO> assembleCart(String skey) {
        long startTime = System.currentTimeMillis();
        List<CartVO> cartList = new ArrayList<>();

        // 获取操作人id（会员id）
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        // 根据会员id获取购物车List
        DubboPageResult pageResult = commerceItemsService.getCommercelItemsListByMemeberId(memberId);
        if (!pageResult.isSuccess()) {
            throw new ArgumentException(pageResult.getCode(), pageResult.getMsg());
        }
        List<EsCommercelItemsDO> commerceItemsList = pageResult.getData().getList();
        commerceItemsList.sort(Comparator.comparing(EsCommercelItemsDO::getShopId).reversed());
        // 根据购物车项中的店铺id, 重新组装购物车
        List<Long> shopIdList = new ArrayList<>();
        CartVO cart = null;
        List<CartItemsVO> cartItemsList = new ArrayList<>();
        // 用于最后一次循环，保证cart能加入到list中
        commerceItemsList.add(new EsCommercelItemsDO());
        for (EsCommercelItemsDO commerceItemsDO : commerceItemsList) {

            // 店铺id不存在的场合, 新建一个购物车VO
            if (!shopIdList.contains(commerceItemsDO.getShopId())) {
                if (cart != null) {
                    cart.setCartItemsList(cartItemsList);
                    cartList.add(cart);
                    cartItemsList = new ArrayList<>();
                }
                // 店铺为空的场合跳出循环
                if (commerceItemsDO.getShopId() == null) {
                    continue;
                }

                DubboResult result = shopService.getShop(commerceItemsDO.getShopId());
                if (!result.isSuccess()) {
                    throw new ArgumentException(result.getCode(), result.getMsg());
                }
                EsShopDO shopDO = (EsShopDO) result.getData();
                cart = new CartVO(shopDO.getId(), shopDO.getShopName());
                shopIdList.add(shopDO.getId());
            }

            // 获取商品信息
            DubboResult goodsResult = goodsService.getEsBuyerGoods(commerceItemsDO.getProductId());
            if (!goodsResult.isSuccess()) {
                // 如果这个商品被物理删除 则删除购物车中该条商品数据
                int skuId = commerceItemsDO.getSkuId().intValue();
                Integer [] skuIds = {skuId};
                this.delete(skuIds,skey);
                continue;
            }
            EsGoodsCO goods = (EsGoodsCO) goodsResult.getData();

            // 获取Sku信息
            DubboResult goodsSkuResult = goodsSkuService.getGoodsSku(commerceItemsDO.getSkuId());
            if (!goodsSkuResult.isSuccess()) {
                //  如果这个商品被物理删除 则删除购物车中该条商品数据
                int skuId = commerceItemsDO.getSkuId().intValue();
                Integer [] skuIds = {skuId};
                this.delete(skuIds,skey);
                continue;
            }

            //  trade 商品是否失效，商品是否是生鲜，运费模板，是否包邮都没有设置，后面看看是否查询这些信息
            EsGoodsSkuCO goodsSkuCO = (EsGoodsSkuCO)goodsSkuResult.getData();
            CartItemsVO cartItem = new CartItemsVO(goodsSkuCO);
            cartItem.setNum(commerceItemsDO.getQuantity());
            cartItem.setCartPrice(commerceItemsDO.getPrice());
            cartItem.setSubtotal(MathUtil.multiply(commerceItemsDO.getQuantity(),
                    ((EsGoodsSkuCO) goodsSkuResult.getData()).getMoney()));
            cartItem.setIsFreeFreight(goods.getGoodsTransfeeCharge());
            cartItem.setIsFresh(goods.getIsFresh());
            cartItem.setTemplateId(goods.getTemplateId());
            cartItem.setGoodsImage(goods.getOriginal());
            cartItem.setLastModify(commerceItemsDO.getUpdateTime());
            // 默认未选中
            cartItem.setChecked(0);
            cartItem.setIsSelf(goodsSkuCO.getIsSelf());
            // 判断购物车商品是否失效
            Integer isDel = goods.getIsDel();
            Integer marketEnable = goods.getMarketEnable();
            Integer isAuth = goods.getIsAuth();
            Integer isEnable = goodsSkuCO.getIsEnable();

            Integer isFailures = 1;
            // 判断条件
            if (isDel == 1 || marketEnable == 2 || isAuth != 1 || isEnable ==2){
                isFailures = 2;
                cartItem.setIsFailure(isFailures);
                if (marketEnable == 2){
                    cartItem.setMarketEnable(2);
                }
                // 设置默认配送方式 配送方式 notInScope 不在配送范围，express 快递(默认)，selfMention 自提
                cartItem.setDeliveryMethod("express");
                cartItemsList.add(cartItem);
            }else {
                isFailures = 1;
                cartItem.setIsFailure(isFailures);
                // 设置默认配送方式 配送方式 notInScope 不在配送范围，express 快递(默认)，selfMention 自提
                cartItem.setDeliveryMethod("express");
                // V_2
                // 读取这个商品品参与的所有活动
                this.getPromotionList(cartItem,skey);

                cartItemsList.add(cartItem);
            }

        }
        long endTime = System.currentTimeMillis();
        long total = endTime - startTime;
        logger.info("重组购物车信息所用时间======"+ total);
        return cartList;
    }

    /**
     * 设置优惠券的时候分为三种情况：前2种情况couponId 不为0,不为空。第3种情况couponId为0<br>
     * 1、使用优惠券:在刚进入订单结算页，为使用任何优惠券之前。<br>
     * 2、切换优惠券:在1、情况之后，当用切换优惠券的时候。<br>
     * 3、取消已使用的优惠券:用户不想使用优惠券的时候。<br>
     * @param couponId
     * @param shopId
     */
    public ReturnCouponMsgVO userCoupon(long couponId, long shopId,String skey){
        // 获取购物车商品数据
        List<CartVO> cartList = this.getCacheCartList(skey);
        Long ownerId = shopId;
        if (CollectionUtils.isEmpty(cartList)){
            throw new ArgumentException(TradeErrorCode.CART_EMPTY.getErrorCode(),TradeErrorCode.CART_EMPTY.getErrorMsg());
        }
        CartVO cartVO  = this.findCart(ownerId, cartList);

        // 获取操作人id（会员id）
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }

        //读取此购物车的总价。
        //需要考虑的是如果是切换优惠券时，之前使用的优惠券优惠金额已经在总价中减去。在下面需要加上切换之前的优惠券金额
//        Double totalPrice = cartVO.getPrice().getTotalPrice();
        Double totalPrice = MathUtil.add(cartVO.getPrice().getTotalPrice(),cartVO.getPrice().getDiscountPrice());

        OrderCouponVO orderCouponVO = new OrderCouponVO();

        //使用或者取消使用,1：要使用
        int isUse = 0;
        if(couponId!=0){
            // 获取该会员的优惠券信息
            EsMemberCouponDTO esMemberCouponDTO = new EsMemberCouponDTO();
            esMemberCouponDTO.setCouponId(couponId);
            esMemberCouponDTO.setMemberId(memberId);
            // 通过优惠券ID 和会员ID 检测该会员是否存在该优惠券
            DubboPageResult<EsMemberCouponDO> byMemberIdAndCouponIdList = this.iEsMemberCouponService.getByMemberIdAndCouponIdList(esMemberCouponDTO);
            List<EsMemberCouponDO> memberCouponDOList = byMemberIdAndCouponIdList.getData().getList();
            if (memberCouponDOList.size() > 0){
                // 获取第一条
                EsMemberCouponDO memberCouponData = memberCouponDOList.get(0);

                if(totalPrice < memberCouponData.getCouponThresholdPrice().doubleValue()){
                    throw new ArgumentException(TradeErrorCode.COUPON_NOT_ENOUGH.getErrorCode(),TradeErrorCode.COUPON_NOT_ENOUGH.getErrorMsg());
                }
                orderCouponVO = this.setCouponParam(memberCouponData,shopId);
                isUse=1;
            }

        }
        orderCouponVO.setShopId(shopId);
        this.promotionToolManager.countCouponPrice(cartVO, orderCouponVO, isUse,skey);

        //重新压入内存
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        this.putCache(cacheKey, cartList);

        List<CartVO> collect = cartList.stream().filter(cart -> cart.getCouponList().size() > 0 ).collect(Collectors.toList());

        Integer num = 0;
        Double total = 0.0;
        ReturnCouponMsgVO returnCouponMsgVO = new ReturnCouponMsgVO();
        for (CartVO cartVO1 : collect) {
            List<OrderCouponVO> couponList = cartVO1.getCouponList();
            if (CollectionUtils.isNotEmpty(couponList)){
                for (OrderCouponVO orderCouponVO1:couponList) {

                    Double couponMoney = orderCouponVO1.getCouponMoney();
                    num = num + 1;
                    total = MathUtil.add(total, couponMoney);
//                    total += couponMoney;
                    returnCouponMsgVO.setNum(num);
                    returnCouponMsgVO.setTotalPrice(total);
                }
            }
            continue;
        }
        return returnCouponMsgVO;
    };

    /**
     * 设置购物车优惠券的参数
     * @return
     */
    private OrderCouponVO setCouponParam(EsMemberCouponDO memberCoupon, long shopId){
        OrderCouponVO coupon = new OrderCouponVO();
        coupon.setCouponMoney(memberCoupon.getCouponMoney().doubleValue());
        coupon.setMemberCouponId(memberCoupon.getId());
        coupon.setEndTime(memberCoupon.getEndTime());
        coupon.setShopId(shopId);
        coupon.setCouponId(memberCoupon.getCouponId());
        coupon.setUseTerm("满"+memberCoupon.getCouponThresholdPrice()+"元可用");
        return coupon;
    }

    /**
     * 查询商品的所有活动
     * 将活动分类为：单品活动类，组合活动类、平台活动类
     *
     * @param cartItems 购物车sku
     * @param memberId  会员ID
     * @return
     */
    private CartItemsVO getPromotion(CartItemsVO cartItems, Long memberId) {

        List<PromotionVO> list = promotionGoodsManager.getPromotion(cartItems.getGoodsId(),cartItems.getSkuId(), memberId);

        // 单品活动集合 目前没有.活动全部可以组合
        List<TradePromotionGoodsVO> singleList = new ArrayList<>();
        // 组合活动集合
        List<TradePromotionGoodsVO> groupList = new ArrayList<>();

        // 遍历这个商品参与的所有活动
        for (PromotionVO promotionVO : list) {
            TradePromotionGoodsVO promotionGoodsVO = new TradePromotionGoodsVO();
            BeanUtil.copyProperties(promotionVO, promotionGoodsVO);
            promotionGoodsVO.setIsCheck(2);
            promotionGoodsVO.setStartTime(promotionVO.getStartTime());
            promotionGoodsVO.setEndTime(promotionVO.getEndTime());


            if (promotionGoodsVO.getPromotionType().equals(PromotionTypeEnum.SECKILL.name())) {
                SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(promotionVO.getGoodsId());
                promotionGoodsVO.setSoldOutCount(seckillGoodsVO.getSoldQuantity());
            }

            if (PromotionTypeEnum.isSingle(promotionGoodsVO.getPromotionType())) {
                promotionGoodsVO.setIsCheck(1);
                if (promotionVO.getPromotionType().equals(PromotionTypeEnum.GOODS_DISCOUNT.name())){
                    promotionGoodsVO.setGoodsDiscounts(promotionVO.getGoodsDiscount().getDiscount());
                }
                singleList.add(promotionGoodsVO);
            } else {
                promotionGoodsVO.setIsCheck(1);
                groupList.add(promotionGoodsVO);
            }
        }

        cartItems.setSingleList(singleList);
        cartItems.setGroupList(groupList);
        return cartItems;
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
     * 由一个产品列表中找到产品
     *
     * @param skuId   产品id
     * @param skuList 产品列表
     * @return 找到的产品
     */
    protected CartItemsVO findSku(Long skuId, List<CartItemsVO> skuList) {

        for (CartItemsVO item : skuList) {
            if (item.getSkuId().equals(skuId)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 将立即购买的商品添加到商品缓存中
     *
     * @param skuId skuId
     * @param num   商品数量
     * @author: libw 981087977@qq.com
     * @date: 2019/07/12 14:45:18
     * @return: void
     */
    private void addBuy(Long skuId, Integer num,String skey) {

        // 读取这个产品的信息
        CartItemsVO sku = this.getSku(skuId, num);
        // 读取这个产品参与的所有活动
        this.getPromotionList(sku,skey);

        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }

        // 获取购物车缓存
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartList = JSON.parseArray(itemListJson, CartVO.class);
        List<CartVO> cartList1 = new ArrayList<>();
        // 查找出此货品的卖家是否已经加入到购物车中
        CartVO cart = this.findCart(sku.getShopId(), cartList);

        // 此卖家还没有在购物列表中，new一个新的
        if (cart == null) {
            cart = new CartVO(sku.getShopId(), sku.getShopName());

            // 加入此商品
            sku.setNum(num);
            sku.setSubtotal(MathUtil.multiply(sku.getGoodsPrice(), num));
            cart.getCartItemsList().add(sku);
            CartVO cartVO = immediatePurchaseAssembleCart(cart,skey);
            // 加入购物车列表
            cartList1.add(cartVO);

        } else {
            this.updateSkuNum(sku, num, cart,false,skey);
        }

        //调用促销价格计算的接口
//        this.promotionToolManager.countPrice(cartList1);
        // V_2 start
        this.promotionToolManager.countPromotion(cartList1,skey);
        // V_2 end
        //组合展示购物车数据
        CartVO cartVo = this.promotionToolManager.copySkuToPromotion(cart);
        cartList.remove(cart);
        cartList.add(cartVo);

        // 存到缓存里
        this.putCache(cacheKey, cartList);

    }

    /**
     * 立即购买购物车数据封装
     *
     * @auther: AJin
     * @date: 2019/06/11 16:12:48
     * @return: void
     */
    private CartVO immediatePurchaseAssembleCart(CartVO cartVO,String skey) {

        List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();

        List<CartItemsVO> cartItemsList1 = new ArrayList<>();
        cartItemsList.forEach(cartItemsVO -> {
            // 获取商品信息
            DubboResult goodsResult = goodsService.getEsBuyerGoods(cartItemsVO.getGoodsId());
            EsGoodsCO goods = (EsGoodsCO) goodsResult.getData();
            // 获取Sku信息
            DubboResult goodsSkuResult = goodsSkuService.getGoodsSku(cartItemsVO.getSkuId());

            //  trade 商品是否失效，商品是否是生鲜，运费模板，是否包邮都没有设置，后面看看是否查询这些信息
            EsGoodsSkuCO goodsSkuCO = (EsGoodsSkuCO) goodsSkuResult.getData();
            CartItemsVO cartItem = new CartItemsVO(goodsSkuCO);
            cartItem.setNum(cartItemsVO.getNum());
            cartItem.setCartPrice(cartItemsVO.getCartPrice());
            cartItem.setSubtotal(MathUtil.multiply(cartItemsVO.getNum(),
                    ((EsGoodsSkuCO) goodsSkuResult.getData()).getMoney()));
            cartItem.setIsFreeFreight(goods.getGoodsTransfeeCharge());
            cartItem.setIsFresh(goods.getIsFresh());
            cartItem.setTemplateId(goods.getTemplateId());
            cartItem.setGoodsImage(goods.getOriginal());
            cartItem.setIsSelf(goodsSkuCO.getIsSelf());
            // 默认选中
            cartItem.setChecked(1);
            // 能购买的商品 失效状态一般都为有效
            Integer isFailures = 1;
            cartItem.setIsFailure(isFailures);
//            if (cartItemsVO.getIsFreeFreight() == 2){
//                cartItem.setDeliveryMethod("express");
//            }else {
//                this.setShipMethod(cartItem);
//            }
            this.getPromotionList(cartItem,skey);
            cartItemsList1.add(cartItem);

        });
        cartVO.setCartItemsList(cartItemsList1);
        // 立即购买 选中状态
        cartVO.setChecked(1);
        return cartVO;
    }


    /**
     *
     * @param sku 产品信息
     * @param num	数量
     * @param cart	购物车
     * @param overwriteNum
     */
    protected void updateSkuNum(CartItemsVO sku , int num, CartVO cart, boolean overwriteNum,String skey){

        List<CartItemsVO> skuList = cart.getCartItemsList();
        // **如果已经购买过更新数量及小计
        CartItemsVO orginalSku  = this.findSku(sku.getSkuId(), skuList);



        //已经购买过此产品
        if(orginalSku!=null){

            if(!overwriteNum){
                // 原始的购买数量
                Integer originalNum = orginalSku.getNum();
                num = originalNum + num;
            }

            // 获取商品sku信息
            DubboResult<EsGoodsSkuCO> goodsSku = this.goodsSkuService.getGoodsSku(sku.getSkuId());
            if (!goodsSku.isSuccess()){
                throw new ArgumentException(goodsSku.getCode(),goodsSku.getMsg());
            }
            GoodsSkuVO skuVO = new GoodsSkuVO();
            EsGoodsSkuCO goodsSkuCO = goodsSku.getData();

            BeanUtils.copyProperties(goodsSkuCO,skuVO);
            // 通过 goodsId 获取商品的上架状态，删除状态，运费状态
            DubboResult<EsGoodsCO> esGoods = this.goodsService.getEsGoods(sku.getGoodsId());
            if (!esGoods.isSuccess()){
                throw new ArgumentException(esGoods.getCode(),esGoods.getMsg());
            }
            EsGoodsCO esGoodsCO = esGoods.getData();
            skuVO.setMarketEnable(esGoodsCO.getMarketEnable());
            skuVO.setGoodsTransfeeCharge(esGoodsCO.getGoodsTransfeeCharge());
            skuVO.setDisabled(esGoodsCO.getIsDel());
            TradeConvertGoodsSkuVO goodsSkuVO = TradeGoodsConverter.goodsSkuVOConverter(skuVO);

            //如果购物车中总的商品数量大于库存中可用库存时，则将购物车中的商品库存设为可用库存的最大值
            if(num>goodsSkuVO.getEnableQuantity()){
                num = goodsSkuVO.getEnableQuantity();
            }

            orginalSku.setNum(num);
            //修改数量的时候需要把价格重新赋值。
            orginalSku.setGoodsPrice(sku.getGoodsPrice());
            //计算小计金额
            orginalSku.setSubtotal(MathUtil.multiply(sku.getGoodsPrice(),num));
        }else{
            // 购物车中商品设置未选中，立即购买商品设置为选中状态
            skuList.forEach(skulist->{
                skulist.setChecked(0);
            });
            sku.setNum(num);
            sku.setSubtotal(MathUtil.multiply(sku.getGoodsPrice(),num));
            sku.setChecked(1);
            // 设置默认配送方式 配送方式 notInScope 不在配送范围，express 快递(默认)，selfMention 自提
            // 获取该用户的默认收货地址
            this.setShipMethod(sku,skey);
            skuList.add(sku);
        }

    }

    /**
     * 获取sku信息
     *
     * @param skuId skuId
     * @param num   商品数量
     * @author: libw 981087977@qq.com
     * @date: 2019/07/12 14:52:39
     * @return: com.shopx.trade.api.model.domain.vo.CartItemsVO
     */
    private CartItemsVO getSku(Long skuId, Integer num) {

        // 获取Sku信息
        DubboResult goodsSkuResult = goodsSkuService.getGoodsSku(skuId);
        if (!goodsSkuResult.isSuccess()) {
            throw new ArgumentException(goodsSkuResult.getCode(), goodsSkuResult.getMsg());
        }
        EsGoodsSkuCO sku = (EsGoodsSkuCO) goodsSkuResult.getData();

        // 获取商品信息
        DubboResult goodsResult = goodsService.getEsGoods(sku.getGoodsId());
        if (!goodsResult.isSuccess()) {
            throw new ArgumentException(goodsResult.getCode(), goodsResult.getMsg());
        }

        EsGoodsCO goods = (EsGoodsCO) goodsResult.getData();

        // 判断可用库存是否为null || 可用库存是否为0 || 购买数量大于库存数量
        if (sku.getEnableQuantity() == null || sku.getEnableQuantity() == 0 || num > sku.getEnableQuantity()) {
            throw new ArgumentException(TradeErrorCode.OVERSELL_ERROR.getErrorCode(),
                    TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
        }

        // 生成一个购物项
        CartItemsVO skuVO = new CartItemsVO(sku);
        skuVO.setNum(num);
        skuVO.setCartPrice(sku.getMoney());
        skuVO.setSubtotal(MathUtil.multiply(num, sku.getMoney()));
        skuVO.setThisGoodsPrice(MathUtil.multiply(num, sku.getMoney()));
        skuVO.setIsFreeFreight(goods.getGoodsTransfeeCharge());
        skuVO.setIsFresh(goods.getIsFresh());
        skuVO.setLastModify(goods.getUpdateTime());
        skuVO.setIsFreeFreight(goods.getGoodsTransfeeCharge());
        skuVO.setIsFresh(goods.getIsFresh());
        skuVO.setTemplateId(goods.getTemplateId());
        skuVO.setGoodsImage(goods.getOriginal());
        skuVO.setIsFailure(1);
        return skuVO;
    }

    /**
     * 根据属主id 从一个集合中查找cart
     * @param ownerId 属主id
     * @param itemList 购物车列表
     * @return 购物车
     */
    protected CartVO findCart(Long ownerId, List<CartVO> itemList) {
        if (itemList == null){
            return null;
        }
        for (CartVO item : itemList) {
            if (item.getShopId().equals(ownerId)) {
                return item;
            }
        }
        return null;
    }


    /**
     * @Description: 设置是否自提
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/10/14 11:03
     * @param        skuIds     skuId列表
     * @param        isDelivery 是否自提
     * @return       void
     */
    protected void setDelivery(List<Long> skuIds, Integer isDelivery,String skey) {
        // 获取购物车缓存
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartList = JSON.parseArray(itemListJson, CartVO.class);
        // 如果为空查询数据库，重新生成缓存
        if (cartList == null || cartList.isEmpty()) {
            cartList = this.assembleCart(skey);
            cartList = cartList.stream().filter(cartVO -> (cartVO.getCartItemsList().size()!=0)).collect(Collectors.toList());
        }

        // 调用促销价格计算的接口
        // V_2 start
        this.promotionToolManager.countPromotion(cartList,skey);
        // V_2 end

        // 为每一个可以设置自提的商品设置自提标识 (只有卓付商城(14)的订单支持自提 )
        List<CartVO> collect = cartList.stream().filter(cartVO -> "14".equals(cartVO.getShopId().toString())).collect(Collectors.toList());
        collect.forEach(cartVO -> {
            List<CartItemsVO> itemsList = cartVO.getCartItemsList();
            for (CartItemsVO cartItems : itemsList) {
                if (skuIds.contains(cartItems.getSkuId())) {
                    cartItems.setIsDelivery(isDelivery);
                    if (isDelivery == 1){
                        cartVO.setShippingTypeName("自提");
                        cartItems.setDeliveryMethod("selfMention");
                    }else if (isDelivery == 2){
                        cartVO.setShippingTypeName("快递");
                        cartItems.setDeliveryMethod("express");
                    }
                }
            }
        });

        // 存到缓存里
        this.putCache(cacheKey, cartList);
    }

    public List<CartVO> priceDownCartList(List<CartVO> list,String skey) {

        List<CartVO> cartList = new ArrayList<>();
        // 筛选购物车中降价的商品，
        if (list == null) {
            return cartList;
        }
        // 遍历 所有购物车商品  每一个店铺
        for (CartVO cartCache : list) {
            // 调用会员优惠券列表接口 获取coupon list
            DubboPageResult<EsCouponDO> esCouponList = iEsCouponService.getEsCouponListByShopId(cartCache.getShopId());
            List<EsCouponDO> couponDOList = esCouponList.getData().getList();

            List<EsCouponVO> esCouponVOS = BeanUtil.copyList(couponDOList, EsCouponVO.class);

            List<Long> couponDOIds = esCouponVOS.stream().map(EsCouponVO::getId).collect(Collectors.toList());

            Long memberId = null;
            if (StringUtils.isBlank(skey)){
                memberId = ShiroKit.getUser().getId();
            }else {
                //小程序获取当前用户ID
                memberId = this.getMemberIdApplet(skey);
            }
            DubboResult result = iesMemberCouponService.getMemberWhetherCouponIds(memberId, couponDOIds);
            List<Long> couponList = (List<Long>)result.getData();
            if(null != couponList){
                esCouponVOS = esCouponVOS.stream().map(e -> {
                    if(couponList.contains(e.getId())){
                        e.setIsReceive(1);
                        return e;
                    }
                    e.setIsReceive(0);
                    return e;
                }).collect(Collectors.toList());
            }

            // 遍历 购物车项中的商品信息 判断价格变动
            List<CartItemsVO> cartItemsList = cartCache.getCartItemsList();
            List<CartItemsVO> newCartItems = new ArrayList<>();
            newCartItems = cartItemsList.stream()
                    .filter(cartItemsVO ->(cartItemsVO.getGoodsPrice() < cartItemsVO.getCartPrice())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(newCartItems)){
                cartCache.setCartItemsList(newCartItems);
                cartCache.setEsCouponVO(esCouponVOS);
                cartList.add(cartCache);
            }
        }
        return cartList;
    }

    public List<CartVO> stockShortageCartList(List<CartVO> list,String skey) {
        List<CartVO> cartList = new ArrayList<>();
        // 筛选购物车中库存紧缺的商品，
        if (list == null) {
            return cartList;
        }
        // 遍历 所有购物车商品  每一个店铺
        for (CartVO cartCache : list) {
            // 调用会员优惠券列表接口 获取coupon list
            DubboPageResult<EsCouponDO> esCouponList = iEsCouponService.getEsCouponListByShopId(cartCache.getShopId());
            List<EsCouponDO> couponDOList = esCouponList.getData().getList();

            List<EsCouponVO> esCouponVOS = BeanUtil.copyList(couponDOList, EsCouponVO.class);

            List<Long> couponDOIds = esCouponVOS.stream().map(EsCouponVO::getId).collect(Collectors.toList());

            Long memberId = null;
            if (StringUtils.isBlank(skey)){
                memberId = ShiroKit.getUser().getId();
            }else {
                //小程序获取当前用户ID
                memberId = this.getMemberIdApplet(skey);
            }
            DubboResult result = iesMemberCouponService.getMemberWhetherCouponIds(memberId, couponDOIds);
            List<Long> couponList = (List<Long>)result.getData();
            if(null != couponList){
                esCouponVOS = esCouponVOS.stream().map(e -> {
                    if(couponList.contains(e.getId())){
                        e.setIsReceive(1);
                        return e;
                    }
                    e.setIsReceive(0);
                    return e;
                }).collect(Collectors.toList());
            }

            // 遍历 购物车项中的商品信息 判断价格变动
            List<CartItemsVO> cartItemsList = cartCache.getCartItemsList();
            List<CartItemsVO> newCartItems = new ArrayList<>();
            newCartItems = cartItemsList.stream()
                    .filter(cartItemsVO ->((cartItemsVO.getWarningValue() == null ? 10 : cartItemsVO.getWarningValue()) > cartItemsVO.getEnableQuantity())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(newCartItems)){
                cartCache.setEsCouponVO(esCouponVOS);
                cartCache.setCartItemsList(newCartItems);
                cartList.add(cartCache);
            }
        }
        return cartList;
    }


    /**
     * 购物车页面读取购物车数量
     *
     * @author: yuanj 595831329@qq.com
     * @date: 2020/03/09 18:21:20
     * @return: EsTabCountVO
     */
    public EsTabCountVO getCartTabCount(String skey) {
        // 获取购物车缓存
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartList = JSON.parseArray(itemListJson, CartVO.class);
        EsTabCountVO esTabCountVO=new EsTabCountVO();
        int i=0;
        int j=0;
        if (cartList != null && !cartList.isEmpty()) {
            // 遍历 所有购物车商品  每一个店铺
            for (CartVO cartCache : cartList) {
                // 遍历 购物车项中的商品信息 判断价格变动
                List<CartItemsVO> cartItemsList = cartCache.getCartItemsList();
                for (CartItemsVO cv:cartItemsList) {
                    j++;
                    if (cv.getGoodsPrice() < cv.getCartPrice()){
                        i++;
                    }
                }
            }
        }
        esTabCountVO.setPriceDownCount(i);
        esTabCountVO.setAllCount(j);

        return esTabCountVO;
    }

    /**
     * @Description: PC 端再次购买
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/6/24 10:45
     * @param
     * @return       void
     * @exception
     *
     */
    public void buyAgainPC(String orderSn,String skey) {


        DubboPageResult<EsOrderItemsDO> result= itemsService.getEsOrderItemsByOrderSn(orderSn);
        if (result.isSuccess()){
            List<EsOrderItemsDO> items = result.getData().getList();
            // 将购物车内原有的商品全部设置为未选中
            this.checkedAll(0,null,skey);
            //将订单项库存够的全部加入购物车
            for (EsOrderItemsDO it:items) {
                // 验证商品SKU启用状态
                DubboResult<EsGoodsSkuCO> goodsSku1 = goodsSkuService.getGoodsSkuEnable(it.getSkuId());
                if (!goodsSku1.isSuccess()) {
                    continue;
                }
                // 验证商品是否状态
                DubboResult<EsGoodsCO> esGoods = this.goodsService.getEsGoods(goodsSku1.getData().getGoodsId());
                if (!esGoods.isSuccess()) {
                    continue;
                }
                this.addV2(it.getSkuId(),it.getNum(),skey);

            }

        }

    }

    /**
     *
     *
     * @param orderSn 订单号
     * @author: yuanj 595831329@qq.com
     * @date: 2020/04/01 14:46:17
     * @return: void
     */
    public void buyAgain(String orderSn,String skey) {
        DubboPageResult<EsOrderItemsDO> result= itemsService.getEsOrderItemsByOrderSn(orderSn);
        if (result.isSuccess()){
            List<EsOrderItemsDO> items = result.getData().getList();
            // 将购物车内原有的商品全部设置为未选中
            this.checkedAll(0,null,skey);
            //将订单项库存够的全部加入购物车
            for (EsOrderItemsDO it:items) {
                // 验证商品SKU启用状态
                DubboResult<EsGoodsSkuCO> goodsSku1 = goodsSkuService.getGoodsSkuEnable(it.getSkuId());
                if (!goodsSku1.isSuccess()) {
                    continue;
                }
                // 验证商品是否状态
                DubboResult<EsGoodsCO> esGoods = this.goodsService.getEsGoods(goodsSku1.getData().getGoodsId());
                if (!esGoods.isSuccess()) {
                    continue;
                }
                this.addAgain(it.getSkuId(),it.getNum(),skey);
            }
        }
    }

    /**
     * 往购物车中添加商品(库存不够的不添加)
     *
     * @param skuId      商品skuID
     * @param num        商品数量
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/04/21 10:13:35
     * @return: void
     */
    public List<CartVO> addAgain(Long skuId, Integer num,String skey) {

        // 获取用户id
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        Long cartId;
        Integer skuNum;

        // 根据会员id 查询购物车项数量及购物车id
        DubboResult cartCountResult = cartService.getByMemberId(memberId);
        if (!cartCountResult.isSuccess()) {
            throw new ArgumentException(cartCountResult.getCode(), cartCountResult.getMsg());
        }

        // 如果查询为空, 则新增购物车。设置购物车id，设置sku项数量
        EsCartNumDO cartNum = (EsCartNumDO) cartCountResult.getData();
        if (cartNum == null ||cartNum.getCartId() == null) {
            EsCartDTO cartDTO = new EsCartDTO();
            cartDTO.setMemberId(memberId);
            // 插入购物车
            DubboResult cartResult = cartService.insertCart(cartDTO);
            if (!cartResult.isSuccess()) {
                throw new ArgumentException(cartResult.getCode(), cartResult.getMsg());
            }

            cartId = (Long) cartResult.getData();
            skuNum = 0;
        } else {
            cartId = cartNum.getCartId();
            skuNum = cartNum.getSkuNum();
        }

        // 判断sku是否存在，存在的场合更新购物车项数量
        DubboResult cartItemResult = this.commerceItemsService.getItemsBySkuId(skuId, cartId);
        if (cartItemResult.isSuccess()) {
            if (cartItemResult.getData() == null) {
                // 取出配置的购物车数量
                DubboResult configCountResult = cartConfigureService.getCartConfigure();
                if (!configCountResult.isSuccess()) {
                    throw new ArgumentException(configCountResult.getCode(), configCountResult.getMsg());
                }

                // 判断购物车数量是否大于后台配置的购物车数量
                if (skuNum >= ((EsCartConfigureDO) configCountResult.getData()).getQuantity()) {
                    throw new ArgumentException(TradeErrorCode.CART_COUNT_MAX.getErrorCode(), TradeErrorCode.CART_COUNT_MAX.getErrorMsg());
                }

                // 查询商品sku信息 组装购物车项
                DubboResult goodsSkuResult = goodsSkuService.getGoodsSku(skuId);
                if (!goodsSkuResult.isSuccess()) {
                    throw new ArgumentException(goodsSkuResult.getCode(), goodsSkuResult.getMsg());
                }

                EsGoodsSkuCO goodsSku = (EsGoodsSkuCO) goodsSkuResult.getData();
                EsCommercelItemsDTO cartItemDTO = new EsCommercelItemsDTO();
                cartItemDTO.setCartId(cartId);
                cartItemDTO.setGoodsSn(goodsSku.getGoodsSn());
                cartItemDTO.setPrice(goodsSku.getMoney());
                cartItemDTO.setShopId(goodsSku.getShopId());
                cartItemDTO.setProductId(goodsSku.getGoodsId());
                cartItemDTO.setSkuId(goodsSku.getId());
                cartItemDTO.setQuantity(num);
                cartItemDTO.setShopId(goodsSku.getShopId());
                // 存到数据库里
                DubboResult result = commerceItemsService.insertCommercelItems(cartItemDTO);
                if (!result.isSuccess()) {
                    throw new ArgumentException(result.getCode(), result.getMsg());
                }
            } else {
                // sku存在的场合更新数量
                EsCommercelItemsDO cartItemsDO = (EsCommercelItemsDO) cartItemResult.getData();
                cartItemsDO.setSkuId(skuId);
                cartItemsDO.setQuantity(cartItemsDO.getQuantity() + num);

                EsCommercelItemsDTO commerceItemsDTO = new EsCommercelItemsDTO();
                BeanUtil.copyProperties(cartItemsDO, commerceItemsDTO);
                DubboResult result = commerceItemsService.updateCommercelItems(commerceItemsDTO);
                if (!result.isSuccess()) {
                    throw new ArgumentException(result.getCode(), result.getMsg());
                }
            }
        }
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        // 清除购物车缓存
        jedisCluster.del(cacheKey);
        // 获取购物车缓存
        String itemListJson = jedisCluster.get(cacheKey);
        List<CartVO> cartList = JSON.parseArray(itemListJson, CartVO.class);
        // 如果为空查询数据库，重新生成缓存
        if (cartList != null && !cartList.isEmpty()) {
            return cartList;
        }
        cartList = this.assembleCart(skey);

        cartList = cartList.stream().filter(cartVO -> (cartVO.getCartItemsList().size()!=0)).collect(Collectors.toList());
        // 调用促销价格计算的接口
        this.promotionToolManager.countPromotion(cartList,skey);

        // 存到缓存里
        this.putCache(cacheKey, cartList);
        return cartList;
    }


    /**
     * Version_2 start
     */
    /**
     * 查询商品的所有活动
     * 将活动分类为：单品活动类，组合活动类、平台活动类
     *
     * @param cartItems 购物车sku
     * @return
     */
    public CartItemsVO getPromotionList(CartItemsVO cartItems,String skey) {
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        List<PromotionVO> list = promotionGoodsManager.getPromotion(cartItems.getGoodsId(),cartItems.getSkuId(), memberId);

        // 单品活动集合 目前没有.活动全部可以组合
        List<TradePromotionGoodsVO> singleList = new ArrayList<>();
        // 组合活动集合
        List<TradePromotionGoodsVO> groupList = new ArrayList<>();
        // 该商品参与的所以优惠活动信息
        List<TradePromotionGoodsVO> promotionList = new ArrayList<>();

        // 遍历这个商品参与的所有活动
        for (PromotionVO promotionVO : list) {
            TradePromotionGoodsVO promotionGoodsVO = new TradePromotionGoodsVO();
            BeanUtil.copyProperties(promotionVO, promotionGoodsVO);
            promotionGoodsVO.setStartTime(promotionVO.getStartTime());
            promotionGoodsVO.setEndTime(promotionVO.getEndTime());

            if (PromotionTypeEnum.SECKILL.name().equals(promotionGoodsVO.getPromotionType())) {
                SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(promotionVO.getGoodsId());
                promotionGoodsVO.setSoldOutCount(seckillGoodsVO.getSoldQuantity());
            }

            if (PromotionTypeEnum.isSingle(promotionGoodsVO.getPromotionType())) {
                if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(promotionVO.getPromotionType())){
                    promotionGoodsVO.setGoodsDiscounts(promotionVO.getGoodsDiscount().getDiscount());
                }
                singleList.add(promotionGoodsVO);
            } else {
                groupList.add(promotionGoodsVO);
            }

            if (!PromotionTypeEnum.COMPANY_DISCOUNT.name().equals(promotionVO.getPromotionType())){
                promotionList.add(promotionGoodsVO);
            }
        }

        if (promotionList.size() > 0){
            TradePromotionGoodsVO tradePromotionGoodsVO = new TradePromotionGoodsVO();
            //不使用优惠
            tradePromotionGoodsVO.setActivityId(0l);
            tradePromotionGoodsVO.setTitle("不使用优惠");
            tradePromotionGoodsVO.setPromotionType(PromotionTypeEnum.NO.name());
            promotionList.add(tradePromotionGoodsVO);
            // 重组活动列表信息
            promotionList = promotionList.stream().map(tradePromotionGoodsVO1 -> {

                return new TradePromotionGoodsVO(tradePromotionGoodsVO1);
            }).collect(Collectors.toList());

            // 默认选中一个活动
            List<String> collect = promotionList.stream().map(TradePromotionGoodsVO::getPromotionType).collect(Collectors.toList());

            if (collect.contains(PromotionTypeEnum.FULL_DISCOUNT.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }else if (collect.contains(PromotionTypeEnum.GOODS_DISCOUNT.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }else if (collect.contains(PromotionTypeEnum.HALF_PRICE.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.HALF_PRICE.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }else if (collect.contains(PromotionTypeEnum.MINUS.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.MINUS.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }else if (collect.contains(PromotionTypeEnum.SECKILL.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.SECKILL.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }
        }else{
            // 不存在活动设置为NO
            cartItems.setPromotionType(PromotionTypeEnum.NO.name());
        }

        cartItems.setPromotionList(promotionList);
        cartItems.setSingleList(singleList);
        cartItems.setGroupList(groupList);
        return cartItems;
    }

    /**
     * 查询商品的所有活动（mq进行修改活动数据）
     * 将活动分类为：单品活动类，组合活动类、平台活动类
     *
     * @param cartItems 购物车sku
     * @return
     */
    public CartItemsVO getPromotionChangeList(CartItemsVO cartItems) {

        List<PromotionVO> list = promotionGoodsManager.getMqPromotionChange(cartItems.getGoodsId(),cartItems.getSkuId());

        // 单品活动集合 目前没有.活动全部可以组合
        List<TradePromotionGoodsVO> singleList = new ArrayList<>();
        // 组合活动集合
        List<TradePromotionGoodsVO> groupList = new ArrayList<>();
        // 该商品参与的所以优惠活动信息
        List<TradePromotionGoodsVO> promotionList = new ArrayList<>();

        // 遍历这个商品参与的所有活动
        for (PromotionVO promotionVO : list) {
            TradePromotionGoodsVO promotionGoodsVO = new TradePromotionGoodsVO();
            BeanUtil.copyProperties(promotionVO, promotionGoodsVO);
            promotionGoodsVO.setStartTime(promotionVO.getStartTime());
            promotionGoodsVO.setEndTime(promotionVO.getEndTime());

            if (PromotionTypeEnum.SECKILL.name().equals(promotionGoodsVO.getPromotionType())) {
                SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(promotionVO.getGoodsId());
                promotionGoodsVO.setSoldOutCount(seckillGoodsVO.getSoldQuantity());
            }

            if (PromotionTypeEnum.isSingle(promotionGoodsVO.getPromotionType())) {
                if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(promotionVO.getPromotionType())){
                    promotionGoodsVO.setGoodsDiscounts(promotionVO.getGoodsDiscount().getDiscount());
                }
                singleList.add(promotionGoodsVO);
            } else {
                groupList.add(promotionGoodsVO);
            }

            if (!PromotionTypeEnum.COMPANY_DISCOUNT.name().equals(promotionVO.getPromotionType())){
                promotionList.add(promotionGoodsVO);
            }
        }

        if (promotionList.size() > 0){
            TradePromotionGoodsVO tradePromotionGoodsVO = new TradePromotionGoodsVO();
            //不使用优惠
            tradePromotionGoodsVO.setActivityId(0l);
            tradePromotionGoodsVO.setTitle("不使用优惠");
            tradePromotionGoodsVO.setPromotionType(PromotionTypeEnum.NO.name());
            promotionList.add(tradePromotionGoodsVO);
            // 重组活动列表信息
            promotionList = promotionList.stream().map(tradePromotionGoodsVO1 -> {

                return new TradePromotionGoodsVO(tradePromotionGoodsVO1);
            }).collect(Collectors.toList());

            // 默认选中一个活动
            List<String> collect = promotionList.stream().map(TradePromotionGoodsVO::getPromotionType).collect(Collectors.toList());

            if (collect.contains(PromotionTypeEnum.FULL_DISCOUNT.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }else if (collect.contains(PromotionTypeEnum.GOODS_DISCOUNT.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }else if (collect.contains(PromotionTypeEnum.HALF_PRICE.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.HALF_PRICE.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }else if (collect.contains(PromotionTypeEnum.MINUS.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.MINUS.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }else if (collect.contains(PromotionTypeEnum.SECKILL.name())){
                promotionList.forEach(tradePromotionGoodsVO1 -> {
                    if (PromotionTypeEnum.SECKILL.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                        tradePromotionGoodsVO1.setIsCheck(1);
                        cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                    }
                });
            }
        }else{
            // 不存在活动设置为NO
            cartItems.setPromotionType(PromotionTypeEnum.NO.name());
        }

        cartItems.setPromotionList(promotionList);
        cartItems.setSingleList(singleList);
        cartItems.setGroupList(groupList);
        return cartItems;
    }


    /**
     * 检测购物车商品是否新增活动信息，如果有新增 则追加到活动列表中
     *
     * @param cartItems 购物车sku
     * @return
     */
    private CartItemsVO checkPromotionList(CartItemsVO cartItems) {

        Long memberId = ShiroKit.getUser().getId();
        List<PromotionVO> list = promotionGoodsManager.getPromotion(cartItems.getGoodsId(),cartItems.getSkuId(), memberId);

        // 单品活动集合 目前没有.活动全部可以组合
        List<TradePromotionGoodsVO> singleList = new ArrayList<>();
        // 组合活动集合
        List<TradePromotionGoodsVO> groupList = new ArrayList<>();
        // 该商品参与的所以优惠活动信息
        List<TradePromotionGoodsVO> promotionList = new ArrayList<>();

        // 遍历这个商品参与的所有活动
        for (PromotionVO promotionVO : list) {
            TradePromotionGoodsVO promotionGoodsVO = new TradePromotionGoodsVO();
            BeanUtil.copyProperties(promotionVO, promotionGoodsVO);
            promotionGoodsVO.setStartTime(promotionVO.getStartTime());
            promotionGoodsVO.setEndTime(promotionVO.getEndTime());

            if (PromotionTypeEnum.SECKILL.name().equals(promotionGoodsVO.getPromotionType())) {
                SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(promotionVO.getGoodsId());
                promotionGoodsVO.setSoldOutCount(seckillGoodsVO.getSoldQuantity());
            }

            if (PromotionTypeEnum.isSingle(promotionGoodsVO.getPromotionType())) {
                if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(promotionVO.getPromotionType())){
                    promotionGoodsVO.setGoodsDiscounts(promotionVO.getGoodsDiscount().getDiscount());
                }
                singleList.add(promotionGoodsVO);
            } else {
                groupList.add(promotionGoodsVO);
            }

            if (!PromotionTypeEnum.COMPANY_DISCOUNT.name().equals(promotionVO.getPromotionType())){
                promotionList.add(promotionGoodsVO);
            }
        }
        // 原活动list列表
        List<TradePromotionGoodsVO> promotionList1 = cartItems.getPromotionList();
        // 如果原活动列表为空 则重新组装；否则追加到原活动集合中
        if (CollectionUtils.isEmpty(promotionList1)){
            if (promotionList.size() > 0){
                TradePromotionGoodsVO tradePromotionGoodsVO = new TradePromotionGoodsVO();
                //不使用优惠
                tradePromotionGoodsVO.setActivityId(0l);
                tradePromotionGoodsVO.setTitle("不使用优惠");
                tradePromotionGoodsVO.setPromotionType(PromotionTypeEnum.NO.name());
                promotionList.add(tradePromotionGoodsVO);
                // 重组活动列表信息
                promotionList = promotionList.stream().map(tradePromotionGoodsVO1 -> {

                    return new TradePromotionGoodsVO(tradePromotionGoodsVO1);
                }).collect(Collectors.toList());

                // 默认选中一个活动
                List<String> collect = promotionList.stream().map(TradePromotionGoodsVO::getPromotionType).collect(Collectors.toList());

                if (collect.contains(PromotionTypeEnum.FULL_DISCOUNT.name())){
                    promotionList.forEach(tradePromotionGoodsVO1 -> {
                        if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                            tradePromotionGoodsVO1.setIsCheck(1);
                            cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                        }
                    });
                }else if (collect.contains(PromotionTypeEnum.GOODS_DISCOUNT.name())){
                    promotionList.forEach(tradePromotionGoodsVO1 -> {
                        if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                            tradePromotionGoodsVO1.setIsCheck(1);
                            cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                        }
                    });
                }else if (collect.contains(PromotionTypeEnum.HALF_PRICE.name())){
                    promotionList.forEach(tradePromotionGoodsVO1 -> {
                        if (PromotionTypeEnum.HALF_PRICE.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                            tradePromotionGoodsVO1.setIsCheck(1);
                            cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                        }
                    });
                }else if (collect.contains(PromotionTypeEnum.MINUS.name())){
                    promotionList.forEach(tradePromotionGoodsVO1 -> {
                        if (PromotionTypeEnum.MINUS.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                            tradePromotionGoodsVO1.setIsCheck(1);
                            cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                        }
                    });
                }else if (collect.contains(PromotionTypeEnum.SECKILL.name())){
                    promotionList.forEach(tradePromotionGoodsVO1 -> {
                        if (PromotionTypeEnum.SECKILL.name().equals(tradePromotionGoodsVO1.getPromotionType())){
                            tradePromotionGoodsVO1.setIsCheck(1);
                            cartItems.setPromotionType(tradePromotionGoodsVO1.getPromotionType());
                        }
                    });
                }
            }
            cartItems.setPromotionList(promotionList);
        }else {
            // 验证原有活动列表活动时间是够过期
            long timeMillis = System.currentTimeMillis();
            List<TradePromotionGoodsVO> promotionGoodsVOList = promotionList1.stream()
                    .filter(tradePromotionGoodsVO -> timeMillis < tradePromotionGoodsVO.getEndTime()).collect(Collectors.toList());
            // 合并并去重活动
            promotionGoodsVOList.addAll(promotionList);

            ArrayList<TradePromotionGoodsVO> promotionGoodsVOS = promotionGoodsVOList.stream()
                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(TradePromotionGoodsVO::getActivityId))), ArrayList::new));
            cartItems.setPromotionList(promotionGoodsVOS);
        }

        cartItems.setSingleList(singleList);
        cartItems.setGroupList(groupList);
        return cartItems;
    }


    public List<CartVO> setPromotion(Long activityId,Long skuId,String skey) {
        // 获取购物缓存数据
        List<CartVO> cacheCartList = this.getCacheCartList(skey);

        cacheCartList.forEach(cartVO -> {
            List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
            List<CartItemsVO> collect = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getSkuId() == skuId.doubleValue()).collect(Collectors.toList());
            collect.forEach(cartItemsVO -> {
                // 判断活动的时间 是否过期
                DubboResult<Boolean> dubboResult = iEsPromotionGoodsService.getPromotionGoods(activityId);
                // 如果过期则 重新封装活动信息
                if (dubboResult.getData()){
                    this.getPromotionList(cartItemsVO,skey);
                }
                if (cartItemsVO.getSkuId().longValue() == skuId.longValue()){
                    // 1把其他活动设置未选中
                    // 2重新设置活动的选中状态
                    List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
                    promotionList.forEach(tradePromotionGoodsVO -> {
                        if (tradePromotionGoodsVO.getActivityId().doubleValue() == activityId.doubleValue()){
                            // 切换活动
                            tradePromotionGoodsVO.setIsCheck(1);
                            cartItemsVO.setPromotionType(tradePromotionGoodsVO.getPromotionType());
                            //初始化优惠信息
                            cartItemsVO.setPreferentialMessage(new PreferentialMessageVO());
                        } else {
                            tradePromotionGoodsVO.setIsCheck(2);
                        }
                    });

                    cartItemsVO.setPromotionList(promotionList);
                }
            });
            cartVO.setCartItemsList(cartItemsList);
        });
        // 设置活动选中状态后 计算优惠价格
        this.promotionToolManager.countPromotion(cacheCartList,skey);
        // 存到缓存里
        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        this.putCache(cacheKey, cacheCartList);

        return cacheCartList;
    }
    /**
     * Version_2 end
     */
}
