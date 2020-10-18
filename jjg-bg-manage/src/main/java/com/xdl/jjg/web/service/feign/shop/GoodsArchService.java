package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsArchDO;
import com.jjg.shop.model.dto.EsGoodsArchDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-shop")
public interface GoodsArchService {

    /**
     *
     * @param goodsArchDTO 查询条件实体
     * @param pageSize 页数
     * @param pageNum 页码
     * @return
     */
    @GetMapping("/getGoodsArchList")
    DubboPageResult<EsGoodsArchDO> getGoodsArchList(@RequestBody EsGoodsArchDTO goodsArchDTO, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
    /**
     * 商品档案逻辑删除
     * @param ids 主键ID集合
     * @return
     */
    @DeleteMapping("/deleteGoodsArch")
    DubboResult<EsGoodsArchDO> deleteGoodsArch(@RequestParam("ids") Integer[] ids);

    /**
     * 商品档案入库
     * @param goodsArchDTO 查询条件实体
     * @return
     */
    @PostMapping("/adminInsertGoodsArch")
    DubboResult<EsGoodsArchDO> adminInsertGoodsArch(@RequestBody EsGoodsArchDTO goodsArchDTO);
    /**
     * 商品档案修改
     * @param goodsArchDTO 查询条件实体
     *@return
     */
    @PostMapping("/adminUpdateGoodsArch")
    DubboResult<EsGoodsArchDO> adminUpdateGoodsArch(@RequestBody EsGoodsArchDTO goodsArchDTO,@RequestParam("id") Long id);
    /**
     * 根据商品id 查询
     * @param id
     * @return
     */
    @GetMapping("/getGoodsArch")
    DubboResult<EsGoodsArchDO> getGoodsArch(@RequestParam("id")Long id);
}
