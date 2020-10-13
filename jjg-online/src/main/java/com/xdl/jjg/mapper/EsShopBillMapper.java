package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shopx.trade.api.model.domain.EsSettlementDetailDO;
import com.shopx.trade.dao.entity.EsExportOrder;
import com.shopx.trade.dao.entity.EsShopBill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 店铺结算单 Mapper 接口
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-08-23 15:54:01
 */
@Repository
public interface EsShopBillMapper extends BaseMapper<EsShopBill> {

    /**
     * 根据店铺汇总账单
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 16:07:56
     * @param idList idList
     * @return: java.util.List<com.shopx.trade.dao.entity.EsShopBill>
     */
    List<EsShopBill> summary(List<String> idList);

//    IPage<EsShopBill> selectList(Page<EsShopBill> page, Long time, Long startTime, @Param("settlementId") Long settlementId);

    /**
     * 查询店铺结算单明细
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 14:18:20
     * @param page          分页插件
     * @param settlementId  结算id
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.shopx.trade.api.model.domain.EsSettlementDetailDO>
     */
    IPage<EsSettlementDetailDO> getSettlementDetail(Page<EsShopBill> page, @Param("settlementId") Long settlementId);

    /**
     * 查询店铺结算单退款明细
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 14:18:20
     * @param page          分页插件
     * @param settlementId  结算id
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.shopx.trade.api.model.domain.EsSettlementDetailDO>
     */
    IPage<EsSettlementDetailDO> getSettlementRefundDetail(Page<EsShopBill> page, @Param("settlementId") Long settlementId);

    /**
     * 查询店铺结算单明细
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 14:18:20
     * @param settlementId  结算id
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.shopx.trade.api.model.domain.EsSettlementDetailDO>
     */
    List<EsExportOrder> getExportOrder(@Param("settlementId") Long settlementId);

    /**
     * 查询店铺结算单退款明细
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 14:18:20
     * @param settlementId  结算id
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.shopx.trade.api.model.domain.EsSettlementDetailDO>
     */
    List<EsExportOrder> getExportRefundOrder(@Param("settlementId") Long settlementId);


}
