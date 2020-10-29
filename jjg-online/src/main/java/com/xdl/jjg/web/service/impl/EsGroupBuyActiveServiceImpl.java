package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsGroupBuyActiveDO;
import com.jjg.trade.model.dto.EsGroupBuyActiveDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsGroupBuyActive;
import com.xdl.jjg.mapper.EsGroupBuyActiveMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGroupBuyActiveService;
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
 * 团购活动表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGroupBuyActiveService.class, timeout = 50000)
public class EsGroupBuyActiveServiceImpl extends ServiceImpl<EsGroupBuyActiveMapper, EsGroupBuyActive> implements IEsGroupBuyActiveService {

    private static Logger logger = LoggerFactory.getLogger(EsGroupBuyActiveServiceImpl.class);

    @Autowired
    private EsGroupBuyActiveMapper groupBuyActiveMapper;

    /**
     * 插入团购活动表数据
     *
     * @param groupBuyActiveDTO 团购活动表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyActiveDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGroupBuyActive(EsGroupBuyActiveDTO groupBuyActiveDTO) {
        try {
            EsGroupBuyActive groupBuyActive = new EsGroupBuyActive();
            BeanUtil.copyProperties(groupBuyActiveDTO, groupBuyActive);
            this.groupBuyActiveMapper.insert(groupBuyActive);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("团购活动表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("团购活动表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新团购活动表数据
     *
     * @param groupBuyActiveDTO 团购活动表DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyActiveDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGroupBuyActive(EsGroupBuyActiveDTO groupBuyActiveDTO, Long id) {
        try {
            EsGroupBuyActive groupBuyActive = this.groupBuyActiveMapper.selectById(id);
            if (groupBuyActive == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(groupBuyActiveDTO, groupBuyActive);
            QueryWrapper<EsGroupBuyActive> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGroupBuyActive::getId, id);
            this.groupBuyActiveMapper.update(groupBuyActive, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("团购活动表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("团购活动表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取团购活动表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyActiveDO>
     */
    @Override
    public DubboResult<EsGroupBuyActiveDO> getGroupBuyActive(Long id) {
        try {
            EsGroupBuyActive groupBuyActive = this.groupBuyActiveMapper.selectById(id);
            if (groupBuyActive == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGroupBuyActiveDO groupBuyActiveDO = new EsGroupBuyActiveDO();
            BeanUtil.copyProperties(groupBuyActive, groupBuyActiveDO);
            return DubboResult.success(groupBuyActiveDO);
        } catch (ArgumentException ae){
            logger.error("团购活动表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("团购活动表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询团购活动表列表
     *
     * @param groupBuyActiveDTO 团购活动表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGroupBuyActiveDO>
     */
    @Override
    public DubboPageResult<EsGroupBuyActiveDO> getGroupBuyActiveList(EsGroupBuyActiveDTO groupBuyActiveDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGroupBuyActive> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGroupBuyActive> page = new Page<>(pageNum, pageSize);
            IPage<EsGroupBuyActive> iPage = this.page(page, queryWrapper);
            List<EsGroupBuyActiveDO> groupBuyActiveDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                groupBuyActiveDOList = iPage.getRecords().stream().map(groupBuyActive -> {
                    EsGroupBuyActiveDO groupBuyActiveDO = new EsGroupBuyActiveDO();
                    BeanUtil.copyProperties(groupBuyActive, groupBuyActiveDO);
                    return groupBuyActiveDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(groupBuyActiveDOList);
        } catch (ArgumentException ae){
            logger.error("团购活动表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("团购活动表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除团购活动表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGroupBuyActiveDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGroupBuyActive(Long id) {
        try {
            this.groupBuyActiveMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("团购活动表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("团购活动表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
