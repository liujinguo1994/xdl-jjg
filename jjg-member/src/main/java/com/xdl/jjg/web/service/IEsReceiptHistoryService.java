package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsReceiptHistoryDO;
import com.xdl.jjg.model.dto.EsReceiptHistoryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 发票历史 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsReceiptHistoryService {

    /**
     * 插入数据
     *
     * @param receiptHistoryDTO 发票历史DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult insertReceiptHistory(EsReceiptHistoryDTO receiptHistoryDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param receiptHistoryDTO 发票历史DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult updateReceiptHistory(EsReceiptHistoryDTO receiptHistoryDTO);

    /**
     * 根据goodsId获取数据
     *
     * @param goodsId 订单编号 goodsId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult<EsReceiptHistoryDO> getReceiptHistoryByGoodsId(Long goodsId);

    /**
     * 根据ordsn和goodsId获取数据
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult<EsReceiptHistoryDO> getReceiptHistoryByInvoiceSerialNum(String invoiceSerialNum);

    /**
     * 根据发票流水号获取数据
     *
     * @param goodsId 订单编号 goodsId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult<EsReceiptHistoryDO> getReceiptHistoryByGoodsIdAndOrdersn(Long goodsId, String orderSn);


    /**
     * 根据id获取数据
     *
     * @param id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult<EsReceiptHistoryDO> getReceiptHistoryById(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param receiptHistoryDTO 发票历史DTO
     * @param pageSize          行数UserCO
     * @param pageNum           页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsReceiptHistoryDO>
     */
    DubboPageResult<EsReceiptHistoryDO> getReceiptHistoryList(EsReceiptHistoryDTO receiptHistoryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult deleteReceiptHistory(Long id);
}
