package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsDeliveryInfoDO;
import com.jjg.trade.model.dto.EsDeliveryInfoDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsDeliveryInfo;
import com.xdl.jjg.mapper.EsDeliveryInfoMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsDeliveryInfoService;
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
 * 自提信息表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsDeliveryInfoService.class, timeout = 50000)
public class EsDeliveryInfoServiceImpl extends ServiceImpl<EsDeliveryInfoMapper, EsDeliveryInfo> implements IEsDeliveryInfoService {

    private static Logger logger = LoggerFactory.getLogger(EsDeliveryInfoServiceImpl.class);

    @Autowired
    private EsDeliveryInfoMapper deliveryInfoMapper;

    /**
     * 插入自提信息表数据
     *
     * @param deliveryInfoDTO 自提信息表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryInfoDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertDeliveryInfo(EsDeliveryInfoDTO deliveryInfoDTO) {
        try {
            EsDeliveryInfo deliveryInfo = new EsDeliveryInfo();
            BeanUtil.copyProperties(deliveryInfoDTO, deliveryInfo);
            this.deliveryInfoMapper.insert(deliveryInfo);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("自提信息表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("自提信息表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新自提信息表数据
     *
     * @param deliveryInfoDTO 自提信息表DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryInfoDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateDeliveryInfo(EsDeliveryInfoDTO deliveryInfoDTO, Long id) {
        try {
            EsDeliveryInfo deliveryInfo = this.deliveryInfoMapper.selectById(id);
            if (deliveryInfo == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(deliveryInfoDTO, deliveryInfo);
            QueryWrapper<EsDeliveryInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDeliveryInfo::getId, id);
            this.deliveryInfoMapper.update(deliveryInfo, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("自提信息表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("自提信息表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取自提信息表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryInfoDO>
     */
    @Override
    public DubboResult<EsDeliveryInfoDO> getDeliveryInfo(Long id) {
        try {
            EsDeliveryInfo deliveryInfo = this.deliveryInfoMapper.selectById(id);
            if (deliveryInfo == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsDeliveryInfoDO deliveryInfoDO = new EsDeliveryInfoDO();
            BeanUtil.copyProperties(deliveryInfo, deliveryInfoDO);
            return DubboResult.success(deliveryInfoDO);
        } catch (ArgumentException ae){
            logger.error("自提信息表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("自提信息表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询自提信息表列表
     *
     * @param deliveryInfoDTO 自提信息表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsDeliveryInfoDO>
     */
    @Override
    public DubboPageResult<EsDeliveryInfoDO> getDeliveryInfoList(EsDeliveryInfoDTO deliveryInfoDTO, int pageSize, int pageNum) {
        QueryWrapper<EsDeliveryInfo> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsDeliveryInfo> page = new Page<>(pageNum, pageSize);
            IPage<EsDeliveryInfo> iPage = this.page(page, queryWrapper);
            List<EsDeliveryInfoDO> deliveryInfoDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                deliveryInfoDOList = iPage.getRecords().stream().map(deliveryInfo -> {
                    EsDeliveryInfoDO deliveryInfoDO = new EsDeliveryInfoDO();
                    BeanUtil.copyProperties(deliveryInfo, deliveryInfoDO);
                    return deliveryInfoDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(deliveryInfoDOList);
        } catch (ArgumentException ae){
            logger.error("自提信息表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("自提信息表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除自提信息表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryInfoDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteDeliveryInfo(Long id) {
        try {
            this.deliveryInfoMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("自提信息表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("自提信息表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
