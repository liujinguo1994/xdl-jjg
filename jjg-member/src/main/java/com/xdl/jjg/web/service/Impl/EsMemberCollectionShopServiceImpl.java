package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.model.domain.EsAdminTagGoodsDO;
import com.shopx.goods.api.model.domain.EsAdminTagsDO;
import com.shopx.goods.api.model.domain.EsGoodsDO;
import com.shopx.goods.api.service.IEsAdminTagGoodsService;
import com.shopx.goods.api.service.IEsAdminTagsService;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsTagsService;
import com.shopx.member.api.constant.MemberConstant;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.*;
import com.shopx.member.api.model.domain.dto.EsMemberCollectionShopDTO;
import com.shopx.member.api.model.domain.dto.EsQueryCollectShopDTO;
import com.shopx.member.api.model.domain.dto.EsUpdateTopShopDTO;
import com.shopx.member.api.model.domain.enums.ShopStatusEnums;
import com.shopx.member.api.service.IEsGrowthValueStrategyService;
import com.shopx.member.api.service.IEsMemberCollectionShopService;
import com.shopx.member.api.service.IEsMemberService;
import com.xdl.jjg.entity.*;
import  com.xdl.jjg.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员收藏店铺表 服务实现类
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Service
public class EsMemberCollectionShopServiceImpl extends ServiceImpl<EsMemberCollectionShopMapper, EsMemberCollectionShop> implements IEsMemberCollectionShopService {

    private static Logger logger = LoggerFactory.getLogger(EsCompanyServiceImpl.class);
    @Autowired
    private EsMemberCollectionShopMapper esMemberCollectionShopMapper;

    @Autowired
    private EsMemberMapper esMemberMapper;

    @Autowired
    private EsMemberShopMapper esMemberShopMapper;
    @Autowired
    private EsShopMapper esShopMapper;
    @Autowired
    private EsShopDetailMapper esShopDetailMapper;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGoodsService iEsGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsTagsService iEsTagsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGrowthValueStrategyService iEsGrowthValueStrategyService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService iEsMemberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsAdminTagGoodsService adminTagGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsAdminTagsService adminTagsService;

