package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCommentLabelDO;
import com.jjg.member.model.domain.EsTagLabelListDO;
import com.jjg.member.model.dto.EsCommentLabelDTO;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCommentLabel;
import com.xdl.jjg.mapper.EsCommentLabelMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCommentLabelService;
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
 * @author lins 1220316142@qq.com
 * @since 2019-06-24
 */
@Service
public class EsCommentLabelServiceImpl extends ServiceImpl<EsCommentLabelMapper, EsCommentLabel> implements IEsCommentLabelService {

    private static Logger logger = LoggerFactory.getLogger(EsCommentLabelServiceImpl.class);

    @Autowired
    private EsCommentLabelMapper commentLabelMapper;

    /**
     * 插入数据
     *
     * @param commentLabelDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCommentLabel(EsCommentLabelDTO commentLabelDTO) {
        try {
            if (commentLabelDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsCommentLabel commentLabel = new EsCommentLabel();
            BeanUtil.copyProperties(commentLabelDTO, commentLabel);
            this.commentLabelMapper.insert(commentLabel);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param commentLabelDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCommentLabel(EsCommentLabelDTO commentLabelDTO) {
        try {
            if (null == commentLabelDTO || commentLabelDTO.getId() == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCommentLabel commentLabel = new EsCommentLabel();
            BeanUtil.copyProperties(commentLabelDTO, commentLabel);
            QueryWrapper<EsCommentLabel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCommentLabel::getId, commentLabelDTO.getId());
            this.commentLabelMapper.update(commentLabel, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    @Override
    public DubboResult<EsCommentLabelDO> getCommentLabel(Long id) {
        try {
            QueryWrapper<EsCommentLabel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCommentLabel::getId, id);
            EsCommentLabel commentLabel = this.commentLabelMapper.selectOne(queryWrapper);
            EsCommentLabelDO commentLabelDO = new EsCommentLabelDO();
            if (null == commentLabel) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(),MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(commentLabel, commentLabelDO);
            return DubboResult.success(commentLabelDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param commentLabelDTO DTO
     * @param pageSize        页码
     * @param pageNum         页数
     * @auther:HQL 236154186@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentLabelDO>
     */
    @Override
    public DubboPageResult<EsCommentLabelDO> getCommentLabelList(EsCommentLabelDTO commentLabelDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCommentLabel> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if (null != commentLabelDTO.getCommentLabel()) {
                queryWrapper.lambda().like(EsCommentLabel::getCommentLabel, commentLabelDTO.getCommentLabel());
            }
            if (null != commentLabelDTO.getType()) {
                queryWrapper.lambda().eq(EsCommentLabel::getType, commentLabelDTO.getType());
            }
            Page<EsCommentLabel> page = new Page<>(pageNum, pageSize);
            IPage<EsCommentLabel> iPage = this.page(page, queryWrapper);
            List<EsCommentLabelDO> commentLabelDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                commentLabelDOList = iPage.getRecords().stream().map(commentLabel -> {
                    EsCommentLabelDO commentLabelDO = new EsCommentLabelDO();
                    BeanUtil.copyProperties(commentLabel, commentLabelDO);
                    return commentLabelDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), commentLabelDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据查询列表
     *
     * @auther:lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentLabelDO>
     */
    @Override
    public DubboResult<EsTagLabelListDO> getCommentLabes(Long categoryId) {
        try {
            // 查询条件
            List<EsCommentLabelDO> result = this.commentLabelMapper.getAllTags(categoryId);
            EsTagLabelListDO esTagLabelListDO = new EsTagLabelListDO();
            List<EsCommentLabelDO> esTagGoodDOS = new ArrayList<>();
            List<EsCommentLabelDO> esTagServicelDOS = new ArrayList<>();
            List<EsCommentLabelDO> esTagDeliveryDOS = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(result)) {
                for (EsCommentLabelDO es : result) {
                    if (null != es.getType() && es.getType() == MemberConstant.goodsTag) {
                        esTagGoodDOS.add(es);
                        esTagLabelListDO.setGoodsTag(esTagGoodDOS);
                    }
                    if (null != es.getType() && es.getType() == MemberConstant.serviceTag) {
                        esTagServicelDOS.add(es);
                        esTagLabelListDO.setServiceTag(esTagServicelDOS);
                    }
                    if (null != es.getType() && es.getType() == MemberConstant.deliveryTag) {
                        esTagDeliveryDOS.add(es);
                        esTagLabelListDO.setDeliveryTag(esTagDeliveryDOS);
                    }
                }
            }
            return DubboResult.success(esTagLabelListDO);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCommentLabel(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsCommentLabel> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCommentLabel::getId, id);
            this.commentLabelMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 批量删除标签内容
     *
     * @param
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteEsCommentLabelBatch(Integer[] ids) {
        try {
            QueryWrapper<EsCommentLabel> esCommentLabelQueryWrapper = new QueryWrapper<>();
            esCommentLabelQueryWrapper.lambda().in(EsCommentLabel::getId, ids);
            this.commentLabelMapper.delete(esCommentLabelQueryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("批量删除标签内容失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("批量删除标签内容失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }
}
