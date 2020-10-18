package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsCategoryDO;
import com.jjg.shop.model.dto.EsCategoryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-shop")
public interface CategoryService {

    /**
     * 根据主键ID获取 分类下面所有的子类
     * @param id 主键ID
     * @return
     */
@GetMapping("/getCategoryParentList")
    DubboPageResult<EsCategoryDO> getCategoryParentList(@RequestParam("id") Long id);

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param categoryDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    @PostMapping("/insertCategory")
    DubboResult<EsCategoryDO> insertCategory(@RequestBody EsCategoryDTO categoryDTO);


    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param categoryDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    @PostMapping("/updateCategory")
    DubboResult<EsCategoryDO> updateCategory(@RequestBody EsCategoryDTO categoryDTO, @RequestParam("id") Long id);


    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    @DeleteMapping("/deleteCategory")
    DubboResult<EsCategoryDO> deleteCategory(@RequestParam("id") Long id);

    /**
     * 查询所有分类 父子关系
     * @param id
     * @return
     */
    @GetMapping("/getCategoryChildren")
    DubboPageResult<EsCategoryDO> getCategoryChildren(@RequestParam("id") Long id);

}
