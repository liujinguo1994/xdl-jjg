package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsGroupBuyCatDO;
import com.jjg.trade.model.dto.EsGroupBuyCatDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsGroupBuyCat;
import com.xdl.jjg.mapper.EsGroupBuyCatMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGroupBuyCatService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;
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
 * 团购分类 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service
public class EsGroupBuyCatServiceImpl extends ServiceImpl<EsGroupBuyCatMapper, EsGroupBuyCat> implements IEsGroupBuyCatService {

    private static Logger logger = LoggerFactory.getLogger(EsGroupBuyCatServiceImpl.class);

    @Autowired
    private EsGroupBuyCatMapper groupBuyCatMapper;

    /**
     * 插入团购分类数据
     *
     * @param groupBuyCatDTO 团购分类DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyCatDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGroupBuyCat(EsGroupBuyCatDTO groupBuyCatDTO) {
        try {
            EsGroupBuyCat groupBuyCat = new EsGroupBuyCat();
            BeanUtil.copyProperties(groupBuyCatDTO, groupBuyCat);
            this.groupBuyCatMapper.insert(groupBuyCat);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("团购分类新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("团购分类新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新团购分类数据
     *
     * @param groupBuyCatDTO 团购分类DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyCatDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGroupBuyCat(EsGroupBuyCatDTO groupBuyCatDTO, Long id) {
        try {
            EsGroupBuyCat groupBuyCat = this.groupBuyCatMapper.selectById(id);
            if (groupBuyCat == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(groupBuyCatDTO, groupBuyCat);
            QueryWrapper<EsGroupBuyCat> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGroupBuyCat::getId, id);
            this.groupBuyCatMapper.update(groupBuyCat, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("团购分类更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("团购分类更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取团购分类详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyCatDO>
     */
    @Override
    public DubboResult<EsGroupBuyCatDO> getGroupBuyCat(Long id) {
        try {
            EsGroupBuyCat groupBuyCat = this.groupBuyCatMapper.selectById(id);
            if (groupBuyCat == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGroupBuyCatDO groupBuyCatDO = new EsGroupBuyCatDO();
            BeanUtil.copyProperties(groupBuyCat, groupBuyCatDO);
            return DubboResult.success(groupBuyCatDO);
        } catch (ArgumentException ae){
            logger.error("团购分类查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("团购分类查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询团购分类列表
     *
     * @param groupBuyCatDTO 团购分类DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGroupBuyCatDO>
     */
    @Override
    public DubboPageResult<EsGroupBuyCatDO> getGroupBuyCatList(EsGroupBuyCatDTO groupBuyCatDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGroupBuyCat> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGroupBuyCat> page = new Page<>(pageNum, pageSize);
            IPage<EsGroupBuyCat> iPage = this.page(page, queryWrapper);
            List<EsGroupBuyCatDO> groupBuyCatDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                groupBuyCatDOList = iPage.getRecords().stream().map(groupBuyCat -> {
                    EsGroupBuyCatDO groupBuyCatDO = new EsGroupBuyCatDO();
                    BeanUtil.copyProperties(groupBuyCat, groupBuyCatDO);
                    return groupBuyCatDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(groupBuyCatDOList);
        } catch (ArgumentException ae){
            logger.error("团购分类分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("团购分类分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除团购分类数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyCatDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGroupBuyCat(Long id) {
        try {
            this.groupBuyCatMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("团购分类删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("团购分类删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
