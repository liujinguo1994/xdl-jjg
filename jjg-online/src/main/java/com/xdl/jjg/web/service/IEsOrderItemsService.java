package com.xdl.jjg.web.service;



import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.CartItemsDTO;
import com.jjg.trade.model.dto.EsOrderDTO;
import com.jjg.trade.model.dto.EsOrderItemsDTO;
import com.jjg.trade.model.dto.EsReFundQueryBuyerDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单商品明细表-es_order_items 服务类
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface IEsOrderItemsService {
    /**
     * 查询订单商品明细表
     * @param id 订单编号
     * @return
     */
    DubboResult<EsOrderItemsDO> getEsOrderItemsInfo(Long id);

    /**
     * 查询订单商品明细表分页
     * @param esOrderItemsDTO
     * @return
     */
    DubboPageResult<EsOrderItemsDO> getEsOrderItemsList(EsOrderItemsDTO esOrderItemsDTO, int pageSize, int pageNum);

    /**
     *保存订单商品明细表
     * @param esOrderItemsDTO
     * @return
     */
    DubboResult<EsOrderItemsDO> insertOrderItems(EsOrderItemsDTO esOrderItemsDTO);

    /**
     * 修改订单商品明细详情
     * @param esOrderItemsDTO
     * @return
     */
    DubboResult<EsOrderItemsDO> updateOrderItemsMessage(EsOrderItemsDTO esOrderItemsDTO);

    /** 卖家端
     * 修改订单商品明细售后状态
     * @param orderSn  goodsIds
     * @return
     */
    DubboResult<EsOrderItemsDO> updateOrderItemsServiceStatus(String orderSn, Long[] goodsIds, String serviceStatus);


    /** 卖家端
     * 售后审核退款 订单项商品售后状态修改
     * @param orderSn  goodsIds
     * @param skuIds
     * @return
     */
    DubboResult<EsOrderItemsDO> updateOrderItemsServiceStatusByGoodsIdAndSkuId(String orderSn, Long[] goodsIds, Long[] skuIds, String serviceStatus);


    /**
     * 删除订单商品明细表数据
     * @param id
     * @return
     */
    DubboResult<EsOrderItemsDO> deleteOrderItemsMessage(Long id);

    /**
     * 根据订单明细表OrderSn 获取订单商品明细表信息
     * @param orderSn
     * @return
     */
   DubboPageResult<EsOrderItemsDO> getEsOrderItemsByOrderSn(String orderSn);

   /**
   /**
    * @Description: 退款的时候赠品信息不放入退款单中，通过orderSn查询不计入订单商品内
    * @Author       LiuJG 344009799@qq.com
    * @Date         2020/6/10 15:06
    * @param
    * @return       com.shopx.common.model.result.DubboPageResult<com.shopx.trade.api.model.domain.EsOrderItemsDO>
    * @exception
    *
    */
   DubboPageResult<EsOrderItemsDO> getEsOrderItemsByOrderSnNoZP(String orderSn);

   DubboPageResult<EsSellerOrderItemsDO> getSellerEsOrderItemsByOrderSn(String orderSn);
    /**
     * 根据订单明细表tradeSn 获取订单商品明细表信息
     * @param tradeSn
     * @return
     */
    DubboPageResult<EsOrderItemsDO> getEsOrderItemsByTradeSn(String tradeSn);

    /**
     * 根据订单明细表OrderSn 获取订单商品明细表信息
     * @param orderSn
     * @return
     */
    DubboResult<EsOrderItemsDO> getEsOrderItemsByOrderSnAndGoodsId(String orderSn, Long goodsId);
    /**
     * 根据订单明细表OrderSn 获取订单商品明细表信息 售后
     * @param orderSn
     * @return
     */
    DubboResult<EsOrderItemsDO> getEsAfterSaleOrderItemsByOrderSnAndSkuId(String orderSn, Long skuId);

    DubboPageResult<EsOrderItemsDO> getEsRefundOrderItemsByOrderSnAnd(String orderSn);

    /**
     * 根据订单明细表OrderSn 和 商品Id 获取订单商品明细表信息
     * @param orderSn
     * @return
     */
    DubboResult<EsOrderItemsDO> getBuyerEsOrderItemsByOrderSnAndGoodsId(String orderSn, Long goodsId);

    /**
     *  买家端
     * Liujg
     * 1.查询评价信息接口
     * @param memberId
     * @param singleCommentStatus
     * @param pageSize
     * @param pageNum
     * @return
     */
    DubboPageResult<EsBuyerOrderDO> getBuyerOrderCommentGoodsList(Long memberId, String orderSn, String singleCommentStatus, int pageSize, int pageNum);

    DubboResult<EsBuyerOrderDO> getBuyerCommentGoodsList(Long memberId, String orderSn, String singleCommentStatus);
    /**
     *  TODO 买家端
     * 修改订单表 和订单商品明细表评价状态
     * @param orderSn
     * @param goodsId
     * @return
     */
    DubboResult<EsOrderItemsDO> updateOrderCommentStatus(String orderSn, Long goodsId, Long skuId);
    /**
     *   买家端
     * 查询订单商品明细表中未评价的商品id 和订单编号
     * @param
     * @param
     * @return
     */
    List<EsOrderItemsDO> selectNotCommentGoods();
    /**
     *   买家端
     * 通过订单编号list 查询该交易的所有订单项商品
     * @param
     * @param
     * @return
     */
    DubboPageResult<EsOrderItemsDO> getEsOrderItemsByOrderSnList(List<String> collect);
    /**
     *   买家端
     * 查询 可申请售后的订单项列表
     * @param
     * @param
     * @return
     */
    DubboPageResult<EsServiceOrderItemsDO> getEsServiceOrderItemsList(EsReFundQueryBuyerDTO esReFundQueryDTO, int pageSize, int pageNum);

    /**
     *  手机端
     * 查询 可申请售后的订单项列表
     * @param
     * @param
     * @return
     */
    DubboPageResult<EsWapRefundOrderItemsDO> getEsWapOrderItemsList(EsReFundQueryBuyerDTO esReFundQueryDTO, int pageSize, int pageNum);
    /**
     *   买家端
     *  查询点击退货退款 页面回显退款明细
     * @param
     * @param
     * @return
     */
    DubboResult<Map<String, Double>> getEsOrderItemsMoney(String orderSn, Long[] skuIds);

    DubboResult<EsOrderItemsDO> queryBySnapshotId(Long snapshotId, Long goodsId);


    /**
     *
     *  查询国寿订单详情
     * @param
     * @param
     * @return
     */
    DubboResult<EsOrderItemsDO> queryLfcItem(String orderSn);
    /**
     * @Description: 通过订单号和快递单号查询商品图片
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/4/14 14:32
     * @param
     * @return       com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.EsOrderItemsDO>
     * @exception
     *
     */
    DubboPageResult<EsOrderItemsDO> getEsOrderItemsByOrderSnAndShipNo(String orderSn, String shipNo);


    /**
     *  TODO 买家端
     * 修改订单表 和订单商品明细表评价数量
     * @return
     */
    DubboResult<EsOrderCommDO> getCount(Long memberId);

    /**
     * @Description: 异步 保存订单商品项信息
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/6/17 10:45
     *
     */
    DubboResult saveOrderItemMessage(EsOrderDTO esOrder, CartItemsDTO cartItemsDTO);
}
