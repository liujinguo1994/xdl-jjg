package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsBrandDO;
import com.jjg.shop.model.dto.EsBrandDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface BrandService {

    /**
     * 新增品牌
     * @param brandDTO
     * @return
     */
    @PostMapping("/insertBrand")
    DubboResult<EsBrandDO> insertBrand(@RequestBody EsBrandDTO brandDTO);

    /**
     * 修改品牌
     * @param brandDTO
     * @return
     */
    @PostMapping("/updateBrand")
    DubboResult<EsBrandDO> updateBrand(@RequestBody EsBrandDTO brandDTO, @RequestParam("id") Long id);

    /**
     * 分页查询品牌信息
     * @param brandDTO
     * @return
     */
    @GetMapping("/getBrandList")
    DubboPageResult<EsBrandDO> getBrandList(@RequestBody EsBrandDTO brandDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);

    /**
     * 逻辑删除品牌
     * @param ids
     * @return
     */
    @GetMapping("/deleteBrand")
    DubboResult<EsBrandDO> deleteBrand(@RequestParam("ids") Integer[] ids);

}
