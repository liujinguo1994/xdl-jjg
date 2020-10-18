package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsDiscountDO;
import com.jjg.member.model.dto.EsDiscountDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface DiscountService {

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 13:42:53
     * @param discountDTO  公司折扣表DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsDiscountDO>
     */
    @GetMapping("/getDiscountList")
    DubboPageResult<EsDiscountDO> getDiscountList(@RequestBody EsDiscountDTO discountDTO, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 15:39:30
     * @param discountDTO    公司折扣表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @PostMapping("/insertDiscount")
    DubboResult insertDiscount(@RequestBody EsDiscountDTO discountDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:10
     * @param discountDTO    公司折扣表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @PostMapping("/updateDiscount")
    DubboResult updateDiscount(@RequestBody EsDiscountDTO discountDTO);

    /**
     * 批量删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:44
     * @param ids    主键ids
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @DeleteMapping("/deleteDiscount")
    DubboResult deleteDiscount(@RequestParam("ids") Integer[] ids);
}
