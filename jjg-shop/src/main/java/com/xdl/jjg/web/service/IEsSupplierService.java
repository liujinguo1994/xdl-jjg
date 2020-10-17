package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsSupplierDO;
import com.jjg.shop.model.dto.EsSupplierDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 供应商 服务类
 * </p>
 *
 * @author wangaf
 * @since 2019-05-29
 */
public interface IEsSupplierService {

    /**
     * 新增供应商
     * @param supplierDTO
     * @return
     */
    DubboResult<EsSupplierDO> insertSupplier(EsSupplierDTO supplierDTO);
    /**
     * 修改供应商信息
     * @param supplierDTO
     * @return
     */
    DubboResult<EsSupplierDO>  updateSupplier(EsSupplierDTO supplierDTO, Long id);
    /**
     * 逻辑删除/禁用 供应商信息
     * @param ids
     * @return
     */
    DubboResult<EsSupplierDO> deleteSupplier(Integer[] ids);
    /**
     * 启用供应商信息
     * * @param id
     * * @return
     */
    DubboResult<EsSupplierDO> revertSupplier(Integer[] ids);

    /**
     * 启用供应商信息
     * * @param id
     * * @return
     */
    DubboResult<EsSupplierDO> prohibitSupplier(Integer[] ids);
    /**
     * 根据供应商主键ID
     * @param id
     * @return
     */
    DubboResult<EsSupplierDO> getSupplier(Long id);
    /**
     * 分页查询供应商信息
     * @param supplier
     * @return
     */
    DubboPageResult<EsSupplierDO> getSupplierList(EsSupplierDTO supplier, int pageSize, int pageNum);
}
