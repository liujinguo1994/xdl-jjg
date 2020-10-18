package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.system.model.domain.EsCustomCategoryDO;
import com.jjg.system.model.domain.EsFindGoodsDO;
import com.jjg.system.model.domain.EsFindGoodsGalleryDO;
import com.jjg.system.model.domain.EsFindGoodsGalleryDTO;
import com.jjg.system.model.dto.EsFindGoodsDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsFindGoods;
import com.xdl.jjg.entity.EsFindGoodsGallery;
import com.xdl.jjg.mapper.EsFindGoodsGalleryMapper;
import com.xdl.jjg.mapper.EsFindGoodsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCustomCategoryService;
import com.xdl.jjg.web.service.IEsFindGoodsService;
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
 * 发现好货 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Service
public class EsFindGoodsServiceImpl extends ServiceImpl<EsFindGoodsMapper, EsFindGoods> implements IEsFindGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsFindGoodsServiceImpl.class);

    @Autowired
    private EsFindGoodsMapper findGoodsMapper;
    @Autowired
    private IEsCustomCategoryService customCategoryService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private EsFindGoodsGalleryMapper findGoodsGalleryMapper;

    /**
     * 插入发现好货数据
     *
     * @param findGoodsDTO 发现好货DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertFindGoods(EsFindGoodsDTO findGoodsDTO) {
        try {
            if (findGoodsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsFindGoods findGoods = new EsFindGoods();
            BeanUtil.copyProperties(findGoodsDTO, findGoods);
            findGoodsMapper.insert(findGoods);
            //插入相册
            List<EsFindGoodsGalleryDTO> galleryList = findGoodsDTO.getGalleryList();
            if (CollectionUtils.isNotEmpty(galleryList)) {
                for (EsFindGoodsGalleryDTO galleryDTO : galleryList) {
                    EsFindGoodsGallery findGoodsGallery = new EsFindGoodsGallery();
                    findGoodsGallery.setFindGoodsId(findGoods.getId());
                    findGoodsGallery.setUrl(galleryDTO.getUrl());
                    findGoodsGalleryMapper.insert(findGoodsGallery);
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("发现好货新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("发现好货新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新发现好货数据
     *
     * @param findGoodsDTO 发现好货DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateFindGoods(EsFindGoodsDTO findGoodsDTO) {
        try {
            if (findGoodsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsFindGoods findGoods = new EsFindGoods();
            BeanUtil.copyProperties(findGoodsDTO, findGoods);
            QueryWrapper<EsFindGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFindGoods::getId, findGoodsDTO.getId());
            this.findGoodsMapper.update(findGoods, queryWrapper);
            //相册先删后插
            QueryWrapper<EsFindGoodsGallery> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsFindGoodsGallery::getFindGoodsId, findGoodsDTO.getId());
            findGoodsGalleryMapper.delete(wrapper);
            List<EsFindGoodsGalleryDTO> galleryList = findGoodsDTO.getGalleryList();
            if (CollectionUtils.isNotEmpty(galleryList)) {
                for (EsFindGoodsGalleryDTO galleryDTO : galleryList) {
                    EsFindGoodsGallery findGoodsGallery = new EsFindGoodsGallery();
                    findGoodsGallery.setFindGoodsId(findGoods.getId());
                    findGoodsGallery.setUrl(galleryDTO.getUrl());
                    findGoodsGalleryMapper.insert(findGoodsGallery);
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("发现好货更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发现好货更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取发现好货详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     */
    @Override
    public DubboResult<EsFindGoodsDO> getFindGoods(Long id) {
        try {
            QueryWrapper<EsFindGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFindGoods::getId, id);
            EsFindGoods findGoods = this.findGoodsMapper.selectOne(queryWrapper);
            EsFindGoodsDO findGoodsDO = new EsFindGoodsDO();
            if (findGoods == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(findGoods, findGoodsDO);
            return DubboResult.success(findGoodsDO);
        } catch (ArgumentException ae) {
            logger.error("发现好货查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发现好货查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询发现好货列表
     *
     * @param findGoodsDTO 发现好货DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboPageResult<EsFindGoodsDO>
     */
    @Override
    public DubboPageResult<EsFindGoodsDO> getFindGoodsList(EsFindGoodsDTO findGoodsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsFindGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(findGoodsDTO.getCustomCategoryId() != null, EsFindGoods::getCustomCategoryId, findGoodsDTO.getCustomCategoryId());
            Page<EsFindGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsFindGoods> iPage = this.page(page, queryWrapper);
            List<EsFindGoodsDO> findGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                findGoodsDOList = iPage.getRecords().stream().map(findGoods -> {
                    EsFindGoodsDO findGoodsDO = new EsFindGoodsDO();
                    BeanUtil.copyProperties(findGoods, findGoodsDO);
                    DubboResult<EsCustomCategoryDO> result = customCategoryService.getCustomCategory(findGoodsDO.getCustomCategoryId());
                    EsCustomCategoryDO customCategoryDO = result.getData();
                    if (customCategoryDO != null) {
                        findGoodsDO.setCategoryName(customCategoryDO.getCategoryName());
                    }
                    QueryWrapper<EsFindGoodsGallery> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(EsFindGoodsGallery::getFindGoodsId, findGoodsDO.getId());
                    List<EsFindGoodsGallery> esFindGoodsGalleries = findGoodsGalleryMapper.selectList(wrapper);
                    if (CollectionUtils.isNotEmpty(esFindGoodsGalleries)) {
                        List<EsFindGoodsGalleryDO> doList = BeanUtil.copyList(esFindGoodsGalleries, EsFindGoodsGalleryDO.class);
                        findGoodsDO.setGalleryList(doList);
                    }
                    return findGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), findGoodsDOList);
        } catch (ArgumentException ae) {
            logger.error("发现好货分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发现好货分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除发现好货数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-07
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteFindGoods(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsFindGoods> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsFindGoods::getId, id);
            this.findGoodsMapper.delete(deleteWrapper);
            //删除相册
            QueryWrapper<EsFindGoodsGallery> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsFindGoodsGallery::getFindGoodsId, id);
            findGoodsGalleryMapper.delete(wrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("发现好货删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发现好货删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsFindGoodsDO> getListByCustomCategoryId(Long customCategoryId, int pageSize, int pageNum) {
        QueryWrapper<EsFindGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsFindGoods::getCustomCategoryId, customCategoryId);
            Page<EsFindGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsFindGoods> iPage = this.page(page, queryWrapper);
            List<EsFindGoodsDO> findGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                iPage.getRecords().stream().forEach(findGoods -> {
                    EsFindGoodsDO findGoodsDO = new EsFindGoodsDO();
                    BeanUtil.copyProperties(findGoods, findGoodsDO);
                    //设置相册
                    QueryWrapper<EsFindGoodsGallery> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(EsFindGoodsGallery::getFindGoodsId, findGoodsDO.getId());
                    List<EsFindGoodsGallery> esFindGoodsGalleries = findGoodsGalleryMapper.selectList(wrapper);
                    if (CollectionUtils.isNotEmpty(esFindGoodsGalleries)) {
                        List<EsFindGoodsGalleryDO> doList = BeanUtil.copyList(esFindGoodsGalleries, EsFindGoodsGalleryDO.class);
                        findGoodsDO.setGalleryList(doList);
                    }
                    //查询商品信息
                    DubboResult<EsGoodsCO> goodsCODubboResult = goodsService.getEsBuyerGoods(findGoodsDO.getGoodsId());
                    EsGoodsCO esGoodsCO = goodsCODubboResult.getData();
                    //商品不存在或者下线将数据删除
                    if (!goodsCODubboResult.isSuccess() || esGoodsCO == null || esGoodsCO.getIsDel() == 1 || esGoodsCO.getIsAuth() != 1 || esGoodsCO.getMarketEnable() == 2 || esGoodsCO.getIsGifts() == 1) {
                        //删除数据库该条数据
                        deleteFindGoods(findGoodsDO.getId());
                    } else {
                        //设置商品价格
                        findGoodsDO.setMoney(esGoodsCO.getMoney());
                        //设置浏览数量
                        findGoodsDO.setViewCount(esGoodsCO.getViewCount());
                        findGoodsDOList.add(findGoodsDO);
                    }
                });
            }
            return DubboPageResult.success(iPage.getTotal(), findGoodsDOList);
        } catch (ArgumentException ae) {
            logger.error("发现好货分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发现好货分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsFindGoodsDO> getByCustomCategoryId(Long customCategoryId) {

        QueryWrapper<EsFindGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsFindGoods::getCustomCategoryId, customCategoryId);
            List<EsFindGoods> esFindGoods = this.findGoodsMapper.selectList(queryWrapper);
            List<EsFindGoodsDO> findGoodsDOList = new ArrayList<>();
            esFindGoods.forEach(findGoods -> {
                EsFindGoodsDO findGoodsDO = new EsFindGoodsDO();
                BeanUtil.copyProperties(findGoods, findGoodsDO);
                //设置相册
                QueryWrapper<EsFindGoodsGallery> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(EsFindGoodsGallery::getFindGoodsId, findGoodsDO.getId());
                List<EsFindGoodsGallery> esFindGoodsGalleries = findGoodsGalleryMapper.selectList(wrapper);
                if (CollectionUtils.isNotEmpty(esFindGoodsGalleries)) {
                    List<EsFindGoodsGalleryDO> doList = BeanUtil.copyList(esFindGoodsGalleries, EsFindGoodsGalleryDO.class);
                    findGoodsDO.setGalleryList(doList);
                }
                //查询商品信息
                DubboResult<EsGoodsCO> goodsCODubboResult = goodsService.getEsBuyerGoods(findGoodsDO.getGoodsId());
                EsGoodsCO esGoodsCO = goodsCODubboResult.getData();
                //商品不存在或者下线将数据删除
                if (!goodsCODubboResult.isSuccess() || esGoodsCO == null || esGoodsCO.getIsDel() == 1 || esGoodsCO.getIsAuth() != 1 || esGoodsCO.getMarketEnable() == 2 || esGoodsCO.getIsGifts() == 1) {
                    //删除数据库该条数据
                    deleteFindGoods(findGoodsDO.getId());
                } else {
                    //设置商品价格
                    findGoodsDO.setMoney(esGoodsCO.getMoney());
                    //设置浏览数量
                    findGoodsDO.setViewCount(esGoodsCO.getViewCount());
                    findGoodsDOList.add(findGoodsDO);
                }
            });
            return DubboPageResult.success(findGoodsDOList);
        } catch (ArgumentException ae) {
            logger.error("发现好货查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发现好货查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
