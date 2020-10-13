package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsOrderDO;
import com.shopx.trade.api.model.domain.dto.EsSupplierBillDetailDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-08-20
 */
public interface IEsSupplierBillDetailService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param supplierBillDetailDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDetailDO>
     */
    DubboResult insertSupplierBillDetail(EsSupplierBillDetailDTO supplierBillDetailDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param supplierBillDetailDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDetailDO>
     */
    DubboResult updateSupplierBillDetail(EsSupplierBillDetailDTO supplierBillDetailDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDetailDO>
     */
    DubboResult getSupplierBillDetail(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param supplierBillDetailDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSupplierBillDetailDO>
     */
    DubboPageResult getSupplierBillDetailList(EsSupplierBillDetailDTO supplierBillDetailDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDetailDO>
     */
    DubboResult deleteSupplierBillDetail(Long id);

    DubboResult generateBill(EsOrderDO esOrder);
}
