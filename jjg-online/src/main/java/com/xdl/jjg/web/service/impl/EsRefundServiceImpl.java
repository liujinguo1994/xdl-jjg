package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.dto.EsMemberDTO;
import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.EsOrderDTO;
import com.jjg.trade.model.dto.EsReFundQueryDTO;
import com.jjg.trade.model.dto.EsRefundDTO;
import com.jjg.trade.model.dto.EsWapAfterSaleRecordQueryDTO;
import com.jjg.trade.model.enums.*;
import com.jjg.trade.model.vo.EsCouponVO;
import com.jjg.trade.model.vo.EsWapRefundCountVO;
import com.xdl.jjg.constant.RefundBuyerOperateChecker;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constants.AfterSaleOperateAllowable;
import com.xdl.jjg.entity.EsRefund;
import com.xdl.jjg.entity.EsRefundGoods;
import com.xdl.jjg.mapper.EsOrderMapper;
import com.xdl.jjg.mapper.EsRefundGoodsMapper;
import com.xdl.jjg.mapper.EsRefundMapper;
import com.xdl.jjg.mapper.EsTradeMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.web.service.*;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsRefundService.class, timeout = 50000)
public class EsRefundServiceImpl extends ServiceImpl<EsRefundMapper, EsRefund> implements IEsRefundService {

    private static Logger logger = LoggerFactory.getLogger(EsRefundServiceImpl.class);

    @Autowired
    private EsRefundMapper refundMapper;

    @Autowired
    private IEsRefundGoodsService iEsRefundGoodsService;

    @Autowired
    private IEsRefundService iEsRefundService;

    @Autowired
    private IEsOrderService iEsOrderService;

    @Autowired
    private IEsOrderItemsService iEsOrderItemsService;

    @Autowired
    private IEsFullDiscountService iEsFullDiscountService;

    @Autowired
    private IEsCouponService iEsCouponService;

    @Autowired
    private EsRefundGoodsMapper refundGoodsMapper;
    @Autowired
    private EsOrderMapper orderMapper;
    @Autowired
    private EsTradeMapper esTradeMapper;

