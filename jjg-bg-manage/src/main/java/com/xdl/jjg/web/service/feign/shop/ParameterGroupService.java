package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsParameterGroupDO;
import com.jjg.shop.model.domain.ParameterGroupDO;
import com.jjg.shop.model.dto.EsParameterGroupDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-shop")
public interface ParameterGroupService {


    /**
     * 根据商品分类ID 获取参数组 参数
     * @param categoryId 商品分类ID
     * @return
     */
    @GetMapping("/adminGoodsTotalStatistics")
    DubboPageResult<ParameterGroupDO> getParameterGroupList(@RequestParam("categoryId") Long categoryId);

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param parameterGroupDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    @PostMapping("/insertParameterGroup")
    DubboResult<EsParameterGroupDO> insertParameterGroup(@RequestBody EsParameterGroupDTO parameterGroupDTO);

    /**
     *  修改数据
     * @param esParameterGroupDTO
     * @param id
     * @return
     */
    @PostMapping("/updateParameterGroup")
    DubboResult<EsParameterGroupDO> updateParameterGroup(@RequestBody EsParameterGroupDTO esParameterGroupDTO, @RequestParam("id") Long id);
    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    @DeleteMapping("/deleteParameterGroup")
    DubboResult<EsParameterGroupDO> deleteParameterGroup(@RequestParam("id") Long id);

    /**
     *
     * @param groupId 参数组ID
     * @param sortType 移动类型
     * @return
     */
    @GetMapping("/sortParameterGroup")
    DubboResult<EsParameterGroupDO> sortParameterGroup(@RequestParam("groupId") Long groupId,@RequestParam("sortType") String sortType);

}
