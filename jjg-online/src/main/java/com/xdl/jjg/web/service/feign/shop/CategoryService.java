package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsBuyerCategoryDO;
import com.jjg.shop.model.domain.EsCategoryDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface CategoryService {

    /**
     * 所有的一级分类
     * @return
     */
    @GetMapping("/getFirstByName")
    DubboResult<EsCategoryDO> getFirstByName(@RequestParam("name") String name);

    /**
     * 查询所有分类 父子关系
     * @param id
     * @return
     */
    @GetMapping("/getCategoryChildren")
    DubboPageResult<EsCategoryDO> getCategoryChildren(@RequestParam("id") Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    @GetMapping("/getCategory")
    DubboResult<EsCategoryDO> getCategory(@RequestParam("id") Long id);


    /**
     * 根据主键ID获取 分类下面所有的子类
     * @param id 主键ID
     * @return
     */
    @GetMapping("/getCategoryParentList")
    DubboPageResult<EsCategoryDO> getCategoryParentList(@RequestParam("id") Long id);


    /**
     * 所有的一级分类
     * @return
     */
    @GetMapping("/getFirstBrandList")
    DubboPageResult<EsCategoryDO> getFirstBrandList();

    /**
     * 首页分类
     * @param id
     * @return
     */
    @GetMapping("/getBuyCategoryChildren")
    DubboPageResult<EsBuyerCategoryDO> getBuyCategoryChildren(@RequestParam("id") Long id);

}
