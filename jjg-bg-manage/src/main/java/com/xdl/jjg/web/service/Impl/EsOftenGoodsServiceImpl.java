package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.system.model.domain.EsCustomCategoryDO;
import com.jjg.system.model.domain.EsOftenGoodsDO;
import com.jjg.system.model.dto.EsOftenGoodsDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsOftenGoods;
import com.xdl.jjg.mapper.EsOftenGoodsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCustomCategoryService;
import com.xdl.jjg.web.service.IEsOftenGoodsService;
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
 * 常买商品 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Service
public class EsOftenGoodsServiceImpl extends ServiceImpl<EsOftenGoodsMapper, EsOftenGoods> implements IEsOftenGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsOftenGoodsServiceImpl.class);

    @Autowired
    private EsOftenGoodsMapper oftenGoodsMapper;
    @Autowired
    private IEsCustomCategoryService customCategoryService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 插入常买商品数据
     *
     * @param oftenGoodsDTO 常买商品DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertOftenGoods(EsOftenGoodsDTO oftenGoodsDTO) {
        try {
            if (oftenGoodsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsOftenGoods oftenGoods = new EsOftenGoods();
            BeanUtil.copyProperties(oftenGoodsDTO, oftenGoods);
            this.oftenGoodsMapper.insert(oftenGoods);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("常买商品新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("常买商品新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新常买商品数据
     *
     * @param oftenGoodsDTO 常买商品DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateOftenGoods(EsOftenGoodsDTO oftenGoodsDTO) {
        try {
            if (oftenGoodsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsOftenGoods oftenGoods = new EsOftenGoods();
            BeanUtil.copyProperties(oftenGoodsDTO, oftenGoods);
            QueryWrapper<EsOftenGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOftenGoods::getId, oftenGoodsDTO.getId());
            this.oftenGoodsMapper.update(oftenGoods, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("常买商品更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("常买商品更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取常买商品详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     */
    @Override
    public DubboResult<EsOftenGoodsDO> getOftenGoods(Long id) {
        try {
            QueryWrapper<EsOftenGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOftenGoods::getId, id);
            EsOftenGoods oftenGoods = this.oftenGoodsMapper.selectOne(queryWrapper);
            EsOftenGoodsDO oftenGoodsDO = new EsOftenGoodsDO();
            if (oftenGoods == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(oftenGoods, oftenGoodsDO);
            return DubboResult.success(oftenGoodsDO);
        } catch (ArgumentException ae) {
            logger.error("常买商品查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("常买商品查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询常买商品列表
     *
     * @param oftenGoodsDTO 常买商品DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboPageResult<EsOftenGoodsDO>
     */
    @Override
    public DubboPageResult<EsOftenGoodsDO> getOftenGoodsList(EsOftenGoodsDTO oftenGoodsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsOftenGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(oftenGoodsDTO.getCustomCategoryId() != null, EsOftenGoods::getCustomCategoryId, oftenGoodsDTO.getCustomCategoryId());
            Page<EsOftenGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsOftenGoods> iPage = this.page(page, queryWrapper);
            List<EsOftenGoodsDO> oftenGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                oftenGoodsDOList = iPage.getRecords().stream().map(oftenGoods -> {
                    EsOftenGoodsDO oftenGoodsDO = new EsOftenGoodsDO();
                    BeanUtil.copyProperties(oftenGoods, oftenGoodsDO);
                    DubboResult<EsCustomCategoryDO> result = customCategoryService.getCustomCategory(oftenGoodsDO.getCustomCategoryId());
                    EsCustomCategoryDO customCategoryDO = result.getData();
                    if (customCategoryDO != null) {
                        oftenGoodsDO.setCategoryName(customCategoryDO.getCategoryName());
                    }
                    return oftenGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), oftenGoodsDOList);
        } catch (ArgumentException ae) {
            logger.error("常买商品分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("常买商品分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除常买商品数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteOftenGoods(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsOftenGoods> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsOftenGoods::getId, id);
            this.oftenGoodsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("常买商品删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("常买商品删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsOftenGoodsDO> getListByCustomCategoryId(Long customCategoryId, int pageSize, int pageNum) {
        QueryWrapper<EsOftenGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsOftenGoods::getCustomCategoryId, customCategoryId);
            Page<EsOftenGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsOftenGoods> iPage = this.page(page, queryWrapper);
            List<EsOftenGoodsDO> oftenGoodsDOList = new ArrayList<>();
            List<EsOftenGoods> records = iPage.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                records.stream().forEach(oftenGoods -> {
                    EsOftenGoodsDO oftenGoodsDO = new EsOftenGoodsDO();
                    BeanUtil.copyProperties(oftenGoods, oftenGoodsDO);
                    //查询商品信息
                    DubboResult<EsGoodsCO> goodsCODubboResult = goodsService.getEsBuyerGoods(oftenGoodsDO.getGoodsId());
                    EsGoodsCO esGoodsCO = goodsCODubboResult.getData();
                    if (!goodsCODubboResult.isSuccess() || esGoodsCO == null || esGoodsCO.getIsDel() == 1 || esGoodsCO.getIsAuth() != 1 || esGoodsCO.getMarketEnable() == 2 || esGoodsCO.getIsGifts() == 1) {
                        //删除数据库该条数据
                        deleteOftenGoods(oftenGoodsDO.getId());
                    } else {
                        //设置商品价格
                        oftenGoodsDO.setMoney(esGoodsCO.getMoney());
                        oftenGoodsDOList.add(oftenGoodsDO);
                    }
                });
            }
            return DubboPageResult.success(iPage.getTotal(), oftenGoodsDOList);
        } catch (ArgumentException ae) {
            logger.error("常买商品分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("常买商品分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    @Override
    public DubboPageResult<EsOftenGoodsDO> getByCustomCategoryId(Long customCategoryId) {
        QueryWrapper<EsOftenGoods> queryWrapper = new QueryWrapper<>();
        try {

            queryWrapper.lambda().eq(EsOftenGoods::getCustomCategoryId, customCategoryId);
            List<EsOftenGoods> esOfenGoods = this.oftenGoodsMapper.selectList(queryWrapper);

            List<EsOftenGoodsDO> oftenGoodsDOList = new ArrayList<>();

            esOfenGoods.forEach(oftenGood -> {
                EsOftenGoodsDO oftenGoodsDO = new EsOftenGoodsDO();
                BeanUtil.copyProperties(oftenGood, oftenGoodsDO);
                //查询商品信息
                DubboResult<EsGoodsCO> goodsCODubboResult = goodsService.getEsBuyerGoods(oftenGoodsDO.getGoodsId());
                EsGoodsCO esGoodsCO = goodsCODubboResult.getData();
                if (!goodsCODubboResult.isSuccess() || esGoodsCO == null || esGoodsCO.getIsDel() == 1 || esGoodsCO.getIsAuth() != 1 || esGoodsCO.getMarketEnable() == 2 || esGoodsCO.getIsGifts() == 1) {
                    //删除数据库该条数据
                    deleteOftenGoods(oftenGoodsDO.getId());
                } else {
                    //设置商品价格
                    oftenGoodsDO.setMoney(esGoodsCO.getMoney());
                    oftenGoodsDOList.add(oftenGoodsDO);
                }
            });

            return DubboPageResult.success(oftenGoodsDOList);
        } catch (ArgumentException ae) {
            logger.error("常买商品查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("常买商品查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
