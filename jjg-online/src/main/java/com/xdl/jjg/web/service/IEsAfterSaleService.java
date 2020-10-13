package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsAfterSaleDO;
import com.shopx.trade.api.model.domain.EsServiceOrderDO;
import com.shopx.trade.api.model.domain.dto.BuyerRefundApplyDTO;
import com.shopx.trade.api.model.domain.dto.EsAfterSaleDTO;
import com.shopx.trade.api.model.domain.dto.SellerRefundApprovalDTO;
import com.shopx.trade.api.model.domain.vo.RefundApplyVO;

/**
 * <p>
 * 售后维护配置 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsAfterSaleService {

    /**
     * 申请退款
     * @param refundApply 退款申请
     */
    DubboResult<EsServiceOrderDO> applyRefund(BuyerRefundApplyDTO refundApply);
    /**
     * 申请退款
     */
    DubboResult<EsServiceOrderDO> cancelOrder(BuyerRefundApplyDTO refundApplyDTO);

    /**
     * 获取退款申请信息
     * @param orderSn
     * @param skuId
     * @return
     */
    RefundApplyVO refundApply(String orderSn, Integer skuId);

    /**
     * 获取配置信息
     *
     * @param afterSaleDTO 售后维护配置DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsAfterSaleDO>
     */
    DubboResult insertAfterSale(EsAfterSaleDTO afterSaleDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param afterSaleDTO 售后维护配置DTO
     * @param id           主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsAfterSaleDO>
     */
    DubboResult updateAfterSale(EsAfterSaleDTO afterSaleDTO, Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAfterSaleDO>
     */
    DubboResult<EsAfterSaleDO> getAfterSale(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param afterSaleDTO 售后维护配置DTO
     * @param pageSize     行数
     * @param pageNum      页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsAfterSaleDO>
     */
    DubboPageResult<EsAfterSaleDO> getAfterSaleList(EsAfterSaleDTO afterSaleDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsAfterSaleDO>
     */
    DubboResult deleteAfterSale(Long id);


    DubboResult approval(SellerRefundApprovalDTO refundApproval);
}
