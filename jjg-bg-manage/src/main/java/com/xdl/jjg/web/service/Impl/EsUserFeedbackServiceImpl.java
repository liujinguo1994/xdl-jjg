package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsUserFeedbackDO;
import com.jjg.system.model.dto.EsUserFeedbackDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsUserFeedback;
import com.xdl.jjg.mapper.EsUserFeedbackMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsUserFeedbackService;
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
 * 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-06-03
 */
@Service
public class EsUserFeedbackServiceImpl extends ServiceImpl<EsUserFeedbackMapper, EsUserFeedback> implements IEsUserFeedbackService {

    private static Logger logger = LoggerFactory.getLogger(EsUserFeedbackServiceImpl.class);

    @Autowired
    private EsUserFeedbackMapper userFeedbackMapper;

    /**
     * 插入数据
     *
     * @param userFeedbackDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertUserFeedback(EsUserFeedbackDTO userFeedbackDTO) {
        try {
            if (userFeedbackDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsUserFeedback userFeedback = new EsUserFeedback();
            BeanUtil.copyProperties(userFeedbackDTO, userFeedback);
            this.userFeedbackMapper.insert(userFeedback);
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
     * @param userFeedbackDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateUserFeedback(EsUserFeedbackDTO userFeedbackDTO) {
        try {
            if (userFeedbackDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsUserFeedback userFeedback = new EsUserFeedback();
            BeanUtil.copyProperties(userFeedbackDTO, userFeedback);
            QueryWrapper<EsUserFeedback> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsUserFeedback::getId, userFeedbackDTO.getId());
            this.userFeedbackMapper.update(userFeedback, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    @Override
    public DubboResult getUserFeedback(Long id) {
        try {
            QueryWrapper<EsUserFeedback> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsUserFeedback::getId, id);
            EsUserFeedback userFeedback = this.userFeedbackMapper.selectOne(queryWrapper);
            EsUserFeedbackDO userFeedbackDO = new EsUserFeedbackDO();
            if (userFeedback != null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(userFeedback, userFeedbackDO);
            return DubboResult.success(userFeedbackDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param userFeedbackDTO DTO
     * @param pageSize        行数
     * @param pageNum         页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsUserFeedbackDO>
     */
    @Override
    public DubboPageResult getUserFeedbackList(EsUserFeedbackDTO userFeedbackDTO, int pageSize, int pageNum) {
        QueryWrapper<EsUserFeedback> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsUserFeedback> page = new Page<>(pageNum, pageSize);
            IPage<EsUserFeedback> iPage = this.page(page, queryWrapper);
            List<EsUserFeedbackDO> userFeedbackDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                userFeedbackDOList = iPage.getRecords().stream().map(userFeedback -> {
                    EsUserFeedbackDO userFeedbackDO = new EsUserFeedbackDO();
                    BeanUtil.copyProperties(userFeedback, userFeedbackDO);
                    return userFeedbackDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(userFeedbackDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteUserFeedback(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsUserFeedback> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsUserFeedback::getId, id);
            this.userFeedbackMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
