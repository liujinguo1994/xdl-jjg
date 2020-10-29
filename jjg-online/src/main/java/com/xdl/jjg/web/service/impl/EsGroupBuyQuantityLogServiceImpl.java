package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsGroupBuyQuantityLogDO;
import com.jjg.trade.model.dto.EsGroupBuyQuantityLogDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsGroupBuyQuantityLog;
import com.xdl.jjg.mapper.EsGroupBuyQuantityLogMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGroupBuyQuantityLogService;
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
 * 团购商品库存日志表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGroupBuyQuantityLogService.class, timeout = 50000)
public class EsGroupBuyQuantityLogServiceImpl extends ServiceImpl<EsGroupBuyQuantityLogMapper, EsGroupBuyQuantityLog> implements IEsGroupBuyQuantityLogService {

    private static Logger logger = LoggerFactory.getLogger(EsGroupBuyQuantityLogServiceImpl.class);

    @Autowired
    private EsGroupBuyQuantityLogMapper groupBuyQuantityLogMapper;

    /**
     * 插入团购商品库存日志表数据
     *
     * @param groupBuyQuantityLogDTO 团购商品库存日志表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyQuantityLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGroupBuyQuantityLog(EsGroupBuyQuantityLogDTO groupBuyQuantityLogDTO) {
        try {
            EsGroupBuyQuantityLog groupBuyQuantityLog = new EsGroupBuyQuantityLog();
            BeanUtil.copyProperties(groupBuyQuantityLogDTO, groupBuyQuantityLog);
            this.groupBuyQuantityLogMapper.insert(groupBuyQuantityLog);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("团购商品库存日志表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("团购商品库存日志表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新团购商品库存日志表数据
     *
     * @param groupBuyQuantityLogDTO 团购商品库存日志表DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyQuantityLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGroupBuyQuantityLog(EsGroupBuyQuantityLogDTO groupBuyQuantityLogDTO, Long id) {
        try {
            EsGroupBuyQuantityLog groupBuyQuantityLog = this.groupBuyQuantityLogMapper.selectById(id);
            if (groupBuyQuantityLog == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(groupBuyQuantityLogDTO, groupBuyQuantityLog);
            QueryWrapper<EsGroupBuyQuantityLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGroupBuyQuantityLog::getId, id);
            this.groupBuyQuantityLogMapper.update(groupBuyQuantityLog, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("团购商品库存日志表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("团购商品库存日志表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取团购商品库存日志表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyQuantityLogDO>
     */
    @Override
    public DubboResult<EsGroupBuyQuantityLogDO> getGroupBuyQuantityLog(Long id) {
        try {
            EsGroupBuyQuantityLog groupBuyQuantityLog = this.groupBuyQuantityLogMapper.selectById(id);
            if (groupBuyQuantityLog == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGroupBuyQuantityLogDO groupBuyQuantityLogDO = new EsGroupBuyQuantityLogDO();
            BeanUtil.copyProperties(groupBuyQuantityLog, groupBuyQuantityLogDO);
            return DubboResult.success(groupBuyQuantityLogDO);
        } catch (ArgumentException ae){
            logger.error("团购商品库存日志表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("团购商品库存日志表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询团购商品库存日志表列表
     *
     * @param groupBuyQuantityLogDTO 团购商品库存日志表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGroupBuyQuantityLogDO>
     */
    @Override
    public DubboPageResult<EsGroupBuyQuantityLogDO> getGroupBuyQuantityLogList(EsGroupBuyQuantityLogDTO groupBuyQuantityLogDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGroupBuyQuantityLog> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGroupBuyQuantityLog> page = new Page<>(pageNum, pageSize);
            IPage<EsGroupBuyQuantityLog> iPage = this.page(page, queryWrapper);
            List<EsGroupBuyQuantityLogDO> groupBuyQuantityLogDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                groupBuyQuantityLogDOList = iPage.getRecords().stream().map(groupBuyQuantityLog -> {
                    EsGroupBuyQuantityLogDO groupBuyQuantityLogDO = new EsGroupBuyQuantityLogDO();
                    BeanUtil.copyProperties(groupBuyQuantityLog, groupBuyQuantityLogDO);
                    return groupBuyQuantityLogDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(groupBuyQuantityLogDOList);
        } catch (ArgumentException ae){
            logger.error("团购商品库存日志表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("团购商品库存日志表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除团购商品库存日志表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyQuantityLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGroupBuyQuantityLog(Long id) {
        try {
            this.groupBuyQuantityLogMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("团购商品库存日志表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("团购商品库存日志表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
