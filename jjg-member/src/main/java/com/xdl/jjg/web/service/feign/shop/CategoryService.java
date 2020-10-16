package com.xdl.jjg.web.service.feign.shop;


import com.jjg.shop.model.domain.EsCategoryDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "jjg-shop")
public interface CategoryService {

    /**
     *获取分类信息
     */
    @GetMapping("/getCategoryById")
    DubboResult<EsCategoryDO> getCategory(Long id);


    /**
     * 根据主键ID获取 分类下面所有的子类
     * @param id 主键ID
     * @return
     */
    @GetMapping("/getCategoryParentListById")
    DubboPageResult<EsCategoryDO> getCategoryParentList(Long id);

    /**
     * 根据分类ID集合获取分类信息
     * @param cateIds
     * @return
     */
    @GetMapping("/getCategoryByIdsByCateIds")
    DubboPageResult<EsCategoryDO> getCategoryByIds(List<Long> cateIds);
}
