package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsPaymentBillDO;
import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.dto.EsPaymentBillDTO;
import com.jjg.trade.model.enums.OrderPermission;
import com.jjg.trade.model.enums.TradeType;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsOrder;
import com.xdl.jjg.entity.EsPaymentBill;
import com.xdl.jjg.mapper.EsOrderMapper;
import com.xdl.jjg.mapper.EsPaymentBillMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.web.service.IEsOrderOperateService;
import com.xdl.jjg.web.service.IEsOrderService;
import com.xdl.jjg.web.service.IEsPaymentBillService;
import com.xdl.jjg.web.service.IEsTradeService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员支付帐单-es_payment_bill 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsPaymentBillService.class, timeout = 50000)
public class EsPaymentBillServiceImpl extends ServiceImpl<EsPaymentBillMapper, EsPaymentBill> implements IEsPaymentBillService {

    private static Logger logger = LoggerFactory.getLogger(EsPaymentBillServiceImpl.class);

    @Autowired
    private EsPaymentBillMapper paymentBillMapper;

    @Autowired
    private EsOrderMapper esOrderMapper;

    @Autowired
    private IEsOrderService iEsOrderService;

    @Autowired
    private IEsTradeService iEsTradeService;

    @Autowired
    private IEsOrderOperateService iEsOrderOperateService;

