package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsSupplierBillDO;
import com.shopx.trade.api.model.domain.dto.EsSupplierBillDTO;

/**
 * <p>
 * 供应商结算单-es_supplier 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 15:33:45
 */
public interface IEsSupplierBillService {

    /**
     * 插入数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @param supplierBillDTO    供应商结算单-es_supplierDTO
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDO>
     */
    DubboResult insertSupplierBill(EsSupplierBillDTO supplierBillDTO);

    /**
     * 根据条件更新更新数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @param supplierBillDTO   供应商结算单-es_supplierDTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDO>
     */
    DubboResult updateSupplierBill(EsSupplierBillDTO supplierBillDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDO>
     */
    DubboResult<EsSupplierBillDO> getSupplierBill(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @param supplierBillDTO  供应商结算单-es_supplierDTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsSupplierBillDO>
     */
    DubboPageResult<EsSupplierBillDO> getSupplierBillList(EsSupplierBillDTO supplierBillDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDO>
     */
    DubboResult deleteSupplierBill(Long id);
}
