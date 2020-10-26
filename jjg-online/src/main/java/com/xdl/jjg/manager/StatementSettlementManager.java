package com.xdl.jjg.manager;


import com.jjg.system.model.vo.EsClearingCycleVO;
import com.jjg.trade.model.domain.EsHeaderDO;
import com.jjg.trade.model.domain.EsSettlement;
import com.jjg.trade.model.domain.ExcelDO;
import com.jjg.trade.model.dto.EsBillDetailDTO;

/**
 * @ClassName: StatementSettlementManager
 * @Description: 结算单
 * @Author: libw  981087977@qq.com
 * @Date: 2019/8/19 16:18
 * @Version: 1.0
 */
public interface StatementSettlementManager {

    

    /**
     * 订单结算
     *
     * @param clearingCycleVO 结算周期配置类
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 15:20:55
     * @return: void
     */
    void settlement(EsClearingCycleVO clearingCycleVO);

    /**
     * 根据账单编号查询结算单
     * @author: libw 981087977@qq.com
     * @date: 2019/08/30 14:08:07
     * @param billDetailDTO 结算账单参数
     * @param pageSize      行数
     * @param pageNum       页码
     * @return: java.util.List<com.shopx.trade.api.model.domain.EsBillDetailDO>
     */
    EsSettlement getBillDetail(EsBillDetailDTO billDetailDTO, int pageSize, int pageNum);

    /**
     * 获取结算单详情
     *
     * @param settlementId 结算单id
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:21:41
     * @return: com.shopx.common.model.result.DubboResult
     */
    EsHeaderDO getSettlementInfo(Long settlementId);

    /**
     * 获取结算单总订单明细
     *
     * @param settlementId 结算单id
     * @param pageSize      行数
     * @param pageNum       页码
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:25:19
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    EsSettlement getSettlementOrderDetail(Long settlementId, int pageSize, int pageNum);

    /**
     * 获取结算单退款订单金额明细
     *
     * @param settlementId 结算单id
     * @param pageSize      行数
     * @param pageNum       页码
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:25:19
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    EsSettlement getSettlementRefundOrderDetail(Long settlementId, int pageSize, int pageNum);

    /**
     * 导出EXCEL
     *
     * @param settlementId 结算单id
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    ExcelDO exportExcel(Long settlementId);

    /**
     * 更改结算状态
     *
     * @param settlementId  结算单id
     * @param status        结算状态
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    void updateStatus(Long settlementId, int status);
}