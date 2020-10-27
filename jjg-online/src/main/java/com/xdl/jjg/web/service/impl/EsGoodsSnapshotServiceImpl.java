package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsGoodsSnapshotDO;
import com.jjg.trade.model.dto.EsGoodsSnapshotDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsGoodsSnapshot;
import com.xdl.jjg.mapper.EsGoodsSnapshotMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsGoodsSnapshotService;
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
 * 商品快照 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGoodsSnapshotService.class, timeout = 50000)
public class EsGoodsSnapshotServiceImpl extends ServiceImpl<EsGoodsSnapshotMapper, EsGoodsSnapshot> implements IEsGoodsSnapshotService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsSnapshotServiceImpl.class);

    @Autowired
    private EsGoodsSnapshotMapper goodsSnapshotMapper;

    /**
     * 插入商品快照数据
     *
     * @param goodsSnapshotDTO 商品快照DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSnapshotDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGoodsSnapshot(EsGoodsSnapshotDTO goodsSnapshotDTO) {
        try {
            EsGoodsSnapshot goodsSnapshot = new EsGoodsSnapshot();
            BeanUtil.copyProperties(goodsSnapshotDTO, goodsSnapshot);
            this.goodsSnapshotMapper.insert(goodsSnapshot);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品快照新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("商品快照新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新商品快照数据
     *
     * @param goodsSnapshotDTO 商品快照DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSnapshotDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGoodsSnapshot(EsGoodsSnapshotDTO goodsSnapshotDTO, Long id) {
        try {
            EsGoodsSnapshot goodsSnapshot = this.goodsSnapshotMapper.selectById(id);
            if (goodsSnapshot == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(goodsSnapshotDTO, goodsSnapshot);
            QueryWrapper<EsGoodsSnapshot> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsSnapshot::getId, id);
            this.goodsSnapshotMapper.update(goodsSnapshot, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品快照更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品快照更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取商品快照详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSnapshotDO>
     */
    @Override
    public DubboResult<EsGoodsSnapshotDO> getGoodsSnapshot(Long id) {
        try {
            EsGoodsSnapshot goodsSnapshot = this.goodsSnapshotMapper.selectById(id);
            if (goodsSnapshot == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGoodsSnapshotDO goodsSnapshotDO = new EsGoodsSnapshotDO();
            BeanUtil.copyProperties(goodsSnapshot, goodsSnapshotDO);
            return DubboResult.success(goodsSnapshotDO);
        } catch (ArgumentException ae){
            logger.error("商品快照查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品快照查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询商品快照列表
     *
     * @param goodsSnapshotDTO 商品快照DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsSnapshotDO>
     */
    @Override
    public DubboPageResult<EsGoodsSnapshotDO> getGoodsSnapshotList(EsGoodsSnapshotDTO goodsSnapshotDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodsSnapshot> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGoodsSnapshot> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsSnapshot> iPage = this.page(page, queryWrapper);
            List<EsGoodsSnapshotDO> goodsSnapshotDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodsSnapshotDOList = iPage.getRecords().stream().map(goodsSnapshot -> {
                    EsGoodsSnapshotDO goodsSnapshotDO = new EsGoodsSnapshotDO();
                    BeanUtil.copyProperties(goodsSnapshot, goodsSnapshotDO);
                    return goodsSnapshotDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(goodsSnapshotDOList);
        } catch (ArgumentException ae){
            logger.error("商品快照分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品快照分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除商品快照数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSnapshotDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGoodsSnapshot(Long id) {
        try {
            this.goodsSnapshotMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("商品快照删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品快照删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
