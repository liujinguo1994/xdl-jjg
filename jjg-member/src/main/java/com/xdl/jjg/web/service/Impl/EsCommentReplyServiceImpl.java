package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.DateUtils;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsCommentReplyDO;
import com.shopx.member.api.model.domain.EsMemberCommentDO;
import com.shopx.member.api.model.domain.dto.EsCommentReplyDTO;
import com.shopx.member.api.model.domain.dto.EsMemberCommentDTO;
import com.shopx.member.api.service.IEsCommentReplyService;
import com.shopx.member.api.service.IEsMemberCommentService;
import com.xdl.jjg.entity.EsCommentReply;
import  com.xdl.jjg.mapper.EsCommentReplyMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论回复(店家回复) 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-24
 */
@Service
public class EsCommentReplyServiceImpl extends ServiceImpl<EsCommentReplyMapper, EsCommentReply> implements IEsCommentReplyService {

    private static Logger logger = LoggerFactory.getLogger(EsCommentReplyServiceImpl.class);

    @Autowired
    private EsCommentReplyMapper commentReplyMapper;
    @Autowired
    private IEsMemberCommentService memberCommentService;

    /**
     * 插入评论回复(店家回复)数据
     *
     * @param commentReplyDTO 评论回复(店家回复)DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/24 10:19:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCommentReply(EsCommentReplyDTO commentReplyDTO, Long[] ids) {
        try {
            if (ids.length < 1) {
                throw new ArgumentException(MemberErrorCode.COMMETNID_NOT_EXIST.getErrorCode(), MemberErrorCode.COMMETNID_NOT_EXIST.getErrorMsg());
            }
            EsMemberCommentDTO memberCommentDTO = new EsMemberCommentDTO();
            memberCommentDTO.setReplyStatus(1);
            for (Long id : ids) {
                DubboResult<EsMemberCommentDO> memberComment = memberCommentService.getMemberComment(id);
                if (memberComment == null) {
                    throw new ArgumentException(MemberErrorCode.COMMETN_NOT_EXIST.getErrorCode(), MemberErrorCode.COMMETN_NOT_EXIST.getErrorMsg());
                }
                commentReplyDTO.setCommentId(id);
                commentReplyDTO.setParentId(id);
                commentReplyDTO.setCreateTime(DateUtils.MILLIS_PER_SECOND);
                commentReplyDTO.setPath(commentReplyDTO.getParentId().toString() + "|");
                EsCommentReply commentReply = new EsCommentReply();
                BeanUtil.copyProperties(commentReplyDTO, commentReply);
                this.commentReplyMapper.insert(commentReply);

                //修改评论回复状态
                memberCommentDTO.setId(id);
                memberCommentService.updateMemberComment(memberCommentDTO);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("评论回复(店家回复)新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("评论回复(店家回复)新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新评论回复(店家回复)数据
     *
     * @param commentReplyDTO 评论回复(店家回复)DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/24 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCommentReply(EsCommentReplyDTO commentReplyDTO) {
        try {
            EsCommentReply esCommentReply = this.commentReplyMapper.selectById(commentReplyDTO.getId());
            if (esCommentReply == null) {
                throw new ArgumentException(MemberErrorCode.NOT_EXIST_COMM.getErrorCode(), MemberErrorCode.NOT_EXIST_COMM.getErrorMsg());
            }
            EsCommentReply commentReply = new EsCommentReply();
            BeanUtil.copyProperties(commentReplyDTO, commentReply);
            QueryWrapper<EsCommentReply> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCommentReply::getId, commentReplyDTO.getId());
            this.commentReplyMapper.update(commentReply, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("评论回复(店家回复)更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论回复(店家回复)更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取评论回复(店家回复)详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/24 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    @Override
    public DubboResult<EsCommentReplyDO> getCommentReply(Long id) {
        try {
            EsCommentReply commentReply = this.commentReplyMapper.selectById(id);
            EsCommentReplyDO commentReplyDO = new EsCommentReplyDO();
            if (commentReply == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(commentReply, commentReplyDO);
            return DubboResult.success(commentReplyDO);
        } catch (ArgumentException ae) {
            logger.error("评论回复(店家回复)查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论回复(店家回复)查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据评论id获取评论回复(店家回复)详情
     *
     * @param commentId 评论id
     * @auther: lns 1220316142@qq.com
     * @date: 2019/07/24 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    @Override
    public DubboResult<EsCommentReplyDO> getCommentReplyByCommentId(Long commentId) {
        QueryWrapper<EsCommentReply> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsCommentReply::getCommentId, commentId);
            EsCommentReply commentReply = this.commentReplyMapper.selectOne(queryWrapper);
            EsCommentReplyDO commentReplyDO = new EsCommentReplyDO();
            if (null == commentReply) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

            BeanUtil.copyProperties(commentReply, commentReplyDO);
            return DubboResult.success(commentReplyDO);
        } catch (ArgumentException ae) {
            logger.error("评论回复(店家回复)查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论回复(店家回复)查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询评论回复(店家回复)列表
     *
     * @param commentReplyDTO 评论回复(店家回复)DTO
     * @param pageSize        页码
     * @param pageNum         页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/24 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentReplyDO>
     */
    @Override
    public DubboPageResult<EsCommentReplyDO> getCommentReplyList(EsCommentReplyDTO commentReplyDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCommentReply> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsCommentReply> page = new Page<>(pageNum, pageSize);
            IPage<EsCommentReply> iPage = this.page(page, queryWrapper);
            List<EsCommentReplyDO> commentReplyDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                commentReplyDOList = iPage.getRecords().stream().map(commentReply -> {
                    EsCommentReplyDO commentReplyDO = new EsCommentReplyDO();
                    BeanUtil.copyProperties(commentReply, commentReplyDO);
                    return commentReplyDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), commentReplyDOList);
        } catch (ArgumentException ae) {
            logger.error("评论回复(店家回复)分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论回复(店家回复)分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除评论回复(店家回复)数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/24 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCommentReply(Long id) {
        try {
            EsCommentReply esCommentReply = this.commentReplyMapper.selectById(id);
            if (esCommentReply == null) {
                throw new ArgumentException(MemberErrorCode.NOT_EXIST_COMM.getErrorCode(), MemberErrorCode.NOT_EXIST_COMM.getErrorMsg());
            }
            this.commentReplyMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("评论回复(店家回复)删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论回复(店家回复)删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
