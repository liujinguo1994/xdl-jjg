package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsReceiptHistoryDO;
import com.shopx.member.api.model.domain.dto.EsReceiptHistoryDTO;
import com.shopx.member.api.service.IEsReceiptHistoryService;
import com.xdl.jjg.entity.EsReceiptHistory;
import com.shopx.member.dao.mapper.EsReceiptHistoryMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 发票历史 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsReceiptHistoryServiceImpl extends ServiceImpl<EsReceiptHistoryMapper, EsReceiptHistory> implements IEsReceiptHistoryService {

    private static Logger logger = LoggerFactory.getLogger(EsReceiptHistoryServiceImpl.class);

    @Autowired
    private EsReceiptHistoryMapper receiptHistoryMapper;

    /**
     * 插入发票历史数据
     *
     * @param receiptHistoryDTO 发票历史DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertReceiptHistory(EsReceiptHistoryDTO receiptHistoryDTO) {
        try {
            if (receiptHistoryDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsReceiptHistory receiptHistory = new EsReceiptHistory();
            BeanUtil.copyProperties(receiptHistoryDTO, receiptHistory);
            receiptHistory.setReceiptAmount(receiptHistoryDTO.getReceiptAmount());
            this.receiptHistoryMapper.insert(receiptHistory);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("发票历史新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("发票历史新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新发票历史数据
     *
     * @param receiptHistoryDTO 发票历史DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateReceiptHistory(EsReceiptHistoryDTO receiptHistoryDTO) {
        try {
            if (receiptHistoryDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsReceiptHistory receiptHistory = new EsReceiptHistory();
            BeanUtil.copyProperties(receiptHistoryDTO, receiptHistory);
            QueryWrapper<EsReceiptHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsReceiptHistory::getHistoryId, receiptHistoryDTO.getHistoryId());
            this.receiptHistoryMapper.update(receiptHistory, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("发票历史更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票历史更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * goodsId
     *
     * @param goodsId goodsId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    @Override
    public DubboResult<EsReceiptHistoryDO> getReceiptHistoryByGoodsId(Long goodsId) {
        if (0 == goodsId) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        try {
            QueryWrapper<EsReceiptHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsReceiptHistory::getGoodsId, goodsId);
            EsReceiptHistory receiptHistory = this.receiptHistoryMapper.selectOne(queryWrapper);
            EsReceiptHistoryDO receiptHistoryDO = new EsReceiptHistoryDO();
            if (null == receiptHistory) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(receiptHistory, receiptHistoryDO);
            return DubboResult.success(receiptHistoryDO);
        } catch (ArgumentException ae) {
            logger.error("发票历史查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票历史查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据ordsn和goodsId获取数据
     *
     * @param goodsId 订单编号 goodsId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    @Override
    public DubboResult<EsReceiptHistoryDO> getReceiptHistoryByGoodsIdAndOrdersn(Long goodsId, String orderSn) {
        if (0 == goodsId || null == orderSn) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        try {
            QueryWrapper<EsReceiptHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsReceiptHistory::getGoodsId, goodsId).eq(EsReceiptHistory::getOrderSn, orderSn);
            EsReceiptHistory receiptHistory = this.receiptHistoryMapper.selectOne(queryWrapper);
            EsReceiptHistoryDO receiptHistoryDO = new EsReceiptHistoryDO();
            if (null == receiptHistory) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(receiptHistory, receiptHistoryDO);
            return DubboResult.success(receiptHistoryDO);
        } catch (ArgumentException ae) {
            logger.error("发票历史查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票历史查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据invoiceSerialNum获取数据
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    @Override
    public DubboResult<EsReceiptHistoryDO> getReceiptHistoryByInvoiceSerialNum(String invoiceSerialNum) {
        if (StringUtils.isBlank(invoiceSerialNum)) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        try {
            EsReceiptHistoryDO receiptHistoryDO = this.receiptHistoryMapper.getReceiptHistoryByInvoiceSerialNum(invoiceSerialNum);
            if (null == receiptHistoryDO) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            return DubboResult.success(receiptHistoryDO);
        } catch (ArgumentException ae) {
            logger.error("发票历史查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票历史查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * @param id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    @Override
    public DubboResult<EsReceiptHistoryDO> getReceiptHistoryById(Long id) {
        if (0 == id) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        try {
            EsReceiptHistory receiptHistory = this.receiptHistoryMapper.selectById(id);
            EsReceiptHistoryDO receiptHistoryDO = new EsReceiptHistoryDO();
            if (null == receiptHistory) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(receiptHistory, receiptHistoryDO);
            return DubboResult.success(receiptHistoryDO);
        } catch (ArgumentException ae) {
            logger.error("发票历史查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票历史查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询发票历史列表
     *
     * @param receiptHistoryDTO 发票历史DTO
     * @param pageSize          页码
     * @param pageNum           页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsReceiptHistoryDO>
     */
    @Override
    public DubboPageResult<EsReceiptHistoryDO> getReceiptHistoryList(EsReceiptHistoryDTO receiptHistoryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsReceiptHistory> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsReceiptHistory::getMemberId, receiptHistoryDTO.getMemberId());
            Page<EsReceiptHistory> page = new Page<>(pageNum, pageSize);
            IPage<EsReceiptHistory> iPage = this.page(page, queryWrapper);
            List<EsReceiptHistoryDO> receiptHistoryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                receiptHistoryDOList = iPage.getRecords().stream().map(receiptHistory -> {
                    EsReceiptHistoryDO receiptHistoryDO = new EsReceiptHistoryDO();
                    BeanUtil.copyProperties(receiptHistory, receiptHistoryDO);
                    return receiptHistoryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), receiptHistoryDOList);
        } catch (ArgumentException ae) {
            logger.error("发票历史分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票历史分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除发票历史数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteReceiptHistory(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsReceiptHistory> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsReceiptHistory::getHistoryId, id);
            this.receiptHistoryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("发票历史删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票历史删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
