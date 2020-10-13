package com.xdl.jjg.manager;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.redisson.RedissonLock;
import com.shopx.common.util.DateUtils;
import com.shopx.common.util.JsonUtil;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.system.api.model.enums.CachePrefix;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.constant.cacheprefix.TradeCachePrefix;
import com.shopx.trade.api.model.domain.vo.PriceDetailVO;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.Date;

/**
 * 交易价格业务
 *
 * @author Snow create in 2018/3/22
 * @version v2.0
 * @since v7.0.0
 */

@Component
public class TradePriceManager {
    private static Logger logger = LoggerFactory.getLogger(TradePriceManager.class);
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;
    @Autowired
    RedissonLock redissonLock;
    @Autowired
    private JedisCluster jedisCluster;
    public static final String TRADE_PRICE_CHANGE_REDISSON_LOCK_NAME_PREFIX = "trade-price-change-";

    public PriceDetailVO getTradePrice(String skey) {
        Long memberId = null;
        if (StringUtils.isBlank(skey)){
            memberId = ShiroKit.getUser().getId();
        }else {
            //小程序获取当前用户ID
            memberId = this.getMemberIdApplet(skey);
        }
        String redissonLockName = TRADE_PRICE_CHANGE_REDISSON_LOCK_NAME_PREFIX+memberId.toString();
        // 获取锁 超时2秒 等待5秒
        redissonLock.lock(redissonLockName,2,5);

        String cacheKey = "";
        if (StringUtils.isBlank(skey)){
            cacheKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车的key
            cacheKey = this.getSessionKeyApplet(skey);
        }
        PriceDetailVO priceDetail = JSON.parseObject(this.jedisCluster.get(cacheKey), PriceDetailVO.class);
        if (priceDetail == null) {
            priceDetail = new PriceDetailVO();
        }
        if(priceDetail.getFreightPrice()==0.0){
            priceDetail = JSON.parseObject(this.jedisCluster.get(cacheKey), PriceDetailVO.class);
            logger.info("结算页价格再次获取:"+ JsonUtil.objectToJson(priceDetail));
        }
        logger.info("获取运费时间："+ DateUtils.format(new Date(),DateUtils.TIMESTAMP_PATTERN));
        logger.info("结算页价格:"+ JsonUtil.objectToJson(priceDetail));
        redissonLock.release(redissonLockName); // 释放锁
        return priceDetail;
    }

    public PriceDetailVO getTradePriceYC(Long memberId) {
        String redissonLockName = TRADE_PRICE_CHANGE_REDISSON_LOCK_NAME_PREFIX+memberId.toString();
        // 获取锁 超时2秒 等待5秒
        redissonLock.lock(redissonLockName,2,5);
//        String cacheKey = this.getSessionKey();
        PriceDetailVO priceDetail = JSON.parseObject(this.jedisCluster.get(CachePrefix.PRICE_SESSION_ID_PREFIX.getPrefix()+memberId), PriceDetailVO.class);
        if (priceDetail == null) {
            priceDetail = new PriceDetailVO();
        }
        if(priceDetail.getFreightPrice()==0.0){
            priceDetail = JSON.parseObject(this.jedisCluster.get(CachePrefix.PRICE_SESSION_ID_PREFIX.getPrefix()+memberId), PriceDetailVO.class);
            logger.info("结算页价格再次获取:"+ JsonUtil.objectToJson(priceDetail));
        }
        logger.info("获取运费时间："+ DateUtils.format(new Date(),DateUtils.TIMESTAMP_PATTERN));
        logger.info("结算页价格:"+ JsonUtil.objectToJson(priceDetail));
        redissonLock.release(redissonLockName); // 释放锁
        return priceDetail;
    }

    public void pushPrice(PriceDetailVO detail,String skey) {
        String sessionKey = "";
        if (StringUtils.isBlank(skey)){
            sessionKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车价格的key
            sessionKey = this.getSessionKeyApplet(skey);
        }
        this.jedisCluster.set(sessionKey, JSONObject.toJSONString(detail));
    }


    public void cleanPrice(String skey) {
        String sessionKey = "";
        if (StringUtils.isBlank(skey)){
            sessionKey = this.getSessionKey();
        }else {
            //小程序获取当前用户购物车价格的key
            sessionKey = this.getSessionKeyApplet(skey);
        }
        this.jedisCluster.del(sessionKey);
    }


//    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
//    public void updatePrice(String tradeSn, Double tradePrice, Double discountPrice) {
//        String sql = "update es_trade set total_price=?,discount_price=? where trade_sn=?";
//        this.daoSupport.execute(sql, tradePrice, discountPrice, tradeSn);
//    }


    /**
     * 根据会话id获取缓存的key<br>
     * 如果会员没登陆使用会话id做为key<br>
     * 如果会员登录了，用会员id做为key<br>
     *
     * @return 缓存key
     */
    private String getSessionKey() {
        String cacheKey = "";

        // 如果会员登陆了，则要以会员id为key
        ShiroUser buyer = ShiroKit.getUser();
        if (buyer != null) {
            cacheKey = CachePrefix.PRICE_SESSION_ID_PREFIX.getPrefix() + buyer.getId();
        }
        return cacheKey;
    }

    /**
     * 小程序获取当前用户购物车价格的key
     */
    private String getSessionKeyApplet(String skey) {
        //获取当前用户
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            throw new ArgumentException(TradeErrorCode.NOT_LOGIN.getErrorCode(),TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        // 拼接购物车价格Key + 用户id
        String cacheKey = CachePrefix.PRICE_SESSION_ID_PREFIX.getPrefix() + memberId;
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
}
