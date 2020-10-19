package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.dto.EsBillDTO;
import com.jjg.trade.model.dto.EsBillDetailDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-online")
public interface BillService {

    /**
     * 根据查询条件查询账单列表
     *
     * @param billDTO  账单DTO
     * @param pageSize 行数
     * @param pageNum  页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsBillDO>
     */
    @GetMapping("/getBillList")
    DubboPageResult getBillList(@RequestBody EsBillDTO billDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
    /**
     * 获取账单详情（结算单列表）
     * @author: libw 981087977@qq.com
     * @date: 2019/08/17 11:01:54
     * @param billDetailDTO  账单DTO
     * @return: com.shopx.common.model.result.DubboResult
     */
    @GetMapping("/getBillDetail")
    DubboPageResult getBillDetail(@RequestBody EsBillDetailDTO billDetailDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 获取结算单头部信息
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:21:41
     * @return: com.shopx.common.model.result.DubboResult
     */
    @GetMapping("/getSettlementInfo")
    DubboResult getSettlementInfo(@RequestParam("settlementId") Long settlementId,@RequestParam("type") int type);

    /**
     * 获取结算单总订单明细
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @param pageSize 行数
     * @param pageNum  页码
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:25:19
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    @GetMapping("/getSettlementOrderDetail")
    DubboPageResult getSettlementOrderDetail(@RequestParam("settlementId") Long settlementId,@RequestParam("type") int type,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
    /**
     * 获取结算单退款订单明细
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @param pageSize 行数
     * @param pageNum  页码
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:25:19
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    @GetMapping("/getSettlementRefundOrderDetail")
    DubboPageResult getSettlementRefundOrderDetail(@RequestParam("settlementId") Long settlementId,@RequestParam("type") int type, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 更改结算状态
     *
     * @param settlementId  结算单id
     * @param type          结算单类型
     * @param status        结算状态
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    @PostMapping("/updateStatus")
    DubboResult updateStatus(@RequestParam("settlementId") Long settlementId,@RequestParam("type") int type,@RequestParam("status") int status);
    /**
     * 导出EXCEL
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    @PostMapping("/exportExcel")
    DubboResult exportExcel(@RequestParam("settlementId") Long settlementId,@RequestParam("type") int type);


}
