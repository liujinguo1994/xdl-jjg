package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsPaymentBill;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员支付帐单-es_payment_bill Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsPaymentBillMapper extends BaseMapper<EsPaymentBill> {

    EsPaymentBill selectEsPaymentBill(@Param("outTradeNo") String outTradeNo);

    EsPaymentBill selectByReturnTradeNo(@Param("returnTradeNo") String returnTradeNo);

    Integer selectTradeCount(@Param("outTradeNo") String outTradeNo);

}
