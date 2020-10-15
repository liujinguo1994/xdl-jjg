package com.xdl.jjg.web.service.Impl;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.*;
import com.jjg.member.model.dto.*;
import com.jjg.member.model.enums.CommentSortEnums;
import com.jjg.member.model.enums.GoodsCommentSortEnums;
import com.jjg.member.model.vo.wap.EsWapMemberSpecValuesVO;
import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsOrderItemsDO;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.*;
import com.xdl.jjg.mapper.*;
import com.xdl.jjg.model.domain.EsGrowthValueStrategyDO;
import com.xdl.jjg.model.domain.EsMemberCommentDO;
import com.xdl.jjg.model.domain.EsMemberDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.web.service.*;
import jodd.typeconverter.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberCommentServiceImpl extends ServiceImpl<EsMemberCommentMapper, EsMemberComment> implements IEsMemberCommentService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberCommentServiceImpl.class);

    @Autowired
    private EsMemberCommentMapper memberCommentMapper;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsOrderItemsService iEsOrderItemsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberShopScoreService iEsMemberShopScoreService;
    @Autowired
    private EsMemberShopScoreMapper memberShopScoreMapper;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsOrderService iEsOrderService;
    private IEsMemberCommentService esMemberCommentService;
    @Autowired
    private EsCommentSortConfigMapper esCommentSortConfigMapper;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCommentGalleryService iEsCommentGalleryService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCommentReplyService iEsCommentReplyService;
    @Autowired
    private EsCommentGalleryMapper commentGalleryMapper;
    @Autowired
    private EsCommentLabelMapper commentLabelMapper;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGrowthValueStrategyService iEsGrowthValueStrategyService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService iEsMemberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGoodsService iEsGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCommentSupportService commentSupportService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsAutoCommentConfigService iEsAutoCommentConfigService;
    @Autowired
    private EsAddCommentMapper addCommentMapper;
    @Autowired
    private EsCommentReplyMapper commentReplyMapper;

    @Autowired
    private IEsAddCommentPictureService addCommentPictureService;

    @Reference(version = "${dubbo.application.version}" ,timeout = 50000,check = false)
    private IEsGoodsSkuService skuService;
    /**
     * 插入评论数据
     *
     * @param memberCommentDTO 评论DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberComment(EsMemberCommentCopyDTO memberCommentDTO) {
        try {
            if (memberCommentDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //判断是否已有评论
            QueryWrapper<EsMemberComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberComment::getOrderSn , memberCommentDTO.getOrderSn())
                    .eq(EsMemberComment::getGoodsId, memberCommentDTO.getGoodsId()).eq(EsMemberComment::getSkuId,memberCommentDTO.getSkuId());
            EsMemberComment memberComment = memberCommentMapper.selectOne(queryWrapper);
            if(memberComment != null){
                throw new ArgumentException(MemberErrorCode.COMMENTED.getErrorCode(), MemberErrorCode.COMMENTED.getErrorMsg());
            }
            EsCommentGalleryDTO esCommentGalleryDTO = new EsCommentGalleryDTO();
            DubboResult<EsMemberCommentDO> resultScore = null;
            memberComment = new EsMemberComment();
            BeanUtil.copyProperties(memberCommentDTO, memberComment);

            DubboResult<EsGoodsDO> goodsResult = iEsGoodsService.buyGetEsGoods(memberComment.getGoodsId());
            if (goodsResult.isSuccess() && null != goodsResult.getData() && null != goodsResult.getData().getGoodsName()) {
                memberComment.setGoodsName(goodsResult.getData().getGoodsName());
                memberComment.setGoodsSn(goodsResult.getData().getGoodsSn());
            }

            DubboResult<EsMemberDO> memberResult = iEsMemberService.getAdminMember(memberComment.getMemberId());
            if (memberResult.isSuccess() && null != memberResult.getData()) {
                memberComment.setMemberFace(memberResult.getData().getFace());
                memberComment.setMemberName(memberResult.getData().getName());
            }

            if (null != memberCommentDTO.getDeliveryScore() || null != memberCommentDTO.getDescriptionScore() || null != memberCommentDTO.getServiceScore()) {
                EsMemberShopScoreDTO esMemberShopScoreDTO = new EsMemberShopScoreDTO();
                BeanUtil.copyProperties(memberCommentDTO, esMemberShopScoreDTO);
                //插入店铺评分
                DubboResult resultShopScore = this.insertShopScore(esMemberShopScoreDTO);
                if (!resultShopScore.isSuccess()) {
                    throw new ArgumentException(resultShopScore.getCode(), resultScore.getMsg());
                }
                //计算好中差
                resultScore = this.getsCountCommentType(esMemberShopScoreDTO);
                if (!resultScore.isSuccess()) {
                    throw new ArgumentException(resultScore.getCode(), resultScore.getMsg());
                }
            }
            //添加好中差评和评分
            if (null != resultScore) {
                memberComment.setGrade(resultScore.getData().getGrade());
                memberComment.setCommentScore(resultScore.getData().getCommentScore());
            }
            memberComment.setReplyStatus(MemberConstant.noRepalyContent);
            memberComment.setState(MemberConstant.commentDefault);
            if (CollectionUtils.isNotEmpty(memberCommentDTO.getOriginal())){
                memberComment.setHaveImage(1);
            }

            memberCommentMapper.insert(memberComment);
            if(CollectionUtils.isNotEmpty(memberCommentDTO.getOriginal())){
                //添加评论图片
                List<String> original = memberCommentDTO.getOriginal();
                for (String s : original) {
                    esCommentGalleryDTO.setOriginal(s);
                    esCommentGalleryDTO.setCommentId(memberComment.getId());
                    iEsCommentGalleryService.insertCommentGallery(esCommentGalleryDTO);
                }

            }
            //评价添加成长值
            DubboResult<EsGrowthValueStrategyDO> resultInfo = iEsGrowthValueStrategyService.getComandcolleGrowthvalueConfigByType(MemberConstant.collectionComment);
            int num = getCollectNum(memberCommentDTO.getMemberId());
            //判断是否添加成长值
            if (resultInfo.isSuccess() && null != resultInfo.getData() && null != resultInfo.getData().getLimitNum()) {
                if (num <= resultInfo.getData().getLimitNum()) {
                    iEsMemberService.updateGrowthValue(memberCommentDTO.getMemberId(), resultInfo.getData().getGrowthValue(), true, MemberConstant.collectionStragety);
                }
            }
            //修改交易模块评论状态
            DubboResult<EsOrderItemsDO> resultState = iEsOrderItemsService.updateOrderCommentStatus(memberCommentDTO.getOrderSn(), memberCommentDTO.getGoodsId(),memberCommentDTO.getSkuId());
            if (!resultState.isSuccess()) {
                throw new ArgumentException(resultState.getCode(), resultState.getMsg());
            }
            //修改商品评论数量
            iEsGoodsService.updateCommenCount(memberCommentDTO.getGoodsId());
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("评论新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("评论新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 自动评论插入评论数据
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult AutoInsertMemberComment() {

        try {
            //调用交易模块查询未评论的订单
            List<EsOrderItemsDO> es = iEsOrderItemsService.selectNotCommentGoods();
            if (CollectionUtils.isEmpty(es) || es.size() == 0) {
                return DubboResult.success();
            }
            es.forEach(esItemsDO -> {
                EsMemberComment memberComment = new EsMemberComment();
                if (null != esItemsDO.getGoodsId()) {
                    memberComment.setGoodsId(esItemsDO.getGoodsId());
                    DubboResult<EsGoodsDO> goodsResult = iEsGoodsService.buyGetEsGoods(esItemsDO.getGoodsId());
                    if (goodsResult.isSuccess() && null != goodsResult.getData() && null != goodsResult.getData().getGoodsName()) {
                        memberComment.setGoodsName(goodsResult.getData().getGoodsName());
                    }
                    if (goodsResult.isSuccess() && null != goodsResult.getData() && null != goodsResult.getData().getGoodsSn()) {
                        memberComment.setGoodsSn(goodsResult.getData().getGoodsSn());
                    }
                }
                if ( null != esItemsDO && null != esItemsDO.getSkuId()) {
                    memberComment.setSkuId(esItemsDO.getSkuId());
                }
                if ( null != esItemsDO &&  null != esItemsDO.getOrderSn()) {
                    memberComment.setOrderSn(esItemsDO.getOrderSn());
                    DubboResult<EsOrderDO> esResult = iEsOrderService.getEsBuyerOrderInfo(esItemsDO.getOrderSn());
                    if (esResult.isSuccess() && null != esResult.getData() && null != esResult.getData().getMemberId() && null != esResult.getData().getShopId()) {
                        memberComment.setMemberId(esResult.getData().getMemberId());
                        memberComment.setShopId(esResult.getData().getShopId());
                        DubboResult<EsMemberDO> memberResult = iEsMemberService.getAdminMember(esResult.getData().getMemberId());
                        if (memberResult.isSuccess() && null != memberResult.getData() && null != memberResult.getData().getFace()) {
                            memberComment.setMemberFace(memberResult.getData().getFace());
                        }

                    }
                    if (esResult.isSuccess() && null != esResult.getData() && esResult.getData().getMemberName() != null) {
                        memberComment.setMemberName(esResult.getData().getMemberName());
                    }

                }
                DubboResult<EsAutoCommentConfigDO> resultAuto = iEsAutoCommentConfigService.getAutoCommentConfigList();
                if (resultAuto.isSuccess() && null != resultAuto.getData() && null != resultAuto.getData().getAutoComment()) {
                    memberComment.setContent(resultAuto.getData().getAutoComment());
                }
                //默认无回复
                memberComment.setReplyStatus(MemberConstant.noRepalyContent);
                memberComment.setHaveImage(MemberConstant.emptyImage);
                memberComment.setState(MemberConstant.defaultCommentState);
                //默认好评
                memberComment.setGrade(String.valueOf(MemberConstant.goodsComment));
                this.memberCommentMapper.insert(memberComment);
                //查询该商品标签
                //修改交易模块评论状态
                DubboResult<EsOrderItemsDO> resultState = iEsOrderItemsService.updateOrderCommentStatus(esItemsDO.getOrderSn(), esItemsDO.getGoodsId(),esItemsDO.getSkuId());
                if (!resultState.isSuccess()) {
                    throw new ArgumentException(resultState.getCode(), resultState.getMsg());
                }
            });
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("评论新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("评论新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 好中差评价计算，综合评分计算
     *
     * @param memberShopScoreDTO
     * @return
     */
    public DubboResult<EsMemberCommentDO> getsCountCommentType(EsMemberShopScoreDTO memberShopScoreDTO) {
        EsMemberCommentDO esMemberCommentDO = new EsMemberCommentDO();
        try {
            if (null == memberShopScoreDTO) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", memberShopScoreDTO));
            }
            //查询不同服务类型的权重
            com.xdl.jjg.model.domain.EsGradeWeightConfigDO deliveryDO = this.memberShopScoreMapper.getScoreWeightValue(Integer.valueOf(CommentSortEnums.DELIVERY.getKey()));
            com.xdl.jjg.model.domain.EsGradeWeightConfigDO goodsDO = this.memberShopScoreMapper.getScoreWeightValue(Integer.valueOf(CommentSortEnums.DESCRIPTION.getKey()));
            com.xdl.jjg.model.domain.EsGradeWeightConfigDO serviceDO = this.memberShopScoreMapper.getScoreWeightValue(Integer.valueOf(CommentSortEnums.SERVICE.getKey()));
            if (null == deliveryDO || null == deliveryDO.getWeightValue() || null == goodsDO || null == goodsDO.getWeightValue() || null == serviceDO || null == serviceDO.getWeightValue()) {
                throw new ArgumentException(MemberErrorCode.ERROR_WEIGHT_VALUE.getErrorCode(), MemberErrorCode.ERROR_WEIGHT_VALUE.getErrorMsg());
            }
            Double commentScore = memberShopScoreDTO.getDeliveryScore() * deliveryDO.getWeightValue() + memberShopScoreDTO.getDescriptionScore() * goodsDO.getWeightValue() + memberShopScoreDTO.getServiceScore() * serviceDO.getWeightValue();
            EsCommentSortConfigDO esCommentSortConfigGood = this.esCommentSortConfigMapper.getCommentSortLevel(Integer.valueOf(GoodsCommentSortEnums.GOOD_COMMENT.getKey()));
            EsCommentSortConfigDO esCommentSortConfigComment = this.esCommentSortConfigMapper.getCommentSortLevel(Integer.valueOf(GoodsCommentSortEnums.COMMONT_COMMENT.getKey()));
            if (esCommentSortConfigGood.getMinScore() < commentScore) {//好评
                esMemberCommentDO.setCommentScore(commentScore);
                esMemberCommentDO.setGrade(GoodsCommentSortEnums.GOOD_COMMENT.getKey().toString());
                return DubboResult.success(esMemberCommentDO);
            } else if (esCommentSortConfigComment.getMinScore() < commentScore) {//中评
                esMemberCommentDO.setCommentScore(commentScore);
                esMemberCommentDO.setGrade(GoodsCommentSortEnums.COMMONT_COMMENT.getKey().toString());
                return DubboResult.success(esMemberCommentDO);
            } else {//差评
                esMemberCommentDO.setCommentScore(commentScore);
                esMemberCommentDO.setGrade(GoodsCommentSortEnums.BAD_COMMENT.getKey().toString());
                return DubboResult.success(esMemberCommentDO);
            }
        } catch (ArgumentException ae) {
            logger.error("好评中评差评查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("好评中评差评查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据条件更新评论数据
     *
     * @param memberCommentDTO 评论DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberComment(EsMemberCommentDTO memberCommentDTO) {
        try {
            if (memberCommentDTO == null || memberCommentDTO.getId() == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberComment memberComment = new EsMemberComment();
//            memberComment.setId(memberCommentDTO.getId());
//            if (StringUtils.isNotEmpty(memberCommentDTO.getContent())) {
//                memberComment.setContent(memberCommentDTO.getContent());
//            }
//            if (StringUtils.isNotEmpty(memberCommentDTO.getGrade())) {
//                memberComment.setGrade(memberCommentDTO.getGrade());
//            }
//            if (memberCommentDTO.getReplyStatus() != null) {
//                memberComment.setReplyStatus(memberCommentDTO.getReplyStatus());
//            }

            BeanUtil.copyProperties(memberCommentDTO, memberComment);
            QueryWrapper<EsMemberComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberComment::getId, memberCommentDTO.getId());
            this.memberCommentMapper.update(memberComment, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("评论更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取评论详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @Override
    public DubboResult<EsMemberCommentDO> getMemberComment(Long id) {
        try {
            QueryWrapper<EsMemberComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberComment::getId, id);
            EsMemberComment memberComment = this.memberCommentMapper.selectOne(queryWrapper);
            EsMemberCommentDO memberCommentDO = new EsMemberCommentDO();
            if (memberComment == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberComment, memberCommentDO);
            return DubboResult.success(memberCommentDO);
        } catch (ArgumentException ae) {
            logger.error("评论查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 依据订单号和商品id查询评价信息
     * PC端 单独提供
     * @param goodsId
     * @param orderSn
     * @return
     */
    public DubboResult<com.xdl.jjg.model.domain.EsCommentInfoDO> getPCCommentInfoByGoodsIdAndOrderSn(Long goodsId, String orderSn, Long skuId) {
        try {
            com.xdl.jjg.model.domain.EsCommentInfoDO esCommentInfoCopyDO = new com.xdl.jjg.model.domain.EsCommentInfoDO();
            com.xdl.jjg.model.domain.EsCommentInfoDO esCommentInfoDO = memberCommentMapper.getCommentByOrdersAndGoodsId(goodsId, orderSn,skuId);
            if (null == esCommentInfoDO) {
                return DubboResult.success(esCommentInfoCopyDO);
            }
            BeanUtil.copyProperties(esCommentInfoDO, esCommentInfoCopyDO);


            List<String> labelList = new ArrayList<>();
            List<com.xdl.jjg.model.domain.EsCommentGalleryDO> commentGalleryList = this.commentGalleryMapper.getCommentGalleryList(esCommentInfoDO.getId());
            if (CollectionUtils.isNotEmpty(commentGalleryList)) {
                //查询图片信息

                List<CommentImageVO> commentImageVOList = new ArrayList<>();

                for (com.xdl.jjg.model.domain.EsCommentGalleryDO esCommentGalleryDO : commentGalleryList) {
                    CommentImageVO commentImageVO1 = new CommentImageVO();
                    commentImageVO1.setUrl(esCommentGalleryDO.getOriginal());
                    commentImageVOList.add(commentImageVO1);
                }
                esCommentInfoCopyDO.setCommentImageVOList(commentImageVOList);
            }

            //查询标签信息
            QueryWrapper<EsCommentLabel> queryWrapper = new QueryWrapper<>();
            String labels = esCommentInfoDO.getLabels();
            if (!StringUtils.isBlank(labels)) {
                String[] labs = labels.split(",");
                Integer[] la = Convert.toIntArray(labs);
                queryWrapper.lambda().in(EsCommentLabel::getId, la);
                List<EsCommentLabel> es = this.commentLabelMapper.selectList(queryWrapper);
                for (EsCommentLabel labelsInfo : es) {
                    labelList.add(labelsInfo.getCommentLabel());
                }
                esCommentInfoCopyDO.setTags(labelList);
            }
            // 服务评分
            DubboResult<com.xdl.jjg.model.domain.EsMemberShopScoreDO> scoreResult = iEsMemberShopScoreService.getMemberShopScoreByGoodAndMemberAndSn(goodsId, esCommentInfoDO.getMemberId(), orderSn, esCommentInfoDO.getShopId());
            if(scoreResult.isSuccess()){
                esCommentInfoCopyDO.setDescriptionScore(scoreResult.getData().getDeliveryScore());
                esCommentInfoCopyDO.setServiceScore(scoreResult.getData().getServiceScore());
                esCommentInfoCopyDO.setDeliveryScore(scoreResult.getData().getDeliveryScore());
            }

            //追加评论
            EsAddComment addComment = addCommentMapper.getAddCommentByCommentId(esCommentInfoCopyDO.getId());
            com.xdl.jjg.model.domain.EsAddCommentDO addCommentDo = new com.xdl.jjg.model.domain.EsAddCommentDO();
            if(addComment != null){
                BeanUtil.copyProperties(addComment, addCommentDo);
                DubboPageResult<EsAddCommentPictureDO> result= addCommentPictureService.getAddCommentPictureList(addComment.getId());
                if(result.isSuccess()){
                    if(CollectionUtils.isNotEmpty(result.getData().getList())){
                        List<String> originalList = result.getData().getList().stream().map(EsAddCommentPictureDO::getOriginal).collect(Collectors.toList());
                        addCommentDo.setOriginal(originalList);
                    }else{
                        addCommentDo.setOriginal(new ArrayList<>());
                    }
                }
                esCommentInfoCopyDO.setAddContentDO(addCommentDo);
            }
            return DubboResult.success(esCommentInfoCopyDO);
        } catch (ArgumentException ae) {
            logger.error("评论查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 依据订单号和商品id查询评价信息
     *
     * @param goodsId
     * @param orderSn
     * @return
     */
    public DubboResult<com.xdl.jjg.model.domain.EsCommentInfoDO> getCommentInfoByGoodsIdAndOrderSn(Long goodsId, String orderSn, Long skuId) {
        try {
            com.xdl.jjg.model.domain.EsCommentInfoDO esCommentInfoCopyDO = new com.xdl.jjg.model.domain.EsCommentInfoDO();
            com.xdl.jjg.model.domain.EsCommentInfoDO esCommentInfoDO = memberCommentMapper.getCommentByOrdersAndGoodsId(goodsId, orderSn,skuId);
            if (null == esCommentInfoDO) {
                return DubboResult.success(esCommentInfoCopyDO);
            }
            BeanUtil.copyProperties(esCommentInfoDO, esCommentInfoCopyDO);


            BeanUtil.copyProperties(esCommentInfoDO, esCommentInfoCopyDO);
            //查询图片信息
            List<String> images = new ArrayList<>();
            List<String> labelList = new ArrayList<>();
            List<com.xdl.jjg.model.domain.EsCommentGalleryDO> commentGalleryList = this.commentGalleryMapper.getCommentGalleryList(esCommentInfoDO.getId());
            if (CollectionUtils.isNotEmpty(commentGalleryList)) {
                for (com.xdl.jjg.model.domain.EsCommentGalleryDO esCommentGalleryDO : commentGalleryList) {
                    images.add(esCommentGalleryDO.getOriginal());
                }
                esCommentInfoCopyDO.setOriginals(images);
            }
//
//            List<String> labelList = new ArrayList<>();
//            List<EsCommentGalleryDO> commentGalleryList = this.commentGalleryMapper.getCommentGalleryList(esCommentInfoDO.getId());
//            if (CollectionUtils.isNotEmpty(commentGalleryList)) {
//                //查询图片信息
//                List<String> images = new ArrayList<>();
//                CommentImageVO commentImageVO = new CommentImageVO();
//                for (EsCommentGalleryDO esCommentGalleryDO : commentGalleryList) {
//                    images.add(esCommentGalleryDO.getOriginal());
//                }
//                commentImageVO.setImageUrl(images);
//                esCommentInfoCopyDO.setCommentImageVO(commentImageVO);
//            }

            //查询标签信息
            QueryWrapper<EsCommentLabel> queryWrapper = new QueryWrapper<>();
            String labels = esCommentInfoDO.getLabels();
            if (!StringUtils.isBlank(labels)) {
                String[] labs = labels.split(",");
                Integer[] la = Convert.toIntArray(labs);
                queryWrapper.lambda().in(EsCommentLabel::getId, la);
                List<EsCommentLabel> es = this.commentLabelMapper.selectList(queryWrapper);
                for (EsCommentLabel labelsInfo : es) {
                    labelList.add(labelsInfo.getCommentLabel());
                }
                esCommentInfoCopyDO.setTags(labelList);
            }
            // 服务评分
            DubboResult<com.xdl.jjg.model.domain.EsMemberShopScoreDO> scoreResult = iEsMemberShopScoreService.getMemberShopScoreByGoodAndMemberAndSn(goodsId, esCommentInfoDO.getMemberId(), orderSn, esCommentInfoDO.getShopId());
            if(scoreResult.isSuccess()){
                esCommentInfoCopyDO.setDescriptionScore(scoreResult.getData().getDeliveryScore());
                esCommentInfoCopyDO.setServiceScore(scoreResult.getData().getServiceScore());
                esCommentInfoCopyDO.setDeliveryScore(scoreResult.getData().getDeliveryScore());
            }

            //追加评论
            EsAddComment addComment = addCommentMapper.getAddCommentByCommentId(esCommentInfoCopyDO.getId());
            com.xdl.jjg.model.domain.EsAddCommentDO addCommentDo = new com.xdl.jjg.model.domain.EsAddCommentDO();
            if(addComment != null){
                BeanUtil.copyProperties(addComment, addCommentDo);
                DubboPageResult<EsAddCommentPictureDO> result= addCommentPictureService.getAddCommentPictureList(addComment.getId());
                if(result.isSuccess()){
                    if(CollectionUtils.isNotEmpty(result.getData().getList())){
                        List<String> originalList = result.getData().getList().stream().map(EsAddCommentPictureDO::getOriginal).collect(Collectors.toList());
                        addCommentDo.setOriginal(originalList);
                    }else{
                        addCommentDo.setOriginal(new ArrayList<>());
                    }
                }
                esCommentInfoCopyDO.setAddContentDO(addCommentDo);
            }
            return DubboResult.success(esCommentInfoCopyDO);
        } catch (ArgumentException ae) {
            logger.error("评论查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询评论列表
     *
     * @param memberCommentDTO 评论DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCommentDO>
     */
    @Override
    public DubboPageResult<EsMemberCommentDO> getMemberCommentList(EsMemberCommentDTO memberCommentDTO, int pageNum, int pageSize) {
        QueryWrapper<EsMemberComment> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if(!StringUtils.isBlank(memberCommentDTO.getKeyword())){
                queryWrapper.lambda().like(EsMemberComment::getGoodsName, memberCommentDTO.getKeyword())
                        .or()
                        .eq(EsMemberComment::getGoodsSn, memberCommentDTO.getKeyword());
            }
            queryWrapper.lambda().eq(EsMemberComment::getShopId, memberCommentDTO.getShopId()).orderByDesc(EsMemberComment::getCreateTime);

            Page<EsMemberComment> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberComment> iPage = this.page(page, queryWrapper);
            //首次查询评论信息
            List<EsMemberCommentDO> memberCommentDOList = new ArrayList<>();
            if (CollectionUtils.isEmpty(iPage.getRecords())) {
                return DubboPageResult.success(iPage.getTotal(), memberCommentDOList);
            }

            memberCommentDOList = iPage.getRecords().stream().map(memberComment -> {
                EsMemberCommentDO memberCommentDO = new EsMemberCommentDO();
                BeanUtil.copyProperties(memberComment, memberCommentDO);
                //查询标签信息
                if (StringUtils.isNotEmpty(memberComment.getLabels())) {
                    String[] array = memberComment.getLabels().split(",");
                    List<EsCommentLabelDO> esMemberCouponDO = memberCommentMapper.getCommentLabel(array);
                    if (CollectionUtils.isNotEmpty(esMemberCouponDO))
                        memberCommentDO.setEsCommentLabelDOList(esMemberCouponDO);
                }

                //查询图片信息
                QueryWrapper<EsCommentGallery> queryWrapperImage = new QueryWrapper<>();
                queryWrapperImage.lambda().eq(EsCommentGallery::getCommentId, memberComment.getId());
                List<EsCommentGallery> resultImage = this.commentGalleryMapper.selectList(queryWrapperImage);
                if (CollectionUtils.isNotEmpty(resultImage)) {
                    List<com.xdl.jjg.model.domain.EsCommentGalleryDO> es = BeanUtil.copyList(resultImage, com.xdl.jjg.model.domain.EsCommentGalleryDO.class);
                    memberCommentDO.setCommentsImage(es);
                }

                //追加评论
                EsAddComment addComment = addCommentMapper.getAddCommentByCommentId(memberComment.getId());
                com.xdl.jjg.model.domain.EsAddCommentDO addCommentDo = new com.xdl.jjg.model.domain.EsAddCommentDO();
                if(addComment != null){
                    BeanUtil.copyProperties(addComment, addCommentDo);
                    memberCommentDO.setAddContent(addCommentDo);
                }

                //商家回复
                EsCommentReply commentReply = commentReplyMapper.getCommentReplyByCommentId(memberComment.getId());
                com.xdl.jjg.model.domain.EsCommentReplyDO commentReplyDo = new com.xdl.jjg.model.domain.EsCommentReplyDO();
                if(commentReply != null){
                    BeanUtil.copyProperties(commentReply, commentReplyDo);
                }
                memberCommentDO.setReplayContent(commentReplyDo);
                return memberCommentDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(iPage.getTotal(), memberCommentDOList);
        } catch (ArgumentException ae) {
            logger.error("评论分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除评论数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberComment(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberComment> deleteWrapper = new QueryWrapper<>();
            EsMemberComment esMemberComment = new EsMemberComment();
            esMemberComment.setState(MemberConstant.IsDel);
            deleteWrapper.lambda().eq(EsMemberComment::getId, id);
            this.memberCommentMapper.update(esMemberComment, deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("评论删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 插入评论数据
     *
     * @param memberCommentDTO 评论DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberCommentOrder(EsMemberCommentDTO memberCommentDTO) {
        try {

            EsMemberComment memberComment = new EsMemberComment();
            BeanUtil.copyProperties(memberCommentDTO, memberComment);
            String labels = "";
            if (null != memberCommentDTO && memberCommentDTO.getLabels().size() > 0) {
                for (String s : memberCommentDTO.getLabels()) {
                    labels += s;
                }
            }
            memberComment.setLabels(labels);
            this.memberCommentMapper.insert(memberComment);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("评论新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("评论新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 店铺添加评分
     *
     * @param memberShopScoreDTO
     * @return
     */
    public DubboResult insertShopScore(EsMemberShopScoreDTO memberShopScoreDTO) {
        try {
            if (memberShopScoreDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //查询该子订单信息
           /* DubboResult<EsOrderDO> result = iEsOrderService.getEsBuyerOrderInfo(memberShopScoreDTO.getOrderSn());
            if (MemberPermission.BUYER.equals(MemberPermission.BUYER)) {
                // 获取当前用户进行判断
                EsMember member = new EsMember();
                if (null == result.getData() || !member.getId().equals(result.getData().getMemberId())) {
                    throw new ArgumentException(MemberErrorCode.AUTHORITY.getErrorCode(), MemberErrorCode.AUTHORITY.getErrorMsg());
                }
            }*/
            // 添加店铺评分
            EsMemberShopScore memberShopScore = new EsMemberShopScore();
            BeanUtil.copyProperties(memberShopScoreDTO, memberShopScore);
            memberShopScore.setGoodId(memberShopScoreDTO.getGoodsId());
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

    @Override
    public DubboResult<Double> getGoodCommentRate(Long goodsId) {
        QueryWrapper<EsMemberComment> queryWrapper = new QueryWrapper<>();
        QueryWrapper<EsMemberComment> queryGoodsWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMemberComment::getGoodsId, goodsId);
            Integer count = this.memberCommentMapper.selectCount(queryWrapper);
            queryGoodsWrapper.lambda().eq(EsMemberComment::getGoodsId, goodsId).eq(EsMemberComment::getGrade, MemberConstant.goodsComment);
            Integer goodCount = this.memberCommentMapper.selectCount(queryGoodsWrapper);
            Double result = MathUtil.divide(goodCount, count, 2)*100;
            return DubboPageResult.success(result);
        } catch (ArgumentException ae) {
            logger.error("评论分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据查询商品详情页面评论列表
     *
     * @param memberCommentDTO 评论DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCommentDO>
     */
    @Override
    public DubboPageResult<EsMemberCommentDetailDO> getMemberDetailCommentList(EsQueryDetailCommentDTO memberCommentDTO, Long memberId, int pageSize, int pageNum) {
        QueryWrapper<EsMemberComment> queryWrapper = new QueryWrapper<>();
        try {

            queryWrapper.lambda().eq(EsMemberComment::getGoodsId, memberCommentDTO.getGoodsId()).orderByDesc(EsMemberComment::getCreateTime);
            Page<EsMemberComment> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberCommentDetailDO> pages = memberCommentMapper.getMemberDetailCommentList(page, memberCommentDTO);
            List<EsMemberCommentDetailDO> list = new ArrayList<>();
            if (CollectionUtils.isEmpty(pages.getRecords())) {
                return DubboPageResult.success(pages.getTotal(), list);
            }

            list = pages.getRecords().stream().map(memberComment -> {
                EsMemberCommentDetailDO memberCommentDO = new EsMemberCommentDetailDO();
                BeanUtil.copyProperties(memberComment, memberCommentDO);
                //查询图片信息
                QueryWrapper<EsCommentGallery> queryWrapperImage = new QueryWrapper<>();
                queryWrapperImage.lambda().eq(EsCommentGallery::getCommentId, memberComment.getId());
                List<EsCommentGallery> resultImage = this.commentGalleryMapper.selectList(queryWrapperImage);
                if (CollectionUtils.isNotEmpty(resultImage)) {
                    List<com.xdl.jjg.model.domain.EsCommentGalleryDO> es = BeanUtil.copyList(resultImage, com.xdl.jjg.model.domain.EsCommentGalleryDO.class);
                    memberCommentDO.setCommentsImage(es);
                }
                DubboResult<EsGoodsSkuCO> sku = skuService.getGoodsSku(memberComment.getSkuId());
                if (sku.isSuccess() && !StringUtil.isEmpty(sku.getData().getSpecs())){
                    List<EsWapMemberSpecValuesVO> resultSpecValueList = JsonUtil.jsonToList(sku.getData().getSpecs(), EsWapMemberSpecValuesVO.class);
                    memberCommentDO.setEsMemberSpecValuesDO(resultSpecValueList);
                }
                //查询点赞数量
                DubboResult<Integer> resultNum = this.commentSupportService.getCommentSupportNum(memberComment.getId());
                if (resultNum.isSuccess() && null != resultNum.getData()) {
                    memberCommentDO.setSupportNum(resultNum.getData());
                }
                //查询标签信息
                QueryWrapper<EsCommentLabel> queryWrapperLabels = new QueryWrapper<>();
                String labels = memberCommentDO.getLabels();
                List<String> labelList = new ArrayList<>();
                if (!StringUtils.isBlank(labels)) {
                    String[] labs = labels.split(",");
                    queryWrapperLabels.lambda().in(EsCommentLabel::getId, labs);
                    List<EsCommentLabel> es = this.commentLabelMapper.selectList(queryWrapperLabels);
                    for (EsCommentLabel labelsInfo : es) {
                        labelList.add(labelsInfo.getCommentLabel());
                    }
                    memberCommentDO.setLabelsName(labelList);
                }

                //追加评论
                EsAddComment addComment = addCommentMapper.getAddCommentByCommentId(memberComment.getId());
                com.xdl.jjg.model.domain.EsAddCommentDO addCommentDo = new com.xdl.jjg.model.domain.EsAddCommentDO();
                if(addComment != null){
                    BeanUtil.copyProperties(addComment, addCommentDo);
                    DubboPageResult<EsAddCommentPictureDO> result= addCommentPictureService.getAddCommentPictureList(addComment.getId());
                    if(result.isSuccess()){
                        List<String> originalList = result.getData().getList().stream().map(EsAddCommentPictureDO::getOriginal).collect(Collectors.toList());
                        if (originalList.contains("")){
                            addCommentDo.setOriginal(new ArrayList<>());
                        }else {
                            addCommentDo.setOriginal(originalList);
                        }
                    }
                    memberCommentDO.setAddContent(addCommentDo);
                }

                //商家回复
                EsCommentReply commentReply = commentReplyMapper.getCommentReplyByCommentId(memberComment.getId());
                com.xdl.jjg.model.domain.EsCommentReplyDO commentReplyDo = new com.xdl.jjg.model.domain.EsCommentReplyDO();
                if(commentReply != null){
                    BeanUtil.copyProperties(commentReply, commentReplyDo);
                }
                memberCommentDO.setReplayContent(commentReplyDo);
                memberCommentDO.setJudgeCommentSupport(0);
                if(memberId != null && memberId>0){
                    DubboResult esCommentSupport = commentSupportService.judgeCommentSupport(memberComment.getId(), memberId);
                    if(esCommentSupport.getData() != null){
                        memberCommentDO.setJudgeCommentSupport(1);
                    }
                }
                return memberCommentDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(pages.getTotal(), list);
        } catch (ArgumentException ae) {
            logger.error("评论分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<List<Map<String, Object>>> getLabelsGroup(Long goodsId) {
        QueryWrapper<EsCommentLabel> queryWrapperLabels = new QueryWrapper<>();
        List<String> list = memberCommentMapper.getLabelsGroup(goodsId);
        List<Map<String, Object>> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            List<String> nameList = new ArrayList<>();
            Map<String, Long> finalMap = new LinkedHashMap<>();
            String[] labelsArray = null;
            Integer[] labels = null;
            for(String labelStr : list){
                List<String> labelsList = new ArrayList<>();
                labelsArray = labelStr.split(",");
                labels = Convert.toIntArray(labelsArray);
                labelsList = commentLabelMapper.getLabelsById(Arrays.asList(labels));
                nameList.addAll(labelsList);
            }

            Map<String, Long> map = nameList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            map.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue()
                            .reversed()).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

            if(finalMap.size() > 0){
                for(Map.Entry<String, Long> entry : finalMap.entrySet()){
                    Map<String, Object> temporaryMap = new HashMap<>();
                    temporaryMap.put("name", entry.getKey());
                    temporaryMap.put("num", entry.getValue());
                    result.add(temporaryMap);
                }
            }
        }
        return DubboResult.success(result);
    }

    /**
     * 统计商品评论数量
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCommentDO>
     */
    @Override
    public DubboResult<com.xdl.jjg.model.domain.GradeLevelDO> getCountComment(Long goodsId) {
        QueryWrapper<EsMemberComment> queryWrapper = new QueryWrapper<>();
        QueryWrapper<EsMemberComment> queryWrapperGood = new QueryWrapper<>();
        QueryWrapper<EsMemberComment> queryWrapperComment = new QueryWrapper<>();
        QueryWrapper<EsMemberComment> queryWrapperBad = new QueryWrapper<>();
        QueryWrapper<EsMemberComment> queryWrapperImg = new QueryWrapper<>();
        com.xdl.jjg.model.domain.GradeLevelDO gradeLevelDO = new com.xdl.jjg.model.domain.GradeLevelDO();
        try {
            queryWrapper.lambda().eq(EsMemberComment::getGoodsId,goodsId);
            //评论总数
            Integer num =  this.memberCommentMapper.selectCount(queryWrapper);
            gradeLevelDO.setNum(num);
            //好评总数
            queryWrapperGood.lambda().eq(EsMemberComment::getGoodsId,goodsId).eq(EsMemberComment::getGrade,GoodsCommentSortEnums.GOOD_COMMENT.getKey());
            Integer goodNum = this.memberCommentMapper.selectCount(queryWrapperGood);
            gradeLevelDO.setGoodNum(goodNum);
            //中评
            queryWrapperComment.lambda().eq(EsMemberComment::getGoodsId,goodsId).eq(EsMemberComment::getGrade,GoodsCommentSortEnums.COMMONT_COMMENT.getKey());
            Integer commentNum = this.memberCommentMapper.selectCount(queryWrapperComment);
            gradeLevelDO.setCommentNum(commentNum);
            //差评
            queryWrapperBad.lambda().eq(EsMemberComment::getGoodsId,goodsId).eq(EsMemberComment::getGrade,GoodsCommentSortEnums.BAD_COMMENT.getKey());
            Integer badNum = this.memberCommentMapper.selectCount(queryWrapperBad);
            gradeLevelDO.setBadNum(badNum);
            //带评论图片
            queryWrapperImg.lambda().eq(EsMemberComment::getGoodsId,goodsId).eq(EsMemberComment::getHaveImage,MemberConstant.hasImage);
            Integer pictureNum = this.memberCommentMapper.selectCount(queryWrapperImg);
            gradeLevelDO.setPictureNum(pictureNum);
            return DubboResult.success(gradeLevelDO);
        } catch (ArgumentException ae) {
            logger.error("统计评论数量", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("统计评论数量", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 后台-评价列表管理功能
     *
     * @param queryCommentListDTO
     * @param pageSize 页码
     * @param pageNum  页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCommentDO>
     */
    @Override
    public DubboPageResult<EsAdminManagerDO> getAdminManagerList(QueryCommentListDTO queryCommentListDTO, int pageSize, int pageNum) {
        try {
            Page<EsMemberComment> page = new Page<>(pageNum, pageSize);
            IPage<EsAdminManagerDO> pages = memberCommentMapper.getAdminManagerList(page, queryCommentListDTO);
            List<EsAdminManagerDO> lists = new ArrayList<>();
            if (CollectionUtils.isEmpty(pages.getRecords())) {
                return DubboPageResult.success(pages.getTotal(), lists);
            }
            for (EsAdminManagerDO memberComment : pages.getRecords()) {
                EsAdminManagerDO esAdminManagerDO = new EsAdminManagerDO();
                BeanUtil.copyProperties(memberComment, esAdminManagerDO);
                //查询商品订单明细 商品规格
                DubboResult<EsOrderItemsDO> resultItems = iEsOrderItemsService
                        .getBuyerEsOrderItemsByOrderSnAndGoodsId(memberComment.getOrderSn(),
                        memberComment.getGoodsId());
                //录入商品规格信息 EsSpecValuesDO
                if (resultItems.isSuccess() && null != resultItems.getData()
                        && !StringUtils.isBlank(resultItems.getData().getSpecJson())) {
                    esAdminManagerDO.setSpecValues(resultItems.getData().getSpecJson());
                    List<com.xdl.jjg.model.domain.EsMemberSpecValuesDO> resultSpecValueList = JSON.parseObject(resultItems.getData().getSpecJson(),
                            new TypeReference<List<com.xdl.jjg.model.domain.EsMemberSpecValuesDO>>() {
                    });
                    esAdminManagerDO.setEsMemberSpecValuesDO(resultSpecValueList);
                }
                //查询商品评价图片
                DubboPageResult<String> resultImage = iEsCommentGalleryService.getCommentImageList(memberComment.getId());
                if (resultImage.isSuccess() && null != resultImage.getData()
                        && CollectionUtils.isNotEmpty(resultImage.getData().getList())) {
                    esAdminManagerDO.setCommentsImage(resultImage.getData().getList());
                }
                //查询已回复
                if (memberComment.getReplyStatus() == MemberConstant.repalyContent) {
                    DubboResult<com.xdl.jjg.model.domain.EsCommentReplyDO> resultReplay = iEsCommentReplyService.getCommentReplyByCommentId(memberComment.getId());
                    if (resultReplay.isSuccess() && null != resultReplay.getData() && !StringUtils.isBlank(resultReplay.getData().getContent())) {
                        esAdminManagerDO.setReplayContent(resultReplay.getData().getContent());
                    }
                }

                //获取详细评分
                if(org.apache.commons.lang3.StringUtils.isNotBlank(memberComment.getOrderSn())
                        && memberComment.getGoodsId() != null && memberComment.getId() != null){
                    DubboResult<com.xdl.jjg.model.domain.EsMemberShopScoreDO> memberShopScoreDODubboResult = iEsMemberShopScoreService
                            .getMemberShopScoreByGoodAndMemberAndSn(memberComment.getGoodsId(), memberComment.getMemberId(), memberComment.getOrderSn(), memberComment.getShopId());
                    if(memberShopScoreDODubboResult.isSuccess() && memberShopScoreDODubboResult.getData() != null){
                        com.xdl.jjg.model.domain.EsMemberShopScoreDO memberShopScoreDO = memberShopScoreDODubboResult.getData();
                        esAdminManagerDO.setDeliveryScore(memberShopScoreDO.getDeliveryScore());
                        esAdminManagerDO.setDescriptionScore(memberShopScoreDO.getDescriptionScore());
                        esAdminManagerDO.setServiceScore(memberShopScoreDO.getServiceScore());
                    }
                }
                lists.add(esAdminManagerDO);
            }
            return DubboPageResult.success(pages.getTotal(), lists);
        } catch (ArgumentException ae) {
            logger.error("评论分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("评论分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteDiscountComment(Integer[] ids) {
        try {
            QueryWrapper<EsMemberComment> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsMemberComment::getId, ids);
            this.memberCommentMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("批量删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("批量删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public int getCollectNum(Long userId) {
        int num = 0;
        try {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String timesNow = simpleDateFormat.format(date);
            num = this.memberCommentMapper.getCollectCommentNum(userId, timesNow);
            return num;
        } catch (Exception ae) {
            logger.error("查詢商品列表失敗", ae);
        }
        return num;
    }


    /**
     * 统计商品评论数量
     *
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/06/02 13:42:53
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCountDO>
     */
    @Override
    public DubboResult<EsCommentCountDO> getCommentCount(Long goodsId) {
        EsCommentCountDO commentCountDO = new EsCommentCountDO();
        int good=0;
        int comm=0;
        int low=0;
        int hasImages=0;
        int recent = 0; // 最近评论
        int hasAdd = 0; // 有追加
        try {
            EsQueryDetailCommentDTO esQueryDetailCommentDTO = new EsQueryDetailCommentDTO();
            esQueryDetailCommentDTO.setGoodsId(goodsId);
            IPage<EsMemberCommentDetailDO> memberDetailCommentList = memberCommentMapper.getMemberDetailCommentList(new Page(1, Integer.MAX_VALUE), esQueryDetailCommentDTO);
            List<EsMemberCommentDetailDO> records = memberDetailCommentList.getRecords();
            for (EsMemberCommentDetailDO comment: records) {
                if (comment.getGrade().equals(GoodsCommentSortEnums.GOOD_COMMENT.getKey().toString())){
                    good ++;
                }else if(comment.getGrade().equals(GoodsCommentSortEnums.COMMONT_COMMENT.getKey().toString())){
                    comm ++;
                }else if(comment.getGrade().equals(GoodsCommentSortEnums.BAD_COMMENT.getKey().toString())){
                    low ++;
                }
                if(StringUtils.isNotEmpty(comment.getAddContentInfo())){
                    hasAdd ++;
                }
                if(DateUtil.between(new Date(),new Date(comment.getCreateTime()), DateUnit.DAY,true)/30<3){
                    recent++;
                }
                if (comment.getHaveImage() != null && comment.getHaveImage()==1){
                    hasImages ++;
                }
            }
            commentCountDO.setHighCount(good);
            commentCountDO.setCommCount(comm);
            commentCountDO.setLowCount(low);
            commentCountDO.setHasImageCount(hasImages);
            commentCountDO.setRecentCount(recent);
            commentCountDO.setAddCount(hasAdd);
            return DubboResult.success(commentCountDO);
        } catch (ArgumentException ae) {
            logger.error("统计评论数量", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("统计评论数量", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

}