    /**
     * 添加会员收藏店铺
     *
     * @param shopId
     * @param memberId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberCollectionShopDO> insertMemberCollectionShop(Long shopId, Long memberId) {
        if (shopId == 0 || null == shopId) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", shopId));
        }
        try {
            //获取店铺信息
            QueryWrapper<EsShop> queryWrapperShop = new QueryWrapper<>();
            queryWrapperShop.lambda().eq(EsShop::getId, shopId)
                    .eq(EsShop::getState, ShopStatusEnums.OPEN);
            EsShop esShopInfo = this.esShopMapper.selectOne(queryWrapperShop);
            if (null == esShopInfo) {
                throw new ArgumentException(MemberErrorCode.SHOP_EXIST.getErrorCode(),
                        MemberErrorCode.SHOP_EXIST.getErrorMsg());
            }

            //依据会员id查询会员店铺表
            QueryWrapper<EsMemberShop> queryWrapperMemberShop = new QueryWrapper<>();
            queryWrapperMemberShop.lambda().eq(EsMemberShop::getMemberId, memberId);
            EsMemberShop esMemberShop = this.esMemberShopMapper.selectOne(queryWrapperMemberShop);

            //判断是否收藏的店铺是自己的
            if (null != esMemberShop && esMemberShop.getShopId().equals(esShopInfo.getId())) {
                throw new ArgumentException(MemberErrorCode.MEMBER_COLLECTION_SHOP_MY.getErrorCode(),
                        MemberErrorCode.MEMBER_COLLECTION_SHOP_MY.getErrorMsg());
            }

            //判断该店铺是否已经收藏
            QueryWrapper<EsMemberCollectionShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCollectionShop::getMemberId, memberId)
                    .eq(EsMemberCollectionShop::getShopId, shopId);
            EsMemberCollectionShop esJudgeMemberCollection = this.esMemberCollectionShopMapper.selectOne(queryWrapper);
            if (esJudgeMemberCollection != null) {
                throw new ArgumentException(MemberErrorCode.MEMBER_COLLECTION_SHOP_EXIST.getErrorCode(), "该店铺已收藏");
            }
            //查询店铺当日收藏次数
            int num = getCollectNum(memberId);
            //查询店铺详细信息
            QueryWrapper<EsShopDetail> shopDetailQueryWrapper=new QueryWrapper<>();
            shopDetailQueryWrapper.lambda().eq(EsShopDetail::getShopId,shopId);
            EsShopDetail esShopDetail = esShopDetailMapper.selectOne(shopDetailQueryWrapper);
            //添加店铺收藏
            EsMemberCollectionShop esMemberCollectionShop = new EsMemberCollectionShop();
            esMemberCollectionShop.setMemberId(memberId);
            esMemberCollectionShop.setShopId(esShopInfo.getId());
            esMemberCollectionShop.setShopName(esShopInfo.getShopName());
            esMemberCollectionShop.setLogo(esShopDetail.getShopLogo());
            esMemberCollectionShop.setShopProvince(esShopDetail.getShopProvince());
            esMemberCollectionShop.setShopCity(esShopDetail.getShopCity());
            esMemberCollectionShop.setShopRegion(esShopDetail.getShopCounty());
            esMemberCollectionShop.setShopTown(esShopDetail.getShopTown());
            this.save(esMemberCollectionShop);
            //调用会员收藏成长值服务
            DubboResult<EsGrowthValueStrategyDO> resultInfo = iEsGrowthValueStrategyService.getComandcolleGrowthvalueConfigByType(MemberConstant.collectionShop);
            //判断是否添加成长值
            if (resultInfo.isSuccess() && null != resultInfo.getData() && null != resultInfo.getData().getLimitNum()) {
                if (num <= resultInfo.getData().getLimitNum()) {
                    iEsMemberService.updateGrowthValue(memberId, resultInfo.getData().getGrowthValue(), true,MemberConstant.collectionStragety);
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("添加店铺收藏失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("添加店铺收藏失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public int getCollectNum(Long userId) {
        int num = 0;
        try {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String timesNow = simpleDateFormat.format(date);
            num = this.esMemberCollectionShopMapper.getCollectShopNum(userId, timesNow);
            return num;
        } catch (Exception ae) {
            logger.error("查詢商品列表失敗", ae);
        }
        return num;
    }

    /**
     * 取消会员收藏店铺
     *
     * @param shopId
     * @param memberId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberCollectionShopDO> deleteMemberCollectionShop(Long memberId, Long shopId) {
        if (shopId == 0 || null == shopId) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", shopId));
        }
        try {
            //判断该店铺是否是此人收藏
            QueryWrapper<EsMemberCollectionShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCollectionShop::getMemberId, memberId)
                    .eq(EsMemberCollectionShop::getShopId, shopId);
            EsMemberCollectionShop memberCollection = esMemberCollectionShopMapper.selectOne(queryWrapper);
            if (memberCollection == null) {
                throw new ArgumentException(MemberErrorCode.AUTHORITY.getErrorCode(), "你无权限操作");
            }
            queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCollectionShop::getMemberId, memberId)
                    .eq(EsMemberCollectionShop::getShopId, shopId);
            esMemberCollectionShopMapper.delete(queryWrapper);

            //调用会员收藏成长值服务
            DubboResult<EsGrowthValueStrategyDO> resultInfo = iEsGrowthValueStrategyService
                    .getComandcolleGrowthvalueConfigByType(MemberConstant.collectionShop);
            //判断是否添加成长值
            if (resultInfo.isSuccess() && null != resultInfo.getData() && null != resultInfo.getData().getGrowthValue()) {
                iEsMemberService.updateGrowthValue(memberId, resultInfo.getData().getGrowthValue(), false,MemberConstant.collectionStragety);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除收藏店铺失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除收藏店铺失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 批量取消会员收藏店铺
     *
     * @param shopIds
     * @param memberId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberCollectionShopDO> deleteMemberCollectionShopBatch(Long memberId, Long[] shopIds) {
        try {
            QueryWrapper<EsMemberCollectionShop> queryWrapperDeleteMemberCollection = new QueryWrapper<>();
            queryWrapperDeleteMemberCollection.lambda().eq(EsMemberCollectionShop::getMemberId, memberId)
                    .in(EsMemberCollectionShop::getShopId, shopIds);
            this.esMemberCollectionShopMapper.delete(queryWrapperDeleteMemberCollection);
            if (null != shopIds || shopIds.length != 0) {
             int num = shopIds.length;
                //调用会员收藏成长值服务
                DubboResult<EsGrowthValueStrategyDO> resultInfo = iEsGrowthValueStrategyService.getComandcolleGrowthvalueConfigByType(MemberConstant.collectionShop);
                //判断是否添加成长值
                if (resultInfo.isSuccess() && null != resultInfo.getData() && null != resultInfo.getData().getGrowthValue()) {
                    int value = num * resultInfo.getData().getGrowthValue();
                    iEsMemberService.updateGrowthValue(memberId, value, false,MemberConstant.collectionStragety);
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除收藏店铺失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除收藏店铺失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 设置收藏的店铺置顶
     *
     * @param id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberCollectionShopDO> shopTop(Long id) {
        if (null == id || id == 0) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
        }
        try {
            QueryWrapper<EsMemberCollectionShop> queryWrapperDeleteMemberCollection = new QueryWrapper<>();
            EsMemberCollectionShop esMemberCollectionShop = new EsMemberCollectionShop();
            Integer sort = esMemberCollectionShopMapper.getMaxSort();
            if (null == sort) {
                esMemberCollectionShop.setSort(MemberConstant.IsTop);
            } else {
                esMemberCollectionShop.setSort(sort + MemberConstant.IsTop);
            }
            queryWrapperDeleteMemberCollection.lambda().eq(EsMemberCollectionShop::getId, id);
            this.esMemberCollectionShopMapper.update(esMemberCollectionShop, queryWrapperDeleteMemberCollection);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺置顶失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺置顶失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }


    /**
     * 添加备注
     *
     * @param dto
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    public DubboResult<EsMemberCollectionShopDO> insertRemarks(EsMemberCollectionShopDTO dto) {
        if (null == dto) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", dto));
        }
        try {
            EsMemberCollectionShop esMemberCollectionShop = new EsMemberCollectionShop();
            esMemberCollectionShop.setMark(dto.getMark());
            esMemberCollectionShop.setId(dto.getId());
            this.updateById(esMemberCollectionShop);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺备注添加失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺备注添加失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }


    /**
     * 查詢備注
     *
     * @param id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberCollectionShopDO> getEsMemberCollectionMarks(Long id) {
        if (null == id || id == 0) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
        }
        try {
            QueryWrapper<EsMemberCollectionShop> queryWrapperDeleteMemberCollection = new QueryWrapper<>();
            EsMemberCollectionShopDO esMemberCollectionShopDO = new EsMemberCollectionShopDO();
            queryWrapperDeleteMemberCollection.lambda().eq(EsMemberCollectionShop::getId, id);
            EsMemberCollectionShop result = this.esMemberCollectionShopMapper.selectOne(queryWrapperDeleteMemberCollection);
            if(null == result){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(result, esMemberCollectionShopDO);
            return DubboResult.success(esMemberCollectionShopDO);
        } catch (ArgumentException ae) {
            logger.error("店铺备注添加失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺备注添加失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 置顶操作
     *
     * @param
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    public DubboResult updateShopTop(EsUpdateTopShopDTO esUpdateTopShopDTO, Long memberId) {
        QueryWrapper<EsMemberCollectionShop> queryWrapper = new QueryWrapper<>();
        QueryWrapper<EsMemberCollectionShop> queryWrapperUncel = new QueryWrapper<>();
        if (null == esUpdateTopShopDTO) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", esUpdateTopShopDTO));
        }
        try {
            //把所有店铺都取消置顶
            queryWrapperUncel.lambda().eq(EsMemberCollectionShop::getMemberId, memberId).eq(EsMemberCollectionShop::getSort, MemberConstant.IsTop);
            EsMemberCollectionShop esShop = new EsMemberCollectionShop();
            esShop.setSort(MemberConstant.uncelTop);
            this.esMemberCollectionShopMapper.update(esShop, queryWrapperUncel);
            //置顶操作
            queryWrapper.lambda().eq(EsMemberCollectionShop::getShopId,esUpdateTopShopDTO.getId())
                    .eq(EsMemberCollectionShop::getMemberId, memberId);
            EsMemberCollectionShop esMemberCollectionShop = new EsMemberCollectionShop();
            esMemberCollectionShop.setSort(esUpdateTopShopDTO.getSort());
            this.esMemberCollectionShopMapper.update(esMemberCollectionShop, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺置顶失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺置顶失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 统计收藏店铺个数
     *
     * @return
     */
    @Override
    public DubboResult<Integer> getCountShopNum(Long memberId) {
        try {
            QueryWrapper<EsMemberCollectionShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCollectionShop::getMemberId, memberId);
            List<EsMemberCollectionShop> esMemberCollectionGoodsList = this.esMemberCollectionShopMapper.selectList(queryWrapper);
            return DubboResult.success(esMemberCollectionGoodsList.size());
        } catch (ArgumentException ae) {
            logger.error("查詢商品列表失敗", ae);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢商品列表失敗", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 查询会员收藏店铺列表
     * @param pageNum
     * @param pageSize
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionShopDO>
     */
    @Override
    public DubboPageResult<EsQueryCollectionShopDO> getMemberCollectionShopList(int pageNum, int pageSize, EsQueryCollectShopDTO dto) {
        try {
            QueryWrapper<EsMemberCollectionShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByDesc(EsMemberCollectionShop::getSort)
                    .orderByDesc(EsMemberCollectionShop::getCreateTime);
            queryWrapper.lambda().eq(EsMemberCollectionShop::getMemberId, dto.getMemberId());
            if (StringUtils.isNotEmpty(dto.getKeyword())) {
                queryWrapper.lambda().like(EsMemberCollectionShop::getShopName, dto.getKeyword());
            }
            List<EsMemberCollectionShop> esMemberCollectionShopList = this.list(queryWrapper);
            List<EsQueryCollectionShopDO> list = new ArrayList<>();
            if(dto.getTagId() == null){
                dto.setTagId(0l);
            }
            for (EsMemberCollectionShop memberCollectionShop : esMemberCollectionShopList) {
                DubboPageResult<EsAdminTagGoodsDO> adminTagGoodsResult = adminTagGoodsService.getAdminGoodsTagsByShopId(memberCollectionShop.getShopId(), dto.getTagId());
                if(!adminTagGoodsResult.isSuccess() || adminTagGoodsResult.getData() == null
                        || adminTagGoodsResult.getData().getList().size() == 0){
                    continue;
                }
                List<EsMemberCollectionShopLabelDO> labelList = new ArrayList<>();
                List<EsAdminTagGoodsDO> adminTagGoodsList = adminTagGoodsResult.getData().getList();
                Map<Long, List<EsAdminTagGoodsDO>> map = adminTagGoodsList.stream().collect(Collectors.groupingBy(EsAdminTagGoodsDO::getTagId));
                for(Map.Entry<Long, List<EsAdminTagGoodsDO>> entry: map.entrySet()){
                    DubboResult<EsAdminTagsDO> adminTagsDOResult = adminTagsService.getAdminTags(entry.getKey());
                    EsMemberCollectionShopLabelDO memberCollectionShopLabelDO = new EsMemberCollectionShopLabelDO();
                    if(adminTagsDOResult.isSuccess() && adminTagsDOResult.getData() != null){
                        EsAdminTagsDO adminTagsDO = adminTagsDOResult.getData();
                        BeanUtil.copyProperties(adminTagsDO, memberCollectionShopLabelDO);
                    }
                    List<EsAdminTagGoodsDO> adminTagGoodsDOList = entry.getValue();
                    Long[] goodsArray = adminTagGoodsDOList.stream().map(EsAdminTagGoodsDO::getGoodsId).toArray(Long[]::new);
                    DubboPageResult<EsGoodsDO> goodsDOPageResult = iEsGoodsService.getEsGoods(goodsArray);
                    if(goodsDOPageResult.isSuccess() && goodsDOPageResult.getData() != null){
                        memberCollectionShopLabelDO.setMemberGoodsDO(BeanUtil.copyList(goodsDOPageResult.getData().getList(), EsMemberGoodsDO.class));
                    }
                    labelList.add(memberCollectionShopLabelDO);
                }
                EsQueryCollectionShopDO queryCollectionShopDO = new EsQueryCollectionShopDO();
                BeanUtil.copyProperties(memberCollectionShop, queryCollectionShopDO);
                queryCollectionShopDO.setCollectionShopLabelDOList(labelList);
                list.add(queryCollectionShopDO);
            }
            List<EsQueryCollectionShopDO> resultList = manualPage(list, pageNum, pageSize);
            return DubboPageResult.success((long)list.size(), resultList);
        } catch (ArgumentException ae) {
            logger.error("查詢店鋪列表失敗", ae);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢店鋪列表失敗", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 查询会员收藏店铺列表
     * @param pageNum
     * @param pageSize
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionShopDO>
     */
    @Override
    public DubboPageResult<EsQueryCollectionShopDO> getMemberCollectionShopListNew(int pageNum, int pageSize, EsQueryCollectShopDTO dto) {
        try {
            QueryWrapper<EsMemberCollectionShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByDesc(EsMemberCollectionShop::getSort)
                    .orderByDesc(EsMemberCollectionShop::getCreateTime);
            queryWrapper.lambda().eq(EsMemberCollectionShop::getMemberId, dto.getMemberId());
            if (StringUtils.isNotEmpty(dto.getKeyword())) {
                queryWrapper.lambda().like(EsMemberCollectionShop::getShopName, dto.getKeyword());
            }
            Page<EsMemberCollectionShop> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberCollectionShop> iPage = this.page(page, queryWrapper);

            List<EsQueryCollectionShopDO> list = new ArrayList<>();
            List<EsMemberCollectionShop> esMemberCollectionShopList = iPage.getRecords();
            if(CollectionUtils.isNotEmpty(esMemberCollectionShopList)){
                list = esMemberCollectionShopList.stream().map(e -> {
                    EsQueryCollectionShopDO queryCollectionShopDO = new EsQueryCollectionShopDO();
                    BeanUtil.copyProperties(e, queryCollectionShopDO);
                    if(dto.getTagId() == null){
                        queryCollectionShopDO.setHotGoodList(getHotOrNewGoods(e.getShopId(), 1L, 1L));
                        queryCollectionShopDO.setNewGoodList(getHotOrNewGoods(e.getShopId(), 2L, 2L));
                    }else if(dto.getTagId() == 1L){
                        queryCollectionShopDO.setHotGoodList(getHotOrNewGoods(e.getShopId(), dto.getTagId(), 1L));
                    }else if(dto.getTagId() == 2L){
                        queryCollectionShopDO.setNewGoodList(getHotOrNewGoods(e.getShopId(), dto.getTagId(), 2L));
                    }
                    return queryCollectionShopDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), list);
        } catch (ArgumentException ae) {
            logger.error("查询店铺列表失败", ae);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查询店铺列表失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    private List<EsMemberGoodsDO> getHotOrNewGoods(Long shopId, Long tagId, Long type){
        DubboPageResult<EsGoodsDO> goodsDOHotResult = adminTagGoodsService.getBuyerAdminGoodsTags(shopId, tagId, type);
        List<EsMemberGoodsDO> goodsDOList = new ArrayList<>();
        if(goodsDOHotResult.isSuccess() && goodsDOHotResult.getData() != null){
            goodsDOList = BeanUtil.copyList(goodsDOHotResult.getData().getList(), EsMemberGoodsDO.class);
        }
        return goodsDOList;
    }

    //手动分页
    private List<EsQueryCollectionShopDO> manualPage(List<EsQueryCollectionShopDO> list, int pageNum, int pageSize){
        List<EsQueryCollectionShopDO> subList = new ArrayList<>();
        //总记录数
        int total = list.size();
        // 开始索引
        int fromIndex = (pageNum-1) * pageSize;
        // 结束索引
        int toIndex = fromIndex + pageSize;
        // 如果结束索引大于集合的最大索引，那么规定结束索引=集合大小
        if(toIndex > total){
            toIndex = total;
        }
        if(fromIndex <= total){
            subList = list.subList(fromIndex, toIndex);
        }
        return subList;
    }


    /**
     * 判断当前商品是否已经添加为收藏
     *
     * @return
     */
    @Override
    public DubboResult<Boolean> hasCollection(Long shopId,Long memberId ) {
        try {
            boolean flag = false;
            QueryWrapper<EsMemberCollectionShop> queryWrapperGood = new QueryWrapper<>();
            queryWrapperGood.lambda().eq(EsMemberCollectionShop::getShopId, shopId)
                    .eq(EsMemberCollectionShop::getMemberId, memberId);
            EsMemberCollectionShop collectionShop = esMemberCollectionShopMapper.selectOne(queryWrapperGood);
            if (collectionShop != null){
                flag = true;
            }else {
                flag = false;
            }
            return DubboResult.success(flag);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    @Override
    public DubboPageResult<EsCollectionShopInfoDO> getCollectionShopList(Long memberId, int pageSize, int pageNum) {
        try {
            QueryWrapper<EsMemberCollectionShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByDesc(EsMemberCollectionShop::getSort)
                    .orderByDesc(EsMemberCollectionShop::getCreateTime);
            queryWrapper.lambda().eq(EsMemberCollectionShop::getMemberId, memberId);
            Page<EsMemberCollectionShop> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberCollectionShop> iPage = esMemberCollectionShopMapper.selectPage(page, queryWrapper);
            List<EsCollectionShopInfoDO> doList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())){
                 doList = BeanUtil.copyList(iPage.getRecords(), EsCollectionShopInfoDO.class);
                 doList.stream().forEach(esCollectionShopInfoDO -> {
                     QueryWrapper<EsMemberCollectionShop> wrapper = new QueryWrapper<>();
                     wrapper.lambda().eq(EsMemberCollectionShop::getShopId, esCollectionShopInfoDO.getShopId());
                     Integer count = esMemberCollectionShopMapper.selectCount(wrapper);
                     esCollectionShopInfoDO.setShopCollect(count);
                 });
            }
            return DubboPageResult.success(iPage.getTotal(), doList);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
