package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsBrandSelectDO;
import com.jjg.shop.model.domain.EsCategoryBrandDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface CategoryBrandService {

    /**
     * 根据分类ID 获取品牌信息
     * @return
     */
    @GetMapping("/getCategoryList")
    DubboPageResult<EsBrandSelectDO> getCategoryList(@RequestParam("cateId") Long cateId);

    /**
     * 保存分类绑定的品牌
     * @param categoryId 分类ID
     * @param brandId 品牌ID
     * @return
     */
    @PostMapping("/saveCategoryBrand")
    DubboPageResult<EsCategoryBrandDO> saveCategoryBrand(@RequestParam("categoryId") Long categoryId, @RequestParam("brandId") Integer[] brandId);
}
