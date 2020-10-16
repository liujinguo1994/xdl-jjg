package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCommentGalleryDO;
import com.jjg.member.model.dto.EsCommentGalleryDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCommentGallery;
import com.xdl.jjg.mapper.EsCommentGalleryMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCommentGalleryService;
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
 *  评论图片 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsCommentGalleryServiceImpl extends ServiceImpl<EsCommentGalleryMapper, EsCommentGallery> implements IEsCommentGalleryService {

    private static Logger logger = LoggerFactory.getLogger(EsCommentGalleryServiceImpl.class);

    @Autowired
    private EsCommentGalleryMapper commentGalleryMapper;

    /**
     * 插入 评论图片数据
     *
     * @param commentGalleryDTO  评论图片DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCommentGallery(EsCommentGalleryDTO commentGalleryDTO) {
        try {
            if (commentGalleryDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            String[] s = commentGalleryDTO.getOriginal().split(",");
            for(int i =0;i<s.length;i++){
                EsCommentGallery commentGallery = new EsCommentGallery();
                BeanUtil.copyProperties(commentGalleryDTO, commentGallery);
                commentGallery.setOriginal(s[i]);
                commentGallery.setSort(i);
                this.commentGalleryMapper.insert(commentGallery);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(" 评论图片新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error(" 评论图片新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新 评论图片数据
     *
     * @param commentGalleryDTO  评论图片DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCommentGallery(EsCommentGalleryDTO commentGalleryDTO) {
        try {
            if (commentGalleryDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCommentGallery commentGallery = new EsCommentGallery();
            BeanUtil.copyProperties(commentGalleryDTO, commentGallery);
            QueryWrapper<EsCommentGallery> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCommentGallery::getId, commentGalleryDTO.getId());
            this.commentGalleryMapper.update(commentGallery, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(" 评论图片更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error(" 评论图片更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取 评论图片详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    @Override
    public DubboResult<EsCommentGalleryDO> getCommentGallery(Long id) {
        try {
            QueryWrapper<EsCommentGallery> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCommentGallery::getId, id);
            EsCommentGallery commentGallery = this.commentGalleryMapper.selectOne(queryWrapper);
            EsCommentGalleryDO commentGalleryDO = new EsCommentGalleryDO();
            if( null == commentGallery){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(),MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(commentGallery, commentGalleryDO);
            return DubboResult.success(commentGalleryDO);
        } catch (ArgumentException ae){
            logger.error(" 评论图片查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(" 评论图片查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询 评论图片列表
     *
     * @param commentGalleryDTO  评论图片DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentGalleryDO>
     */
    @Override
    public DubboPageResult<EsCommentGalleryDO> getCommentGalleryList(EsCommentGalleryDTO commentGalleryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCommentGallery> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsCommentGallery> page = new Page<>(pageNum, pageSize);
            IPage<EsCommentGallery> iPage = this.page(page, queryWrapper);
            List<EsCommentGalleryDO> commentGalleryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                commentGalleryDOList = iPage.getRecords().stream().map(commentGallery -> {
                    EsCommentGalleryDO commentGalleryDO = new EsCommentGalleryDO();
                    BeanUtil.copyProperties(commentGallery, commentGalleryDO);
                    return commentGalleryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(commentGalleryDOList);
        } catch (ArgumentException ae){
            logger.error(" 评论图片分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error(" 评论图片分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除 评论图片数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCommentGallery(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsCommentGallery> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCommentGallery::getId, id);
            this.commentGalleryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error(" 评论图片删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(" 评论图片删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据commentId获取 评论图片地址列表
     *
     * @param commentId 评论commentId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    @Override
    public DubboPageResult<EsCommentGalleryDO> getCommentGalleryList(Long commentId) {
        if (commentId == null) {
            throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        try {
            List<EsCommentGalleryDO> commentGalleryList = this.commentGalleryMapper.getCommentGalleryList(commentId);
            return DubboPageResult.success(commentGalleryList);
        } catch (ArgumentException ae){
            logger.error(" 评论图片查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(" 评论图片查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据commentId获取 评论图片地址列表
     *
     * @param commentId 评论commentId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    @Override
    public DubboPageResult<String> getCommentImageList(Long commentId) {
        if (commentId == null) {
            throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        try {
            List<String> commentGalleryList = this.commentGalleryMapper.getCommentImageList(commentId);
            return DubboPageResult.success(commentGalleryList);
        } catch (ArgumentException ae){
            logger.error(" 评论图片查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(" 评论图片查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

}
