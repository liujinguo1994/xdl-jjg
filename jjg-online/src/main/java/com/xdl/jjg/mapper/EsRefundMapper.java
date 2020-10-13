package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shopx.trade.api.model.domain.dto.EsReFundQueryDTO;
import com.shopx.trade.dao.entity.EsRefund;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsRefundMapper extends BaseMapper<EsRefund> {

    Double selectRefundPayMoney(@Param("orderSn") String orderSn, @Param("shopId") Long shopId, @Param("status") String[] status);

    IPage<EsRefund> selectRefundList(Page<EsRefund> page, @Param("esReFundQueryDTO") EsReFundQueryDTO esReFundQueryDTO);

    IPage<EsRefund> getAfterSalesRecords(Page<EsRefund> page, @Param("memberId") Long memberId);

    Integer selectRefundNum(@Param("orderSn") String orderSn, @Param("goodsId") Long goodsId, @Param("shState") String shState, @Param("tkState") String tkState);

    Integer selectNum(@Param("pefundStatus1") String RefundStatus1, @Param("processStatus") String processStatus, @Param("orderSn") String orderSn);

    EsRefund getRefundSn(@Param("orderSn") String orderSn, @Param("skuId") Long skuId);

}
