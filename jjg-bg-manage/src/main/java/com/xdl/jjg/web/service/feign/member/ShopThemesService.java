package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsShopThemesDO;
import com.jjg.member.model.dto.EsShopThemesDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface ShopThemesService {
    /**
     * 根据查询条件查询列表-后台
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 13:42:53
     * @param shopThemesDTO  店铺模版DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopThemesDO>
     */
    @GetMapping("/getShopThemesList")
    DubboPageResult<EsShopThemesDO> getShopThemesList(@RequestBody EsShopThemesDTO shopThemesDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 14:39:30
     * @param shopThemesDTO    店铺模版DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    @PostMapping("/insertShopThemes")
    DubboResult insertShopThemes(@RequestBody EsShopThemesDTO shopThemesDTO);
    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 13:10:10
     * @param shopThemesDTO    店铺模版DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    @PostMapping("/updateShopThemes")
    DubboResult updateShopThemes(@RequestBody EsShopThemesDTO shopThemesDTO, @RequestParam("id") Long id);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-07-01 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    @DeleteMapping("/deleteShopThemes")
    DubboResult deleteShopThemes(@RequestParam("id") Long id);

}
