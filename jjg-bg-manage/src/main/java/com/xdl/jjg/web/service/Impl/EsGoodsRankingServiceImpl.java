package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.domain.EsSalesRankingGoodsDO;
import com.jjg.system.model.domain.EsGoodsRankingDO;
import com.jjg.system.model.dto.EsGoodsRankingDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsGoodsRanking;
import com.xdl.jjg.mapper.EsGoodsRankingMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsGoodsRankingService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
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
 * 热门榜单 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Service
public class EsGoodsRankingServiceImpl extends ServiceImpl<EsGoodsRankingMapper, EsGoodsRanking> implements IEsGoodsRankingService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsRankingServiceImpl.class);

    @Autowired
    private EsGoodsRankingMapper goodsRankingMapper;
    @Autowired
    private GoodsService goodsService;

    /**
     * 插入热门榜单数据
     *
     * @param goodsRankingDTO 热门榜单DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsRankingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGoodsRanking(EsGoodsRankingDTO goodsRankingDTO) {
        try {
            if (goodsRankingDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            //判断首页榜单的个数
            if (goodsRankingDTO.getHomePage() == 1) {
                QueryWrapper<EsGoodsRanking> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsGoodsRanking::getHomePage, 1);
                Integer count = goodsRankingMapper.selectCount(queryWrapper);
                if (count >= 3) {
                    throw new ArgumentException(ErrorCode.GOODS_RANKING_COUNT_ERROR.getErrorCode(), "首页榜单个数不能超过三个");
                }
            }
            EsGoodsRanking goodsRanking = new EsGoodsRanking();
            BeanUtil.copyProperties(goodsRankingDTO, goodsRanking);
            this.goodsRankingMapper.insert(goodsRanking);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("热门榜单新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("热门榜单新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新热门榜单数据
     *
     * @param goodsRankingDTO 热门榜单DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsRankingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGoodsRanking(EsGoodsRankingDTO goodsRankingDTO) {
        try {
            if (goodsRankingDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //判断首页榜单的个数
            if (goodsRankingDTO.getHomePage() == 1) {
                QueryWrapper<EsGoodsRanking> query = new QueryWrapper<>();
                query.lambda().eq(EsGoodsRanking::getHomePage, 1);
                List<EsGoodsRanking> list = goodsRankingMapper.selectList(query);
                List<Long> ids = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(list)) {
                    list.stream().forEach(goodsRanking -> {
                        ids.add(goodsRanking.getId());
                    });
                }
                if (!ids.contains(goodsRankingDTO.getId()) && list.size() >= 3) {
                    throw new ArgumentException(ErrorCode.GOODS_RANKING_COUNT_ERROR.getErrorCode(), "首页榜单个数不能超过三个");
                }
            }
            EsGoodsRanking goodsRanking = new EsGoodsRanking();
            BeanUtil.copyProperties(goodsRankingDTO, goodsRanking);
            QueryWrapper<EsGoodsRanking> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsRanking::getId, goodsRankingDTO.getId());
            this.goodsRankingMapper.update(goodsRanking, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("热门榜单更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("热门榜单更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取热门榜单详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsRankingDO>
     */
    @Override
    public DubboResult<EsGoodsRankingDO> getGoodsRanking(Long id) {
        try {
            QueryWrapper<EsGoodsRanking> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsRanking::getId, id);
            EsGoodsRanking goodsRanking = this.goodsRankingMapper.selectOne(queryWrapper);
            EsGoodsRankingDO goodsRankingDO = new EsGoodsRankingDO();
            if (goodsRanking == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(goodsRanking, goodsRankingDO);
            return DubboResult.success(goodsRankingDO);
        } catch (ArgumentException ae) {
            logger.error("热门榜单查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("热门榜单查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询热门榜单列表
     *
     * @param goodsRankingDTO 热门榜单DTO
     * @param pageSize        页码
     * @param pageNum         页数
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsRankingDO>
     */
    @Override
    public DubboPageResult<EsGoodsRankingDO> getGoodsRankingList(EsGoodsRankingDTO goodsRankingDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodsRanking> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().like(StringUtil.notEmpty(goodsRankingDTO.getRankingName()), EsGoodsRanking::getRankingName, goodsRankingDTO.getRankingName()).eq(goodsRankingDTO.getHomePage() != null, EsGoodsRanking::getHomePage, goodsRankingDTO.getHomePage());
            Page<EsGoodsRanking> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsRanking> iPage = this.page(page, queryWrapper);
            List<EsGoodsRankingDO> goodsRankingDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodsRankingDOList = iPage.getRecords().stream().map(goodsRanking -> {
                    EsGoodsRankingDO goodsRankingDO = new EsGoodsRankingDO();
                    BeanUtil.copyProperties(goodsRanking, goodsRankingDO);
                    DubboPageResult<EsSalesRankingGoodsDO> result = goodsService.getByCategoryId(goodsRanking.getGoodsCategoryId(), goodsRanking.getGoodsId());
                    if (result.isSuccess()) {
                        int sum = result.getData().getList().stream().mapToInt(EsSalesRankingGoodsDO::getBuyCount).sum();
                        goodsRankingDO.setBuyCount(sum);

//                        int sum = 0;
//                        List<EsSalesRankingGoodsDO> list = result.getData().getList();
//                        for (EsSalesRankingGoodsDO esSalesRankingGoodsDO : list) {
//                            sum = sum + esSalesRankingGoodsDO.getBuyCount();
//                        }
//                        goodsRankingDO.setBuyCount(sum);
                    }
                    return goodsRankingDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), goodsRankingDOList);
        } catch (ArgumentException ae) {
            logger.error("热门榜单分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("热门榜单分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除热门榜单数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsRankingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGoodsRanking(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsGoodsRanking> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsGoodsRanking::getId, id);
            this.goodsRankingMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("热门榜单删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("热门榜单删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsGoodsRankingDO> selectGoodsRankingList(int pageSize, int pageNum) {
        QueryWrapper<EsGoodsRanking> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsGoodsRanking::getHomePage, 2);
            Page<EsGoodsRanking> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsRanking> iPage = this.page(page, queryWrapper);
            List<EsGoodsRankingDO> goodsRankingDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                iPage.getRecords().stream().forEach(goodsRanking -> {
                    EsGoodsRankingDO goodsRankingDO = new EsGoodsRankingDO();
                    BeanUtil.copyProperties(goodsRanking, goodsRankingDO);
                    //查询商品信息
                    DubboResult<EsGoodsCO> goodsCODubboResult = goodsService.getEsBuyerGoods(goodsRankingDO.getGoodsId());
                    EsGoodsCO esGoodsCO = goodsCODubboResult.getData();
                    if (!goodsCODubboResult.isSuccess() || esGoodsCO == null || esGoodsCO.getIsDel() == 1 || esGoodsCO.getIsAuth() != 1 || esGoodsCO.getMarketEnable() == 2 || esGoodsCO.getIsGifts() == 1) {
                        //删除数据库该条数据
                        deleteGoodsRanking(goodsRankingDO.getId());
                    } else {
                        //设置商品价格
                        goodsRankingDO.setMoney(esGoodsCO.getMoney());
                        //设置销量
                        goodsRankingDO.setBuyCount(esGoodsCO.getBuyCount());
                        //设置商品名称
                        goodsRankingDO.setGoodsName(esGoodsCO.getGoodsName());
                        //设置图片
                        goodsRankingDO.setOriginal(esGoodsCO.getOriginal());
                        //设置榜单销量
                        int count = 0;
                        DubboPageResult<EsGoodsDO> result = goodsService.buyerGetGoodsByCategoryId(goodsRankingDO.getGoodsCategoryId());
                        if (result.isSuccess()) {
                            List<EsGoodsDO> list = result.getData().getList();
                            if (CollectionUtils.isNotEmpty(list)) {
                                for (EsGoodsDO goodsDO : list) {
                                    count += goodsDO.getBuyCount();
                                }
                            }
                        }
                        goodsRankingDO.setCount(count);
                        goodsRankingDOList.add(goodsRankingDO);
                    }
                });
            }
            return DubboPageResult.success(iPage.getTotal(), goodsRankingDOList);
        } catch (ArgumentException ae) {
            logger.error("热门榜单分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("热门榜单分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
