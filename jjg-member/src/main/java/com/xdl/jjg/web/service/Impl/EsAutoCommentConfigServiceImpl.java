package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsAutoCommentConfigDO;
import com.jjg.member.model.dto.EsAutoCommentConfigDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsAutoCommentConfig;
import com.xdl.jjg.mapper.EsAutoCommentConfigMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsAutoCommentConfigService;
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
 *  自动评论服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-16 14:19:40
 */
@Service
public class EsAutoCommentConfigServiceImpl extends ServiceImpl<EsAutoCommentConfigMapper, EsAutoCommentConfig> implements IEsAutoCommentConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsAutoCommentConfigServiceImpl.class);

    @Autowired
    private EsAutoCommentConfigMapper autoCommentConfigMapper;

    /**
     * 插入数据
     *
     * @param autoCommentConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAutoCommentConfig(EsAutoCommentConfigDTO autoCommentConfigDTO) {
        try {
            EsAutoCommentConfig autoCommentConfig = new EsAutoCommentConfig();
            BeanUtil.copyProperties(autoCommentConfigDTO, autoCommentConfig);
            this.autoCommentConfigMapper.delete();
            this.autoCommentConfigMapper.insert(autoCommentConfig);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param autoCommentConfigDTO DTO
     * @param id                          主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAutoCommentConfig(EsAutoCommentConfigDTO autoCommentConfigDTO, Long id) {
        try {
            EsAutoCommentConfig autoCommentConfig = this.autoCommentConfigMapper.selectById(id);
            if (autoCommentConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(autoCommentConfigDTO, autoCommentConfig);
            QueryWrapper<EsAutoCommentConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAutoCommentConfig::getId, id);
            this.autoCommentConfigMapper.update(autoCommentConfig, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
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
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    @Override
    public DubboResult<EsAutoCommentConfigDO> getAutoCommentConfig(Long id) {
        try {
            EsAutoCommentConfig autoCommentConfig = this.autoCommentConfigMapper.selectById(id);
            EsAutoCommentConfigDO autoCommentConfigDO = new EsAutoCommentConfigDO();
            if(null == autoCommentConfig){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(),MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(autoCommentConfig, autoCommentConfigDO);
            return DubboResult.success(autoCommentConfigDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 获取列表
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    @Override
    public DubboResult<EsAutoCommentConfigDO> getAutoCommentConfigList() {
        QueryWrapper<EsAutoCommentConfig> queryWrapper = new QueryWrapper<>();
        try {
            List<EsAutoCommentConfig> autoCommentConfigLists = this.autoCommentConfigMapper.selectList(queryWrapper);
            EsAutoCommentConfigDO autoCommentConfigDO = new EsAutoCommentConfigDO();
            if(CollectionUtils.isEmpty(autoCommentConfigLists) || autoCommentConfigLists.size() == 0){
                return DubboResult.success(autoCommentConfigDO);
            }
            BeanUtil.copyProperties(autoCommentConfigLists.get(0), autoCommentConfigDO);
            return DubboResult.success(autoCommentConfigDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据查询列表
     *
     * @param autoCommentConfigDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsAutoCommentConfigDO>
     */
    @Override
    public DubboPageResult<EsAutoCommentConfigDO> getAutoCommentConfig(EsAutoCommentConfigDTO autoCommentConfigDTO, int pageSize, int pageNum) {
        QueryWrapper<EsAutoCommentConfig> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsAutoCommentConfig> page = new Page<>(pageNum, pageSize);
            IPage<EsAutoCommentConfig> iPage = this.page(page, queryWrapper);
            List<EsAutoCommentConfigDO> autoCommentConfigDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                autoCommentConfigDOList = iPage.getRecords().stream().map(autoCommentConfig -> {
                    EsAutoCommentConfigDO autoCommentConfigDO = new EsAutoCommentConfigDO();
                    BeanUtil.copyProperties(autoCommentConfig, autoCommentConfigDO);
                    return autoCommentConfigDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(autoCommentConfigDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
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
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAutoCommentConfig(Long id) {
        try {
            this.autoCommentConfigMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 系统自动评论
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
   /* @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult InsertAutoComment() {
        try {
            iEsOrderService
            this.autoCommentConfigMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }*/
}
