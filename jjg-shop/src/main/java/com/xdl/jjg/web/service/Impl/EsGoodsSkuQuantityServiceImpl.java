package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsGoodsQuantityLogDO;
import com.jjg.shop.model.domain.EsGoodsSkuQuantityDO;
import com.jjg.shop.model.domain.EsSellerGoodsDO;
import com.jjg.shop.model.domain.EsSellerGoodsSkuDO;
import com.jjg.shop.model.dto.EsGoodsQuantityLogDTO;
import com.jjg.shop.model.dto.EsGoodsSkuQuantityDTO;
import com.jjg.shop.model.enums.GoodsCachePrefix;
import com.xdl.jjg.entity.EsGoods;
import com.xdl.jjg.entity.EsGoodsSku;
import com.xdl.jjg.mapper.EsGoodsSkuMapper;
import com.xdl.jjg.redisson.annotation.DistributedLock;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsGoodsQuantityLogService;
import com.xdl.jjg.web.service.IEsGoodsSkuQuantityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.List;

@Service
public class EsGoodsSkuQuantityServiceImpl  extends ServiceImpl<EsGoodsSkuMapper, EsGoodsSku> implements IEsGoodsSkuQuantityService {
    private static Logger logger = LoggerFactory.getLogger(EsGoodsSkuQuantityServiceImpl.class);

    private  static final  String  GOODS_QUANTITY_INSERT = "goods_quantity_insert";

    private  static final  String    GOODS_QUANTITY_REDUCE = "goods_quantity_reduce";
    @Value("${zhuox.redis.expire}")
    private int TIME_OUT;

    @Autowired
    private EsGoodsServiceImpl esGoodsService;
    @Autowired
    private EsGoodsSkuServiceImpl esGoodsSkuService;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private IEsGoodsQuantityLogService esGoodsQuantityLogService;

