package com.xdl.jjg.manager;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jjg.member.model.domain.EsMemberAddressDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.system.model.enums.CachePrefix;
import com.jjg.trade.model.domain.EsDeliveryMessageDO;
import com.jjg.trade.model.dto.EsDeliveryMessageDTO;
import com.jjg.trade.model.enums.PaymentTypeEnum;
import com.jjg.trade.model.form.DeliveryMessageForm;
import com.jjg.trade.model.vo.CartItemsVO;
import com.jjg.trade.model.vo.CartVO;
import com.jjg.trade.model.vo.CheckoutParamVO;
import com.jjg.trade.model.vo.ReceiptVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.CheckoutParamCachePrefix;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsDeliveryServiceService;
import com.xdl.jjg.web.service.feign.member.MemberAddressService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 结算参数 业务层实现类
 *
 * @author Snow create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class CheckoutParamManager {

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private CartManager cartManager;

    @Autowired
    private GoodsSkuService iEsGoodsSkuService;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private IEsDeliveryServiceService esDeliveryServiceService;

    @Autowired
    private MemberService memberService;


    public CheckoutParamVO getParam(String skey) {

        CheckoutParamVO param = this.read(skey);

        // 如果session中没有 new一个，并赋给默认值
        if (param == null) {

            param = new CheckoutParamVO();
            // 获取用户信息
            Long memberId = null;
            if(StringUtils.isBlank(skey)){
                memberId = ShiroKit.getUser().getId();
            }else {
                //小程序获取当前用户ID
                memberId = getMemberIdApplet(skey);
            }

            // 根据用户id获取用户默认地址
            DubboResult memberAddressResult = this.memberAddressService.getDefaultMemberAddress(memberId);
            if (!memberAddressResult.isSuccess()) {
                throw new ArgumentException(memberAddressResult.getCode(), memberAddressResult.getMsg());
            }

            EsMemberAddressDO address = (EsMemberAddressDO)memberAddressResult.getData();


            if (address != null) {
                Long addrId = address.getId();
                //默认配送地址
                param.setAddressId(addrId);
            }


            //默认支付方式
            param.setPaymentType(PaymentTypeEnum.defaultType());

            //默认不需要发票
            ReceiptVO receipt = new ReceiptVO();
            param.setReceipt(receipt);

            //默认时间
            param.setReceiveTime("任意时间");

            this.write(param,skey);
        }
        return param;
    }

    public void setAddressId(Long addressId,String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hset(key, CheckoutParamCachePrefix.ADDRESS_ID.name(), String.valueOf(addressId));
    }

    public void setDeliveryReceiveMessage(DeliveryMessageForm deliveryMessageForm, Long shopId, String skey) {
        EsDeliveryMessageDTO deliveryMessageDTO = new EsDeliveryMessageDTO();
        BeanUtils.copyProperties(deliveryMessageForm,deliveryMessageDTO);
        DubboResult dubboResult = esDeliveryServiceService.getDeliveryTextMessage(deliveryMessageDTO);
        EsDeliveryMessageDO esDeliveryMessageDO = (EsDeliveryMessageDO)dubboResult.getData();

        String redisKey = "";
        if(StringUtils.isBlank(skey)){
            redisKey = getRedisKey();
        }else {
            redisKey = getRedisKeyApplet(skey);
        }
        String delivery = this.jedisCluster.hget(redisKey, CheckoutParamCachePrefix.DELIVERY.name());
        Map delivery1 = JSONObject.parseObject(delivery, Map.class);
        if (delivery1 == null){
            HashMap<Integer, String> deliveryMessageFormHashMap = new HashMap<>(12);
            if (deliveryMessageFormHashMap == null){
                deliveryMessageFormHashMap = new HashMap<>();
                deliveryMessageFormHashMap.put(shopId.intValue(), JsonUtil.objectToJson(esDeliveryMessageDO));
            }else {
                deliveryMessageFormHashMap.put(shopId.intValue(),JsonUtil.objectToJson(esDeliveryMessageDO));
            }
            this.jedisCluster.hset(redisKey,CheckoutParamCachePrefix.DELIVERY.name(),JSON.toJSONString(deliveryMessageFormHashMap));
        }else {
            String delivery2 = this.jedisCluster.hget(redisKey, CheckoutParamCachePrefix.DELIVERY.name());
            Map<Integer, String>  map = JSONObject.parseObject(delivery2, Map.class);
            if (map == null){
                map = new HashMap<>();
                map.put(shopId.intValue(), JsonUtil.objectToJson(esDeliveryMessageDO));
            }else {
                map.put(shopId.intValue(), JsonUtil.objectToJson(esDeliveryMessageDO));
            }
            this.jedisCluster.hset(redisKey, CheckoutParamCachePrefix.DELIVERY.name(), JSON.toJSONString(map));

        }
//        this.jedisCluster.hset(getRedisKey(), CheckoutParamCachePrefix.DELIVERY.name(), JSON.toJSONString(deliveryMessageForm));
    }

    public void setPaymentType(PaymentTypeEnum paymentTypeEnum,String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hset(key, CheckoutParamCachePrefix.PAYMENT_TYPE.name(), JSON.toJSONString(paymentTypeEnum));
    }

    public void setReceipt(ReceiptVO receipt,String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hset(key, CheckoutParamCachePrefix.RECEIPT.name(), JSON.toJSONString(receipt));
    }

    public void setReceiveTime(String receiveTime,String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hset(key, CheckoutParamCachePrefix.RECEIVE_TIME.name(), receiveTime);
    }

    public void setRemark(Long shopId, String remark,String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
//        String shop = String.valueOf(shopId);
        Map remarks =  JSONObject.parseObject(this.jedisCluster.hget(key, CheckoutParamCachePrefix.REMARK.name()), Map.class);

        if (remarks == null) {
            Map<Integer, String> map = new HashMap<>(12);
            if (map == null){
                map = new HashMap<>();
                map.put(shopId.intValue(), remark);
            }else {
                map.put(shopId.intValue(), remark);
            }
            this.jedisCluster.hset(key, CheckoutParamCachePrefix.REMARK.name(), JSON.toJSONString(map));
        } else {
            String json = this.jedisCluster.hget(key, CheckoutParamCachePrefix.REMARK.name());

            Map<Integer, String> map = JSONObject.parseObject(json, Map.class);
            if (map == null){
                map = new HashMap<>();
                map.put(shopId.intValue(), remark);
            }else {
                map.put(shopId.intValue(), remark);
            }
            this.jedisCluster.hset(key, CheckoutParamCachePrefix.REMARK.name(), JSON.toJSONString(map));
        }
    }

    private void setRemark(Map<Integer, String> remark,String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hset(key, CheckoutParamCachePrefix.REMARK.name(), JSON.toJSONString(remark));
    }

    public void setClientType(String clientType,String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hset(key, CheckoutParamCachePrefix.CLIENT_TYPE.name(), clientType);
    }

    public void deleteReceipt(String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hset(key, CheckoutParamCachePrefix.RECEIPT.name(), null);
    }

    /*public void setAll(CheckoutParamVO paramVO) {
        this.write(paramVO);
    }*/

    public void cleanCheckParam() {
        this.jedisCluster.del(this.getRedisKey());
    }

    public void deleteDelivery(String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hdel(key, CheckoutParamCachePrefix.DELIVERY.name());
    }
    public void deleteRemark(String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        this.jedisCluster.hdel(key, CheckoutParamCachePrefix.REMARK.name());
    }
    public void deleteAddressId() {
        this.jedisCluster.hdel(getRedisKey(), CheckoutParamCachePrefix.ADDRESS_ID.name());
    }


    /**
     * 读取Key
     *
     * @return
     */
    private String getRedisKey() {
        String cacheKey = "";
        // 获取用户信息
        ShiroUser buyer = ShiroKit.getUser();

        if (buyer != null) {
            cacheKey = CachePrefix.CHECKOUT_PARAM_ID_PREFIX.getPrefix() + buyer.getId();
        }
        return cacheKey;
    }
    /**
     * 小程序读取Key
     *
     * @return
     */
    private String getRedisKeyApplet(String skey) {
        //获取当前用户
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            throw new ArgumentException(TradeErrorCode.NOT_LOGIN.getErrorCode(),TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();

        String cacheKey = CachePrefix.CHECKOUT_PARAM_ID_PREFIX.getPrefix() + memberId;

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
     * 写入map值
     *
     * @param paramVO
     */
    private void write(CheckoutParamVO paramVO,String skey) {

        if (paramVO.getAddressId() != null) {
            setAddressId(paramVO.getAddressId(),skey);
        }

        if (paramVO.getReceiveTime() != null) {
            setReceiveTime(paramVO.getReceiveTime(),skey);
        }

        if (paramVO.getPaymentType() != null) {
            setPaymentType(paramVO.getPaymentType(),skey);
        }

        if (paramVO.getReceipt() != null) {
            setReceipt(paramVO.getReceipt(),skey);
        }

        if (paramVO.getRemark() != null) {
            setRemark(paramVO.getRemark(),skey);
        }

        if (paramVO.getClientType() != null) {
            setClientType(paramVO.getClientType(),skey);
        }
    }

    /**
     * 由Redis中读取出参数
     */
    private CheckoutParamVO read(String skey) {
        String key = "";
        if(StringUtils.isBlank(skey)){
            key = getRedisKey();
        }else {
            key = getRedisKeyApplet(skey);
        }
        // 如果取到了，则取出来生成param
        Long addressId = this.jedisCluster.hget(key, (CheckoutParamCachePrefix.ADDRESS_ID.name())) == null ? null :
                Long.valueOf(this.jedisCluster.hget(key, (CheckoutParamCachePrefix.ADDRESS_ID.name())));
        PaymentTypeEnum paymentType = JSONObject.parseObject(this.jedisCluster.hget(key,
                CheckoutParamCachePrefix.PAYMENT_TYPE.name()), PaymentTypeEnum.class);
        ReceiptVO receipt = JSONObject.parseObject(this.jedisCluster.hget(key,
                CheckoutParamCachePrefix.RECEIPT.name()), ReceiptVO.class);
        // 自提信息
        Map<Integer, String> map1 = JSONObject.parseObject(this.jedisCluster.hget(key,
                CheckoutParamCachePrefix.DELIVERY.name()), Map.class);

//        Map<Integer, DeliveryMessageForm>  map = JSONObject.parseObject(delivery2, Map.class);

        String receiveTime = this.jedisCluster.hget(key, CheckoutParamCachePrefix.RECEIVE_TIME.name());
        Map remark =  JSONObject.parseObject(this.jedisCluster.hget(key, CheckoutParamCachePrefix.REMARK.name()), Map.class);
        String clientType = this.jedisCluster.hget(key, CheckoutParamCachePrefix.CLIENT_TYPE.name());

        if (addressId == null && paymentType == null && receipt == null && receiveTime == null
                && remark == null && clientType == null && map1 == null) {
            return null;
        }

        CheckoutParamVO param = new CheckoutParamVO();

        param.setAddressId(addressId);
        param.setReceipt(receipt);
        param.setDeliveryMessageVOMap(map1);
        if (receiveTime == null) {
            receiveTime = "任意时间";
        }
        param.setReceiveTime(receiveTime);
        if (remark == null) {
            param.setRemark(new HashMap<>(12));
        } else {
            param.setRemark(remark);
        }
        if (paymentType == null) {
            paymentType = PaymentTypeEnum.defaultType();
        }
        param.setPaymentType(paymentType);
        param.setClientType(clientType);
        return param;
    }
    /**
     * 验证商品是否支持自提
     */
    public void setDelivery(Integer isDelivery,Long shopId,String skey) {
        // 校验购物车中选中的商品的 自提标识是否支持自提，
        List<CartVO> cartList = cartManager.getCartListCheck(skey).stream().filter(cartVO -> cartVO.getShopId().equals(shopId)).collect(Collectors.toList());
        //遍历购物车中商品的自提标识
        cartList.forEach(cartVO -> {
            List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
            // 筛选出购物车中选中的商品
            List<CartItemsVO> collect = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
            // 支持自提商品的SKUID
            List<Long> skuIds = new ArrayList<>();

            if(isDelivery == 1){
                // 组装不能自提的商品信息
                for (CartItemsVO cartItemsVO : collect ) {
                    Long skuId = cartItemsVO.getSkuId();
                    // 通过skuId 查询出该商品的自提标识，
                    DubboResult<EsGoodsSkuCO> goodsSku = iEsGoodsSkuService.getGoodsSku(skuId);
                    if (!goodsSku.isSuccess()){
                        throw new ArgumentException(goodsSku.getCode(),goodsSku.getMsg());
                    }
                    // 1:支持自提，2:不支持自提
                    if(goodsSku.getData().getIsSelf() == 1){
                        skuIds.add(skuId);
                    }
                }
            }else {
                // 组装不能自提的商品信息
                for (CartItemsVO cartItemsVO : collect ) {
                    Long skuId = cartItemsVO.getSkuId();
                    // 通过skuId 查询出该商品的自提标识，
                    DubboResult<EsGoodsSkuCO> goodsSku = iEsGoodsSkuService.getGoodsSku(skuId);
                    if (!goodsSku.isSuccess()){
                        throw new ArgumentException(goodsSku.getCode(),goodsSku.getMsg());
                    }
                    // 1:支持自提，2:不支持自提
                    if(goodsSku.getData().getIsSelf() == 1){
                        // 支持自提
                        skuIds.add(skuId);
                    }
                }
                // 取消设置自提 则清空自提信息
                this.deleteDelivery(skey);
            }
            // 设置是否自提
            this.cartManager.setDelivery(skuIds,isDelivery,skey);

            String where = "delivery";
            // 重新计算运费
            this.cartManager.setShipping(where,isDelivery,skey);

        });

    }

    /**
     * @Description: 防止 用户在结算页面设置自提后 按F5 刷新页面 运费价格问题
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/6/16 9:08
     */
    public void setDeliveryFreshPage(Integer isDelivery,Long shopId,String skey) {
        // 校验购物车中选中的商品的 自提标识是否支持自提，
        List<CartVO> cartList = cartManager.getCartListCheck(skey).stream().filter(cartVO -> cartVO.getShopId().equals(shopId)).collect(Collectors.toList());
        //遍历购物车中商品的自提标识
        cartList.forEach(cartVO -> {
            List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
            // 筛选出购物车中选中的商品
            List<CartItemsVO> collect = cartItemsList.stream().filter(cartItemsVO -> cartItemsVO.getChecked() == 1).collect(Collectors.toList());
            // 支持自提商品的SKUID
            List<Long> skuIds = new ArrayList<>();

            if(isDelivery == 1){
                // 组装不能自提的商品信息
                for (CartItemsVO cartItemsVO : collect ) {
                    Long skuId = cartItemsVO.getSkuId();
                    // 通过skuId 查询出该商品的自提标识，
                    DubboResult<EsGoodsSkuCO> goodsSku = iEsGoodsSkuService.getGoodsSku(skuId);
                    if (!goodsSku.isSuccess()){
                        throw new ArgumentException(goodsSku.getCode(),goodsSku.getMsg());
                    }
                    // 1:支持自提，2:不支持自提
                    if(goodsSku.getData().getIsSelf() == 1){
                        skuIds.add(skuId);
                    }
                }
            }else {
                // 组装不能自提的商品信息
                for (CartItemsVO cartItemsVO : collect ) {
                    Long skuId = cartItemsVO.getSkuId();
                    // 通过skuId 查询出该商品的自提标识，
                    DubboResult<EsGoodsSkuCO> goodsSku = iEsGoodsSkuService.getGoodsSku(skuId);
                    if (!goodsSku.isSuccess()){
                        throw new ArgumentException(goodsSku.getCode(),goodsSku.getMsg());
                    }
                    // 1:支持自提，2:不支持自提
                    if(goodsSku.getData().getIsSelf() == 1){
                        // 支持自提
                        skuIds.add(skuId);
                    }
                }
                // 取消设置自提 则清空自提信息
                this.deleteDelivery(skey);
            }
            // 设置是否自提
            this.cartManager.setDelivery(skuIds,isDelivery,skey);

        });

    }

    public void setRemarks(Map<Long, String> remark) {
        this.jedisCluster.hset(getRedisKey(), CheckoutParamCachePrefix.REMARK.name(), JSON.toJSONString(remark));
    }
    /**
     * 获取结算页面 自提文本信息
     */
//    public EsDeliveryMessageVO getDeliveryText() {
//        CheckoutParamVO param = this.getParam();
//
//        Map<Integer, String> deliveryMessageVOMap = param.getDeliveryMessageVOMap();
//        if (deliveryMessageVOMap != null){
//            EsDeliveryMessageDTO deliveryMessageDTO = new EsDeliveryMessageDTO();
//            BeanUtils.copyProperties(deliveryMessageVO,deliveryMessageDTO);
//            DubboResult dubboResult = esDeliveryServiceService.getDeliveryTextMessage(deliveryMessageDTO);
//            EsDeliveryMessageDO esDeliveryMessageDO = (EsDeliveryMessageDO)dubboResult.getData();
//            BeanUtils.copyProperties(esDeliveryMessageDO,deliveryMessageVO);
//            return deliveryMessageVO;
//        }else {
//            return null;
//        }
//
//    }
}
