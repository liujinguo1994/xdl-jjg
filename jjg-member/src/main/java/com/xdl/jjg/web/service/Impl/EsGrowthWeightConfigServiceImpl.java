package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsGrowthWeightConfig;
import com.xdl.jjg.mapper.EsGrowthWeightConfigMapper;
import com.xdl.jjg.model.domain.EsGrowthWeightConfigDO;
import com.xdl.jjg.model.dto.EsGrowthWeightConfigDTO;
import com.xdl.jjg.model.dto.EsGrowthWeightConfigListDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGrowthWeightConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 成长值权重配置 服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 11:06:57
 */
@Service(version = "${dubbo.application.version}" , interfaceClass = IEsGrowthWeightConfigService.class, timeout = 50000)
public class EsGrowthWeightConfigServiceImpl extends ServiceImpl<EsGrowthWeightConfigMapper, EsGrowthWeightConfig> implements IEsGrowthWeightConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsGrowthWeightConfigServiceImpl.class);

    @Autowired
    private EsGrowthWeightConfigMapper growthWeightConfigMapper;

    /**
     * 插入数据
     *
     * @param growthWeightConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGrowthWeightConfig(EsGrowthWeightConfigListDTO growthWeightConfigDTO) {
        if (null == growthWeightConfigDTO || CollectionUtils.isEmpty(growthWeightConfigDTO.getWeightVoS())) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            this.growthWeightConfigMapper.deleteAll();
            growthWeightConfigDTO.getWeightVoS().stream().forEach(es -> {
                EsGrowthWeightConfig growthWeightConfig = new EsGrowthWeightConfig();
                if (null != es.getType()) {
                    growthWeightConfig.setType(es.getType());
                }
                if (null != es.getWeight()) {
                    growthWeightConfig.setWeight(es.getWeight());
                }
                if (null != es.getState()) {
                    if (es.getState()) {
                        growthWeightConfig.setState(MemberConstant.IsDefault);
                    } else {
                        growthWeightConfig.setState(MemberConstant.IsDel);
                    }
                }
                this.growthWeightConfigMapper.insert(growthWeightConfig);
            });
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param growthWeightConfigDTO DTO
     * @param id                    主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGrowthWeightConfig(EsGrowthWeightConfigDTO growthWeightConfigDTO, Long id) {
        try {
            EsGrowthWeightConfig growthWeightConfig = this.growthWeightConfigMapper.selectById(id);
            if (growthWeightConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(growthWeightConfigDTO, growthWeightConfig);
            QueryWrapper<EsGrowthWeightConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGrowthWeightConfig::getId, id);
            this.growthWeightConfigMapper.update(growthWeightConfig, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败" , th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    @Override
    public DubboResult<EsGrowthWeightConfigDO> getGrowthWeightConfig(Long id) {
        try {
            EsGrowthWeightConfig growthWeightConfig = this.growthWeightConfigMapper.selectById(id);
            if (growthWeightConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGrowthWeightConfigDO growthWeightConfigDO = new EsGrowthWeightConfigDO();
            BeanUtil.copyProperties(growthWeightConfig, growthWeightConfigDO);
            return DubboResult.success(growthWeightConfigDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败" , ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败" , th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    @Override
    public DubboResult<EsGrowthWeightConfigDO> getGrowthWeightConfigWeight(Integer type) {
        QueryWrapper<EsGrowthWeightConfig> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsGrowthWeightConfig::getState,MemberConstant.growthTypeCommen).eq(EsGrowthWeightConfig::getType,type);
            EsGrowthWeightConfig growthWeightConfig = this.growthWeightConfigMapper.selectOne(queryWrapper);
            if (growthWeightConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGrowthWeightConfigDO growthWeightConfigDO = new EsGrowthWeightConfigDO();
            BeanUtil.copyProperties(growthWeightConfig, growthWeightConfigDO);
            return DubboResult.success(growthWeightConfigDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败" , ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败" , th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGrowthWeightConfigDO>
     */
    @Override
    public DubboPageResult<EsGrowthWeightConfigDO> getGrowthWeightConfigList() {
        QueryWrapper<EsGrowthWeightConfig> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            List<EsGrowthWeightConfig> lists = this.growthWeightConfigMapper.selectList(queryWrapper);
            List<EsGrowthWeightConfigDO> growthWeightConfigDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(lists)) {
                growthWeightConfigDOList = lists.stream().map(growthWeightConfig -> {
                    EsGrowthWeightConfigDO growthWeightConfigDO = new EsGrowthWeightConfigDO();
                    if (null != growthWeightConfig.getId()) {
                        growthWeightConfigDO.setId(growthWeightConfig.getId());
                    }
                    if (null != growthWeightConfig.getType()) {
                        growthWeightConfigDO.setType(growthWeightConfig.getType());
                    }
                    if (null != growthWeightConfig.getWeight()) {
                        growthWeightConfigDO.setWeight(growthWeightConfig.getWeight());
                    }
                    if (null != growthWeightConfig.getState()) {
                        if (growthWeightConfig.getState() == 0) {
                            growthWeightConfigDO.setState(true);
                        } else {
                            growthWeightConfigDO.setState(false);
                        }
                    }
                    BeanUtil.copyProperties(growthWeightConfig, growthWeightConfigDO);
                    return growthWeightConfigDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(growthWeightConfigDOList);
        } catch (ArgumentException ae) {
            logger.error("查询失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败" , th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGrowthWeightConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGrowthWeightConfig(Long id) {
        try {
            this.growthWeightConfigMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败" , th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