    /**
     * 插入会员支付帐单-es_payment_bill数据
     *
     * @param paymentBillDTO 会员支付帐单-es_payment_billDTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentBillDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertPaymentBill(EsPaymentBillDTO paymentBillDTO) {
        try {
            if (paymentBillDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            // 先查找该订单是否已经生成订单流水 如果已经生成 则删除在插入
            QueryWrapper<EsPaymentBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPaymentBill::getOutTradeNo,paymentBillDTO.getOutTradeNo());
            queryWrapper.lambda().eq(EsPaymentBill::getOrderSn,paymentBillDTO.getOrderSn());
            EsPaymentBill esPaymentBill = this.paymentBillMapper.selectOne(queryWrapper);
            if (esPaymentBill != null){
                this.paymentBillMapper.delete(queryWrapper);
            }
            EsPaymentBill paymentBill = new EsPaymentBill();
            BeanUtil.copyProperties(paymentBillDTO, paymentBill);
            this.paymentBillMapper.insert(paymentBill);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("会员支付帐单-es_payment_bill失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员支付帐单-es_payment_bill数据
     *
     * @param paymentBillDTO 会员支付帐单-es_payment_billDTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentBillDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updatePaymentBill(EsPaymentBillDTO paymentBillDTO) {
        try {
            if (paymentBillDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsPaymentBill paymentBill = new EsPaymentBill();
            BeanUtil.copyProperties(paymentBillDTO, paymentBill);
            QueryWrapper<EsPaymentBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPaymentBill::getId, paymentBillDTO.getId());
            this.paymentBillMapper.update(paymentBill, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("会员支付帐单-es_payment_bill更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员支付帐单-es_payment_bill详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentBillDO>
     */
    @Override
    public DubboResult getPaymentBill(Long id) {
        try {
            QueryWrapper<EsPaymentBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPaymentBill::getId, id);
            EsPaymentBill paymentBill = this.paymentBillMapper.selectOne(queryWrapper);
            EsPaymentBillDO paymentBillDO = new EsPaymentBillDO();
            if (paymentBill == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(paymentBill, paymentBillDO);
            return DubboResult.success(paymentBillDO);
        } catch (Throwable th) {
            logger.error("会员支付帐单-es_payment_bill查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员支付帐单-es_payment_bill列表
     *
     * @param paymentBillDTO 会员支付帐单-es_payment_billDTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsPaymentBillDO>
     */
    @Override
    public DubboPageResult getPaymentBillList(EsPaymentBillDTO paymentBillDTO, int pageSize, int pageNum) {
        QueryWrapper<EsPaymentBill> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsPaymentBill> page = new Page<>(pageNum, pageSize);
            IPage<EsPaymentBill> iPage = this.page(page, queryWrapper);
            List<EsPaymentBillDO> paymentBillDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                paymentBillDOList = iPage.getRecords().stream().map(paymentBill -> {
                    EsPaymentBillDO paymentBillDO = new EsPaymentBillDO();
                    BeanUtil.copyProperties(paymentBill, paymentBillDO);
                    return paymentBillDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(paymentBillDOList);
        } catch (Throwable th) {
            logger.error("会员支付帐单-es_payment_bill查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员支付帐单-es_payment_bill数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentBillDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deletePaymentBill(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsPaymentBill> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsPaymentBill::getId, id);
            this.paymentBillMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("会员支付帐单-es_payment_bill查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 使用第三方交易号查询支付流水
     *
     * @param returnTradeNo 返回的第三方交易号
     * @author: libw 981087977@qq.com
     * @date: 2019/07/29 13:43:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult<EsPaymentBillDO> getBillByReturnTradeNo(String returnTradeNo) {
        logger.error("交易号:"+ returnTradeNo);

        EsPaymentBill esPaymentBill = this.paymentBillMapper.selectByReturnTradeNo(returnTradeNo);
        logger.error("付款单"+ esPaymentBill);
        EsPaymentBillDO esPaymentBillDO = new EsPaymentBillDO();
        BeanUtils.copyProperties(esPaymentBill,esPaymentBillDO);
        return DubboResult.success(esPaymentBillDO);

    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult pay(String outTradeNo, String returnTradeNo, TradeType tradeType, Double payPrice) {

        EsPaymentBill esPaymentBill = this.paymentBillMapper.selectEsPaymentBill(outTradeNo);
        //更新流水中的动态
        esPaymentBill.setReturnTradeNo(returnTradeNo);
        esPaymentBill.setIsPay(1);
        this.paymentBillMapper.updateById(esPaymentBill);

        //交易支付
        if(tradeType.equals(TradeType.trade)){
            //修改订单交易号
            EsOrder esOrder = new EsOrder();
            esOrder.setPayOrderNo(returnTradeNo);
            QueryWrapper<EsOrder> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsOrder::getTradeSn,esPaymentBill.getOrderSn());
            this.esOrderMapper.update(esOrder,queryWrapper1);

            //更新订单的支付状态

            DubboResult<EsTradeDO> esTradeInfo = iEsTradeService.getEsTradeInfo(outTradeNo);
            // 第三方支付金额
            Double payMoney = esTradeInfo.getData().getPayMoney();


            DubboPageResult<EsOrderDO> esOrderInfoByTradeSn = iEsOrderService.getEsOrderInfoByTradeSn(esPaymentBill.getOrderSn());
            List<EsOrderDO> esOrderDOList = esOrderInfoByTradeSn.getData().getList();
            //判断交易的金额是否正确
            if(payMoney.longValue() != payPrice.longValue()){
                logger.error("payMoney:"+payMoney+"---payPrice："+payPrice);
                throw new ArgumentException(TradeErrorCode.MONEY_DIFFERENT.getErrorCode(),TradeErrorCode.MONEY_DIFFERENT.getErrorMsg());
            }
            for(EsOrderDO esOrderDO : esOrderDOList){
                iEsOrderOperateService.payOrder(esOrderDO.getOrderSn(),payPrice, OrderPermission.client);
            }
            return DubboResult.success();
        }
        //订单支付
        EsOrder esOrder = new EsOrder();
        esOrder.setPayOrderNo(returnTradeNo);
        esOrder.setOrderSn(esPaymentBill.getOrderSn());
        QueryWrapper<EsOrder> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(EsOrder::getOrderSn,esPaymentBill.getOrderSn());
        this.esOrderMapper.update(esOrder,queryWrapper1);

        DubboResult<EsOrderDO> esBuyerOrderInfo = this.iEsOrderService.getEsBuyerOrderInfo(esPaymentBill.getOrderSn());
        if (!esBuyerOrderInfo.isSuccess()){
            throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(),
                    TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
        }
        EsOrderDO orderDO = esBuyerOrderInfo.getData();

        if(!orderDO.getOrderMoney().equals(MathUtil.add(payPrice,orderDO.getPayedMoney()))){
            throw new ArgumentException(TradeErrorCode.MONEY_DIFFERENT.getErrorCode(),
                    TradeErrorCode.MONEY_DIFFERENT.getErrorMsg());
        }
        iEsOrderOperateService.payOrder(orderDO.getOrderSn(),payPrice, OrderPermission.client);
        return DubboResult.success();
    }

    @Override
    public DubboResult<Integer> getCount(String outTradeNo) {
        try {
            Integer count = this.paymentBillMapper.selectTradeCount(outTradeNo);
            return DubboResult.success(count);
        }catch (Exception th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
    }

    }
}
