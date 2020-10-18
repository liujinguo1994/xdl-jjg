package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsSupplierDO;
import com.jjg.shop.model.dto.EsSupplierDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-shop")
public interface SupplierService {
    /**
     * 新增供应商
     * @param supplierDTO
     * @return
     */
    @PostMapping("/insertSupplier")
    DubboResult<EsSupplierDO> insertSupplier(@RequestBody EsSupplierDTO supplierDTO);
    /**
     * 修改供应商信息
     * @param supplierDTO
     * @return
     */
    @PostMapping("/updateSupplier")
    DubboResult<EsSupplierDO>  updateSupplier(@RequestBody EsSupplierDTO supplierDTO,@RequestParam("id") Long id);
    /**
     * 逻辑删除/禁用 供应商信息
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteSupplier")
    DubboResult<EsSupplierDO> deleteSupplier(@RequestParam("ids") Integer[] ids);
    /**
     * 启用供应商信息
     * * @param id
     * * @return
     */
    @PostMapping("/revertSupplier"
    )
    DubboResult<EsSupplierDO> revertSupplier(@RequestParam("ids") Integer[] ids);

    /**
     * 启用供应商信息
     * * @param id
     * * @return
     */
    @PostMapping("/prohibitSupplier")
    DubboResult<EsSupplierDO> prohibitSupplier(@RequestParam("ids") Integer[] ids);
    /**
     * 根据供应商主键ID
     * @param id
     * @return
     */
    @GetMapping("/getSupplier")
    DubboResult<EsSupplierDO> getSupplier(@RequestParam("ids") Long id);
    /**
     * 分页查询供应商信息
     * @param supplier
     * @return
     */
    @GetMapping("/getSupplierList")
    DubboPageResult<EsSupplierDO> getSupplierList(@RequestBody EsSupplierDTO supplier,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);




}
