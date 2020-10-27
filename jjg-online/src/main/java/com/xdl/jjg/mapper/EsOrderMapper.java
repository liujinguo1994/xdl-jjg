package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.*;
import com.xdl.jjg.entity.EsOrder;
import com.xdl.jjg.entity.EsServiceOrderItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单明细表-es_order Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsOrderMapper extends BaseMapper<EsOrder> {

    IPage<EsOrderDO> getEsSellerOrderList(Page page, @Param("esSellerOrderQueryDTO") EsSellerOrderQueryDTO esSellerOrderQueryDTO);
    IPage<EsOrderDO> getEsLfcOrderList(Page page, @Param("esSellerOrderQueryDTO") EsSellerOrderQueryDTO esSellerOrderQueryDTO);
    IPage<EsBuyerOrderDO> getEsBuyerOrderList(Page page, @Param("esBuyerOrderQueryDTO") EsBuyerOrderQueryDTO esBuyerOrderQueryDTO);

    IPage<EsBuyerOrderDO> getEsMemberOrderList(Page page, @Param("esBuyerOrderQueryDTO") EsBuyerOrderQueryDTO esBuyerOrderQueryDTO);

    void updateOrderPayParamBatch(@Param("esOrderDTO") EsOrderDTO esOrderDTO, @Param("tradeList") List<String> tradeList);

    IPage<EsOrderDO> getEsAdminOrderList(Page page, @Param("esAdminOrderQueryDTO") EsAdminOrderQueryDTO esAdminOrderQueryDTO);

    List<EsOrderDO> getEsBuyerMemberLevel(@Param("currentTime") long currentTime, @Param("time") long time);

    List<EsRFMTradeDO> getEsBuyerMemberLevelLnsSpace(@Param("timeSpace") String timeSpace, @Param("times") String times, @Param("judge") Integer judge);

    IPage<EsBuyerOrderDO> getEsMemberReceiptList(Page page, @Param("esBuyerOrderQueryDTO") EsBuyerOrderQueryDTO esBuyerOrderQueryDTO);

    IPage<EsServiceOrderItems> selectServiceItemsList(Page<EsServiceOrderItems> page, @Param("esReFundQueryDTO") EsReFundQueryBuyerDTO esReFundQueryDTO);


    /**
     * 手机端查看可申请退款单
     * @author: yuanj 595831329@qq.com
     * @date: 2020/04/02 11:07:54
     * @param esReFundQueryDTO   查询条件
     * @param page
     * @return: com.shopx.trade.api.model.domain.EsWapRefundOrderItemsDO
     */
    IPage<EsWapRefundOrderItemsDO> getEsWapOrderItemsList(Page<EsWapRefundOrderItemsDO> page, @Param("esReFundQueryDTO") EsReFundQueryBuyerDTO esReFundQueryDTO);

    List<EsOrderDO> getOrderStatusNum(Long id);

    int getEsAdminOrderCountByTradeSn(@Param("tradeSn") String tradeSn);


    /**
     * 获取可申请售后数量
     * @author: yuanj 595831329@qq.com
     * @date: 2020/04/07 16:06:54
     * @param memberId
     * @return: com.shopx.trade.api.model.domain.vo.EsWapRefundCountVO
     */
    int getCounts(@Param("memberId") Long memberId);



    /**
     * 根据订单id和商品id 查询订单数据及详情
     * @author: libw 981087977@qq.com
     * @date: 2019/09/19 14:07:54

     * @return: com.shopx.trade.api.model.domain.EsOrderDO
     */
//    EsOrderDO getOrderItem(@Param("orderSn") String orderSn, @Param("goodsId") Long goodsId);

    List<EsExportOrdersDO> getExportOrderList(@Param("dto") EsExportOrderDTO dto);
}
