package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.*;
import com.jjg.trade.model.enums.OrderPermission;
import com.jjg.trade.model.vo.*;
import com.xdl.jjg.message.OrderStatusChangeMsg;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 订单明细表-es_order 服务类
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface IEsOrderService {
    /**
     * 卖家端 查看订单详情信息
     * 查询订单明细表
     * @param orderSn
     * @param shopId
     * @return
     */
    DubboResult<EsSellerOrderDO> getEsSellerOrderInfo(String orderSn, Long shopId);

    /**
     * 读取订单列表根据交易编号
     *
     * @param tradeSn
     * @return
     */
    DubboPageResult<EsOrderDO> getEsOrderInfoByTradeSn(String tradeSn);

    /**
     * 读取订单列表根据交易编号 和订单状态
     *
     * @param tradeSn
     * @return
     */
    DubboPageResult<EsOrderDO> getEsOrderInfoByTradeSnAndState(String tradeSn, String orderState);

    /**
     * 查询订单明细表分页
     * @param esOrderDTO
     * @return
     */
    DubboPageResult<EsOrderDO> getEsOrderInfoList(EsOrderDTO esOrderDTO, int pageSize, int pageNum);

    /**
     * 保存订单明细表
     * @param esOrderDTO
     * @return
     */
    DubboResult<EsOrderDO> insertOrder(EsOrderDTO esOrderDTO);

    /**
     * 通用接口
     * 修改订单明细详情
     * @param esOrderDTO
     * @return
     */
    DubboResult<EsOrderDO> updateOrderMessage(EsOrderDTO esOrderDTO);
    /**
     * 已经调试完成
     * 修改订单支付参数
     * @param esOrderDTO
     * @return
     */
    DubboResult<EsOrderDO> updateOrderPayParam(EsOrderDTO esOrderDTO);
    /**
     * 订单成功， 修改订单状态
     * @param esOrderDTO
     * @return
     */
    DubboResult<EsOrderDO> updateOrderStatus(EsOrderDTO esOrderDTO);


    /**
     * 逻辑删除订单明细表数据
     * @param id
     * @return
     */
    DubboResult<EsOrderDO> deleteOrderMessage(Integer id);
    /**
     * 卖家端 交易订单信息变化
     * @param esOrderDTO
     * @return
     */
    DubboResult<EsOrderDO> updateSellerOrderMessage(EsSellerOrderDTO esOrderDTO);
    /**
     * 卖家端
     * 查询订单列表分页
     * @param esSellerOrderQueryDTO
     * @return
     */
     DubboPageResult<EsOrderDO> getEsSellerOrderList(EsSellerOrderQueryDTO esSellerOrderQueryDTO, int pageSize, int pageNum);
    /**
     * 系统后台
     * 查询订单明细信息
     * @param
     * @return EsOrderDO
     */
    DubboResult<EsSellerOrderDO> getEsAdminOrderInfo(String orderSn);

    /**
     * 系统后台订单列表
     * @param esAdminOrderQueryDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    DubboPageResult<EsOrderDO> getEsAdminOrderList(EsAdminOrderQueryDTO esAdminOrderQueryDTO, int pageSize, int pageNum);
    /**
     * 卖家端 待删除
     * 查询订单明细表
     * @param orderSn
     * @return EsOrderDO
     */
    DubboResult<EsOrderDO> getEsSellerOrder(String orderSn, Long shopId);

    /**
     * 买家端
     * 查询订单明细表
     * 会员判断是够已经下过订单
     * @param orderSn
     * @return EsOrderDO
     */
    DubboResult<EsOrderDO> getEsBuyerOrderInfo(String orderSn);
    /**
     * 买家端 待发货，待收货，待评价，
     * 买家端UI 中我的订单页面 根据订单状态 和订单信息查询该用户下的订单
     * @param esBuyerOrderQueryDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    DubboPageResult<EsBuyerOrderDO> getEsBuyerOrderList(EsBuyerOrderQueryDTO esBuyerOrderQueryDTO, int pageSize, int pageNum);

    /**
     * mq 订单状态修改
     * @param orderStatusChangeMsg
     * @return
     */
    DubboResult<EsOrderDO> updateOrderStatusMq(OrderStatusChangeMsg orderStatusChangeMsg);
    /**
     * 系统后台 查询发票详情信息
     * @param orderSn
     * @param goodsId
     * @return
     */
    DubboResult<EsAdminOrderDO> getEsAdminReceiptInfo(String orderSn, Long goodsId);

    /**
     *  买家 会员 等级
     */
   // DubboPageResult<EsRFMTradeDO> getEsBuyerMemberLevel();


    /**
     *  买家 会员 等级
     */
    DubboPageResult<List<EsRFMTradeDO>>  getEsBuyerMemberLevelLnS();

    /**
     * @Description: 会员中心 我的订单
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/20 14:16
     * @param
     * @return       com.shopx.common.model.result.DubboPageResult<com.shopx.trade.api.model.domain.EsBuyerOrderDO>
     * @exception
     *
     */
    DubboPageResult<EsBuyerOrderDO> getEsMemberOrderList(EsBuyerOrderQueryDTO esBuyerOrderQueryDTO, int pageSize, int pageNum);

    /**
     * @Description: 会员中心 发票管理，开具发票
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/20 14:16
     * @param
     * @return       com.shopx.common.model.result.DubboPageResult<com.shopx.trade.api.model.domain.EsBuyerOrderDO>
     * @exception
     *
     */
    DubboPageResult<EsBuyerOrderDO> getEsMemberReceiptList(EsBuyerOrderQueryDTO esBuyerOrderQueryDTO, int pageSize, int pageNum);


    /**
     * 根据订单编号和商品id查询订单及订单物品明细
     * @author: libw 981087977@qq.com
     * @date: 2019/09/19 13:21:01
     * @param orderSn   订单编号
     * @param goodsId   商品id
     * @return: com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.EsOrderDO>
     */
    DubboResult<EsOrderDO> getOrderItem(String orderSn, Long goodsId);

    /**
     * 买家端 支付时查询订单详情
     * 查询订单明细表
     * @param orderSn
     * @return EsOrderDO
     */
    DubboResult<EsOrderDO> getEsBuyerOrder(String orderSn, Long memberId);

    /**
     * 买家端 获取订单信息（售后模块）
     * 查询订单明细表
     * @param orderSn
     * @return EsOrderDO
     */
    DubboResult<EsOrderDO> getEsBuyerOrderInfoAfterSale(String orderSn);


    /**
     * @Description: 根据商品orderSN 会员Id，删除状态 查询订单信息
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/12/10 17:27
     * @param
     * @return       com.shopx.common.model.result.DubboPageResult<com.shopx.trade.api.model.domain.EsServiceOrderDO>
     * @exception
     *
     */
    DubboResult<EsServiceOrderDO> getEsCancelOrderInfo(EsOrderDTO esOrderDTO);

    DubboPageResult<EsOrderFlow> getOrderFlow(String orderSn, Long shopId);

    DubboResult ship(EsDeliveryDTO deliveryDTO, OrderPermission permission);



    /**
     * @Description: 根据 订单编号 会员id 商品id 查询订单单个商品信息
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/12/15 11:36
     * @param
     * @return       com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.EsServiceOrderDO>
     * @exception
     *
     */
    DubboResult<EsServiceOrderDO> getEsServiceOrderInfo(String orderSn, Long skuId, Long memberId);

    DubboResult<EsServiceOrderDO> getEsRefundOrderList(String orderSn, Long memberId);

    DubboResult<EsOrderDO> getEsOrderInfo(String orderSn);

    DubboResult<OrderStatusNumVO> getOrderStatusNum(Long id);
    /**
     * @Description: 根据 订单编号 订单状态查询订单 或者交易单商品详情
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/12/15 11:36
     * @param
     * @return       com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.EsServiceOrderDO>
     * @exception
     *
     */
    DubboResult<EsOrderDO> getEsBuyerOrderDetails(String orderSn, String orderState);


    /**
     * @Description: 手机端根据 订单编号 订单状态查询订单 或者交易单商品详情
     * @Author       yuanj 595831329@qq.com
     * @Date         2020/03/16 19:36
     * @param
     * @return       com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.EsServiceOrderDO>
     * @exception
     *
     */
    DubboPageResult<EsOrderDO> getEsWapOrderDetails(String orderSn, String orderState);


    /**
     * 根据国寿订单号查询订单
     *
     * @param lfcId
     * @return
     */
    DubboResult<EsOrderDO> queryLfcOrder(String lfcId);

    DubboResult<EsOrderDO> cancelLfc(CancelVO cancelVO, OrderPermission permission, Long shopId);

    DubboResult send(String orderSn);


    //卖家端订单导出
    DubboPageResult<EsExportOrdersDO> exportOrder(EsExportOrderDTO dto);

    DubboResult<EsOrderDO> updateOrderState(EsOrderDTO esOrderDTO);

    //批量发货
    DubboResult<EsImportShipVO> importShip(byte[] base64);

    DubboPageResult<EsOrderDO> getLfcOrderList(EsSellerOrderQueryDTO esSellerOrderQueryDTO, int pageSize, int pageNum);


    /**
     * @Description: 手机端发票列表查询
     * @Author       yuanj 595831329@qq.com
     * @Date         2020/04/08 11:16
     * @param
     * @return       com.shopx.common.model.result.DubboPageResult<com.shopx.trade.api.model.domain.vo.EsWapReceptVO>
     * @exception
     *
     */
    DubboPageResult<EsWapReceptVO> getWapMemberReceiptList(EsBuyerOrderQueryDTO esBuyerOrderQueryDTO, int pageSize, int pageNum);
}
