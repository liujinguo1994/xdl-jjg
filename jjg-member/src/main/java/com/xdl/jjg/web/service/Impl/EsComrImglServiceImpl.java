package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsComrImgl;
import com.xdl.jjg.mapper.EsComrImglMapper;
import com.xdl.jjg.model.domain.EsComrImglDO;
import com.xdl.jjg.model.dto.EsComrImglDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsComrImglService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * <p>
 * 投诉举报图片 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-31
 */
@Service
public class EsComrImglServiceImpl extends ServiceImpl<EsComrImglMapper, EsComrImgl> implements IEsComrImglService {

    private static Logger logger = LoggerFactory.getLogger(EsComrImglServiceImpl.class);

    @Autowired
    private EsComrImglMapper esComrImglMapper;


    @Override
    public DubboResult insertComr(EsComrImglDTO esComrImglDTO) {
        try {
            EsComrImgl esComrImgl = new EsComrImgl();
            BeanUtil.copyProperties(esComrImglDTO, esComrImgl);
            this.esComrImglMapper.insert(esComrImgl);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("图片新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("图片新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult updateComr(EsComrImglDTO esComrImglDTO) {
        try {
            EsComrImgl esComrImgl = this.esComrImglMapper.selectById(esComrImglDTO.getId());
            if (esComrImgl==null){
                return DubboResult.fail(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsComrImgl comrImgl = new EsComrImgl();
            BeanUtil.copyProperties(esComrImglDTO, comrImgl);
            QueryWrapper<EsComrImgl> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsComrImgl::getId, esComrImglDTO.getId());
            this.esComrImglMapper.update(comrImgl, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("图片更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("图片更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsComrImglDO> getComr(Long id) {
        try {
            if (id==null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsComrImgl esComrImgl = this.esComrImglMapper.selectById(id);
            if (esComrImgl == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsComrImgl comrImgl=new EsComrImgl();
            BeanUtil.copyProperties(esComrImgl, comrImgl);
            return DubboResult.success(comrImgl);
        } catch (ArgumentException ae){
            logger.error("图片查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("图片查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsComrImglDO> getComrList(Long comId,Integer type) {
        QueryWrapper<EsComrImgl> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsComrImgl::getComId,comId).eq(EsComrImgl::getType,type);
            List<EsComrImgl> esComrImgls = this.esComrImglMapper.selectList(queryWrapper);
            return DubboPageResult.success(esComrImgls);
        } catch (ArgumentException ae){
            logger.error("列表查询相册失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("列表查询相册失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult deleteComrBuyer(Long id,Integer type) {
        try {
            QueryWrapper<EsComrImgl> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsComrImgl::getType,type).eq(EsComrImgl::getComId, id);
            this.esComrImglMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("批量删除相册失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("批量删除相册失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult deleteComr(Integer[] ids,Integer type) {
        try {
            QueryWrapper<EsComrImgl> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsComrImgl::getType,type).in(EsComrImgl::getComId, ids);
            this.esComrImglMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("批量删除相册失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("批量删除相册失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
