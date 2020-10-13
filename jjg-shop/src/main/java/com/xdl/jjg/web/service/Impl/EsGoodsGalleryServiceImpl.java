package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.EsGoodsGalleryDO;
import com.shopx.goods.api.model.domain.dto.EsGoodsGalleryDTO;
import com.shopx.goods.api.service.IEsGoodsGalleryService;
import com.shopx.goods.dao.entity.EsGoodsGallery;
import com.shopx.goods.dao.mapper.EsGoodsGalleryMapper;
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
 *  服务实现类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGoodsGalleryService.class, timeout = 50000)
public class EsGoodsGalleryServiceImpl extends ServiceImpl<EsGoodsGalleryMapper, EsGoodsGallery> implements IEsGoodsGalleryService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsGalleryServiceImpl.class);

    @Autowired
    private EsGoodsGalleryMapper goodsGalleryMapper;

    /**
     * 插入数据
     *
     * @param goodsGalleryDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsGalleryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsGalleryDO> insertGoodsGallery(EsGoodsGalleryDTO goodsGalleryDTO,Long skuId) {
        try {
            if( goodsGalleryDTO != null){
                QueryWrapper<EsGoodsGallery> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsGoodsGallery::getSkuId,skuId);
                this.remove(queryWrapper);
                goodsGalleryDTO.getGalleryList().forEach(gallery->{
                    EsGoodsGallery goodsGallery = new EsGoodsGallery();
                    BeanUtil.copyProperties(gallery,goodsGallery);
                    goodsGallery.setAlbumNo(goodsGalleryDTO.getAlbumNo());
                    goodsGallery.setSkuId(skuId);
                    this.goodsGalleryMapper.insert(goodsGallery);
                });
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品相册新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("商品相册新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 商品相册新增
     * @param goodsGalleryDTOList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsGalleryDO> insertGoodsGallery(List<EsGoodsGalleryDTO> goodsGalleryDTOList,Long skuId) {
        try {
            if(CollectionUtils.isNotEmpty(goodsGalleryDTOList)){
                QueryWrapper<EsGoodsGallery> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsGoodsGallery::getSkuId,skuId);
                this.remove(queryWrapper);
                List<EsGoodsGallery> esGoodsGalleryList = goodsGalleryDTOList.stream().map(esGoodsGalleryDTO -> {
                    EsGoodsGallery gallery = new EsGoodsGallery();
                    BeanUtil.copyProperties(esGoodsGalleryDTO,gallery);
                    gallery.setSkuId(skuId);
                    return gallery;
                }).collect(Collectors.toList());
                this.saveBatch(esGoodsGalleryList,esGoodsGalleryList.size());
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品相册新增", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("商品相册新增", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsGalleryDO>
     */
    @Override
    public DubboResult<EsGoodsGalleryDO> getGoodsGallery(Long id) {
        try {
            QueryWrapper<EsGoodsGallery> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsGallery::getId, id);
            EsGoodsGallery goodsGallery = this.goodsGalleryMapper.selectOne(queryWrapper);
            EsGoodsGalleryDO goodsGalleryDO = new EsGoodsGalleryDO();
            if (goodsGallery == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(goodsGallery, goodsGalleryDO);
            return DubboResult.success(goodsGalleryDO);
        } catch (ArgumentException ae){
            logger.error("商品相册查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品相册查询失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsGoodsGalleryDO> getGoodsGalleryBySkuId(Long skuId) {
        try {
            QueryWrapper<EsGoodsGallery> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsGallery::getSkuId, skuId);
            List<EsGoodsGallery> galleryList = this.list(queryWrapper);
            EsGoodsGalleryDO goodsGalleryDO =  new EsGoodsGalleryDO();
            List<EsGoodsGalleryDO> galleryDOList = BeanUtil.copyList(galleryList,EsGoodsGalleryDO.class);
            return DubboPageResult.success(galleryDOList);
        } catch (ArgumentException ae){
            logger.error("商品相册查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品相册查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param goodsGalleryDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsGalleryDO>
     */
    @Override
    public DubboPageResult<EsGoodsGalleryDO> getGoodsGalleryList(EsGoodsGalleryDTO goodsGalleryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodsGallery> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGoodsGallery> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsGallery> iPage = this.page(page, queryWrapper);
            List<EsGoodsGalleryDO> goodsGalleryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodsGalleryDOList = iPage.getRecords().stream().map(goodsGallery -> {
                    EsGoodsGalleryDO goodsGalleryDO = new EsGoodsGalleryDO();
                    BeanUtil.copyProperties(goodsGallery, goodsGalleryDO);
                    return goodsGalleryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),goodsGalleryDOList);
        } catch (ArgumentException ae){
            logger.error("商品相册分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品相册分页查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsGalleryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsGalleryDO> deleteGoodsGallery(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsGoodsGallery> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsGoodsGallery::getId, id);
            this.goodsGalleryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除商品相册失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除商品相册失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 批量删除相册
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsGalleryDO> deleteGoodsGallery(long[] ids) {
        try {
            QueryWrapper<EsGoodsGallery> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsGoodsGallery::getSkuId, ids);
            this.goodsGalleryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("批量删除商品相册失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("批量删除商品相册失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
