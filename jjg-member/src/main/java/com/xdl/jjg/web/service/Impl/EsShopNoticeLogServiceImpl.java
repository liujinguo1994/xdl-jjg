package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsShopNoticeLogDO;
import com.jjg.member.model.dto.EsShopNoticeLogDTO;
import com.jjg.member.model.dto.GoodsNoticeDTO;
import com.jjg.trade.model.domain.EsOrderDO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsShopNoticeLog;
import com.xdl.jjg.mapper.EsShopNoticeLogMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsShopNoticeLogService;
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
 * 店铺站内消息(平台-店铺) 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-25
 */
@Service
public class EsShopNoticeLogServiceImpl extends ServiceImpl<EsShopNoticeLogMapper, EsShopNoticeLog> implements IEsShopNoticeLogService {

    private static Logger logger = LoggerFactory.getLogger(EsShopNoticeLogServiceImpl.class);

    @Autowired
    private EsShopNoticeLogMapper shopNoticeLogMapper;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderService orderService;

    /**
     * 插入店铺站内消息(平台-店铺)数据
     *
     * @param shopNoticeLogDTO 店铺站内消息(平台-店铺)DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 15:35:30
     * @return: com.shopx.common.model.result.DubboResult<EsShopNoticeLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShopNoticeLog(EsShopNoticeLogDTO shopNoticeLogDTO) {
        try {
            if (shopNoticeLogDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsShopNoticeLog shopNoticeLog = new EsShopNoticeLog();
            DubboResult<EsOrderDO> orderInfo = orderService.getEsBuyerOrderInfo(shopNoticeLogDTO.getOrderSn());
            if (orderInfo.isSuccess()){
                shopNoticeLog.setGoodsJson(orderInfo.getData().getItemsJson());
            }
            List<GoodsNoticeDTO> goodsNoticeDTOList = shopNoticeLogDTO.getGoodsNoticeDTOList();
            String goodsInfo = JsonUtil.objectToJson(goodsNoticeDTOList);
            Long memberId = shopNoticeLogDTO.getMemberIds();
            shopNoticeLog.setMemberId(memberId);
            shopNoticeLog.setIsRead(0);
            shopNoticeLog.setGoodsJson(goodsInfo);
            shopNoticeLog.setIsDel(0);
            shopNoticeLog.setNoticeContent(shopNoticeLogDTO.getNoticeContent());
            shopNoticeLog.setSendTime(shopNoticeLogDTO.getSendTime());
            shopNoticeLog.setType(shopNoticeLogDTO.getType());
            shopNoticeLog.setShopId(shopNoticeLogDTO.getShopId());
            BeanUtil.copyProperties(shopNoticeLogDTO, shopNoticeLog);
            this.shopNoticeLogMapper.insert(shopNoticeLog);

            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺站内消息(平台-店铺)新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺站内消息(平台-店铺)新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 根据id获取店铺站内消息(平台-店铺)详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopNoticeLogDO>
     */
    @Override
    public DubboResult<EsShopNoticeLogDO> getShopNoticeLog(Long id) {
        try {
            if (id==null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsShopNoticeLog shopNoticeLog = this.shopNoticeLogMapper.selectById(id);
            EsShopNoticeLogDO shopNoticeLogDO = new EsShopNoticeLogDO();
            if (shopNoticeLog == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shopNoticeLog, shopNoticeLogDO);
            return DubboResult.success(shopNoticeLogDO);
        } catch (ArgumentException ae){
            logger.error("店铺站内消息(平台-店铺)查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺站内消息(平台-店铺)查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺站内消息(平台-店铺)列表
     *
     * @param shopNoticeLogDTO 店铺站内消息(平台-店铺)DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopNoticeLogDO>
     */
    @Override
    public DubboPageResult<EsShopNoticeLogDO> getShopNoticeLogList(EsShopNoticeLogDTO shopNoticeLogDTO, int pageSize, int pageNum) {
        try {
            // 查询条件
            EsShopNoticeLog esShopNoticeLog = new EsShopNoticeLog();
            BeanUtil.copyProperties(shopNoticeLogDTO,esShopNoticeLog);
            QueryWrapper<EsShopNoticeLog> queryWrapper = new QueryWrapper<>(esShopNoticeLog);
            Page<EsShopNoticeLog> page = new Page<>(pageNum, pageSize);
            IPage<EsShopNoticeLog> iPage = this.page(page, queryWrapper);
            List<EsShopNoticeLogDO> shopNoticeLogDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                shopNoticeLogDOList = iPage.getRecords().stream().map(shopNoticeLog -> {
                    EsShopNoticeLogDO shopNoticeLogDO = new EsShopNoticeLogDO();
                    BeanUtil.copyProperties(shopNoticeLog, shopNoticeLogDO);
                    return shopNoticeLogDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),shopNoticeLogDOList);
        } catch (ArgumentException ae){
            logger.error("店铺站内消息(平台-店铺)分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺站内消息(平台-店铺)分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 批量删除店铺站内消息(平台-店铺)数据
     *
     * @param ids 主键ids
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopNoticeLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsShopNoticeLogDO> deleteShopNoticeLog(long[] ids) {
        try {
            QueryWrapper<EsShopNoticeLog> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsShopNoticeLog::getId, ids);
            this.shopNoticeLogMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店铺站内消息(平台-店铺)删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺站内消息(平台-店铺)删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 批量设置已读
     *
     * @param ids 主键ids
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 16:18:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopNoticeLogDO>
     */
    @Override
    public DubboResult read(Long[] ids) {
        try {
            QueryWrapper<EsShopNoticeLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsShopNoticeLog::getId,ids);
            List<EsShopNoticeLog> shopNoticeLogList = this.shopNoticeLogMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(shopNoticeLogList)){
                throw new ArgumentException(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
            }
            shopNoticeLogList.stream().forEach(shopNoticeLog -> {
                shopNoticeLog.setIsRead(1);
            });
            this.updateBatchById(shopNoticeLogList,shopNoticeLogList.size());
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺站内消息(平台-店铺)删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺站内消息(平台-店铺)删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
