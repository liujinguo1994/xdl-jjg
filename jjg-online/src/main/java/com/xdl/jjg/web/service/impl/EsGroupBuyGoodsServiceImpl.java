package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsGroupBuyGoodsDO;
import com.jjg.trade.model.dto.EsGroupBuyGoodsDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsGroupBuyGoods;
import com.xdl.jjg.mapper.EsGroupBuyGoodsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGroupBuyGoodsService;
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
 * 团购商品 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGroupBuyGoodsService.class, timeout = 50000)
public class EsGroupBuyGoodsServiceImpl extends ServiceImpl<EsGroupBuyGoodsMapper, EsGroupBuyGoods> implements IEsGroupBuyGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsGroupBuyGoodsServiceImpl.class);

    @Autowired
    private EsGroupBuyGoodsMapper groupBuyGoodsMapper;

    /**
     * 插入团购商品数据
     *
     * @param groupBuyGoodsDTO 团购商品DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGroupBuyGoods(EsGroupBuyGoodsDTO groupBuyGoodsDTO) {
        try {
            EsGroupBuyGoods groupBuyGoods = new EsGroupBuyGoods();
            BeanUtil.copyProperties(groupBuyGoodsDTO, groupBuyGoods);
            this.groupBuyGoodsMapper.insert(groupBuyGoods);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("团购商品新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("团购商品新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新团购商品数据
     *
     * @param groupBuyGoodsDTO 团购商品DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGroupBuyGoods(EsGroupBuyGoodsDTO groupBuyGoodsDTO, Long id) {
        try {
            EsGroupBuyGoods groupBuyGoods = this.groupBuyGoodsMapper.selectById(id);
            if (groupBuyGoods == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(groupBuyGoodsDTO, groupBuyGoods);
            QueryWrapper<EsGroupBuyGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGroupBuyGoods::getId, id);
            this.groupBuyGoodsMapper.update(groupBuyGoods, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("团购商品更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("团购商品更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取团购商品详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyGoodsDO>
     */
    @Override
    public DubboResult<EsGroupBuyGoodsDO> getGroupBuyGoods(Long id) {
        try {
            EsGroupBuyGoods groupBuyGoods = this.groupBuyGoodsMapper.selectById(id);
            if (groupBuyGoods == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGroupBuyGoodsDO groupBuyGoodsDO = new EsGroupBuyGoodsDO();
            BeanUtil.copyProperties(groupBuyGoods, groupBuyGoodsDO);
            return DubboResult.success(groupBuyGoodsDO);
        } catch (ArgumentException ae){
            logger.error("团购商品查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("团购商品查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询团购商品列表
     *
     * @param groupBuyGoodsDTO 团购商品DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGroupBuyGoodsDO>
     */
    @Override
    public DubboPageResult<EsGroupBuyGoodsDO> getGroupBuyGoodsList(EsGroupBuyGoodsDTO groupBuyGoodsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGroupBuyGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGroupBuyGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsGroupBuyGoods> iPage = this.page(page, queryWrapper);
            List<EsGroupBuyGoodsDO> groupBuyGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                groupBuyGoodsDOList = iPage.getRecords().stream().map(groupBuyGoods -> {
                    EsGroupBuyGoodsDO groupBuyGoodsDO = new EsGroupBuyGoodsDO();
                    BeanUtil.copyProperties(groupBuyGoods, groupBuyGoodsDO);
                    return groupBuyGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(groupBuyGoodsDOList);
        } catch (ArgumentException ae){
            logger.error("团购商品分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("团购商品分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除团购商品数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGroupBuyGoods(Long id) {
        try {
            this.groupBuyGoodsMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("团购商品删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("团购商品删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
