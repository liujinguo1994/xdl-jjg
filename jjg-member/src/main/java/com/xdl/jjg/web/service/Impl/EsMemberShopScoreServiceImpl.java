package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsMemberShopScoreDO;
import com.jjg.member.model.dto.EsMemberShopScoreDTO;
import com.jjg.member.model.enums.MemberPermission;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.dto.EsOrderDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMember;
import com.xdl.jjg.entity.EsMemberShopScore;
import com.xdl.jjg.mapper.EsCommentSortConfigMapper;
import com.xdl.jjg.mapper.EsMemberShopScoreMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMemberCommentService;
import com.xdl.jjg.web.service.IEsMemberShopScoreService;
import com.xdl.jjg.web.service.feign.trade.OrderService;
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
 * 店铺评分 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberShopScoreServiceImpl extends ServiceImpl<EsMemberShopScoreMapper, EsMemberShopScore> implements IEsMemberShopScoreService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberShopScoreServiceImpl.class);

    @Autowired
    private EsMemberShopScoreMapper memberShopScoreMapper;
    @Autowired
    private OrderService iEsOrderService;
    @Autowired
    private EsCommentSortConfigMapper esCommentSortConfigMapper;

    /**
     * 插入店铺评分数据
     *
     * @param memberShopScoreDTO 店铺评分DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberShopScore(EsMemberShopScoreDTO memberShopScoreDTO) {
        try {
            if (memberShopScoreDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //查询该子订单信息
            DubboResult<EsOrderDO> result = iEsOrderService.getEsBuyerOrderInfo(memberShopScoreDTO.getOrderSn());
            if (MemberPermission.BUYER.equals(MemberPermission.BUYER)) {
                // 获取当前用户进行判断
                EsMember member = new EsMember();
                if (null == result.getData() || !member.getId().equals(result.getData().getMemberId())) {
                    throw new ArgumentException(MemberErrorCode.AUTHORITY.getErrorCode(), MemberErrorCode.AUTHORITY.getErrorMsg());
                }
            }
            // 添加店铺评分
            EsMemberShopScore memberShopScore = new EsMemberShopScore();
            BeanUtil.copyProperties(memberShopScoreDTO, memberShopScore);
            if (null != memberShopScoreDTO.getDeliveryScore() && memberShopScoreDTO.getDeliveryScore() > 0) {
                memberShopScore.setDeliveryScore(memberShopScoreDTO.getDeliveryScore());
            }
            if (null != memberShopScoreDTO.getDescriptionScore() && memberShopScoreDTO.getDescriptionScore() > 0) {
                memberShopScore.setDescriptionScore(memberShopScoreDTO.getDescriptionScore());
            }
            if (null != memberShopScoreDTO.getDeliveryScore() && memberShopScoreDTO.getDeliveryScore() > 0) {
                memberShopScore.setServiceScore(memberShopScoreDTO.getServiceScore());
            }
            this.memberShopScoreMapper.insert(memberShopScore);
            //添加评论
            EsOrderDTO esOrderDTO = new EsOrderDTO();
            BeanUtil.copyProperties(result, esOrderDTO);
           return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺评分新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("店铺评分新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店铺评分数据
     *
     * @param memberShopScoreDTO 店铺评分DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberShopScore(EsMemberShopScoreDTO memberShopScoreDTO) {
        try {
            if (memberShopScoreDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberShopScore memberShopScore = new EsMemberShopScore();
            BeanUtil.copyProperties(memberShopScoreDTO, memberShopScore);
            QueryWrapper<EsMemberShopScore> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberShopScore::getId, memberShopScoreDTO.getId());
            this.memberShopScoreMapper.update(memberShopScore, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺评分更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺评分更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺评分详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    @Override
    public DubboResult<EsMemberShopScoreDO> getMemberShopScore(Long id) {
        try {
            QueryWrapper<EsMemberShopScore> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberShopScore::getId, id);
            EsMemberShopScore memberShopScore = this.memberShopScoreMapper.selectOne(queryWrapper);
            EsMemberShopScoreDO memberShopScoreDO = new EsMemberShopScoreDO();
            if (memberShopScore == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberShopScore, memberShopScoreDO);
            return DubboResult.success(memberShopScoreDO);
        } catch (ArgumentException ae) {
            logger.error("店铺评分查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺评分查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsMemberShopScoreDO> getMemberShopScoreByGoodAndMemberAndSn(Long goodId, Long memberId, String orderSn, Long shopId) {
        try {
            QueryWrapper<EsMemberShopScore> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberShopScore::getMemberId, memberId)
                .eq(EsMemberShopScore::getOrderSn, orderSn)
                    .eq(EsMemberShopScore::getGoodId, goodId)
                    .eq(EsMemberShopScore::getShopId, shopId);
            EsMemberShopScore memberShopScore = this.memberShopScoreMapper.selectOne(queryWrapper);
            EsMemberShopScoreDO memberShopScoreDO = new EsMemberShopScoreDO();
            if (memberShopScore == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberShopScore, memberShopScoreDO);
            return DubboResult.success(memberShopScoreDO);
        } catch (ArgumentException ae) {
            logger.error("店铺评分查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺评分查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺评分列表
     *
     * @param memberShopScoreDTO 店铺评分DTO
     * @param pageSize           页码
     * @param pageNum            页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberShopScoreDO>
     */
    @Override
    public DubboPageResult<EsMemberShopScoreDO> getMemberShopScoreList(EsMemberShopScoreDTO memberShopScoreDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberShopScore> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsMemberShopScore> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberShopScore> iPage = this.page(page, queryWrapper);
            List<EsMemberShopScoreDO> memberShopScoreDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberShopScoreDOList = iPage.getRecords().stream().map(memberShopScore -> {
                    EsMemberShopScoreDO memberShopScoreDO = new EsMemberShopScoreDO();
                    BeanUtil.copyProperties(memberShopScore, memberShopScoreDO);
                    return memberShopScoreDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberShopScoreDOList);
        } catch (ArgumentException ae) {
            logger.error("店铺评分分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺评分分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除店铺评分数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberShopScore(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberShopScore> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberShopScore::getId, id);
            this.memberShopScoreMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺评分删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺评分删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

}
