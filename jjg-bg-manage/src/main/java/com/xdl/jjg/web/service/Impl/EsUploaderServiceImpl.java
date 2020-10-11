package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsUploader;
import com.xdl.jjg.mapper.EsUploaderMapper;
import com.xdl.jjg.model.domain.EsUploaderDO;
import com.xdl.jjg.model.dto.EsUploaderDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsUploaderService;
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
 *  服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsUploaderServiceImpl extends ServiceImpl<EsUploaderMapper, EsUploader> implements IEsUploaderService {

    private static Logger logger = LoggerFactory.getLogger(EsUploaderServiceImpl.class);

    @Autowired
    private EsUploaderMapper uploaderMapper;

    /**
     * 插入数据
     *
     * @param uploaderDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsUploaderDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertUploader(EsUploaderDTO uploaderDTO) {
        try {
            if (uploaderDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsUploader uploader = new EsUploader();
            BeanUtil.copyProperties(uploaderDTO, uploader);
            this.uploaderMapper.insert(uploader);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param uploaderDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsUploaderDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateUploader(EsUploaderDTO uploaderDTO) {
        try {
            if (uploaderDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsUploader uploader = new EsUploader();
            BeanUtil.copyProperties(uploaderDTO, uploader);
            QueryWrapper<EsUploader> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsUploader::getId, uploaderDTO.getId());
            this.uploaderMapper.update(uploader, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
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
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsUploaderDO>
     */
    @Override
    public DubboResult<EsUploaderDO> getUploader(Long id) {
        try {
            QueryWrapper<EsUploader> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsUploader::getId, id);
            EsUploader uploader = this.uploaderMapper.selectOne(queryWrapper);
            EsUploaderDO uploaderDO = new EsUploaderDO();
            if (uploader == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(uploader, uploaderDO);
            return DubboResult.success(uploaderDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboResult<EsUploaderDO> getUploaderByName(String name) {
        try {
            QueryWrapper<EsUploader> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsUploader::getName, name).eq(EsUploader::getOpen,1);
            EsUploader uploader = this.uploaderMapper.selectOne(queryWrapper);
            EsUploaderDO uploaderDO = new EsUploaderDO();
            if (uploader == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(uploader, uploaderDO);
            return DubboResult.success(uploaderDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 根据查询列表
     *
     * @param uploaderDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsUploaderDO>
     */
    @Override
    public DubboPageResult<EsUploaderDO> getUploaderList(EsUploaderDTO uploaderDTO, int pageSize, int pageNum) {
        QueryWrapper<EsUploader> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsUploader> page = new Page<>(pageNum, pageSize);
            IPage<EsUploader> iPage = this.page(page, queryWrapper);
            List<EsUploaderDO> uploaderDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                uploaderDOList = iPage.getRecords().stream().map(uploader -> {
                    EsUploaderDO uploaderDO = new EsUploaderDO();
                    BeanUtil.copyProperties(uploader, uploaderDO);
                    return uploaderDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(uploaderDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsUploaderDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteUploader(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsUploader> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsUploader::getId, id);
            this.uploaderMapper.delete(deleteWrapper);
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
    }
}
