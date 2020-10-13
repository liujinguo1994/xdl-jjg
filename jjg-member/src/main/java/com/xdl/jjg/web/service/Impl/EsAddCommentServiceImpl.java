package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsAddComment;
import com.xdl.jjg.mapper.EsAddCommentMapper;
import com.xdl.jjg.model.domain.EsAddCommentDO;
import com.xdl.jjg.model.dto.EsAddCommentDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsAddCommentPictureService;
import com.xdl.jjg.web.service.IEsAddCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  追加评论服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-10-08 15:19:23
 */
@Service
public class EsAddCommentServiceImpl extends ServiceImpl<EsAddCommentMapper, EsAddComment> implements IEsAddCommentService {

    private static Logger logger = LoggerFactory.getLogger(EsAddCommentServiceImpl.class);

    @Autowired
    private EsAddCommentMapper addCommentMapper;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsAddCommentPictureService addCommentPictureService;

    /**
     * 插入数据
     *
     * @param addCommentDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAddComment(EsAddCommentDTO addCommentDTO) {
        try {
            DubboResult<EsAddCommentDO> result = this.getAddCommentByCommentId(addCommentDTO.getCommentId());
            if(result.isSuccess() &&  null != result.getData()){
                throw new ArgumentException(MemberErrorCode.EXISTS_ADD_COMMENT.getErrorCode(), MemberErrorCode.EXISTS_ADD_COMMENT.getErrorMsg());
            }
            EsAddComment addComment = new EsAddComment();
            BeanUtil.copyProperties(addCommentDTO, addComment);
            this.addCommentMapper.insert(addComment);
            String[] originals = null;
            if(addCommentDTO.getOriginal() != null){
                String original = addCommentDTO.getOriginal();
                originals = original.split(",");
            }
            if(originals != null){
                addCommentPictureService.addPictureBatch(addComment.getId(), new ArrayList<>(Arrays.asList(originals)));
            }
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
     * @param addCommentDTO DTO
     * @param id                          主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAddComment(EsAddCommentDTO addCommentDTO, Long id) {
        try {
            EsAddComment addComment = this.addCommentMapper.selectById(id);
            if (addComment == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(addCommentDTO, addComment);
            QueryWrapper<EsAddComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAddComment::getId, id);
            this.addCommentMapper.update(addComment, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    @Override
    public DubboResult<EsAddCommentDO> getAddComment(Long id) {
        try {
            EsAddComment addComment = this.addCommentMapper.selectById(id);
            if (addComment == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsAddCommentDO addCommentDO = new EsAddCommentDO();
            BeanUtil.copyProperties(addComment, addCommentDO);
            return DubboResult.success(addCommentDO);
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
     * @param commentId 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    @Override
    public DubboResult<EsAddCommentDO> getAddCommentByCommentId(Long commentId) {
        QueryWrapper<EsAddComment> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsAddComment::getCommentId,commentId);
            EsAddComment addComment = this.addCommentMapper.selectOne(queryWrapper);
            if (addComment == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsAddCommentDO addCommentDO = new EsAddCommentDO();
            BeanUtil.copyProperties(addComment, addCommentDO);
            return DubboResult.success(addCommentDO);
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
     * @param addCommentDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsAddCommentDO>
     */
    @Override
    public DubboPageResult<EsAddCommentDO> getAddCommentList(EsAddCommentDTO addCommentDTO, int pageSize, int pageNum) {
        QueryWrapper<EsAddComment> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsAddComment> page = new Page<>(pageNum, pageSize);
            IPage<EsAddComment> iPage = this.page(page, queryWrapper);
            List<EsAddCommentDO> addCommentDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                addCommentDOList = iPage.getRecords().stream().map(addComment -> {
                    EsAddCommentDO addCommentDO = new EsAddCommentDO();
                    BeanUtil.copyProperties(addComment, addCommentDO);
                    return addCommentDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(addCommentDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAddComment(Long id) {
        try {
            this.addCommentMapper.deleteById(id);
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
}
