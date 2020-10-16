package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.domain.EsOrderItemsDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "jjg-online")
public interface OrderItemsService {

    /**
     *  TODO 买家端
     * 修改订单表 和订单商品明细表评价状态

     */
    @PostMapping("/updateOrderCommentStatus")
    DubboResult<EsOrderItemsDO> updateOrderCommentStatus(@RequestParam("orderSn") String orderSn,@RequestParam("goodsId") Long goodsId, @RequestParam("skuId") Long skuId);
    /**
     *   买家端
     * 查询订单商品明细表中未评价的商品id 和订单编号

     */
    @GetMapping("/selectNotCommentGoods")
    List<EsOrderItemsDO> selectNotCommentGoods();

    /**
     * 根据订单明细表OrderSn 和 商品Id 获取订单商品明细表信息
     */
    @GetMapping("/getBuyerEsOrderItemsByOrderSnAndGoodsId")
    DubboResult<EsOrderItemsDO> getBuyerEsOrderItemsByOrderSnAndGoodsId(@RequestParam("orderSn") String orderSn, @RequestParam("goodsId") Long goodsId);

}
