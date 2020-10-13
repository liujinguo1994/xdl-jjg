package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCommentSortConfig;
import com.xdl.jjg.mapper.EsCommentSortConfigMapper;
import com.xdl.jjg.model.domain.EsCommentSortConfigDO;
import com.xdl.jjg.model.dto.EsCommentConfigDTO;
import com.xdl.jjg.model.dto.EsCommentSortConfigDTO;
import com.xdl.jjg.model.enums.GoodsCommentSortEnums;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsCommentSortConfigService;
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
public class EsCommentSortConfigServiceImpl extends ServiceImpl<EsCommentSortConfigMapper, EsCommentSortConfig> implements IEsCommentSortConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsCommentSortConfigServiceImpl.class);

    @Autowired
    private EsCommentSortConfigMapper commentSortConfigMapper;
    @Autowired
    private IEsGradeWeightConfigService iEsGradeWeightConfigService;

    /**
     * 插入数据
     *
     * @param esCommentConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSortConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCommentSortConfig(EsCommentConfigDTO esCommentConfigDTO) {
        try {
            commentSortConfigMapper.deleteCommentSortConfig();
            for (EsCommentSortConfigDTO esGradeWeightConfigDTO : esCommentConfigDTO.getCommentSortConfigDTOList()) {
                EsCommentSortConfig commentSortConfig = new EsCommentSortConfig();
                BeanUtil.copyProperties(esGradeWeightConfigDTO, commentSortConfig);
                if(GoodsCommentSortEnums.GOOD_COMMENT.getKey().compareTo(esGradeWeightConfigDTO.getCommentType()) == 0){
                    commentSortConfig.setCommentSort(GoodsCommentSortEnums.GOOD_COMMENT.getName());
                }
                if(GoodsCommentSortEnums.BAD_COMMENT.getKey().compareTo(esGradeWeightConfigDTO.getCommentType()) == 0){
                    commentSortConfig.setCommentSort(GoodsCommentSortEnums.BAD_COMMENT.getName());
                }
                if(GoodsCommentSortEnums.COMMONT_COMMENT.getKey().compareTo(esGradeWeightConfigDTO.getCommentType()) == 0){
                    commentSortConfig.setCommentSort(GoodsCommentSortEnums.COMMONT_COMMENT.getName());
                }
                this.commentSortConfigMapper.insert(commentSortConfig);
            }
            DubboResult result = iEsGradeWeightConfigService.insertGradeWeightConfig(esCommentConfigDTO.getGradeWeightConfigDTOList());
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
     * @param commentSortConfigDTOList DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSortConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCommentSortConfig(List<EsCommentSortConfigDTO> commentSortConfigDTOList) {
        try {
            for (EsCommentSortConfigDTO esGradeWeightConfigDTO : commentSortConfigDTOList) {
                EsCommentSortConfig commentSortConfig = new EsCommentSortConfig();
                BeanUtil.copyProperties(esGradeWeightConfigDTO, commentSortConfig);
                commentSortConfig.setCommentSort(null);
                commentSortConfig.setCommentType(null);
                QueryWrapper<EsCommentSortConfig> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsCommentSortConfig::getId, esGradeWeightConfigDTO.getId());
                this.commentSortConfigMapper.update(commentSortConfig, queryWrapper);
            }
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
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSortConfigDO>
     */
    @Override
    public DubboResult<EsCommentSortConfigDO> getCommentSortConfig(Long id) {
        try {
            EsCommentSortConfig commentSortConfig = this.commentSortConfigMapper.selectById(id);
            if (commentSortConfig == null) {
                throw new ArgumentException(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsCommentSortConfigDO commentSortConfigDO = new EsCommentSortConfigDO();
            BeanUtil.copyProperties(commentSortConfig, commentSortConfigDO);
            return DubboResult.success(commentSortConfigDO);
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
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentSortConfigDO>
     */
    @Override
    public DubboPageResult<EsCommentSortConfigDO> getCommentSortConfigList() {
        try {
            // 查询条件
            List<EsCommentSortConfigDO> result = this.commentSortConfigMapper.getCommentSortConfigList();
            return DubboPageResult.success(result);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败" , th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSortConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCommentSortConfig(Long id) {
        try {
            this.commentSortConfigMapper.deleteById(id);
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
