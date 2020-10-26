package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsSelfDateDO;
import com.jjg.trade.model.domain.EsSelfTimeDO;
import com.jjg.trade.model.dto.EsSelfDateDTO;
import com.jjg.trade.model.dto.EsSelfTimeDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsDeliveryDate;
import com.xdl.jjg.entity.EsSelfDate;
import com.xdl.jjg.entity.EsSelfTime;
import com.xdl.jjg.mapper.EsDeliveryDateMapper;
import com.xdl.jjg.mapper.EsSelfDateMapper;
import com.xdl.jjg.mapper.EsSelfTimeMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.DateUtils;
import com.xdl.jjg.util.SnowflakeIdWorker;
import com.xdl.jjg.web.service.IEsSelfDateService;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 自提日期 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsSelfDateService.class, timeout = 50000)
public class EsSelfDateServiceImpl extends ServiceImpl<EsSelfDateMapper, EsSelfDate> implements IEsSelfDateService {

    private static Logger logger = LoggerFactory.getLogger(EsSelfDateServiceImpl.class);

    @Autowired
    private EsSelfDateMapper selfDateMapper;

    @Autowired
    private EsSelfTimeMapper selfTimeMapper;

    @Autowired
    private EsDeliveryDateMapper deliveryDateMapper;


    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0,0);
    /**
     *  系统后台
     * 插入自提日期数据
     * @param selfDateDTO 自提日期DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSelfDate(EsSelfDateDTO selfDateDTO) {
        try {
            if (selfDateDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSelfDate selfDate = new EsSelfDate();
            BeanUtil.copyProperties(selfDateDTO, selfDate);
//            selfDate.setId(snowflakeIdWorker.nextId());
            this.selfDateMapper.insert(selfDate);
            // 遍历自提日期中的时间段对象
            List<EsSelfTimeDTO> selfTimeDTOList = selfDateDTO.getSelfTimeDTOList();
            selfTimeDTOList.forEach(esSelfTimeDTO -> {
                EsSelfTime esSelfTime = new EsSelfTime();
                BeanUtils.copyProperties(esSelfTimeDTO,esSelfTime);
                esSelfTime.setDateId(selfDate.getId());
                this.selfTimeMapper.insert(esSelfTime);
            });
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("自提日期失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新自提日期数据
     *
     * @param selfDateDTO 自提日期DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSelfDate(EsSelfDateDTO selfDateDTO) {
        try {
            if (selfDateDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSelfDate selfDate = new EsSelfDate();
            BeanUtil.copyProperties(selfDateDTO, selfDate);
            QueryWrapper<EsSelfDate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSelfDate::getId, selfDateDTO.getId());
            this.selfDateMapper.update(selfDate, queryWrapper);
            // 更新 time 其实就时重新删除 后在此添加
            // 通过 dateId 删除
            QueryWrapper<EsSelfTime> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsSelfTime::getDateId,selfDateDTO.getId());
            this.selfTimeMapper.delete(queryWrapper1);

            // 遍历自提日期中的时间段对象
            List<EsSelfTimeDTO> selfTimeDTOList = selfDateDTO.getSelfTimeDTOList();
            selfTimeDTOList.forEach(esSelfTimeDTO -> {
                EsSelfTime esSelfTime = new EsSelfTime();
                BeanUtils.copyProperties(esSelfTimeDTO,esSelfTime);
                esSelfTime.setDateId(selfDate.getId());
                this.selfTimeMapper.insert(esSelfTime);
            });

            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("自提日期更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     *  系统后台
     * 根据id获取自提日期详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/0702 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    @Override
    public DubboResult getSelfDate(Long id) {
        try {
            // 查询该自提时间信息
            QueryWrapper<EsSelfDate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSelfDate::getId, id);
            EsSelfDate selfDate = this.selfDateMapper.selectOne(queryWrapper);
            EsSelfDateDO selfDateDO = new EsSelfDateDO();
            if (selfDate == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            // 查询自提时间下的时间段信息
            QueryWrapper<EsSelfTime> querySelfTimeWrapper = new QueryWrapper<>();
            querySelfTimeWrapper.lambda().eq(EsSelfTime::getDateId,id);

            List<EsSelfTimeDO> esSelfTimeDOList =  new ArrayList<>();
            List<EsSelfTime> esSelfTimes = this.selfTimeMapper.selectList(querySelfTimeWrapper);
            esSelfTimeDOList = esSelfTimes.stream().map(esSelfTime -> {
                EsSelfTimeDO esSelfTimeDO = new EsSelfTimeDO();
                //时间段实体装DO
                BeanUtils.copyProperties(esSelfTime,esSelfTimeDO);
                return esSelfTimeDO;
            }).collect(Collectors.toList());

            //自提时间实体装DO
            BeanUtil.copyProperties(selfDate, selfDateDO);

            selfDateDO.setSelfTimeDOList(esSelfTimeDOList);
            return DubboResult.success(selfDateDO);
        } catch (Throwable th) {
            logger.error("自提日期查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     *  系统后台
     * 0702 获取当前状态为有效的时间点
     * @return
     */
    @Override
    public DubboResult getAllSelfDate() {
        QueryWrapper<EsSelfDate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsSelfDate::getState,0);
        List<EsSelfDate> esSelfDates = this.selfDateMapper.selectList(queryWrapper);
        List<EsSelfDateDO> esSelfDateDOList = new ArrayList<>();
        esSelfDateDOList = esSelfDates.stream().map(esSelfDate -> {
            EsSelfDateDO esSelfDateDO = new EsSelfDateDO();
            BeanUtils.copyProperties(esSelfDate,esSelfDateDO);
            return esSelfDateDO;
        }).collect(Collectors.toList());
        return DubboResult.success(esSelfDateDOList);
    }

    /**
     *  系统后台
     * 查询自提日期列表
     *
     * @param selfDateDTO 自提日期DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/0702 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSelfDateDO>
     */
    @Override
    public DubboPageResult getSelfDateList(EsSelfDateDTO selfDateDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSelfDate> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsSelfDate> page = new Page<>(pageNum, pageSize);
            IPage<EsSelfDate> iPage = this.selfDateMapper.selectPage(page,queryWrapper);
            List<EsSelfDateDO> selfDateDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                selfDateDOList = iPage.getRecords().stream().map(selfDate -> {
                    //自提日期id
                    Long dateId = selfDate.getId();
                    int personTotal = this.selfTimeMapper.selectPersonTotal(dateId);
                    EsSelfDateDO selfDateDO = new EsSelfDateDO();
                    BeanUtil.copyProperties(selfDate, selfDateDO);
                    selfDateDO.setPersonTotal(personTotal);
                    // 封装time集合
                    QueryWrapper<EsSelfTime> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(EsSelfTime::getDateId,dateId);
                    List<EsSelfTimeDO> EsSelfTimeDOList = new ArrayList<>();
                    List<EsSelfTime> esSelfTimes = this.selfTimeMapper.selectList(wrapper);
                    EsSelfTimeDOList = esSelfTimes.stream().map(esSelfTime -> {
                        EsSelfTimeDO esSelfTimeDO = new EsSelfTimeDO();
                        BeanUtils.copyProperties(esSelfTime,esSelfTimeDO);
                        return esSelfTimeDO;
                    }).collect(Collectors.toList());

                    selfDateDO.setSelfTimeDOList(EsSelfTimeDOList);
                    return selfDateDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),selfDateDOList);
        } catch (Throwable th) {
            logger.error("自提日期查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     *  系统后台
     * 根据主键删除自提日期数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/0703 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSelfDate(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsDeliveryDate> deliveryDateQueryWrapper = new QueryWrapper<>();
            deliveryDateQueryWrapper.lambda().eq(EsDeliveryDate::getDateId,id);
            // 先解除绑定关系
            this.deliveryDateMapper.delete(deliveryDateQueryWrapper);

            QueryWrapper<EsSelfTime> selfTimeQueryWrapper = new QueryWrapper<>();
            selfTimeQueryWrapper.lambda().eq(EsSelfTime::getDateId,id);
            // 删除 字体时间表数据
            this.selfTimeMapper.delete(selfTimeQueryWrapper);

            //删除之前进行判断是否存在关联的日期
//            List<EsDeliveryDate> esDeliveryDates = this.deliveryDateMapper.selectList(deliveryDateQueryWrapper);
//            if (CollectionUtils.isNotEmpty(esDeliveryDates)){
//                throw new ArgumentException(TradeErrorCode.DELIVERY_DATE_BINDING.getErrorCode(),TradeErrorCode.DELIVERY_DATE_BINDING.getErrorMsg());
//            }
            QueryWrapper<EsSelfDate> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSelfDate::getId, id);
            this.selfDateMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }
        catch (Throwable th) {
            logger.error("自提日期查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult getSelfDateListByDeliveryId(Long deliveryId) {
        QueryWrapper<EsDeliveryDate> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsDeliveryDate::getDeliveryId,deliveryId);
            // 获取自提点和日期关系列表
            List<EsDeliveryDate> esDeliveryDates = this.deliveryDateMapper.selectList(queryWrapper);

            // 筛选出该自提点对应的自提日期ID
            List<Long> dateIdList = esDeliveryDates.stream().filter(
                    esDeliveryDate -> esDeliveryDate.getDateId().equals(esDeliveryDate.getDateId())
            ).map(EsDeliveryDate::getDateId).collect(Collectors.toList());
            // 通过dateId获取自提日期
            QueryWrapper<EsSelfDate> wrapper = new QueryWrapper<>();
            Date date = DateUtils.getDayBegin(new Date());
            wrapper.lambda().in(EsSelfDate::getId,dateIdList).ge(EsSelfDate::getSelfDate,date.getTime());
            List<EsSelfDate> esSelfDates = this.selfDateMapper.selectList(wrapper);

            List<EsSelfDateDO> esSelfDatesDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esSelfDates)) {
                esSelfDatesDOList = esSelfDates.stream().map(esSelfDate -> {
                    EsSelfDateDO esSelfDateDO = new EsSelfDateDO();
                    BeanUtil.copyProperties(esSelfDate, esSelfDateDO);
                    return esSelfDateDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(esSelfDatesDOList);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }
}
