package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCommentSupportDO;
import com.jjg.member.model.dto.EsCommentSupportDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCommentSupport;
import com.xdl.jjg.mapper.EsCommentSupportMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCommentSupportService;
import org.apache.ibatis.annotations.Mapper;
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
 *  评论点赞服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-07 16:56:00
 */
@Service
public class EsCommentSupportServiceImpl extends ServiceImpl<EsCommentSupportMapper, EsCommentSupport> implements IEsCommentSupportService {

    private static Logger logger = LoggerFactory.getLogger(EsCommentSupportServiceImpl.class);

    @Autowired
    private EsCommentSupportMapper commentSupportMapper;

    /**
     * 插入数据
     *
     * @param commentSupportDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCommentSupport(EsCommentSupportDTO commentSupportDTO) {
        QueryWrapper<EsCommentSupport> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsCommentSupport::getMemberId,commentSupportDTO.getMemberId()).eq(EsCommentSupport::getCommentId,commentSupportDTO.getCommentId());
            List<EsCommentSupport> lists = this.commentSupportMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(lists)){
                return DubboResult.success();            }
            EsCommentSupport commentSupport = new EsCommentSupport();
            BeanUtil.copyProperties(commentSupportDTO, commentSupport);
            this.commentSupportMapper.insert(commentSupport);
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
     * @param commentSupportDTO DTO
     * @param id                          主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCommentSupport(EsCommentSupportDTO commentSupportDTO, Long id) {
        try {
            if (commentSupportDTO == null || null == id ||  null == commentSupportDTO.getCommentId()) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            this.commentSupportMapper.delete(commentSupportDTO.getCommentId(),id);
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
     * 根据评论id统计数量
     *
     * @param commentId 评论id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    @Override
    public  DubboResult<Integer> getCommentSupportNum(Long commentId){
        QueryWrapper<EsCommentSupport> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsCommentSupport::getCommentId,commentId);
            Integer num = this.commentSupportMapper.selectCount(queryWrapper);
            return DubboResult.success(num);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    @Override
    public DubboResult<EsCommentSupportDO> getCommentSupport(Long id) {
        try {
            EsCommentSupport commentSupport = this.commentSupportMapper.selectById(id);
            if (commentSupport == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsCommentSupportDO commentSupportDO = new EsCommentSupportDO();
            BeanUtil.copyProperties(commentSupport, commentSupportDO);
            return DubboResult.success(commentSupportDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param commentSupportDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentSupportDO>
     */
    @Override
    public DubboPageResult<EsCommentSupportDO> getCommentSupportList(EsCommentSupportDTO commentSupportDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCommentSupport> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsCommentSupport> page = new Page<>(pageNum, pageSize);
            IPage<EsCommentSupport> iPage = this.page(page, queryWrapper);
            List<EsCommentSupportDO> commentSupportDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                commentSupportDOList = iPage.getRecords().stream().map(commentSupport -> {
                    EsCommentSupportDO commentSupportDO = new EsCommentSupportDO();
                    BeanUtil.copyProperties(commentSupport, commentSupportDO);
                    return commentSupportDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(commentSupportDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCommentSupport(Long id) {
        try {
            this.commentSupportMapper.deleteById(id);
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

    @Override
    public DubboResult judgeCommentSupport(Long commentId, Long memberId) {
        try {
            EsCommentSupport esCommentSupport = commentSupportMapper.judgeCommentSupport(commentId, memberId);
            return DubboResult.success(esCommentSupport);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("查询失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
