package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsBrandShowDO;
import com.jjg.shop.model.dto.EsBrandShowDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-shop")
public interface BrandShowService {

    /**
     * 插入数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @param brandShowDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    @PostMapping("/insertBrandShow")
    DubboResult insertBrandShow(@RequestBody EsBrandShowDTO brandShowDTO);

    /**
     * 根据条件更新更新数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param brandShowDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    @PostMapping("/updateBrandShow")
    DubboResult updateBrandShow(@RequestBody EsBrandShowDTO brandShowDTO, @RequestParam("id") Long id);

    /**
     * 根据查询条件查询列表
     * @auther: WAF 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @param brandShowDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsBrandShowDO>
     */
    @GetMapping("/getBrandShowList")
    DubboPageResult<EsBrandShowDO> getBrandShowList(@RequestBody EsBrandShowDTO brandShowDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);

    /**
     * 根据主键删除数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    @DeleteMapping("/deleteBrandShow")
    DubboResult deleteBrandShow(@RequestParam("id") Long id);

}
