package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.system.api.constant.ErrorCode;
import com.shopx.trade.api.model.domain.EsDeliveryDateDO;
import com.shopx.trade.api.model.domain.dto.EsDeliveryDateDTO;
import com.shopx.trade.api.service.IEsDeliveryDateService;
import com.shopx.trade.dao.entity.EsDeliveryDate;
import com.shopx.trade.dao.mapper.EsDeliveryDateMapper;
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
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-07-02
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsDeliveryDateService.class, timeout = 50000)
public class EsDeliveryDateServiceImpl extends ServiceImpl<EsDeliveryDateMapper, EsDeliveryDate> implements IEsDeliveryDateService {

    private static Logger logger = LoggerFactory.getLogger(EsDeliveryDateServiceImpl.class);

    @Autowired
    private EsDeliveryDateMapper deliveryDateMapper;

    /**
     * 插入数据
     *
     * @param deliveryDateDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryDateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertDeliveryDate(EsDeliveryDateDTO deliveryDateDTO) {
        try {
            if (deliveryDateDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsDeliveryDate deliveryDate = new EsDeliveryDate();
            BeanUtil.copyProperties(deliveryDateDTO, deliveryDate);
            this.deliveryDateMapper.insert(deliveryDate);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param deliveryDateDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryDateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateDeliveryDate(EsDeliveryDateDTO deliveryDateDTO) {
        try {
            if (deliveryDateDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsDeliveryDate deliveryDate = new EsDeliveryDate();
            BeanUtil.copyProperties(deliveryDateDTO, deliveryDate);
            QueryWrapper<EsDeliveryDate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDeliveryDate::getId, deliveryDateDTO.getId());
            this.deliveryDateMapper.update(deliveryDate, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryDateDO>
     */
    @Override
    public DubboResult getDeliveryDate(Long id) {
        try {
            QueryWrapper<EsDeliveryDate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDeliveryDate::getId, id);
            EsDeliveryDate deliveryDate = this.deliveryDateMapper.selectOne(queryWrapper);
            EsDeliveryDateDO deliveryDateDO = new EsDeliveryDateDO();
            if (deliveryDate != null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(deliveryDate, deliveryDateDO);
            return DubboResult.success(deliveryDateDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param deliveryDateDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsDeliveryDateDO>
     */
    @Override
    public DubboPageResult getDeliveryDateList(EsDeliveryDateDTO deliveryDateDTO, int pageSize, int pageNum) {
        QueryWrapper<EsDeliveryDate> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsDeliveryDate> page = new Page<>(pageNum, pageSize);
            IPage<EsDeliveryDate> iPage = this.page(page, queryWrapper);
            List<EsDeliveryDateDO> deliveryDateDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                deliveryDateDOList = iPage.getRecords().stream().map(deliveryDate -> {
                    EsDeliveryDateDO deliveryDateDO = new EsDeliveryDateDO();
                    BeanUtil.copyProperties(deliveryDate, deliveryDateDO);
                    return deliveryDateDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(deliveryDateDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryDateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteDeliveryDate(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsDeliveryDate> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsDeliveryDate::getId, id);
            this.deliveryDateMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult getDeliveryDateByDeliveryId(Long deliveryId) {
        QueryWrapper<EsDeliveryDate> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsDeliveryDate::getDeliveryId,deliveryId);
            List<EsDeliveryDate> esDeliveryDates = this.deliveryDateMapper.selectList(queryWrapper);
            List<EsDeliveryDateDO> deliveryDateDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esDeliveryDates)) {
                deliveryDateDOList = esDeliveryDates.stream().map(deliveryDate -> {
                    EsDeliveryDateDO deliveryDateDO = new EsDeliveryDateDO();
                    BeanUtil.copyProperties(deliveryDate, deliveryDateDO);
                    return deliveryDateDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(deliveryDateDOList);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
