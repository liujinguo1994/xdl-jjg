package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberCollectionGoods;
import com.xdl.jjg.entity.EsMemberShop;
import com.xdl.jjg.mapper.EsMemberCollectionGoodsMapper;
import com.xdl.jjg.mapper.EsMemberMapper;
import com.xdl.jjg.mapper.EsMemberShopMapper;
import com.xdl.jjg.model.domain.*;
import com.xdl.jjg.model.dto.EsQueryMemberCollectionGoodsDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.web.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员商品收藏 服务实现类
 * </p>
 *
 * @author LNS 1220316142@qq.com
 * @since 2019-05-29
 */
@Service
public class EsMemberCollectionGoodsServiceImpl extends ServiceImpl<EsMemberCollectionGoodsMapper, EsMemberCollectionGoods> implements IEsMemberCollectionGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsCompanyServiceImpl.class);
    @Autowired
    private EsMemberCollectionGoodsMapper esMemberCollectionGoodsMapper;
    @Autowired
    private EsMemberMapper esMemberMapper;
    @Autowired
    private EsMemberShopMapper esMemberShopMapper;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGoodsService iEsGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCategoryService iEsCategoryService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGoodsSkuService iEsGoodsSkuService;
    @Autowired
    private IEsMemberService iEsMemberService;
    //短信验证码长度
    @Value("${zhuo.member.code.length}")
    private int SMSLENGTH;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsSmsService esSmsService;
    @Autowired
    private IEsGrowthValueStrategyService iEsGrowthValueStrategyService;


    /**
     * 查询会员收藏商品列表全部
     *
     * @param esQueryMemberCollectionGoodsDTO
     * @param pageNo
     * @param pageSize
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    public DubboPageResult<EsMemberCollectionGoodsDO> getMemberCollectionGoodList(int pageNo, int pageSize, EsQueryMemberCollectionGoodsDTO esQueryMemberCollectionGoodsDTO) {
        try {
            Page<EsMemberCollectionGoods> pages = new Page<>(pageNo, pageSize);
            List<EsMemberCollectionGoods> listSort;
            QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, esQueryMemberCollectionGoodsDTO.getUserId());
            //添加分类查询条件
            if (null != esQueryMemberCollectionGoodsDTO.getCategoryId()) {
                queryWrapper.lambda().eq(EsMemberCollectionGoods::getCategoryId, esQueryMemberCollectionGoodsDTO.getCategoryId());
                listSort = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
                List<Long> ids = listSort.stream().map(EsMemberCollectionGoods::getId).collect(Collectors.toList());
                queryWrapper.lambda().in(EsMemberCollectionGoods::getId, ids);
            }
            IPage<EsMemberCollectionGoods> esMemberCollectionGoodsList = this.page(pages, queryWrapper);
            List<EsMemberCollectionGoodsDO> esMemberCollectionGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esMemberCollectionGoodsList.getRecords())) {
                esMemberCollectionGoodsDOList = esMemberCollectionGoodsList.getRecords().stream().map(esMemberCollectionShop -> {
                    EsMemberCollectionGoodsDO esMemberCollectionGoodsDO = new EsMemberCollectionGoodsDO();
                    BeanUtil.copyProperties(esMemberCollectionShop, esMemberCollectionGoodsDO);
                    DubboResult<EsGoodsSkuDO> result = iEsGoodsSkuService.buyGetGoodsSku(esMemberCollectionGoodsDO.getSkuId(), esMemberCollectionGoodsDO.getGoodsId());
                    if (!result.isSuccess() && result.getCode() == GoodsErrorCode.DATA_NOT_EXIST.getErrorCode()) {
                        //失效商品添加标识
                        esMemberCollectionGoodsDO.setLoseSign(MemberConstant.effect);
                    } else {
                        //降价商品
                        if (!result.isSuccess()) {
                            throw new ArgumentException(result.getCode(), result.getMsg());
                        }
                        if (result.getData().getMoney()<(esMemberCollectionGoodsDO.getGoodsPrice())) {
                            //商品降价
                            Double subMoney = MathUtil.subtract(esMemberCollectionGoodsDO.getGoodsPrice(), result.getData().getMoney());
                            esMemberCollectionGoodsDO.setCutMoney(subMoney);
                        }
                        esMemberCollectionGoodsDO.setCurrentPrice(result.getData().getMoney());
                        //商品库存
                        esMemberCollectionGoodsDO.setQuantity(result.getData().getQuantity());
                        //比较是否库存紧缺
                        if (result.getData().getWarningValue() != null && result.getData().getWarningValue().compareTo(result.getData().getQuantity()) >= 0) {
                            esMemberCollectionGoodsDO.setShortQuantity("库存紧缺");
                        }
                    }
                    return esMemberCollectionGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(esMemberCollectionGoodsList.getTotal(), esMemberCollectionGoodsDOList);
        } catch (ArgumentException ae) {
            logger.error("查詢商品列表失敗", ae);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢商品列表失敗", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 代条件查询
     *
     * @param esQueryMemberCollectionGoodsDTO
     * @param pageNo
     * @param pageSize
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    public DubboPageResult<EsMemberCollectionGoodsDO> getMemberCollectionGoodListByType(int pageNo, int pageSize, EsQueryMemberCollectionGoodsDTO esQueryMemberCollectionGoodsDTO) {
        int judge = 1;
        try {
            QueryWrapper<EsMemberCollectionGoods> queryWrapperSort = new QueryWrapper<>();
            QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            queryWrapperSort.orderByDesc("id");
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, esQueryMemberCollectionGoodsDTO.getUserId());
            queryWrapperSort.lambda().eq(EsMemberCollectionGoods::getMemberId, esQueryMemberCollectionGoodsDTO.getUserId());
            //查询所有收藏商品
            List<EsMemberCollectionGoods> listGoods = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(listGoods) || listGoods.size() == 0) {
                throw new ArgumentException(MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorCode(), MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorMsg());
            }
            EsEffectAndPriceCollectionGoodsDO es = this.getEfectCollection(listGoods);
            //失效商品id
            //查询分类id
            if (null != esQueryMemberCollectionGoodsDTO && null != esQueryMemberCollectionGoodsDTO.getCategoryId()) {
                queryWrapperSort.lambda().eq(EsMemberCollectionGoods::getCategoryId, esQueryMemberCollectionGoodsDTO.getCategoryId());
            }
            //查询降价
            if (null != esQueryMemberCollectionGoodsDTO && esQueryMemberCollectionGoodsDTO.getType() == MemberConstant.queryCutPrice && null != es && CollectionUtils.isNotEmpty(es.getCutPriceIds())) {
                if (CollectionUtils.isEmpty(es.getCutPriceIds()) || es.getCutPriceIds().size() == 0) {
                    throw new ArgumentException(MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorCode(), MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorMsg());
                }
                queryWrapperSort.lambda().in(EsMemberCollectionGoods::getSkuId, es.getCutPriceIds());
                judge = 2;
            }
            //查询失效商品
            if (null != esQueryMemberCollectionGoodsDTO && esQueryMemberCollectionGoodsDTO.getType() == MemberConstant.effect && null != es && null != es.getEffectGooodIds()) {
                if (CollectionUtils.isEmpty(es.getEffectGooodIds()) || es.getEffectGooodIds().size() == 0) {
                    throw new ArgumentException(MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorCode(), MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorMsg());
                }
                queryWrapperSort.lambda().in(EsMemberCollectionGoods::getSkuId, es.getEffectGooodIds());
                judge = 3;
            }
            Page<EsMemberCollectionGoods> pages = new Page<>(pageNo, pageSize);
            IPage<EsMemberCollectionGoods> esMemberCollectionGoodsList;
            if (judge != 1) {
                esMemberCollectionGoodsList = this.page(pages, queryWrapperSort);
            } else {
                esMemberCollectionGoodsList = this.page(pages, queryWrapper);
            }
            List<EsMemberCollectionGoodsDO> esMemberCollectionGoodsDOList = new ArrayList<>();
            for (EsMemberCollectionGoods esMemberCollectionShop : esMemberCollectionGoodsList.getRecords()) {
                EsMemberCollectionGoodsDO esMemberCollectionGoodsDO = new EsMemberCollectionGoodsDO();
                BeanUtil.copyProperties(esMemberCollectionShop, esMemberCollectionGoodsDO);
                DubboResult<EsGoodsSkuDO> result = iEsGoodsSkuService.buyGetGoodsSku(esMemberCollectionGoodsDO.getSkuId(), esMemberCollectionGoodsDO.getGoodsId());
                if (!result.isSuccess() && result.getCode() == GoodsErrorCode.DATA_NOT_EXIST.getErrorCode() && judge == 3) {
                    //失效商品添加标识
                    esMemberCollectionGoodsDO.setLoseSign(MemberConstant.effect);
                    esMemberCollectionGoodsDOList.add(esMemberCollectionGoodsDO);
                    continue;
                } else {
                    //降价商品
                    if (judge == 2) {
                        if (!result.isSuccess() && result.getCode() == GoodsErrorCode.DATA_NOT_EXIST.getErrorCode()) {
                            continue;
                        }
                        if (!result.isSuccess()) {
                            throw new ArgumentException(result.getCode(), result.getMsg());
                        }
                        if (result.getData().getMoney()<(esMemberCollectionGoodsDO.getGoodsPrice())) {
                            //商品降价
                            Double subMoney = MathUtil.subtract(esMemberCollectionGoodsDO.getGoodsPrice(), result.getData().getMoney());
                            esMemberCollectionGoodsDO.setCutMoney(subMoney);
                        }
                        esMemberCollectionGoodsDO.setCurrentPrice(result.getData().getMoney());
                        //商品库存
                        esMemberCollectionGoodsDO.setQuantity(result.getData().getQuantity());
                        //比较是否库存紧缺
                        if (result.getData().getWarningValue() != null && result.getData().getWarningValue().compareTo(result.getData().getQuantity()) >= 0) {
                            esMemberCollectionGoodsDO.setShortQuantity("库存紧缺");
                        }
                        esMemberCollectionGoodsDOList.add(esMemberCollectionGoodsDO);
                        continue;
                    }
                }
            }
            return DubboPageResult.success(esMemberCollectionGoodsDOList);
        } catch (ArgumentException ae) {
            logger.error("查詢商品列表失敗", ae);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢商品列表失敗", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 分类统计数量
     *
     * @param memberId
     * @return
     */
    @Override
    public DubboPageResult<EsCollectCateryNumDO> getGoodSort(Long memberId) {
        try {
            //分类统计
            QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, memberId);
            //查询所有收藏商品
            List<EsMemberCollectionGoods> list = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
            List<Long> listGoods = list.stream().map(EsMemberCollectionGoods::getSkuId).collect(Collectors.toList());
            List<EsCollectCateryNumDO> cateryNumDOList;
            List<EsCollectCateryNumDO> resultList = new ArrayList<>();
            if (CollectionUtils.isEmpty(list)) {
                cateryNumDOList = esMemberCollectionGoodsMapper.getCateryNum(memberId);
            } else {
                cateryNumDOList = esMemberCollectionGoodsMapper.getCateryNumByType(memberId, listGoods);
            }
            cateryNumDOList.stream().forEach(es -> {
                EsCollectCateryNumDO esCollectCateryNumDO = new EsCollectCateryNumDO();
                DubboResult<EsCategoryDO> result = iEsCategoryService.getCategory(es.getCategoryId());
                BeanUtil.copyProperties(es, esCollectCateryNumDO);
                esCollectCateryNumDO.setCategoryName(result.getData().getName());
                resultList.add(esCollectCateryNumDO);
            });
            return DubboPageResult.success(resultList);
        } catch (ArgumentException ae) {
            logger.error("查詢商品列表失敗", ae);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢商品列表失敗", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 统计降价和失效收藏数量
     *
     * @param memberId
     * @return
     */
    public DubboResult<EsCutAndEffectDO> getCutAndEffetNum(Long memberId) {
        try {
            //查询所有收藏商品
            EsCutAndEffectDO esCutAndEffectDO = new EsCutAndEffectDO();
            QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, memberId);
            List<EsMemberCollectionGoods> listEfect = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
            EsEffectAndPriceCollectionGoodsDO es;
            Integer effect;
            Integer price;
            if (CollectionUtils.isNotEmpty(listEfect)) {
                //查询失效商品id
                es = this.getEfectCollection(listEfect);
                if (null != es && CollectionUtils.isNotEmpty(es.getEffectGooodIds())) {
                    //失效商品数量
                    effect = es.getEffectGooodIds().size();
                    esCutAndEffectDO.setEffectNum(effect);
                }
                if (null != es && CollectionUtils.isNotEmpty(es.getCutPriceIds())) {
                    // 降价商品数量
                    price = es.getCutPriceIds().size();
                    esCutAndEffectDO.setCutPricNum(price);
                }
            } else {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            return DubboResult.success(esCutAndEffectDO);
        } catch (ArgumentException ae) {
            logger.error("查詢商品列表失敗", ae);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢商品列表失敗", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 计算失效收藏
     *
     * @param esMemberCollectionGoodsList
     * @return
     */
    public EsEffectAndPriceCollectionGoodsDO getEfectCollection(List<EsMemberCollectionGoods> esMemberCollectionGoodsList) {
        EsEffectAndPriceCollectionGoodsDO esEffectAndPriceCollectionGoodsDO = new EsEffectAndPriceCollectionGoodsDO();
        List<Long> listEfect = new ArrayList<>();
        List<Long> listPrice = new ArrayList<>();
        for (EsMemberCollectionGoods esMemberCollectionGoods : esMemberCollectionGoodsList) {
            // 查询所有商品信息 查询商品信息不需要判断是否上架或者删除
            DubboResult<EsGoodsSkuDO> result = iEsGoodsSkuService.buyGetGoodsSku(esMemberCollectionGoods.getSkuId(), esMemberCollectionGoods.getGoodsId());
            //查询失效商品id
            if (!result.isSuccess() && result.getCode() == GoodsErrorCode.DATA_NOT_EXIST.getErrorCode()) {
                listEfect.add(esMemberCollectionGoods.getSkuId());
                continue;
            }
            //查询降价商品id
            if (result.getData().getMoney()<(esMemberCollectionGoods.getGoodsPrice())) {
                listPrice.add(result.getData().getId());
            }
        }
        esEffectAndPriceCollectionGoodsDO.setEffectGooodIds(listEfect);
        esEffectAndPriceCollectionGoodsDO.setCutPriceIds(listPrice);
        return esEffectAndPriceCollectionGoodsDO;
    }

    /**
     * 计算降价商品
     *
     * @param esMemberCollectionGoodsList
     * @return
     */
    public EsEffectAndPriceCollectionGoodsDO getReducePriceCollection(List<EsMemberCollectionGoods> esMemberCollectionGoodsList) {
        EsEffectAndPriceCollectionGoodsDO esEffectAndPriceCollectionGoodsDO = new EsEffectAndPriceCollectionGoodsDO();
        List<EsMemberCollectionGoodsDO> listPriceChange = new ArrayList<>();
        List<Long> list = new ArrayList<>();
        for (EsMemberCollectionGoods esQueryMemberCollectionGoods : esMemberCollectionGoodsList) {
            DubboResult<EsGoodsCO> goodsResult = iEsGoodsService.getEsGoods(esQueryMemberCollectionGoods.getGoodsId());//TODO 查询商品信息不需要判断是否上架或者删除 需要新接口
            if (!goodsResult.isSuccess()) {
                throw new ArgumentException(goodsResult.getCode(), goodsResult.getMsg());
            }
            if (null == goodsResult.getData()) {//TODO 查询商品信息不需要判断是否上架或者删除 需要新接口
                throw new ArgumentException(MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorCode(), MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorMsg());
            }
            int num = esQueryMemberCollectionGoods.getGoodsPrice().compareTo(goodsResult.getData().getMoney());
            if (num > 0) {
                EsMemberCollectionGoodsDO esMemberCollectionGoodsDO = new EsMemberCollectionGoodsDO();
                esMemberCollectionGoodsDO.setCurrentPrice(goodsResult.getData().getMoney());
                esMemberCollectionGoodsDO.setGoodsId(goodsResult.getData().getId());
                listPriceChange.add(esMemberCollectionGoodsDO);
                list.add(goodsResult.getData().getId());
            }
        }
        esEffectAndPriceCollectionGoodsDO.setEsMemberCollectionGoodsDOList(listPriceChange);
        return esEffectAndPriceCollectionGoodsDO;
    }


    /**
     * 删除会员收藏商品
     *
     * @param memberId
     * @param goodId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberCollectionGoodsDO> deleteMemberCollectionGood(Long memberId, Long goodId) {
        try {
            //判断该商品是否是此人收藏
            QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, memberId).eq(EsMemberCollectionGoods::getGoodsId, goodId);
            List<EsMemberCollectionGoods> esJudgeMemberCollection = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(esJudgeMemberCollection)) {
                throw new ArgumentException(MemberErrorCode.AUTHORITY.getErrorCode(), "你无权限操作");
            }
            this.esMemberCollectionGoodsMapper.delete(queryWrapper);
            //调用会员收藏成长值服务
            DubboResult<EsGrowthValueStrategyDO> resultInfo = iEsGrowthValueStrategyService.getComandcolleGrowthvalueConfigByType(MemberConstant.collectionShop);
            //判断是否添加成长值
            if (resultInfo.isSuccess() && null != resultInfo.getData() && null != resultInfo.getData().getGrowthValue()) {
                iEsMemberService.updateGrowthValue(memberId, resultInfo.getData().getGrowthValue(), false, MemberConstant.collectionStragety);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除收藏商品失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除收藏商品失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 批量删除会员收藏商品
     *
     * @param goodIds
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberCollectionGoodsDO> deleteMemberCollectionGoodBatch(Long memberId, List<Long> goodIds) {
        try {
            QueryWrapper<EsMemberCollectionGoods> queryWrapperDeleteMemberCollection = new QueryWrapper<>();
            queryWrapperDeleteMemberCollection.lambda().eq(EsMemberCollectionGoods::getMemberId, memberId)
                    .in(EsMemberCollectionGoods::getGoodsId, goodIds);
            this.esMemberCollectionGoodsMapper.delete(queryWrapperDeleteMemberCollection);
            //删除成长值
            int num = goodIds.size();
            //调用会员收藏成长值服务
            DubboResult<EsGrowthValueStrategyDO> resultInfo = iEsGrowthValueStrategyService.getComandcolleGrowthvalueConfigByType(MemberConstant.collectionGoods);
            //判断是否添加成长值
            if (resultInfo.isSuccess() && null != resultInfo.getData() && null != resultInfo.getData().getGrowthValue()) {
                int value = num * resultInfo.getData().getGrowthValue();
                iEsMemberService.updateGrowthValue(memberId, value, false, MemberConstant.collectionStragety);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("批量删除收藏商品失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("批量删除收藏商品失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 添加会员收藏商品
     *
     * @param dto
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberCollectionGoodsDO> insertMemberCollectionGood(EsMemberCollectionGoodsDTO dto) {
        try {
            //TODO判断商品是否存在
            DubboResult<EsGoodsCO> goodsResult = iEsGoodsService.getEsGoods(dto.getGoodsId());
            if (!goodsResult.isSuccess() || null == goodsResult.getData()) {
                return DubboResult.fail(MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorCode(), MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorMsg());
            }else{
                EsGoodsCO goods = goodsResult.getData();
                dto.setGoodsName(goods.getGoodsName());
                dto.setCategoryId(goods.getCategoryId());
                dto.setGoodsPrice(goods.getMoney());
                dto.setGoodsImg(goods.getOriginal());
                dto.setGoodsSn(goods.getGoodsSn());
                dto.setShopId(goods.getShopId());
            }

            //查询该商品二级级分类
            DubboResult<EsCategoryDO> categoryResult = iEsCategoryService.getCategory(goodsResult.getData().getCategoryId());
            if (!categoryResult.isSuccess() || null == categoryResult.getData()) {
                return DubboResult.fail(MemberErrorCode.GOOD_ERROR_CATAERRY.getErrorCode(), MemberErrorCode.GOOD_ERROR_CATAERRY.getErrorMsg());

            }
            // 判断当前会员id是否存在
            if(null == dto.getMemberId()){
                return DubboResult.fail(MemberErrorCode.NOT_LOGIN);
            }

            //判断当前商品是否已经添加为收藏
            QueryWrapper<EsMemberCollectionGoods> queryWrapperGood = new QueryWrapper<>();
            queryWrapperGood.lambda().eq(EsMemberCollectionGoods::getGoodsId, dto.getGoodsId())
                    .eq(EsMemberCollectionGoods::getMemberId, dto.getMemberId());
            EsMemberCollectionGoods memberCollectionGoods = esMemberCollectionGoodsMapper.selectOne(queryWrapperGood);
            if (null != memberCollectionGoods) {
                return DubboResult.fail(MemberErrorCode.MEMBER_COLLCETION_GOODS_EXIST.getErrorCode(), MemberErrorCode.MEMBER_COLLCETION_GOODS_EXIST.getErrorMsg());
            }
            //依据会员id查询会员店铺表
            QueryWrapper<EsMemberShop> queryWrapperMemberShop = new QueryWrapper<>();
            queryWrapperMemberShop.lambda().eq(EsMemberShop::getMemberId, dto.getMemberId());
            EsMemberShop esMemberShop = esMemberShopMapper.selectOne(queryWrapperMemberShop);
            //判断是否收藏的商品是否是自己店铺的
            if (null != esMemberShop && esMemberShop.getShopId().equals(dto.getShopId())) {
                return DubboResult.fail(MemberErrorCode.MEMBER_COLLECTION_GOODS_MY.getErrorCode(), MemberErrorCode.MEMBER_COLLECTION_GOODS_MY.getErrorMsg());
            }
            //保存收藏的商品信息
            EsMemberCollectionGoods esMemberCollectionGood = new EsMemberCollectionGoods();
//            esMemberCollectionGood.setMemberId(dto.getMemberId());
//            esMemberCollectionGood.setGoodsId(dto.getGoodsId());
//            esMemberCollectionGood.setGoodsName(dto.getGoodsName());
//            esMemberCollectionGood.setGoodsPrice(dto.getGoodsPrice());
//            esMemberCollectionGood.setGoodsImg(dto.getGoodsImg());
//            esMemberCollectionGood.setGoodsSn(dto.getGoodsSn());
//            esMemberCollectionGood.setShopId(dto.getShopId());
            BeanUtil.copyProperties(dto, esMemberCollectionGood);
            esMemberCollectionGood.setCategoryId(categoryResult.getData().getParentId());
            this.save(esMemberCollectionGood);
            //调用会员收藏成长值服务
            DubboResult<EsGrowthValueStrategyDO> resultInfo = iEsGrowthValueStrategyService.getComandcolleGrowthvalueConfigByType(MemberConstant.collectionGoods);
            int num = getCollectNum(dto.getMemberId());
            //判断是否添加成长值
            if (resultInfo.isSuccess() && null != resultInfo.getData() && null != resultInfo.getData().getLimitNum()) {
                if (num <= resultInfo.getData().getLimitNum()) {
                    iEsMemberService.updateGrowthValue(dto.getMemberId(), resultInfo.getData().getGrowthValue(), true, MemberConstant.collectionStragety);
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("添加收藏商品失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("添加收藏商品", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 修改降价提醒
     *
     * @param goodsId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateRemind(Long goodsId, Long userId) {
        QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, userId).eq(EsMemberCollectionGoods::getGoodsId, goodsId);
            EsMemberCollectionGoods memberCollectionGoods = esMemberCollectionGoodsMapper.selectOne(queryWrapper);
            if(memberCollectionGoods == null){
                return DubboResult.fail(MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorCode(), MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorMsg());
            }
            EsMemberCollectionGoods esMemberCollectionGoods = new EsMemberCollectionGoods();
            esMemberCollectionGoods.setPriceRemind(MemberConstant.cutPriceRemind);
            this.esMemberCollectionGoodsMapper.update(esMemberCollectionGoods, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改降价商品失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("修改降价商品失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }


    }


    /**
     * 清空所有降价提醒
     *
     * @param memberId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAllEfect(Long memberId) {
        QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
        try {
            QueryWrapper<EsMemberCollectionGoods> queryWrapperSort = new QueryWrapper<>();
            queryWrapperSort.orderByDesc("id");
            queryWrapperSort.lambda().eq(EsMemberCollectionGoods::getMemberId, memberId);
            //查询所有收藏商品
            List<EsMemberCollectionGoods> listGoods = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(listGoods) || listGoods.size() == 0) {
                throw new ArgumentException(MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorCode(), MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorMsg());
            }
            EsEffectAndPriceCollectionGoodsDO es = this.getEfectCollection(listGoods);
            if (CollectionUtils.isEmpty(es.getEffectGooodIds()) || es.getEffectGooodIds().size() == 0) {
                throw new ArgumentException(MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorCode(), MemberErrorCode.GOOD_IS_NOT_EXIST.getErrorMsg());
            }
            queryWrapperSort.lambda().in(EsMemberCollectionGoods::getSkuId, es.getEffectGooodIds());
            this.esMemberCollectionGoodsMapper.delete(queryWrapperSort);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除所有失效产品失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除所有失效产品失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }


    }

    /**
     * 取消降价提醒
     *
     * @param goodsId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteRemind(Long goodsId, Long userId) {
        QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, userId).eq(EsMemberCollectionGoods::getGoodsId, goodsId);
            EsMemberCollectionGoods esMemberCollectionGoods = new EsMemberCollectionGoods();
            esMemberCollectionGoods.setPriceRemind(MemberConstant.deletePriceRemind);
            this.esMemberCollectionGoodsMapper.update(esMemberCollectionGoods, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改降价商品失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("修改降价商品失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }


    }

    /**
     * 发送降价短信提醒
     *
     * @param goodsId
     * @return
     */
    @Override
    public void sendCutPriceSms(Long goodsId, Long shopId, Double price) {
        //查询所有收藏的降价商品
        QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsMemberCollectionGoods::getGoodsId, goodsId).eq(EsMemberCollectionGoods::getShopId, shopId).eq(EsMemberCollectionGoods::getPriceRemind, MemberConstant.cutPriceRemind);
        List<EsMemberCollectionGoods> lists = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
        //发送短信
        if (CollectionUtils.isNotEmpty(lists)) {
            if (lists.get(0).getGoodsPrice() > price) {
                //查询该用户手机号
                DubboResult<EsMemberDO> resultMember = iEsMemberService.getMemberByIdInfo(lists.get(0).getMemberId());
                if (resultMember.isSuccess() && null != resultMember.getData().getState() && resultMember.getData().getState() == MemberConstant.IsCommon && null != resultMember.getData().getMobile()) {
                    // 发送短信验证码
                    this.sendSMSCode(resultMember.getData().getMobile(), lists.get(0).getGoodsName());
                }
            }
        }
    }

    /**
     * 发送短信服务
     *
     * @param mobile
     */
    @Override
    public void sendSMSCode(String mobile, String goodsName) {
        EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();
        esSmsSendDTO.setMobile(mobile);
        esSmsSendDTO.setTemplateId(SmsTemplateCodeEnum.SMS_172598407.value());
        esSmsSendDTO.setGoodsName(goodsName);
        esSmsService.send(esSmsSendDTO);
    }

    @Override
    public DubboPageResult getMemberCollectionGoodListByMemberId(Long memberId) {
        if(null == memberId){
            return DubboPageResult.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try{
            List<Long> goodIds = esMemberCollectionGoodsMapper.getMemberCollectionGoodListByMemberId(memberId);
            return DubboPageResult.success(goodIds);
        } catch (ArgumentException ae) {
            logger.error("查询商品ID列表失败", ae);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查询商品ID列表失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 统计收藏商品个数
     *
     * @return
     */
    @Override
    public DubboResult<Integer> getCountGoodsNum(Long memberId) {
        try {
            QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, memberId);
            List<EsMemberCollectionGoods> esMemberCollectionGoodsList = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
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
     * 判断当前商品是否已经添加为收藏
     *
     * @return
     */
    @Override
    public DubboResult<Boolean> hasCollection(Long goodsId,Long memberId ) {
        try {
            QueryWrapper<EsMemberCollectionGoods> queryWrapperGood = new QueryWrapper<>();
            queryWrapperGood.lambda().eq(EsMemberCollectionGoods::getGoodsId, goodsId)
                    .eq(EsMemberCollectionGoods::getMemberId, memberId);
            EsMemberCollectionGoods memberCollectionGoods = esMemberCollectionGoodsMapper.selectOne(queryWrapperGood);
            EsCollVO collVO=new EsCollVO();
            if (memberCollectionGoods != null){
                collVO.setState(1);
            }else {
                collVO.setState(2);
            }
            return DubboResult.success(collVO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    public int getCollectNum(Long userId) {
        int num = 0;
        try {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String timesNow = simpleDateFormat.format(date);
            num = this.esMemberCollectionGoodsMapper.getCollectGoodsNum(userId, timesNow);
            return num;
        } catch (Exception ae) {
            logger.error("查詢商品列表失敗", ae);
        }
        return num;
    }


    @Override
    public DubboPageResult<EsMemberCollectionGoodsDO> getMemberCollectionGoodListBuyer(EsQueryMemberCollectionGoodsDTO dto) {
        if(dto.getType() == null){
            dto.setType(0);
        }
        try {
            //获取二级类目
            List<Long> categoryIds = new ArrayList<>();
            if(dto.getCategoryId() != null){
                DubboPageResult<EsCategoryDO> secondCategory= iEsCategoryService.getCategoryParentList(dto.getCategoryId());
                if(secondCategory.isSuccess() && secondCategory.getData() != null){
                    List<EsCategoryDO> secondCategoryList = secondCategory.getData().getList();
                    categoryIds = secondCategoryList.stream().map(e -> e.getId()).collect(Collectors.toList());
                }
            }
            //查询一级类目下的所有收藏商品Id
            QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, dto.getUserId());
            if(dto.getCategoryId() != null){
                queryWrapper.lambda().in(EsMemberCollectionGoods::getCategoryId, categoryIds);
            }
            List<EsMemberCollectionGoods> memberCollectionGoodsList = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(memberCollectionGoodsList)){
                return DubboPageResult.success(Arrays.asList());
            }
            //查询所有收藏商品的信息
            List<Long> goodIds = memberCollectionGoodsList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            Long[] ids = new Long[goodIds.size()];
            goodIds.toArray(ids);
            DubboPageResult<EsGoodsDO> goodsResult = iEsGoodsService.getEsGoods(ids);
            List<EsGoodsDO> goodList = goodsResult.isSuccess() ? goodsResult.getData().getList() : Arrays.asList();
            //判断是否下架
            List<EsMemberCollectionGoodsDO> list = goodsSort(dto, goodList, memberCollectionGoodsList);
            List<EsMemberCollectionGoodsDO> resultListDO = manualPage(list, dto);
            return DubboPageResult.success((long)list.size(), resultListDO);
        } catch (ArgumentException ae) {
            logger.error("查詢商品列表失敗", ae);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢商品列表失敗", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsMemberCollectionGoodsSortStatisticsDO> getMemberCollectionGoodNumBuyer(Long memberId) {
        try {
            //查询所有收藏商品Id
            QueryWrapper<EsMemberCollectionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCollectionGoods::getMemberId, memberId);
            List<EsMemberCollectionGoods> esMemberCollectionGoodsList = this.esMemberCollectionGoodsMapper.selectList(queryWrapper);
            if(esMemberCollectionGoodsList.size() == 0){
                return DubboResult.success(new EsMemberCollectionGoodsSortStatisticsDO());
            }
            List<Long> categoryIds = esMemberCollectionGoodsList.stream().map(e -> e.getCategoryId()).collect(Collectors.toList());
            //查询所有收藏商品的信息
            List<Long> goodIds = esMemberCollectionGoodsList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            Long[] ids = new Long[goodIds.size()];
            goodIds.toArray(ids);
            DubboPageResult<EsGoodsDO> goodsResult = iEsGoodsService.getEsGoods(ids);
            List<EsGoodsDO> goodList = goodsResult.isSuccess() ? goodsResult.getData().getList() : Arrays.asList();
            //判断是否下架、失效
            EsMemberCollectionGoodsSortStatisticsDO sortStatisticsDO = sortGoods(categoryIds, goodList, esMemberCollectionGoodsList);
            return DubboResult.success(sortStatisticsDO);
        } catch (ArgumentException ae) {
            logger.error("查詢商品列表失敗", ae);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢商品列表失敗", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<Boolean> getIsMemberCollection(Long goodsId, Long memberId) {

        int isMemberCollection = this.esMemberCollectionGoodsMapper.getIsMemberCollection(goodsId, memberId);

        if (isMemberCollection > 0){
            return DubboResult.success(true);
        }
        else {
            return DubboResult.success(false);
        }
    }

    //分类统计
    private EsMemberCollectionGoodsSortStatisticsDO sortGoods(List<Long> categoryIds, List<EsGoodsDO> goodList, List<EsMemberCollectionGoods> esMemberCollectionGoodsList){
        Map<Long, Long> goodMap = categoryIds.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicInteger invalidNum = new AtomicInteger();
        AtomicInteger cutNum = new AtomicInteger();
        List<EsMemberCollectionGoodsDO> cutGoodIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(goodList)){
            for(EsMemberCollectionGoods collectionGood: esMemberCollectionGoodsList){
                goodList.stream().map(e -> {
                    if(collectionGood.getGoodsId().equals(e.getId()) && e.getMarketEnable() == 2){
                        invalidNum.getAndIncrement();
                    }
                    if(collectionGood.getGoodsId().equals(e.getId()) && e.getMoney() < collectionGood.getGoodsPrice()){
                        cutNum.getAndIncrement();
                    }
                    return e;
                }).collect(Collectors.toList());
            }
        }
        //标签列表
        Map<Long, List<EsCategoryDO>> categoryMap = new HashMap<>();
        List<EsCategoryMemberDO> list = new ArrayList<>();
        DubboPageResult<EsCategoryDO> categoryDO = iEsCategoryService.getCategoryByIds(categoryIds);
        if(categoryDO.isSuccess() && categoryDO.getData().getList() != null){
            List<EsCategoryDO> categoryDOList = categoryDO.getData().getList();
            List<Long> parentIds = categoryDOList.stream().map(e -> e.getParentId()).collect(Collectors.toList());
            DubboPageResult<EsCategoryDO> parentCategoryDO = iEsCategoryService.getCategoryByIds(parentIds);
            if(parentCategoryDO.isSuccess() && parentCategoryDO.getData().getList() != null){
                List<EsCategoryDO> parentIdList = parentCategoryDO.getData().getList();
                categoryMap = categoryDOList.stream().collect(Collectors.groupingBy(EsCategoryDO::getParentId));
                for(EsCategoryDO esCategoryDO : parentIdList){
                    EsCategoryMemberDO categoryMemberDO = new EsCategoryMemberDO();
                    BeanUtil.copyProperties(esCategoryDO, categoryMemberDO);
                    int num = 0;
                    if(categoryMap.containsKey(esCategoryDO.getId())){
                        List<EsCategoryDO> list1 = categoryMap.get(esCategoryDO.getId());
                        for(EsCategoryDO categoryDO1 : list1){
                            if(goodMap.containsKey(categoryDO1.getId())){
                                num += goodMap.get(categoryDO1.getId()).intValue();
                            }
                        }
                    }
                    categoryMemberDO.setNum(num);
                    list.add(categoryMemberDO);
                }
            }
        }
        EsMemberCollectionGoodsSortStatisticsDO sortStatisticsDO = new EsMemberCollectionGoodsSortStatisticsDO();
        sortStatisticsDO.setAllNum(esMemberCollectionGoodsList.size());
        sortStatisticsDO.setInvalidNum(invalidNum.get());
        sortStatisticsDO.setCutNum(cutNum.get());
        sortStatisticsDO.setLabelList(list);
        return sortStatisticsDO;
    }

    //分类统计
    private List<EsMemberCollectionGoodsDO> goodsSort(EsQueryMemberCollectionGoodsDTO dto, List<EsGoodsDO> goodList, List<EsMemberCollectionGoods> esMemberCollectionGoodsList){
        List<EsMemberCollectionGoodsDO> allListDO =  new ArrayList<>();
        List<EsMemberCollectionGoodsDO> resultList = new ArrayList<>();
        List<EsMemberCollectionGoodsDO> invalidGoodIdList = new ArrayList<>();
        List<EsMemberCollectionGoodsDO> cutGoodIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(goodList)){
            if(dto.getType() == 1){
                for(EsMemberCollectionGoods collectionGood: esMemberCollectionGoodsList){
                    goodList.stream().map(e -> {
                        if(collectionGood.getGoodsId().equals(e.getId()) && e.getMoney() < collectionGood.getGoodsPrice()){
                            EsMemberCollectionGoodsDO memberCollectionGoodsDO =  new EsMemberCollectionGoodsDO();
                            BeanUtil.copyProperties(collectionGood, memberCollectionGoodsDO);
                            memberCollectionGoodsDO.setQuantity(e.getQuantity());
                            Double subtract = MathUtil.subtract(collectionGood.getGoodsPrice(),e.getMoney());
                            if (subtract > 0){
                                memberCollectionGoodsDO.setCutMoney(subtract);
                            }
                            cutGoodIdList.add(memberCollectionGoodsDO);
                        }
                        return e;
                    }).collect(Collectors.toList());
                }
                resultList = cutGoodIdList;
            }else if(dto.getType() == 2){
                for(EsMemberCollectionGoods collectionGood: esMemberCollectionGoodsList){
                    goodList.stream().map(e -> {
                        if(collectionGood.getGoodsId().equals(e.getId()) && e.getMarketEnable() == 2){
                            EsMemberCollectionGoodsDO memberCollectionGoodsDO =  new EsMemberCollectionGoodsDO();
                            BeanUtil.copyProperties(collectionGood, memberCollectionGoodsDO);
                            memberCollectionGoodsDO.setLoseSign(2);
                            memberCollectionGoodsDO.setQuantity(e.getQuantity());
                            Double subtract = MathUtil.subtract(collectionGood.getGoodsPrice(),e.getMoney());
                            if (subtract > 0){
                                memberCollectionGoodsDO.setCutMoney(subtract);
                            }
                            invalidGoodIdList.add(memberCollectionGoodsDO);
                        }
                        return e;
                    }).collect(Collectors.toList());
                }
                resultList = invalidGoodIdList;
            }else{
                for(EsMemberCollectionGoods collectionGood: esMemberCollectionGoodsList){
                    goodList.stream().map(e -> {
                        if(collectionGood.getGoodsId().equals(e.getId())){
                            EsMemberCollectionGoodsDO memberCollectionGoodsDO =  new EsMemberCollectionGoodsDO();
                            BeanUtil.copyProperties(collectionGood, memberCollectionGoodsDO);
                            if (e.getMarketEnable() == 2){
                                memberCollectionGoodsDO.setLoseSign(2);
                            }
                            memberCollectionGoodsDO.setQuantity(e.getQuantity());
                            Double subtract = MathUtil.subtract(collectionGood.getGoodsPrice(),e.getMoney());
                            if (subtract > 0){
                                memberCollectionGoodsDO.setCutMoney(subtract);
                            }
                            allListDO.add(memberCollectionGoodsDO);
                        }
                        return e;
                    }).collect(Collectors.toList());
                }
                resultList = allListDO;
            }
        }
        return resultList;
    }

    private List<EsCategoryMemberDO> labelsAssemble(Map<Long, List<EsCategoryDO>> categoryMap, List<EsCategoryMemberDO> categoryList){
        for(EsCategoryMemberDO categoryDO : categoryList){
          if(categoryMap.containsKey(categoryDO.getId())){
              categoryDO.setNum(categoryMap.get(categoryDO.getId()).size());
          }
        }
        return categoryList;
    }

    //手动分页
    private List<EsMemberCollectionGoodsDO> manualPage(List<EsMemberCollectionGoodsDO> list, EsQueryMemberCollectionGoodsDTO dto){
        List<EsMemberCollectionGoodsDO> subList = new ArrayList<>();
        int pageNum = dto.getPageNum();
        int pageSize = dto.getPageSize();
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

}
