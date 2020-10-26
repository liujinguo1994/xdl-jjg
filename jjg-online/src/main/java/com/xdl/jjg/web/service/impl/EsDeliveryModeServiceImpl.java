package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsDeliveryModeDO;
import com.shopx.trade.api.model.domain.dto.EsDeliveryModeDTO;
import com.shopx.trade.api.service.IEsDeliveryModeService;
import com.shopx.trade.dao.entity.EsDeliveryMode;
import com.shopx.trade.dao.mapper.EsDeliveryModeMapper;
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
 * 配送方式 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsDeliveryModeService.class, timeout = 50000)
public class EsDeliveryModeServiceImpl extends ServiceImpl<EsDeliveryModeMapper, EsDeliveryMode> implements IEsDeliveryModeService {

    private static Logger logger = LoggerFactory.getLogger(EsDeliveryModeServiceImpl.class);

    @Autowired
    private EsDeliveryModeMapper deliveryModeMapper;

    /**
     * 插入配送方式数据
     *
     * @param deliveryModeDTO 配送方式DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryModeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertDeliveryMode(EsDeliveryModeDTO deliveryModeDTO) {
        try {
            EsDeliveryMode deliveryMode = new EsDeliveryMode();
            BeanUtil.copyProperties(deliveryModeDTO, deliveryMode);
            this.deliveryModeMapper.insert(deliveryMode);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("配送方式新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("配送方式新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新配送方式数据
     *
     * @param deliveryModeDTO 配送方式DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryModeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateDeliveryMode(EsDeliveryModeDTO deliveryModeDTO, Long id) {
        try {
            EsDeliveryMode deliveryMode = this.deliveryModeMapper.selectById(id);
            if (deliveryMode == null) {
                throw new ArgumentException(TradeErrorCode.DELIVERY_MODE_NOT_EXIST.getErrorCode(), TradeErrorCode.DELIVERY_MODE_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(deliveryModeDTO, deliveryMode);
            QueryWrapper<EsDeliveryMode> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDeliveryMode::getId, id);
            this.deliveryModeMapper.update(deliveryMode, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("配送方式更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("配送方式更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取配送方式详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryModeDO>
     */
    @Override
    public DubboResult<EsDeliveryModeDO> getDeliveryMode(Long id) {
        try {
            EsDeliveryMode deliveryMode = this.deliveryModeMapper.selectById(id);
            if (deliveryMode == null) {
                throw new ArgumentException(TradeErrorCode.DELIVERY_MODE_NOT_EXIST.getErrorCode(), TradeErrorCode.DELIVERY_MODE_NOT_EXIST.getErrorMsg());
            }
            EsDeliveryModeDO deliveryModeDO = new EsDeliveryModeDO();
            BeanUtil.copyProperties(deliveryMode, deliveryModeDO);
            return DubboResult.success(deliveryModeDO);
        } catch (ArgumentException ae){
            logger.error("配送方式查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("配送方式查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询配送方式列表
     *
     * @param deliveryModeDTO 配送方式DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsDeliveryModeDO>
     */
    @Override
    public DubboPageResult<EsDeliveryModeDO> getDeliveryModeList(EsDeliveryModeDTO deliveryModeDTO, int pageSize, int pageNum) {
        QueryWrapper<EsDeliveryMode> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsDeliveryMode> page = new Page<>(pageNum, pageSize);
            IPage<EsDeliveryMode> iPage = this.page(page, queryWrapper);
            List<EsDeliveryModeDO> deliveryModeDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                deliveryModeDOList = iPage.getRecords().stream().map(deliveryMode -> {
                    EsDeliveryModeDO deliveryModeDO = new EsDeliveryModeDO();
                    BeanUtil.copyProperties(deliveryMode, deliveryModeDO);
                    return deliveryModeDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(deliveryModeDOList);
        } catch (ArgumentException ae){
            logger.error("配送方式分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("配送方式分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除配送方式数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryModeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteDeliveryMode(Long id) {
        try {
            this.deliveryModeMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("配送方式删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("配送方式删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
