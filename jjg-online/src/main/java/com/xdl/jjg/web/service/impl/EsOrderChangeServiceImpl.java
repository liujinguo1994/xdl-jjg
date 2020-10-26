package com.xdl.jjg.web.service.impl;


import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.dto.EsOrderDTO;
import com.jjg.trade.model.dto.EsTradeDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsOrderChangeService;
import com.xdl.jjg.web.service.IEsOrderService;
import com.xdl.jjg.web.service.IEsTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 订单状态变化 接口实现
 * Description: zhuox-shop-trade
 * <p>
 * Created by LIUJG on 2019/6/29 10:39
 */
@Service(version = "${dubbo.application.version}",interfaceClass = IEsOrderChangeService.class,timeout = 5000)
public class EsOrderChangeServiceImpl implements IEsOrderChangeService {
    private static Logger logger = LoggerFactory.getLogger(EsOrderChangeServiceImpl.class);
    @Autowired
    private IEsOrderService iEsOrderService;

    @Autowired
    private IEsTradeService iEsTradeService;

    /**
     *修改订单状态
     * @param value
     * @param
     * @param  tradeSn 交易编号
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult updateOrderStatus(String value, String tradeSn) {
        try {
            //  订单减库存成功，对订单明细表进行修改订单状态
            EsOrderDTO esOrder = new EsOrderDTO();
            esOrder.setTradeSn(tradeSn);
            esOrder.setOrderState(value);
            DubboResult<EsOrderDO> result1 = iEsOrderService.updateOrderMessage(esOrder);
            if (!result1.isSuccess()){
                throw new ArgumentException(TradeErrorCode.ORDER_STATUS_CHANGE_ERROR.getErrorCode(), TradeErrorCode.ORDER_STATUS_CHANGE_ERROR.getErrorMsg());
            }
            //  订单减库存成功，对交易表进行修改交易状态
            EsTradeDTO esTradeDTO = new EsTradeDTO();
            esTradeDTO.setTradeSn(tradeSn);
            esTradeDTO.setTradeStatus(value);
            iEsTradeService.updateTradeMessage(esTradeDTO);
            return DubboResult.success();
        } catch (ArgumentException e) {
            logger.error("订单状态修改失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("订单状态修改失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