    /**
     * 商品库存增加
     * @param quantityDTO 库存DTO
     * @return
     */
    @Override
    @DistributedLock(value = GOODS_QUANTITY_INSERT,expireSeconds = 60)
    public DubboResult<EsGoodsSkuQuantityDO> insertGoodsSkuQuantity(List<EsGoodsSkuQuantityDTO> quantityDTO) {
        if(CollectionUtils.isEmpty(quantityDTO)){
            throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(),"参数传入错误，商品SKU库存信息不能为空");
        }
        String orderSn = quantityDTO.get(0).getOrderSn();
        try{
            quantityDTO.stream().forEach(quantity ->{
                insertGoodsQuantity(quantity,orderSn);
            });
            quantityDTO.stream().forEach(quantity ->{
                String goods =  jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+quantity.getGoodsId());
                if(!StringUtils.isBlank(goods)){
                    jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+quantity.getGoodsId());
                }
                String sku =  jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + quantity.getSkuId());
                if(!StringUtils.isBlank(sku)){
                    jedisCluster.del(GoodsCachePrefix.SKU.getPrefix() + quantity.getSkuId());
                }
            });
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(String.format("商品SKU 库存增加失败，订单号:[%s]",orderSn) , ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(String.format("商品SKU 库存增加失败，订单号:[%s]",orderSn) , th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 商品库存扣减
     * @param quantityDTO
     * @return
     */
    @Override
    @DistributedLock(value = GOODS_QUANTITY_REDUCE,expireSeconds = 60)
    public DubboResult<EsGoodsSkuQuantityDO> reduceGoodsSkuQuantity(List<EsGoodsSkuQuantityDTO> quantityDTO) {
        if(CollectionUtils.isEmpty(quantityDTO)){
            throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(),"参数传入错误，商品SKU库存信息不能为空");
        }
        //检查库存是否充足
        if(!checkQuantity(quantityDTO)){
            throw new ArgumentException(GoodsErrorCode.QUANTITY_SHORTAGE.getErrorCode(),"库存数量不足");
        };
        String orderSn = quantityDTO.get(0).getOrderSn();
        try{
            quantityDTO.stream().forEach(quantity ->{
                innerReduceGoodsQuantity(quantity,orderSn);
            });
            quantityDTO.stream().forEach(quantity ->{
               String goods =  jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+quantity.getGoodsId());
                if(!StringUtils.isBlank(goods)){
                    jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+quantity.getGoodsId());
                }
                String sku =  jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + quantity.getSkuId());
                if(!StringUtils.isBlank(sku)){
                    jedisCluster.del(GoodsCachePrefix.SKU.getPrefix() + quantity.getSkuId());
                }
            });
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(String.format("商品SKU 库存扣减失败，订单号:[%s]",orderSn) , ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(String.format("商品SKU 库存扣减失败，订单号:[%s]",orderSn) , th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
    /**
     * @Description: 库存扣减 性能优化
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/6/17 15:37
     * @param
     * @return       com.shopx.common.model.result.DubboResult<com.shopx.goods.api.model.domain.EsGoodsSkuQuantityDO>
     * @exception
     *
     */
    @Override
//    @DistributedLock(value = GOODS_QUANTITY_REDUCE,expireSeconds = 60)
    public DubboResult<EsGoodsSkuQuantityDO> reduceGoodsSkuQuantityRedis(List<EsGoodsSkuQuantityDTO> quantityDTO) {
        if(CollectionUtils.isEmpty(quantityDTO)){
            throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(),"参数传入错误，商品SKU库存信息不能为空");
        }
        //检查库存是否充足
        if(!checkQuantity(quantityDTO)){
            throw new ArgumentException(GoodsErrorCode.QUANTITY_SHORTAGE.getErrorCode(),"库存数量不足");
        };
        String orderSn = quantityDTO.get(0).getOrderSn();
        try{
            quantityDTO.stream().forEach(quantity ->{
                // 获取缓存中商品信息；根据传入的数量进行缓存修改
                String goods =  jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+quantity.getGoodsId());
                EsGoodsCO esGoodsCO = JsonUtil.jsonToObject(goods, EsGoodsCO.class);
                String sku =  jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + quantity.getSkuId());
                EsGoodsSkuCO esGoodsSkuCO = JsonUtil.jsonToObject(sku, EsGoodsSkuCO.class);
                Integer oldQuantity = esGoodsCO.getQuantity();
                Integer skuCOQuantity = esGoodsSkuCO.getQuantity();
                Integer goodsNumber = quantity.getGoodsNumber();
                //商品库存计算
                oldQuantity = oldQuantity - goodsNumber;
                esGoodsCO.setQuantity(oldQuantity);
                Integer xnReduction = 0;
                Integer xjReduction = 0;

                //商品SKU虚拟库存
                Integer xnQuantity =  esGoodsSkuCO.getXnQuantity() == null ? 0 :  esGoodsSkuCO.getXnQuantity();
                if(skuCOQuantity != 0){
                    if(goodsNumber > skuCOQuantity){
                        //实际库存扣多少 虚拟库存扣多少
                        xnReduction = goodsNumber - skuCOQuantity;
                        xjReduction = goodsNumber - xnReduction;
                        //虚拟库存扣多少
                        xnQuantity = xnQuantity - xnReduction;
                        skuCOQuantity = skuCOQuantity - xjReduction;
                    }else{
                        skuCOQuantity = skuCOQuantity - goodsNumber;
                    }
                }else{
                    xnQuantity = xnQuantity - goodsNumber;
                }
                List<EsGoodsSkuCO> skuList = esGoodsCO.getSkuList();
                skuList.forEach(esGoodsSkuCO1 -> {
                    if (esGoodsSkuCO1.getId().intValue() == esGoodsSkuCO.getId().intValue()){
                        esGoodsSkuCO1.setQuantity(esGoodsSkuCO.getQuantity());
                        esGoodsSkuCO1.setEnableQuantity(esGoodsSkuCO.getEnableQuantity());
                        esGoodsSkuCO1.setXnQuantity(esGoodsSkuCO.getXnQuantity());
                    }
                });
                jedisCluster.setex(GoodsCachePrefix.GOODS.getPrefix()+quantity.getGoodsId(),TIME_OUT, JsonUtil.objectToJson(esGoodsCO));

                //商品SKU 可用 库存
                Integer oldEnableQuantity =  esGoodsSkuCO.getEnableQuantity();
                //SKU 可用库存=实际库存+虚拟库存
                oldEnableQuantity = skuCOQuantity + xnQuantity;
                esGoodsSkuCO.setEnableQuantity(oldEnableQuantity);
                esGoodsSkuCO.setQuantity(skuCOQuantity);
                esGoodsSkuCO.setXnQuantity(xnQuantity);

                jedisCluster.setex(GoodsCachePrefix.SKU.getPrefix()+quantity.getSkuId(),TIME_OUT, JsonUtil.objectToJson(esGoodsSkuCO));

            });
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(String.format("商品SKU 库存扣减失败，订单号:[%s]",orderSn) , ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(String.format("商品SKU 库存扣减失败，订单号:[%s]",orderSn) , th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    public boolean checkQuantity(List<EsGoodsSkuQuantityDTO> quantityDTOList){
        for(EsGoodsSkuQuantityDTO quantity:quantityDTOList){
            //从缓存中获取SKU信息
            DubboResult<EsGoodsSkuCO> skuCo =esGoodsSkuService.getGoodsSku(quantity.getSkuId());
            EsGoodsSkuCO goodsSkuCo = skuCo.getData();
            //商品SKU 可用库存
            Integer oldEnableQuantity = goodsSkuCo.getEnableQuantity() == null ? 0 : goodsSkuCo.getEnableQuantity();
            //订单商品数量
            Integer goodsNumber = quantity.getGoodsNumber() == null ? 0 : quantity.getGoodsNumber();
            if(oldEnableQuantity < goodsNumber){
                return false;
            }
        }
        return true;
    }
    //库存扣减计算
    public void innerReduceGoodsQuantity(EsGoodsSkuQuantityDTO quantity,String orderSn){
        //从缓存中获取商品信息
        DubboResult<EsSellerGoodsDO> goodsCo = esGoodsService.getSellerEsGoods(quantity.getGoodsId());
        //从缓存中获取SKU信息
        DubboResult<EsGoodsSkuCO> skuCo =esGoodsSkuService.getSkuById(quantity.getSkuId());
        EsSellerGoodsDO esGoodsCO = goodsCo.getData();
        EsGoodsSkuCO goodsSkuCo = skuCo.getData();
        //商品库存 old
        Integer oldGoodsQuantity = esGoodsCO.getQuantity();

        //商品SKU 可用 库存
        Integer oldEnableQuantity =  goodsSkuCo.getEnableQuantity();
        //商品SKU 实际库存
        Integer oldQuantity = goodsSkuCo.getQuantity();
        //商品SKU虚拟库存
        Integer xnQuantity =  goodsSkuCo.getXnQuantity() == null ? 0 :  goodsSkuCo.getXnQuantity();
        //订单商品数量
        Integer goodsNumber = quantity.getGoodsNumber() == null ? 0 : quantity.getGoodsNumber();
        Integer xnReduction = 0;
        Integer xjReduction = 0;
         if(oldQuantity != 0){
             if(goodsNumber > oldQuantity){
                 //实际库存扣多少 虚拟库存扣多少
                 xnReduction = goodsNumber - oldQuantity;
                 xjReduction = goodsNumber - xnReduction;
                //虚拟库存扣多少
                 xnQuantity = xnQuantity - xnReduction;
                 oldQuantity = oldQuantity - xjReduction;
             }else{
                 oldQuantity = oldQuantity - goodsNumber;
                 xjReduction = goodsNumber;
             }
         }else{
             xnQuantity = xnQuantity - goodsNumber;
             xnReduction = goodsNumber;
         }
        //SKU 可用库存=实际库存+虚拟库存
        oldEnableQuantity = oldQuantity + xnQuantity;
        //商品库存计算
        oldGoodsQuantity = oldGoodsQuantity - goodsNumber;

        goodsSkuCo.setEnableQuantity(oldEnableQuantity);
        goodsSkuCo.setQuantity(oldQuantity);
        goodsSkuCo.setXnQuantity(xnQuantity);

        esGoodsCO.setQuantity(oldGoodsQuantity);
        //更新SKU信息
        updateGoodsSkuList(esGoodsCO,quantity.getSkuId(),oldQuantity,oldEnableQuantity,xnQuantity);
        EsGoods goods = new EsGoods();
        EsGoodsSku goodsSku = new EsGoodsSku();
        BeanUtil.copyProperties(esGoodsCO,goods);
        BeanUtil.copyProperties(goodsSkuCo,goodsSku);
        this.esGoodsService.updateById(goods);
        this.esGoodsSkuService.updateById(goodsSku);
        //插入商品库存日志记录
        EsGoodsQuantityLogDTO esGoodsQuantityLogDTO = new EsGoodsQuantityLogDTO();
        esGoodsQuantityLogDTO.setGoodsSum(goodsNumber);
        esGoodsQuantityLogDTO.setXnQuantity(xnReduction);
        esGoodsQuantityLogDTO.setQuantity(xjReduction);
        esGoodsQuantityLogDTO.setOrderSn(orderSn);
        esGoodsQuantityLogDTO.setType(0);
        esGoodsQuantityLogDTO.setGoodsId(quantity.getGoodsId());
        esGoodsQuantityLogDTO.setSkuId(quantity.getSkuId());
        this.esGoodsQuantityLogService.insertGoodsQuantityLog(esGoodsQuantityLogDTO);
    }
    //库存增加计算
    public void insertGoodsQuantity(EsGoodsSkuQuantityDTO quantity,String orderSn){
        //从缓存中获取商品信息
        DubboResult<EsSellerGoodsDO> goodsCo = esGoodsService.getSellerEsGoods(quantity.getGoodsId());
        //从缓存中获取SKU信息
        DubboResult<EsGoodsSkuCO> skuCo =esGoodsSkuService.getSkuById(quantity.getSkuId());
        EsSellerGoodsDO esGoodsCO = goodsCo.getData();
        EsGoodsSkuCO skuCO = skuCo.getData();
        //商品库存 old
        Integer oldGoodsQuantity = esGoodsCO.getQuantity() == null ? 0 : esGoodsCO.getQuantity();
        //商品SKU 可用 库存
        Integer oldEnableQuantity =  skuCO.getEnableQuantity() == null ? 0 :  skuCO.getEnableQuantity();
        //商品SKU 实际库存
        Integer oldQuantity = skuCO.getQuantity() == null ? 0 :  skuCO.getEnableQuantity();
        //商品SKU虚拟库存
        Integer xnQuantity =  skuCO.getXnQuantity() == null ? 0 :   skuCO.getXnQuantity();

        //订单商品数量
        Integer goodsNumber = quantity.getGoodsNumber() == null ? 0 : quantity.getGoodsNumber();
       DubboResult<EsGoodsQuantityLogDO> result = esGoodsQuantityLogService.getGoodsQuantityLog(orderSn,quantity.getSkuId());
       if(!result.isSuccess() || result.getData() == null){
           throw new ArgumentException(GoodsErrorCode.QUANTITY_SHORTAGE.getErrorCode(),String.format("获取库存扣减日志数据失败 %s",orderSn));
       }
        EsGoodsQuantityLogDO quantityLogDO = result.getData();
        Integer xnReduction =quantityLogDO.getXnQuantity();
        Integer xjReduction = quantityLogDO.getQuantity();

        oldQuantity = oldQuantity +xjReduction;
        xnQuantity = xnQuantity + xnReduction;
        //SKU 库存计算  可用库存=实际库存+虚拟库存
        oldEnableQuantity = oldQuantity + xnQuantity;
        //商品库存计算
        oldGoodsQuantity = oldGoodsQuantity + goodsNumber;
        skuCO.setEnableQuantity(oldEnableQuantity);
        skuCO.setQuantity(oldQuantity);
        skuCO.setXnQuantity(xnQuantity);
        esGoodsCO.setQuantity(oldGoodsQuantity);
        logger.info("库存扣减后库存："+oldGoodsQuantity);
        logger.info("库存扣减后SKU库存："+oldEnableQuantity);
        //更新SKU信息
        this.updateGoodsSkuList(esGoodsCO,quantity.getSkuId(),oldQuantity,oldEnableQuantity,xnQuantity);
        EsGoods goods = new EsGoods();
        EsGoodsSku goodsSku = new EsGoodsSku();
        BeanUtil.copyProperties(esGoodsCO,goods);
        BeanUtil.copyProperties(skuCO,goodsSku);

        this.esGoodsSkuService.updateById(goodsSku);
        this.esGoodsService.updateById(goods);
        //插入商品库存日志记录
        EsGoodsQuantityLogDTO esGoodsQuantityLogDTO = new EsGoodsQuantityLogDTO();
        esGoodsQuantityLogDTO.setGoodsSum(goodsNumber);
        esGoodsQuantityLogDTO.setXnQuantity(xnReduction);
        esGoodsQuantityLogDTO.setQuantity(xjReduction);
        esGoodsQuantityLogDTO.setOrderSn(orderSn);
        esGoodsQuantityLogDTO.setType(1);
        esGoodsQuantityLogDTO.setGoodsId(quantity.getGoodsId());
        esGoodsQuantityLogDTO.setSkuId(quantity.getSkuId());
        this.esGoodsQuantityLogService.insertGoodsQuantityLog(esGoodsQuantityLogDTO);
    }

    public void updateGoodsSkuList(EsSellerGoodsDO esGoodsCO, Long skuId, Integer skuQuantity, Integer skuEnableQuantity,Integer xnQuantity){
        //更新商品中的SKU信息
        List<EsSellerGoodsSkuDO> goodsSkuCOList = esGoodsCO.getSkuList();
        for(EsSellerGoodsSkuDO goodsSkuCO:goodsSkuCOList){
            if(goodsSkuCO.getId() == skuId){
                goodsSkuCO.setQuantity(skuQuantity);
                goodsSkuCO.setEnableQuantity(skuEnableQuantity);
                goodsSkuCO.setXnQuantity(xnQuantity);
                break;
            }
        }
    }
}
