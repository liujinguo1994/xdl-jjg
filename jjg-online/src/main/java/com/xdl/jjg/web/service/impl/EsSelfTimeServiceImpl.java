package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsSelfTimeDO;
import com.jjg.trade.model.dto.EsSelfTimeDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsSelfTime;
import com.xdl.jjg.mapper.EsSelfTimeMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsSelfTimeService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;
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
 * 自提时间 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service
public class EsSelfTimeServiceImpl extends ServiceImpl<EsSelfTimeMapper, EsSelfTime> implements IEsSelfTimeService {

    private static Logger logger = LoggerFactory.getLogger(EsSelfTimeServiceImpl.class);

    @Autowired
    private EsSelfTimeMapper selfTimeMapper;

    /**
     * 插入自提时间数据
     *
     * @param selfTimeDTO 自提时间DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsSelfTimeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSelfTime(EsSelfTimeDTO selfTimeDTO) {
        try {
            if (selfTimeDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsSelfTime selfTime = new EsSelfTime();
            BeanUtil.copyProperties(selfTimeDTO, selfTime);
            this.selfTimeMapper.insert(selfTime);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("自提时间失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新自提时间数据
     *
     * @param selfTimeDTO 自提时间DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsSelfTimeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSelfTime(EsSelfTimeDTO selfTimeDTO) {
        try {
            if (selfTimeDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSelfTime selfTime = new EsSelfTime();
            BeanUtil.copyProperties(selfTimeDTO, selfTime);
            QueryWrapper<EsSelfTime> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSelfTime::getId, selfTimeDTO.getId());
            this.selfTimeMapper.update(selfTime, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("自提时间更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取自提时间详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsSelfTimeDO>
     */
    @Override
    public DubboResult getSelfTime(Long id) {
        try {
            QueryWrapper<EsSelfTime> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSelfTime::getId, id);
            EsSelfTime selfTime = this.selfTimeMapper.selectOne(queryWrapper);
            EsSelfTimeDO selfTimeDO = new EsSelfTimeDO();
            if (selfTime == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(selfTime, selfTimeDO);
            return DubboResult.success(selfTimeDO);
        } catch (Throwable th) {
            logger.error("自提时间查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据查询自提时间列表
     *
     * @param selfTimeDTO 自提时间DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSelfTimeDO>
     */
    @Override
    public DubboPageResult getSelfTimeList(EsSelfTimeDTO selfTimeDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSelfTime> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            Page<EsSelfTime> page = new Page<>(pageNum, pageSize);
            IPage<EsSelfTime> iPage = this.page(page, queryWrapper);
            List<EsSelfTimeDO> selfTimeDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                selfTimeDOList = iPage.getRecords().stream().map(selfTime -> {
                    EsSelfTimeDO selfTimeDO = new EsSelfTimeDO();
                    BeanUtil.copyProperties(selfTime, selfTimeDO);
                    return selfTimeDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(selfTimeDOList);
        } catch (Throwable th) {
            logger.error("自提时间查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     *   系统后台
     * 根据主键删除自提时间段数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/0702 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsSelfTimeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSelfTime(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSelfTime> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSelfTime::getId, id);
            this.selfTimeMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("自提时间查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 系统后台
     * 通过dateID获取时间段列表
     * @param dateId
     * @return
     */
    @Override
    public DubboPageResult getSelfTimeListByDateid(Long dateId) {
        QueryWrapper<EsSelfTime> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsSelfTime::getDateId,dateId);
            List<EsSelfTime> esSelfTimes = this.selfTimeMapper.selectList(queryWrapper);
            // 筛选可以选择的自提时间段
            List<EsSelfTime> collect = esSelfTimes.stream().filter(esSelfTime -> esSelfTime.getPersonNumber() > esSelfTime.getCurrentPerson()).collect(Collectors.toList());
            return DubboPageResult.success(collect);
        } catch (Throwable th) {
            logger.error("自提时间查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
