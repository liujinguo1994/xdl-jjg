package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsGoodGoods;
import com.xdl.jjg.mapper.EsGoodGoodsMapper;
import com.xdl.jjg.model.domain.EsGoodGoodsDO;
import com.xdl.jjg.model.dto.EsGoodGoodsDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsGoodGoodsService;
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
 * 品质好货 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-08-06
 */
@Service
public class EsGoodGoodsServiceImpl extends ServiceImpl<EsGoodGoodsMapper, EsGoodGoods> implements IEsGoodGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodGoodsServiceImpl.class);

    @Autowired
    private EsGoodGoodsMapper goodGoodsMapper;

    /**
     * 插入品质好货数据
     *
     * @param goodGoodsDTO 品质好货DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-08-06
     * @return: com.shopx.common.model.result.DubboResult<EsGoodGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGoodGoods(EsGoodGoodsDTO goodGoodsDTO) {
        try {
            if (goodGoodsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsGoodGoods goodGoods = new EsGoodGoods();
            BeanUtil.copyProperties(goodGoodsDTO, goodGoods);
            goodGoods.setState(1);
            this.goodGoodsMapper.insert(goodGoods);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("品质好货新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("品质好货新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新品质好货数据
     *
     * @param goodGoodsDTO 品质好货DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-08-06
     * @return: com.shopx.common.model.result.DubboResult<EsGoodGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGoodGoods(EsGoodGoodsDTO goodGoodsDTO) {
        try {
            EsGoodGoods esGoodGoods = goodGoodsMapper.selectById(goodGoodsDTO.getId());
            if (esGoodGoods == null){
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), "数据不存在");
            }
            if (esGoodGoods.getState() != 1){
                throw new ArgumentException(ErrorCode.NOT_AWAIT_ISSUE_NOT_UPDATE.getErrorCode(), "只有待发布才可以编辑");
            }
            EsGoodGoods goodGoods = new EsGoodGoods();
            BeanUtil.copyProperties(goodGoodsDTO, goodGoods);
            goodGoodsMapper.updateById(goodGoods);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("品质好货更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("品质好货更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取品质好货详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-08-06
     * @return: com.shopx.common.model.result.DubboResult<EsGoodGoodsDO>
     */
    @Override
    public DubboResult<EsGoodGoodsDO> getGoodGoods(Long id) {
        try {
            QueryWrapper<EsGoodGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodGoods::getId, id);
            EsGoodGoods goodGoods = this.goodGoodsMapper.selectOne(queryWrapper);
            EsGoodGoodsDO goodGoodsDO = new EsGoodGoodsDO();
            if (goodGoods == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(goodGoods, goodGoodsDO);
            return DubboResult.success(goodGoodsDO);
        } catch (ArgumentException ae){
            logger.error("品质好货查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("品质好货查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询品质好货列表
     *
     * @param goodGoodsDTO 品质好货DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-08-06
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodGoodsDO>
     */
    @Override
    public DubboPageResult<EsGoodGoodsDO> getGoodGoodsList(EsGoodGoodsDTO goodGoodsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().like(StringUtil.notEmpty(goodGoodsDTO.getGoodsName()),EsGoodGoods::getGoodsName,goodGoodsDTO.getGoodsName()).eq(goodGoodsDTO.getState() != null,EsGoodGoods::getState,goodGoodsDTO.getState());

            Page<EsGoodGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodGoods> iPage = this.page(page, queryWrapper);
            List<EsGoodGoodsDO> goodGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodGoodsDOList = iPage.getRecords().stream().map(goodGoods -> {
                    EsGoodGoodsDO goodGoodsDO = new EsGoodGoodsDO();
                    BeanUtil.copyProperties(goodGoods, goodGoodsDO);
                    return goodGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),goodGoodsDOList);
        } catch (ArgumentException ae){
            logger.error("品质好货分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("品质好货分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 删除或批量删除
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult batchDel(Integer[] ids) {
        try {
            QueryWrapper<EsGoodGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoodGoods::getId, ids);
            List<EsGoodGoods> goodGoodsList = goodGoodsMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(goodGoodsList)){
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), "数据不存在");
            }
            goodGoodsList.stream().forEach(esGoodGoods -> {
                if (esGoodGoods.getState() == 2){
                    throw new ArgumentException(ErrorCode.ISSUE_STATE_NOT_DELETE.getErrorCode(), "存在已发布状态的商品不可以删除");
                }
            });
            this.goodGoodsMapper.delete(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除或批量删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除或批量删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 发布或批量发布
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult batchRelease(Integer[] ids) {
        try {
            QueryWrapper<EsGoodGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoodGoods::getId, ids);
            List<EsGoodGoods> goodGoodsList = goodGoodsMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(goodGoodsList)){
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), "数据不存在");
            }
            goodGoodsList.stream().forEach(esGoodGoods -> {
                if (esGoodGoods.getState() != 1){
                    throw new ArgumentException(ErrorCode.STATE_error_NOT_RELEASE.getErrorCode(), "存在不是待发布状态的商品不可以发布");
                }
            });
            goodGoodsList.stream().forEach(esGoodGoods -> {
                EsGoodGoods goods = new EsGoodGoods();
                goods.setId(esGoodGoods.getId());
                goods.setState(2);
                goodGoodsMapper.updateById(goods);
            });
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("发布或批量发布失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("发布或批量发布失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 下架
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult underGoodGoods(EsGoodGoodsDTO goodGoodsDTO) {
        try {
            EsGoodGoods goods = new EsGoodGoods();
            goods.setId(goodGoodsDTO.getId());
            goods.setState(3);
            goods.setUnderMessage(goodGoodsDTO.getUnderMessage());
            goodGoodsMapper.updateById(goods);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("下架失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("下架失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 分页查询品质好货
     */
    @Override
    public DubboPageResult<EsGoodGoodsDO> getList(int pageSize, int pageNum) {
        QueryWrapper<EsGoodGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsGoodGoods::getState,2);
            Page<EsGoodGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodGoods> iPage = this.page(page, queryWrapper);
            List<EsGoodGoodsDO> goodGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodGoodsDOList = iPage.getRecords().stream().map(goodGoods -> {
                    EsGoodGoodsDO goodGoodsDO = new EsGoodGoodsDO();
                    BeanUtil.copyProperties(goodGoods, goodGoodsDO);
                    return goodGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),goodGoodsDOList);
        } catch (ArgumentException ae){
            logger.error("品质好货分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("品质好货分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
