package com.xdl.jjg.web.service;


import com.jjg.trade.model.dto.EsBillDTO;
import com.jjg.trade.model.dto.EsBillDetailDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 账单服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsBillService {

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
    DubboPageResult getBillList(EsBillDTO billDTO, int pageSize, int pageNum);

    /**
     * 结算单汇总
     * @author: libw 981087977@qq.com
     * @date: 2019/08/17 11:01:54
     * @param type  结算单类型
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult summaryOfStatement(int type);

    /**
     * 获取账单详情（结算单列表）
     * @author: libw 981087977@qq.com
     * @date: 2019/08/17 11:01:54
     * @param billDetailDTO  账单DTO
     * @return: com.shopx.common.model.result.DubboResult
     */
        DubboPageResult getBillDetail(EsBillDetailDTO billDetailDTO, int pageSize, int pageNum);

    /**
     * 获取结算单头部信息
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:21:41
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult getSettlementInfo(Long settlementId, int type);

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
    DubboPageResult getSettlementOrderDetail(Long settlementId, int type, int pageSize, int pageNum);

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
    DubboPageResult getSettlementRefundOrderDetail(Long settlementId, int type, int pageSize, int pageNum);

    /**
     * 导出EXCEL
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult exportExcel(Long settlementId, int type);

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
    DubboResult updateStatus(Long settlementId, int type, int status);
}
