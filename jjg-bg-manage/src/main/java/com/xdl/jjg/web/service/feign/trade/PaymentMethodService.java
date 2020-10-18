package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.dto.EsPaymentMethodDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-online")
public interface PaymentMethodService {

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param paymentMethodDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsPaymentMethodDO>
     */
    @GetMapping("/getPaymentMethodList")
    DubboPageResult getPaymentMethodList(@RequestBody EsPaymentMethodDTO paymentMethodDTO, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
    /**
     * 根据支付插件ID获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param pluginId    支付插件ID
     * @return: com.shopx.common.model.result.DubboResult<PaymentPluginVO>
     */
    @GetMapping("/getPaymentMethod")
    DubboResult getPaymentMethod(@RequestParam("pluginId") String pluginId);
    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param paymentMethod     插件方法
     * @param paymentPluginId   插件id
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentMethodDO>
     */
    @PostMapping("/updatePaymentMethod")
    DubboResult updatePaymentMethod(@RequestBody EsPaymentMethodDTO paymentMethod,@RequestParam("paymentPluginId") String paymentPluginId);
}
