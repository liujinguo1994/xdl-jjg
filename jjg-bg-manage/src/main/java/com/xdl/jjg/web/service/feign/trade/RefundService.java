package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.dto.EsReFundQueryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-online")
public interface RefundService {

    /**
     * 后台管理 卖家端 通用
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/08/10 13:42:53
     * @param esReFundQueryDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundDO>
     */
    @GetMapping("/getRefundList")
    DubboPageResult getRefundList(@RequestBody EsReFundQueryDTO esReFundQueryDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
    /**
     *  系统后台
     * 根据Sn获取退换货数据信息
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/05 09:40
     * @param sn
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    @GetMapping("/getAdminRefundBySn")
    DubboResult getAdminRefundBySn(@RequestParam("sn") String sn);





}
