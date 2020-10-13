package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsPaymentBillDO;
import com.shopx.trade.api.model.domain.dto.EsPaymentBillDTO;
import com.shopx.trade.api.model.enums.TradeType;

/**
 * <p>
 * 会员支付帐单-es_payment_bill 服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsPaymentBillService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param paymentBillDTO    会员支付帐单-es_payment_billDTO
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentBillDO>
     */
    DubboResult insertPaymentBill(EsPaymentBillDTO paymentBillDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param paymentBillDTO    会员支付帐单-es_payment_billDTO
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentBillDO>
     */
    DubboResult updatePaymentBill(EsPaymentBillDTO paymentBillDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentBillDO>
     */
    DubboResult getPaymentBill(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param paymentBillDTO  会员支付帐单-es_payment_billDTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsPaymentBillDO>
     */
    DubboPageResult getPaymentBillList(EsPaymentBillDTO paymentBillDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentBillDO>
     */
    DubboResult deletePaymentBill(Long id);

    /**
     * 使用第三方交易号查询支付流水
     * @author: libw 981087977@qq.com
     * @date: 2019/07/29 13:43:58
     * @param returnTradeNo 返回的第三方交易号
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult<EsPaymentBillDO> getBillByReturnTradeNo(String returnTradeNo);

    /**
     * 支付成功调用
     * @param outTradeNo
     * @param returnTradeNo
     * @param tradeType
     * @param payPrice
     */
    DubboResult pay(String outTradeNo, String returnTradeNo, TradeType tradeType, Double payPrice);


    /**
     * 使用提交给第三方平台单号查询条数
     * @author: yuanj 595831329@qq.com
     * @date: 2020/05/06 17:03:58
     * @param outTradeNo 提交给第三方平台单号
     * @return:
     */
    DubboResult<Integer> getCount(String outTradeNo);
}
