package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsSettlementDO;
import com.jjg.trade.model.dto.EsSettlementDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsSettlement;
import com.xdl.jjg.mapper.EsSettlementMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsSettlementService;
import org.apache.dubbo.common.utils.CollectionUtils;
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
 * 结算单 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-08-20 15:12:53
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsSettlementService.class, timeout = 50000)
public class EsSettlementServiceImpl extends ServiceImpl<EsSettlementMapper, EsSettlement> implements IEsSettlementService {

    private static Logger logger = LoggerFactory.getLogger(EsSettlementServiceImpl.class);

    @Autowired
    private EsSettlementMapper settlementMapper;

    /**
     * 插入结算单数据
     *
     * @param settlementDTO 结算单DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsSettlementDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSettlement(EsSettlementDTO settlementDTO) {
        try {
            EsSettlement settlement = new EsSettlement();
            BeanUtil.copyProperties(settlementDTO, settlement);
            this.settlementMapper.insert(settlement);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("结算单新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("结算单新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新结算单数据
     *
     * @param settlementDTO 结算单DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsSettlementDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSettlement(EsSettlementDTO settlementDTO, Long id) {
        try {
            EsSettlement settlement = this.settlementMapper.selectById(id);
            if (settlement == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(settlementDTO, settlement);
            QueryWrapper<EsSettlement> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSettlement::getId, id);
            this.settlementMapper.update(settlement, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("结算单更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("结算单更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取结算单详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsSettlementDO>
     */
    @Override
    public DubboResult<EsSettlementDO> getSettlement(Long id) {
        try {
            EsSettlement settlement = this.settlementMapper.selectById(id);
            if (settlement == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsSettlementDO settlementDO = new EsSettlementDO();
            BeanUtil.copyProperties(settlement, settlementDO);
            return DubboResult.success(settlementDO);
        } catch (ArgumentException ae){
            logger.error("结算单查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("结算单查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询结算单列表
     *
     * @param settlementDTO 结算单DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSettlementDO>
     */
    @Override
    public DubboPageResult<EsSettlementDO> getSettlementList(EsSettlementDTO settlementDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSettlement> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsSettlement> page = new Page<>(pageNum, pageSize);
            IPage<EsSettlement> iPage = this.page(page, queryWrapper);
            List<EsSettlementDO> settlementDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                settlementDOList = iPage.getRecords().stream().map(settlement -> {
                    EsSettlementDO settlementDO = new EsSettlementDO();
                    BeanUtil.copyProperties(settlement, settlementDO);
                    return settlementDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(settlementDOList);
        } catch (ArgumentException ae){
            logger.error("结算单分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("结算单分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除结算单数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsSettlementDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSettlement(Long id) {
        try {
            this.settlementMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("结算单删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("结算单删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