    @Value("${black.card.start}")
    private Long blackCardStart;
    @Value("${black.card.end}")
    private Long blackCardEnd;
    @Value("${black.card.money}")
    private Double money;
    @Value("${black.card.discount}")
    private Double discount;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    /**
     * 插入数据
     * @param refundDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertRefund(EsRefundDTO refundDTO) {
        try {
            if (refundDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsRefund refund = new EsRefund();
            BeanUtil.copyProperties(refundDTO, refund);
            this.refundMapper.insert(refund);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param refundDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateRefund(EsRefundDTO refundDTO) {
        try {
            if (refundDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            long timeMillis = System.currentTimeMillis();
            if (blackCardStart < timeMillis && timeMillis < blackCardEnd) {
                // 统计订单中已经完成的订单
                Double blackCardMoney = esTradeMapper.getBlackCardUserMessage(blackCardEnd, blackCardStart, refundDTO.getMemberId());
                // 如果完成的订单总金额没有达到黑卡标准
                if (blackCardMoney < money) {
                    // 黑卡标识收回
                    // 更改用户的用户性质 =黑卡
                    DubboResult<EsMemberDO> memberById = memberService.getMemberById(refundDTO.getMemberId());
                    if (memberById.isSuccess()){
                        EsMemberDTO esMemberDTO = new EsMemberDTO();
                        BeanUtil.copyProperties(memberById.getData(),esMemberDTO);
                        esMemberDTO.setBlackCard(2);
                        esMemberDTO.setBlackDiscount(1.0);
                        memberService.updateMemberInfo(esMemberDTO);
                    }
                }
            }

            EsRefund refund = new EsRefund();
            BeanUtil.copyProperties(refundDTO, refund);
            QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefund::getId, refundDTO.getId());
            this.refundMapper.update(refund, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    @Override
    public DubboResult getRefund(Long id) {
        try {
            QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefund::getId, id);
            EsRefund refund = this.refundMapper.selectOne(queryWrapper);
            EsRefundDO refundDO = new EsRefundDO();
            if (refund == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(refund, refundDO);
            return DubboResult.success(refundDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     * 买家端 售后商品列表
     * @param esReFundQueryDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundDO>
     */
    @Override
    public DubboPageResult<EsRefundDO> getServiceRefundList(EsReFundQueryDTO esReFundQueryDTO, int pageSize, int pageNum) {
        try {

            Page<EsRefund> page = new Page<>(pageNum, pageSize);
            IPage<EsRefund> iPage = this.refundMapper.selectRefundList(page, esReFundQueryDTO);
            List<EsRefundDO> refundDOList = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                refundDOList = iPage.getRecords().stream().map(refund -> {
                    EsRefundDO refundDO = new EsRefundDO();
                    BeanUtil.copyProperties(refund, refundDO);
                    // 获取该售后单中的商品List
                    QueryWrapper<EsRefundGoods> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(EsRefundGoods::getRefundSn,refund.getSn());
                    List<EsRefundGoods> esRefundGoods = this.refundGoodsMapper.selectList(wrapper);
                    List<EsRefundGoodsDO> esRefundGoodsDOS = BeanUtil.copyList(esRefundGoods, EsRefundGoodsDO.class);
                    // 赋值售后商品
                    refundDO.setEsRefundGoods(esRefundGoodsDOS);
                    // 设置权限
                    RefundTypeEnum type = RefundTypeEnum.valueOf(refund.getRefundType());
                    ProcessStatusEnum paymentType = ProcessStatusEnum.valueOf(refund.getProcessStatus());
                    AfterSaleOperateAllowable afterSaleOperateAllowable =  setAllowble(type,paymentType);
                    refundDO.setAfterSaleOperateAllowable(afterSaleOperateAllowable);
                    // 设置中文
                    refundDO.setRefundStatusText(RefundStatusEnum.valueOf(refund.getRefundStatus()).description());
                    refundDO.setRefuseTypeText(RefundTypeEnum.valueOf(refund.getRefuseType()).description());
                    refundDO.setProcessStatusText(ProcessStatusEnum.valueOf(refund.getProcessStatus()).description());

                    return refundDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),refundDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param esReFundQueryDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundDO>
     */
    @Override
    public DubboPageResult getRefundList(EsReFundQueryDTO esReFundQueryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
        try {

            // 查询条件
            // 维权类型" "
            if (!StringUtils.isBlank(esReFundQueryDTO.getRefuseType())){
                queryWrapper.lambda().eq(EsRefund::getRefuseType,esReFundQueryDTO.getRefuseType());
            }
            // 售后类型
            if (!StringUtils.isBlank(esReFundQueryDTO.getRefundType())){
                queryWrapper.lambda().eq(EsRefund::getRefundType,esReFundQueryDTO.getRefundType());
            }
            // 处理状态
            if (!StringUtils.isBlank(esReFundQueryDTO.getProcessStatus())){
                queryWrapper.lambda().eq(EsRefund::getProcessStatus,esReFundQueryDTO.getProcessStatus());
            }
            // 审核状态
            if (!StringUtils.isBlank(esReFundQueryDTO.getRefundStatus())){
                queryWrapper.lambda().eq(EsRefund::getRefundStatus,esReFundQueryDTO.getRefundStatus());
            }
            // 申请时间 开始时间
            if (esReFundQueryDTO.getCreateTimeStart() != null){
                queryWrapper.lambda().gt(EsRefund::getCreateTime,esReFundQueryDTO.getCreateTimeStart());
            }
            // 申请时间 结束时间
            if (esReFundQueryDTO.getCreateTimeEnd() != null){
                queryWrapper.lambda().lt(EsRefund::getCreateTime,esReFundQueryDTO.getCreateTimeEnd());
            }
            if (esReFundQueryDTO.getShopId() != null){
                // 店铺ID
                if (esReFundQueryDTO.getShopId() != 0){
                    queryWrapper.lambda().eq(EsRefund::getShopId,esReFundQueryDTO.getShopId());
                }
                // 维权单号
                if (!StringUtils.isBlank(esReFundQueryDTO.getSn())){
                    queryWrapper.lambda().eq(EsRefund::getSn,esReFundQueryDTO.getSn());
                }
                // 订单编号
                if (!StringUtils.isBlank(esReFundQueryDTO.getOrderSn())){
                    queryWrapper.lambda().eq(EsRefund::getOrderSn,esReFundQueryDTO.getOrderSn());
                }
            }else{
                // 维权单号,订单编号,卖家名称
                if (!StringUtils.isBlank(esReFundQueryDTO.getKeyword())){
                    queryWrapper.lambda().and(j->j.eq(EsRefund::getSn,esReFundQueryDTO.getKeyword())
                            .or(i->i.eq(EsRefund::getOrderSn,esReFundQueryDTO.getKeyword()))
                            .or(t->t.eq(EsRefund::getShopName,esReFundQueryDTO.getKeyword()))
                    );
                }
            }
            queryWrapper.lambda().orderByDesc(EsRefund::getCreateTime);
            Page<EsRefund> page = new Page<>(pageNum, pageSize);
            IPage<EsRefund> iPage = this.page(page, queryWrapper);
            List<EsRefundDO> refundDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                refundDOList = iPage.getRecords().stream().map(refund -> {
                    EsRefundDO refundDO = new EsRefundDO();
                    BeanUtil.copyProperties(refund, refundDO);
                    RefundTypeEnum type = RefundTypeEnum.valueOf(refund.getRefundType());
                    ProcessStatusEnum  paymentType = ProcessStatusEnum.valueOf(refund.getProcessStatus());
                    AfterSaleOperateAllowable afterSaleOperateAllowable =  setAllowble(type,paymentType);
                    refundDO.setAfterSaleOperateAllowable(afterSaleOperateAllowable);
                    refundDO.setRefundStatusText(RefundStatusEnum.valueOf(refund.getRefundStatus()).description());
                    refundDO.setRefuseTypeText(RefundTypeEnum.valueOf(refund.getRefuseType()).description());
                    refundDO.setProcessStatusText(ProcessStatusEnum.valueOf(refund.getProcessStatus()).description());
                    return refundDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),refundDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public AfterSaleOperateAllowable setAllowble( RefundTypeEnum type, ProcessStatusEnum  paymentType){
        AfterSaleOperateAllowable allowable = new AfterSaleOperateAllowable();
        boolean allowCancel = RefundBuyerOperateChecker.checkAllowable(type,paymentType, RefundOperateEnum.CANCEL);
        boolean allowApply = RefundBuyerOperateChecker.checkAllowable(type,paymentType, RefundOperateEnum.APPLY);
        boolean allowStockIn = RefundBuyerOperateChecker.checkAllowable(type,paymentType, RefundOperateEnum.STOCK_IN);
        boolean allowSellerApproval = RefundBuyerOperateChecker.checkAllowable(type,paymentType, RefundOperateEnum.SELLER_APPROVAL);
        boolean allowSellerRefund = RefundBuyerOperateChecker.checkAllowable(type,paymentType, RefundOperateEnum.SELLER_REFUND);
        boolean allowSellerDelivery = RefundBuyerOperateChecker.checkAllowable(type,paymentType, RefundOperateEnum.SELLER_DELIVERY);
        allowable.setAllowCancel(allowCancel);
        allowable.setAllowApply(allowApply);
        allowable.setAllowStockIn(allowStockIn);
        allowable.setAllowSellerApproval(allowSellerApproval);
        allowable.setAllowSellerRefund(allowSellerRefund);
        allowable.setAllowSellerDelivery(allowSellerDelivery);
        return allowable;
    }
    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteRefund(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsRefund> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsRefund::getId, id);
            this.refundMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult getRefundBySn(String sn, Long shopId) {
        try {
            if (sn == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入orderSn不能为空[%s]", sn));
            }
            QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefund::getSn, sn).eq(EsRefund::getShopId,shopId);
            EsRefund esRefund = this.refundMapper.selectOne(queryWrapper);
            EsRefundDO refundDO = new EsRefundDO();
            if (esRefund == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(esRefund, refundDO);
            refundDO.setRefundStatusText(RefundStatusEnum.valueOf(refundDO.getRefundStatus()).description());
            refundDO.setRefuseTypeText(RefundTypeEnum.valueOf(refundDO.getRefuseType()).description());
            refundDO.setProcessStatusText(ProcessStatusEnum.valueOf(refundDO.getProcessStatus()).description());
            return DubboResult.success(refundDO);
        } catch (ArgumentException ae) {
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsRefundDO> getRefundDetailBySn(String sn) {
        try {
            if (sn == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入orderSn不能为空[%s]", sn));
            }
            QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefund::getSn, sn);
            EsRefund esRefund = this.refundMapper.selectOne(queryWrapper);
            if (esRefund == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsRefundDO refundDO = new EsRefundDO();
            BeanUtil.copyProperties(esRefund, refundDO);
            // 获取退款单商品信息
            QueryWrapper<EsRefundGoods> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsRefundGoods::getRefundSn,sn);
            List<EsRefundGoods> esRefundGoods = this.refundGoodsMapper.selectList(wrapper);
            List<EsRefundGoodsDO> esRefundGoodsDOS = BeanUtil.copyList(esRefundGoods, EsRefundGoodsDO.class);
            refundDO.setEsRefundGoods(esRefundGoodsDOS);
            return DubboResult.success(refundDO);
        } catch (ArgumentException ae) {
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult getAdminRefundBySn(String sn) {
        try {
            if (sn == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入Sn不能为空[%s]", sn));
            }
            QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefund::getSn, sn);
            EsRefund esRefund = this.refundMapper.selectOne(queryWrapper);
            EsRefundDO refundDO = new EsRefundDO();
            if (esRefund == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(esRefund, refundDO);
            refundDO.setRefundStatusText(RefundStatusEnum.valueOf(esRefund.getRefundStatus()).description());
            refundDO.setRefuseTypeText(RefundTypeEnum.valueOf(esRefund.getRefuseType()).description());
            refundDO.setProcessStatusText(ProcessStatusEnum.valueOf(esRefund.getProcessStatus()).description());
            return DubboResult.success(refundDO);
        } catch (ArgumentException ae) {
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult getRefundPayMoneyByOrderSnAndStatus(String orderSn, Long shopId) {
        try {
            if (orderSn == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入orderSn不能为空[%s]", orderSn));
            }

            String[] status = {"PASS","REFUNDING","COMPLETED"};
            Double payMoney = this.refundMapper.selectRefundPayMoney(orderSn,shopId,status);
            if (payMoney == null) {
                throw new ArgumentException(TradeErrorCode.REFUND_NOT_EXIST.getErrorCode(),TradeErrorCode.REFUND_NOT_EXIST.getErrorMsg());
            }
            return DubboResult.success(payMoney.doubleValue());
        } catch (ArgumentException e) {
            logger.error("查询失败", e);
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult operationServiceExpandMessage(Map<String, Object> afterMap) {
        try {
            String refundSn = (String)afterMap.get("refundSn");
            String orderSn = (String)afterMap.get("orderSn");
            // 通过退款单编号查询 退款单表信息
            DubboResult adminRefundBySn = iEsRefundService.getAdminRefundBySn(refundSn);
            EsRefundDO esRefundDO= (EsRefundDO)adminRefundBySn.getData();
            // 退款的金额
            Double refundMoney = esRefundDO.getRefundMoney();

            // 通过退款单编号 查询出所有申请售后的商品ID，
            DubboPageResult result = iEsRefundGoodsService.getRefundGoodsByRefundSn(refundSn);
            List<EsRefundGoodsDO> esRefundList = result.getData().getList();
            // 通过订单编号查询出该订单信息
            DubboResult<EsOrderDO> esBuyerOrderInfo = iEsOrderService.getEsBuyerOrderInfo(orderSn);
            EsOrderDO data = esBuyerOrderInfo.getData();
            // 通过商品Id和订单编号查询订单明细信息
            esRefundList.forEach(esRefundGoodsDO -> {
                Long goodsId = esRefundGoodsDO.getGoodsId();
                DubboResult<EsOrderItemsDO> orderItemsDO = iEsOrderItemsService.getEsOrderItemsByOrderSnAndGoodsId(orderSn, goodsId);
                // 查询活动信息，
                EsOrderItemsDO esOrderItemsDO = orderItemsDO.getData();
                Long promotionId = esOrderItemsDO.getPromotionId();
                DubboResult<EsFullDiscountDO> fullDiscount = iEsFullDiscountService.getFullDiscount(promotionId);
                EsFullDiscountDO esFullDiscountDO = fullDiscount.getData();
                // 判断是否送优惠券
                if (esFullDiscountDO.getIsSendBonus() == 1){
                    // 判断优惠券赠送条件，
                    Double subtract = MathUtil.subtract(data.getOrderMoney(), refundMoney);

                    // 如果退过款之后 未退的金额 满足满赠的条件，则不退回优惠券信息
                    // 例如：买了100的商品 ， 满50赠优惠券，如果退了50的商品 则刚刚满足满赠，所以不退回优惠券信息
                    // 买了100的商品 ， 满50赠优惠券，如果退了100的所有商品 但是由于损坏所以仅仅退了80

                    // 不满足满赠的条件 优惠门槛金额 > 未退金额
                    if (esFullDiscountDO.getFullMoney() > subtract){
                        // 退款之后不满足赠送优惠券条件，需要退回优惠券。
                        EsCouponVO esCouponVO = new EsCouponVO();
                        esCouponVO.setId(esFullDiscountDO.getBonusId());

                        EsOrderDTO esOrderDTO = new EsOrderDTO();
                        esOrderDTO.setMemberId(data.getMemberId());
                        esOrderDTO.setCreateTime(data.getCreateTime());
                        iEsCouponService.redMemberCoupon(esCouponVO,esOrderDTO);

                    }

                }
            });

            return DubboResult.success();
        } catch (ArgumentException ae) {
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券退回操作失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsRefundDO> getServiceDetail(String sn, Long memberId) {
        try {
            QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefund::getSn,sn).eq(EsRefund::getMemberId,memberId);
            EsRefund esRefund = this.refundMapper.selectOne(queryWrapper);
            // 转DO
            EsRefundDO esRefundDO = new EsRefundDO();
            BeanUtils.copyProperties(esRefund,esRefundDO);
            // 获取该售后单中的商品List
            QueryWrapper<EsRefundGoods> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsRefundGoods::getRefundSn,esRefund.getSn());
            List<EsRefundGoods> esRefundGoods = this.refundGoodsMapper.selectList(wrapper);
            List<EsRefundGoodsDO> esRefundGoodsDOS = BeanUtil.copyList(esRefundGoods, EsRefundGoodsDO.class);
            // 赋值售后商品
            esRefundDO.setEsRefundGoods(esRefundGoodsDOS);

            esRefundDO.setProcessStatusText(ProcessStatusEnum.valueOf(esRefundDO.getProcessStatus()).description());
            return DubboResult.success(esRefundDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult updateCancelAfterSale(String sn, Long memberId) {
        try {
            // 根据sn 获取订单编号 及订单项编号
            QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefund::getSn,sn).eq(EsRefund::getMemberId,memberId);
            EsRefund esRefund = this.refundMapper.selectOne(queryWrapper);

            // 获取该售后单中的商品List
            QueryWrapper<EsRefundGoods> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsRefundGoods::getRefundSn,esRefund.getSn());
            List<EsRefundGoods> esRefundGoods = this.refundGoodsMapper.selectList(wrapper);

            Long[] goodsId = esRefundGoods.stream().map(EsRefundGoods::getGoodsId).toArray(Long[]::new);
            // 根据订单编号 和 会员ID 更新售后状态为 未申请
            EsOrderDTO esOrderDTO = new EsOrderDTO();
            esOrderDTO.setOrderSn(esRefund.getOrderSn());
            esOrderDTO.setServiceState(OrderStatusEnum.PAID_OFF.value());
            esOrderDTO.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
            DubboResult<EsOrderDO> result = this.iEsOrderService.updateOrderMessage(esOrderDTO);
            if (!result.isSuccess()){
                throw new ArgumentException(result.getCode(),result.getMsg());
            }
            // 根据 订单编号和 商品Id更新商品售后状态为 未申请
            DubboResult<EsOrderItemsDO> itemsResult = this.iEsOrderItemsService.updateOrderItemsServiceStatus(esRefund.getOrderSn(), goodsId, ServiceStatusEnum.NOT_APPLY.value());
            if (!itemsResult.isSuccess()){
                throw new ArgumentException(itemsResult.getCode(),itemsResult.getMsg());
            }
            // 更新 退款单
            esRefund.setRefundStatus(RefundStatusEnum.CANCEL.value());
            esRefund.setProcessStatus(ProcessStatusEnum.COMPLETED.value());
            this.refundMapper.update(esRefund,queryWrapper);

            return DubboResult.success();
        } catch (ArgumentException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    //处理中/已完成售后单列表
    @Override
    public DubboPageResult<EsRefundDO> getAfterSalesRecords(EsWapAfterSaleRecordQueryDTO dto, int pageSize, int pageNum) {
        try {
            Page<EsRefund> page = new Page<>(pageNum, pageSize);
            IPage<EsRefund> iPage = null;
            if (dto.getStatus() == 1){
                iPage = refundMapper.getAfterSalesRecords(page, dto.getMemberId());
            }else if (dto.getStatus() == 2){
                QueryWrapper<EsRefund> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsRefund::getMemberId,dto.getMemberId()).eq(EsRefund::getProcessStatus,"COMPLETED");
                iPage = refundMapper.selectPage(page, queryWrapper);
            }

            List<EsRefundDO> refundDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                refundDOList = iPage.getRecords().stream().map(refund -> {
                    EsRefundDO refundDO = new EsRefundDO();
                    BeanUtil.copyProperties(refund, refundDO);
                    // 获取该售后单中的商品List
                    QueryWrapper<EsRefundGoods> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(EsRefundGoods::getRefundSn,refund.getSn());
                    List<EsRefundGoods> esRefundGoods = this.refundGoodsMapper.selectList(wrapper);
                    List<EsRefundGoodsDO> esRefundGoodsDOS = BeanUtil.copyList(esRefundGoods, EsRefundGoodsDO.class);
                    // 赋值售后商品
                    refundDO.setEsRefundGoods(esRefundGoodsDOS);
                    // 设置权限
                    RefundTypeEnum type = RefundTypeEnum.valueOf(refund.getRefundType());
                    ProcessStatusEnum  paymentType = ProcessStatusEnum.valueOf(refund.getProcessStatus());
                    AfterSaleOperateAllowable afterSaleOperateAllowable =  setAllowble(type,paymentType);
                    refundDO.setAfterSaleOperateAllowable(afterSaleOperateAllowable);
                    // 设置中文
                    refundDO.setRefundStatusText(RefundStatusEnum.valueOf(refund.getRefundStatus()).description());
                    refundDO.setRefuseTypeText(RefundTypeEnum.valueOf(refund.getRefuseType()).description());
                    refundDO.setProcessStatusText(ProcessStatusEnum.valueOf(refund.getProcessStatus()).description());

                    return refundDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),refundDOList);
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsRefundDO> getRefundSn(String orderSn, Long skuId) {
        try {
            EsRefund refund = this.refundMapper.getRefundSn(orderSn, skuId);

            EsRefundDO esRefundDO = new EsRefundDO();
            if (refund != null){
                BeanUtil.copyProperties(refund,esRefundDO);
            }
            return DubboResult.success(esRefundDO);
        } catch (ArgumentException e) {
            logger.error("售后单查询失败", e);
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @Override
    public DubboResult<EsWapRefundCountVO> getCount(Long memberId) {

        try {
            int counts = this.orderMapper.getCounts(memberId);
            QueryWrapper<EsRefund> queryWrapper=new QueryWrapper();
            queryWrapper.lambda().eq(EsRefund::getMemberId,memberId);
            List<EsRefund> refunds = this.refundMapper.selectList(queryWrapper);
            EsWapRefundCountVO refundCountVO=new EsWapRefundCountVO();
            int apply=0;
            int comp=0;
            for (EsRefund refund:refunds) {
                if (refund.getRefundStatus().equals(RefundStatusEnum.APPLY.value())){
                    apply++;
                }else if (refund.getRefundStatus().equals(RefundStatusEnum.PASS.value()) || refund.getRefundStatus().equals(RefundStatusEnum.REFUSE.value())){
                    comp++;
                }
            }
            refundCountVO.setCanApply(counts);
            refundCountVO.setApply(apply);
            refundCountVO.setComplete(comp);
            return DubboResult.success(refundCountVO);
        } catch (Throwable th) {
            logger.error("查询售后数量失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }


}
