package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsShopAndDetailDO;
import com.jjg.member.model.domain.EsShopDO;
import com.jjg.member.model.dto.EsShopAndDetailDTO;
import com.jjg.member.model.dto.ShopQueryParam;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface ShopService {

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/03 13:42:53
     * @param shopQueryParam  店铺查询参数
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopDO>
     */
    @GetMapping("/getShopList")
    DubboPageResult<EsShopDO> getShopList(@RequestBody ShopQueryParam shopQueryParam, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
    /**
     * 根据id获取店铺及店铺明细
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    @GetMapping("/getShopDetail")
    DubboResult<EsShopAndDetailDO> getShopDetail(@RequestParam("id") Long id);
    /**
     * 后台店铺关闭
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:10
     * @param id 店铺主键
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @PostMapping("/underShop")
    DubboResult underShop(@RequestParam("id") long id);

    /**
     * 后台店铺开启
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:10
     * @param id 店铺主键
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @PostMapping("/useShop")
    DubboResult useShop(@RequestParam("id") long id);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 15:58:10
     * @param esShopAndDetailDTO    店铺及详情
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    @PostMapping("/updateShop")
    DubboResult updateShop(@RequestBody EsShopAndDetailDTO esShopAndDetailDTO);


}
