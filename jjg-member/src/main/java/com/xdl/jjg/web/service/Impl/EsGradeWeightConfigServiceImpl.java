package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsGradeWeightConfigDTO;
import com.jjg.member.model.enums.CommentSortScoreEnums;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsGradeWeightConfig;
import com.xdl.jjg.mapper.EsGradeWeightConfigMapper;
import com.xdl.jjg.model.domain.EsGradeWeightConfigDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGradeWeightConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:11
 */
@Service
public class EsGradeWeightConfigServiceImpl extends ServiceImpl<EsGradeWeightConfigMapper, EsGradeWeightConfig> implements IEsGradeWeightConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsGradeWeightConfigServiceImpl.class);

    @Autowired
    private EsGradeWeightConfigMapper gradeWeightConfigMapper;

    /**
     * 插入数据
     *
     * @param gradeWeightConfigDTOList DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGradeWeightConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGradeWeightConfig(List<EsGradeWeightConfigDTO> gradeWeightConfigDTOList) {
        try {
            gradeWeightConfigMapper.deleteEsGradeConfig();
            for (EsGradeWeightConfigDTO esGradeWeightConfigDTO : gradeWeightConfigDTOList) {
                EsGradeWeightConfig gradeWeightConfig = new EsGradeWeightConfig();
                BeanUtil.copyProperties(esGradeWeightConfigDTO, gradeWeightConfig);
                if (CommentSortScoreEnums.SERVICE_SCORE.getKey().compareTo(esGradeWeightConfigDTO.getCommentType()) == 0) {
                    gradeWeightConfig.setCommentName(CommentSortScoreEnums.SERVICE_SCORE.getName());
                }
                if (CommentSortScoreEnums.GOODS_SCORE.getKey().compareTo(esGradeWeightConfigDTO.getCommentType()) == 0) {
                    gradeWeightConfig.setCommentName(CommentSortScoreEnums.GOODS_SCORE.getName());
                }
                if (CommentSortScoreEnums.CARRAY_SCORE.getKey().compareTo(esGradeWeightConfigDTO.getCommentType()) == 0) {
                    gradeWeightConfig.setCommentName(CommentSortScoreEnums.CARRAY_SCORE.getName());
                }
                this.gradeWeightConfigMapper.insert(gradeWeightConfig);
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
     * @param gradeWeightConfigDTO DTO
     * @param id                   主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGradeWeightConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGradeWeightConfig(List<EsGradeWeightConfigDTO> gradeWeightConfigDTOList) {
        try {
            double result = 0;
            for (EsGradeWeightConfigDTO esGradeWeightConfigDTO : gradeWeightConfigDTOList) {
                result += esGradeWeightConfigDTO.getWeightValue();
            }
            if (result != Double.valueOf(1)) {
                throw new ArgumentException(MemberErrorCode.ERROR_WEIGHT_VALUE.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            for (EsGradeWeightConfigDTO esGradeWeightConfigDTO : gradeWeightConfigDTOList) {
                EsGradeWeightConfig gradeWeightConfig = new EsGradeWeightConfig();
                BeanUtil.copyProperties(esGradeWeightConfigDTO, gradeWeightConfig);
                gradeWeightConfig.setCommentName(null);
                gradeWeightConfig.setCommentType(null);
                QueryWrapper<EsGradeWeightConfig> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsGradeWeightConfig::getId, esGradeWeightConfigDTO.getId());
                this.gradeWeightConfigMapper.update(gradeWeightConfig, queryWrapper);
            }
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
     * @return: com.shopx.common.model.result.DubboResult<EsGradeWeightConfigDO>
     */
    @Override
    public DubboResult<EsGradeWeightConfigDO> getGradeWeightConfig(Long id) {
        try {
            EsGradeWeightConfig gradeWeightConfig = this.gradeWeightConfigMapper.selectById(id);
            if (gradeWeightConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGradeWeightConfigDO gradeWeightConfigDO = new EsGradeWeightConfigDO();
            BeanUtil.copyProperties(gradeWeightConfig, gradeWeightConfigDO);
            return DubboResult.success(gradeWeightConfigDO);
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
     * @return: com.shopx.common.model.result.DubboPageResult<EsGradeWeightConfigDO>
     */
    @Override
    public DubboPageResult<EsGradeWeightConfigDO> getGradeWeightConfigList() {
        try {
            // 查询条件
            List<EsGradeWeightConfigDO> gradeWeightConfig = this.gradeWeightConfigMapper.getEsGradeConfigList();
            return DubboPageResult.success(gradeWeightConfig);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGradeWeightConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGradeWeightConfig(Long id) {
        try {
            this.gradeWeightConfigMapper.deleteById(id);
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
