package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsRefundGoodsDO;
import com.jjg.trade.model.dto.EsRefundGoodsDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsRefundGoods;
import com.xdl.jjg.mapper.EsRefundGoodsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsRefundGoodsService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service
public class EsRefundGoodsServiceImpl extends ServiceImpl<EsRefundGoodsMapper, EsRefundGoods> implements IEsRefundGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsRefundGoodsServiceImpl.class);

    @Autowired
    private EsRefundGoodsMapper refundGoodsMapper;

    /**
     * 插入数据
     *
     * @param refundGoodsDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsRefundGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertRefundGoods(EsRefundGoodsDTO refundGoodsDTO) {
        try {
            if (refundGoodsDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsRefundGoods refundGoods = new EsRefundGoods();
            BeanUtil.copyProperties(refundGoodsDTO, refundGoods);
            this.refundGoodsMapper.insert(refundGoods);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param refundGoodsDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsRefundGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateRefundGoods(EsRefundGoodsDTO refundGoodsDTO) {
        try {
            if (refundGoodsDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsRefundGoods refundGoods = new EsRefundGoods();
            BeanUtil.copyProperties(refundGoodsDTO, refundGoods);
            QueryWrapper<EsRefundGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefundGoods::getId, refundGoodsDTO.getId());
            this.refundGoodsMapper.update(refundGoods, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsRefundGoodsDO>
     */
    @Override
    public DubboResult getRefundGoods(Long id) {
        try {
            QueryWrapper<EsRefundGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefundGoods::getId, id);
            EsRefundGoods refundGoods = this.refundGoodsMapper.selectOne(queryWrapper);
            EsRefundGoodsDO refundGoodsDO = new EsRefundGoodsDO();
            if (refundGoods == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(refundGoods, refundGoodsDO);
            return DubboResult.success(refundGoodsDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param refundGoodsDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundGoodsDO>
     */
    @Override
    public DubboPageResult getRefundGoodsList(EsRefundGoodsDTO refundGoodsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsRefundGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsRefundGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsRefundGoods> iPage = this.page(page, queryWrapper);
            List<EsRefundGoodsDO> refundGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                refundGoodsDOList = iPage.getRecords().stream().map(refundGoods -> {
                    EsRefundGoodsDO refundGoodsDO = new EsRefundGoodsDO();
                    BeanUtil.copyProperties(refundGoods, refundGoodsDO);
                    return refundGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(refundGoodsDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsRefundGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteRefundGoods(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsRefundGoods> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsRefundGoods::getId, id);
            this.refundGoodsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsRefundGoodsDO> getRefundGoodsByRefundSn(String refundSn) {
        try {
            if (refundSn == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入refundSn不能为空[%s]", refundSn));
            }
            QueryWrapper<EsRefundGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefundGoods::getRefundSn,refundSn);
            List<EsRefundGoods> esRefundGoods = this.refundGoodsMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(esRefundGoods)){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

            List<EsRefundGoodsDO> esRefundGoodsDOList = new ArrayList<>();
            esRefundGoodsDOList = esRefundGoods.stream().map(esRefundGoods1 -> {
                EsRefundGoodsDO esRefundGoodsDO = new EsRefundGoodsDO();
                BeanUtils.copyProperties(esRefundGoods1,esRefundGoodsDO);
                return esRefundGoodsDO;
            }).collect(Collectors.toList());

            return DubboPageResult.success((long)esRefundGoods.size(),esRefundGoodsDOList);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
