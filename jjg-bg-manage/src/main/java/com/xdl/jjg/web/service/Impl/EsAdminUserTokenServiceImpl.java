package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jjg.system.model.domain.EsAdminUserTokenDO;
import com.jjg.system.model.dto.EsAdminUserTokenDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsAdminUserToken;
import com.xdl.jjg.mapper.EsAdminUserTokenMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.service.ErrorCodeEnum;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsAdminUserTokenService;
import com.xdl.jjg.web.service.SnowFlakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@Service
public class EsAdminUserTokenServiceImpl implements IEsAdminUserTokenService {

    private static Logger logger = LoggerFactory.getLogger(EsAdminUserTokenServiceImpl.class);

    @Autowired
    private EsAdminUserTokenMapper adminUserTokenMapper;

    @Autowired
    private SnowFlakeService snowFlakeService;

    /**
     * 插入数据
     *
     * @param adminUserTokenDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAdminUserToken(EsAdminUserTokenDTO adminUserTokenDTO) {
        try {
            if (adminUserTokenDTO == null) {
                throw new ArgumentException(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMassage());
            }
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), ErrorCodeEnum.SYS_ERROR.getErrorMassage());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param adminUserTokenDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAdminUserToken(EsAdminUserTokenDTO adminUserTokenDTO) {
        try {
            if (adminUserTokenDTO == null) {
                throw new ArgumentException(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMassage());
            }

            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), ErrorCodeEnum.SYS_ERROR.getErrorMassage());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     */
    @Override
    public DubboResult<EsAdminUserToken> getAdminUserToken(String id) {
        try {

            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param delFlag
     * @param pageSize 行数
     * @param pageNum  页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     */
    @Override
    public DubboPageResult getAdminUserTokenList(Boolean delFlag, int pageSize, int pageNum) {

        try {

            return DubboPageResult.success(1l, new ArrayList<>());
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 批量删除
     * 根据主键删除数据
     *
     * @param collectionIds 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAdminUserToken(List<String> collectionIds) {
        try {
            if (CollectionUtils.isEmpty(collectionIds)) {
                throw new ArgumentException(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", collectionIds));
            }

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("查询删除失败");
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), ErrorCodeEnum.SYS_ERROR.getErrorMassage());
        }
    }

    /**
     * 获取用户token信息
     */
    @Override
    public DubboResult<EsAdminUserTokenDO> getUserToken(EsAdminUserTokenDTO adminUserTokenDTO) {

        try {
            QueryWrapper<EsAdminUserToken> queryWrapper = new QueryWrapper<>();
            if (adminUserTokenDTO.getUserId() != null) {
                queryWrapper.lambda().eq(EsAdminUserToken::getUserId, adminUserTokenDTO.getUserId());
            }
            if (!StringUtil.isEmpty(adminUserTokenDTO.getToken())) {
                queryWrapper.lambda().eq(EsAdminUserToken::getToken, adminUserTokenDTO.getToken());
            }
            EsAdminUserToken esAdminUserToken = adminUserTokenMapper.selectOne(queryWrapper);
            if (esAdminUserToken == null) {
                return DubboResult.success();
            }
            EsAdminUserTokenDO esAdminUserTokenDO = new EsAdminUserTokenDO();
            BeanUtil.copyProperties(esAdminUserToken, esAdminUserTokenDO);
            return DubboResult.success(esAdminUserTokenDO);
        } catch (ArgumentException ae) {
            logger.error("获取用户token信息失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("获取用户token信息失败", ae);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
