package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberConstant;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsGrowthValueStrategyDO;
import com.shopx.member.api.model.domain.dto.EsGrowthStrategyDTO;
import com.shopx.member.api.model.domain.dto.EsGrowthValueStrategyDTO;
import com.shopx.member.api.model.domain.vo.EsGrowthValueStrategyVO;
import com.shopx.member.api.service.IEsGrowthValueStrategyService;
import com.xdl.jjg.entity.EsGrowthValueStrategy;
import  com.xdl.jjg.mapper.EsGrowthValueStrategyMapper;
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
 * 评价和收藏成长值配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-20 15:51:01
 */
@Service
public class EsGrowthValueStrategyServiceImpl extends ServiceImpl<EsGrowthValueStrategyMapper, EsGrowthValueStrategy> implements IEsGrowthValueStrategyService {

    private static Logger logger = LoggerFactory.getLogger(EsGrowthValueStrategyServiceImpl.class);

    @Autowired
    private EsGrowthValueStrategyMapper esGrowthValueStrategyMapper;

    /**
     * 插入数据
     *
     * @param esGrowthStrategyDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGrowthValueStrategy(EsGrowthStrategyDTO esGrowthStrategyDTO) {
        QueryWrapper<EsGrowthValueStrategy> queryWrapper = new QueryWrapper<>();
        if (null == esGrowthStrategyDTO || CollectionUtils.isEmpty(esGrowthStrategyDTO.getStrategyVOList()) || esGrowthStrategyDTO.getStrategyVOList().size() == 0) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            List<Integer> list = new ArrayList<>();
            esGrowthStrategyDTO.getStrategyVOList().stream().forEach(esDto -> {
                list.add(esDto.getGrowthType());
            });
            queryWrapper.lambda().in(EsGrowthValueStrategy::getGrowthType, list);
            esGrowthValueStrategyMapper.delete(queryWrapper);
            for (EsGrowthValueStrategyVO es : esGrowthStrategyDTO.getStrategyVOList()) {
                EsGrowthValueStrategy comandcolleGrowthvalueConfig = new EsGrowthValueStrategy();
                if (null != es.getGrowthType()) {
                    comandcolleGrowthvalueConfig.setGrowthType(es.getGrowthType());
                }
                if (null != es.getGrowthValue()) {
                    comandcolleGrowthvalueConfig.setGrowthValue(es.getGrowthValue());
                }
                if (null != es.getLimitNum()) {
                    comandcolleGrowthvalueConfig.setLimitNum(es.getLimitNum());
                }
                if (null != es.getConfigSwitch()) {
                    if (es.getConfigSwitch()) {
                        comandcolleGrowthvalueConfig.setConfigSwitch(MemberConstant.IsDefault);
                    } else {
                        comandcolleGrowthvalueConfig.setConfigSwitch(MemberConstant.IsDel);
                    }
                }
                this.esGrowthValueStrategyMapper.insert(comandcolleGrowthvalueConfig);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param comandcolleGrowthvalueConfigDTO DTO
     * @param id                              主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateComandcolleGrowthvalueConfig(EsGrowthValueStrategyDTO comandcolleGrowthvalueConfigDTO, Long id) {
        try {
            EsGrowthValueStrategy comandcolleGrowthvalueConfig = this.esGrowthValueStrategyMapper.selectById(id);
            if (comandcolleGrowthvalueConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(comandcolleGrowthvalueConfigDTO, comandcolleGrowthvalueConfig);
            QueryWrapper<EsGrowthValueStrategy> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGrowthValueStrategy::getId, id);
            this.esGrowthValueStrategyMapper.update(comandcolleGrowthvalueConfig, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    @Override
    public DubboResult<EsGrowthValueStrategyDO> getComandcolleGrowthvalueConfig(Long id) {
        try {
            EsGrowthValueStrategy comandcolleGrowthvalueConfig = this.esGrowthValueStrategyMapper.selectById(id);
            if (comandcolleGrowthvalueConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGrowthValueStrategyDO comandcolleGrowthvalueConfigDO = new EsGrowthValueStrategyDO();
            BeanUtil.copyProperties(comandcolleGrowthvalueConfig, comandcolleGrowthvalueConfigDO);
            return DubboResult.success(comandcolleGrowthvalueConfigDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据收藏类型查询成长值
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    @Override
    public DubboResult<EsGrowthValueStrategyDO> getComandcolleGrowthvalueConfigByType(Integer type) {
        QueryWrapper<EsGrowthValueStrategy> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsGrowthValueStrategy::getGrowthType, type).eq(EsGrowthValueStrategy::getConfigSwitch, MemberConstant.collectionStrategySwitch);
            EsGrowthValueStrategy esGrowthValueStrategy = this.esGrowthValueStrategyMapper.selectOne(queryWrapper);
            EsGrowthValueStrategyDO esGrowthValueStrategyDO = new EsGrowthValueStrategyDO();
            if (esGrowthValueStrategy == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(esGrowthValueStrategy, esGrowthValueStrategyDO);
            return DubboResult.success(esGrowthValueStrategyDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGrowthValueStrategyDO>
     */
    @Override
    public DubboPageResult<EsGrowthValueStrategyDO> getGrowthValueStrategy() {
        QueryWrapper<EsGrowthValueStrategy> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            List<EsGrowthValueStrategyDO> comandcolleGrowthvalueConfigDOList = new ArrayList<>();
            List<EsGrowthValueStrategy> list = esGrowthValueStrategyMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(list)) {
                comandcolleGrowthvalueConfigDOList = list.stream().map(comandcolleGrowthvalueConfig -> {
                    EsGrowthValueStrategyDO comandcolleGrowthvalueConfigDO = new EsGrowthValueStrategyDO();
                    if (null != comandcolleGrowthvalueConfig.getId()) {
                        comandcolleGrowthvalueConfigDO.setId(comandcolleGrowthvalueConfig.getId());
                    }
                    if (null != comandcolleGrowthvalueConfig.getConfigSwitch()) {
                        if (comandcolleGrowthvalueConfig.getConfigSwitch() == MemberConstant.IsDefault) {
                            comandcolleGrowthvalueConfigDO.setConfigSwitch(true);
                        } else {
                            comandcolleGrowthvalueConfigDO.setConfigSwitch(false);
                        }
                    }
                    if (null != comandcolleGrowthvalueConfig.getGrowthType()) {
                        comandcolleGrowthvalueConfigDO.setGrowthType(comandcolleGrowthvalueConfig.getGrowthType());
                    }
                    if (null != comandcolleGrowthvalueConfig.getLimitNum()) {
                        comandcolleGrowthvalueConfigDO.setLimitNum(comandcolleGrowthvalueConfig.getLimitNum());
                    }
                    if (null != comandcolleGrowthvalueConfig.getGrowthValue()) {
                        comandcolleGrowthvalueConfigDO.setGrowthValue(comandcolleGrowthvalueConfig.getGrowthValue());
                    }
                    return comandcolleGrowthvalueConfigDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(comandcolleGrowthvalueConfigDOList);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthValueStrategyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteComandcolleGrowthvalueConfig(Long id) {
        try {
            this.esGrowthValueStrategyMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
