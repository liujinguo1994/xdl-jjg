package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.domain.EsDeliveryServiceDO;
import com.jjg.trade.model.dto.EsDeliveryServiceDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-online")
public interface DeliveryServiceService {

    /**
     *系统后台
     *  自提点新增
     * 插入自提点信息维护数据
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    @PostMapping("/insertDeliveryService")
    DubboResult insertDeliveryService(@RequestBody EsDeliveryServiceDTO deliveryServiceDTO);
    /**
     * 系统后台
     * 根据条件更新更新数据
     *
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @param id                 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    @PostMapping("/updateDeliveryService")
    DubboResult updateDeliveryService(@RequestBody EsDeliveryServiceDTO deliveryServiceDTO, @RequestParam("id") Long id);

    /**
     * 系统后台
     * 根据查询自提点信息维护列表
     *
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @param pageSize           行数
     * @param pageNum            页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsDeliveryServiceDO>
     */
    @GetMapping("/getDeliveryServiceList")
    DubboPageResult<EsDeliveryServiceDO> getDeliveryServiceList(@RequestBody EsDeliveryServiceDTO deliveryServiceDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 系统后台
     *  根据主键删除自提点信息维护数据
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    @DeleteMapping("/deleteDeliveryService")
    DubboResult deleteDeliveryService(@RequestParam("id") Long id);

    /**
     *系统后台
     * 根据id获取自提点信息维护详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    @GetMapping("/getDeliveryService")
    DubboResult<EsDeliveryServiceDO> getDeliveryService(@RequestParam("id") Long id);

}
