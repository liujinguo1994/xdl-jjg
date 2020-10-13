package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsReceiptHistory;
import com.xdl.jjg.model.domain.EsReceiptHistoryDO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 发票历史 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsReceiptHistoryMapper extends BaseMapper<EsReceiptHistory> {

    /**
     *依据发票流水号查询发票信息
     * @param invoiceSerialNum
     * @return
     */
    EsReceiptHistoryDO getReceiptHistoryByInvoiceSerialNum(@Param("es") String invoiceSerialNum);

}
