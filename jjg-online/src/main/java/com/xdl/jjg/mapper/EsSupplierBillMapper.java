package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jjg.trade.model.domain.EsSettlementDetailDO;
import com.xdl.jjg.entity.EsExportOrder;
import com.xdl.jjg.entity.EsShopBill;
import com.xdl.jjg.entity.EsSupplierBill;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 供应商结算单-es_supplier Mapper 接口
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 15:33:46
 */
@Repository
public interface EsSupplierBillMapper extends BaseMapper<EsSupplierBill> {

    /**
     * 根据供应商汇总账单
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 16:07:56
     * @param idList idList
     * @return: java.util.List<com.shopx.trade.dao.entity.EsShopBill>
     */
    List<EsSupplierBill> summary(List<String> idList);



    /**
     * 查询供应商结算单明细
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 14:18:20
     * @param page          分页插件
     * @param settlementId  结算id
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.shopx.trade.api.model.domain.EsSettlementDetailDO>
     */
    IPage<EsSettlementDetailDO> getSettlementDetail(Page<EsShopBill> page, @Param("settlementId") Long settlementId);

    /**
     * 查询供应商结算单退款明细
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 14:18:20
     * @param page          分页插件
     * @param settlementId  结算id
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.shopx.trade.api.model.domain.EsSettlementDetailDO>
     */
    IPage<EsSettlementDetailDO> getSettlementRefundDetail(Page<EsShopBill> page, @Param("settlementId") Long settlementId);

    /**
     * 查询供应商结算单明细
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 14:18:20
     * @param settlementId  结算id
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.shopx.trade.api.model.domain.EsSettlementDetailDO>
     */
    List<EsExportOrder> getExportOrder(@Param("settlementId") Long settlementId);

    /**
     * 查询供应商结算单退款明细
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 14:18:20
     * @param settlementId  结算id
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.shopx.trade.api.model.domain.EsSettlementDetailDO>
     */
    List<EsExportOrder> getExportRefundOrder(@Param("settlementId") Long settlementId);
}
