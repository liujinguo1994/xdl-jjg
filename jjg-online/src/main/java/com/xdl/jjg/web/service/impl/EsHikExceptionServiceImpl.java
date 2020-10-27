package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsHikExceptionOrderDO;
import com.jjg.trade.model.dto.EsHikExceptionOrderDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsHikExceptionOrder;
import com.xdl.jjg.mapper.EsHikExceptionMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsHikExceptionService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 海康异常反馈 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-05-09
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsHikExceptionService.class, timeout = 5000)
public class EsHikExceptionServiceImpl extends ServiceImpl<EsHikExceptionMapper, EsHikExceptionOrder> implements IEsHikExceptionService {

    private static Logger logger = LoggerFactory.getLogger(EsHikExceptionServiceImpl.class);

    @Autowired
    private EsHikExceptionMapper hikExceptionMapper;

    /**
     * 插入数据
     * @param hikExceptionOrderDTO DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/05/09 10:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsHikExceptionOrder>
     */
    @Override
    public DubboResult insertHikException(EsHikExceptionOrderDTO hikExceptionOrderDTO) {

        try {
            EsHikExceptionOrder modelFromSn = this.getModelFromSn(hikExceptionOrderDTO.getOrderSn());
            if (null !=modelFromSn){
                throw new ArgumentException(TradeErrorCode.HIK_PAY_HAS_DATA.getErrorCode(), TradeErrorCode.HIK_PAY_HAS_DATA.getErrorMsg());
            }
            EsHikExceptionOrder exceptionOrder = new EsHikExceptionOrder();
            BeanUtil.copyProperties(hikExceptionOrderDTO, exceptionOrder);
            this.hikExceptionMapper.insert(exceptionOrder);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据查询列表
     *
     * @param esHikExceptionOrderDTO DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/05/09 11:02:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsHikExceptionOrderDO>
     */
    @Override
    public DubboPageResult<EsHikExceptionOrderDO> getHIkExceptionOrderList(EsHikExceptionOrderDTO esHikExceptionOrderDTO) {
        try{
            QueryWrapper<EsHikExceptionOrder> queryWrapper =joinExceptionOrder(esHikExceptionOrderDTO);
            List<EsHikExceptionOrder> hikExceptionOrderList =  this.list(queryWrapper);
            List<EsHikExceptionOrderDO> hikDOList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(hikExceptionOrderList)){
                hikDOList = hikExceptionOrderList.stream().map(hikOrder -> {
                    EsHikExceptionOrderDO hikExceptionOrderDO = new EsHikExceptionOrderDO();
                    BeanUtil.copyProperties(hikOrder,hikExceptionOrderDO);
                    return hikExceptionOrderDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(hikDOList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品查询失败",th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }

    }

    /**
     * 根据条件更新更新数据
     *
     * @param hikExceptionOrderDTO DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/05/09 11:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsHikExceptionOrderDO>
     */
    @Override
    public DubboResult updateHikException(EsHikExceptionOrderDTO hikExceptionOrderDTO) {
        try {
            if (hikExceptionOrderDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsHikExceptionOrder hikExceptionOrder = this.hikExceptionMapper.selectById(hikExceptionOrderDTO.getId());
            if (hikExceptionOrder ==null){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsHikExceptionOrder exceptionOrder = new EsHikExceptionOrder();
            BeanUtil.copyProperties(hikExceptionOrderDTO, exceptionOrder);
            QueryWrapper<EsHikExceptionOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsHikExceptionOrder::getId, hikExceptionOrderDTO.getId());
            this.hikExceptionMapper.update(exceptionOrder, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("异常反馈更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("异常反馈更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 获取异常订单信息
     *
     * @param id
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/05/09 13:12:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsHikExceptionOrderDO>
     */
    @Override
    public DubboResult<EsHikExceptionOrderDO> getModel(Long id) {
        try {
            EsHikExceptionOrder hikExceptionOrder = this.hikExceptionMapper.selectById(id);
            if (hikExceptionOrder==null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsHikExceptionOrderDO hikExceptionOrderDO=new EsHikExceptionOrderDO();
            BeanUtils.copyProperties(hikExceptionOrder,hikExceptionOrderDO);
            return DubboResult.success(hikExceptionOrderDO);
        } catch (ArgumentException ae){
            logger.error("异常反馈查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("异常反馈查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }


    /**
     * 假删异常订单信息
     *
     * @param id
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/05/09 13:12:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsHikExceptionOrderDO>
     */
    @Override
    public DubboResult deleteHikException(Long id) {
        try {
            EsHikExceptionOrder hikExceptionOrder = this.hikExceptionMapper.selectById(id);
            hikExceptionOrder.setStatus(1);
            this.hikExceptionMapper.updateById(hikExceptionOrder);
            return DubboResult.success();
        }  catch (ArgumentException ae){
            logger.error("异常反馈删除失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("异常反馈删除失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 根据订单号查询异常订单信息
     * @auther: libw
     * @date: 2019/05/23 11:26:42
     * @param sn 订单编号
     * @return: com.enation.app.javashop.core.trade.order.model.dos.ExceptionOrderDO
     */

    private EsHikExceptionOrder getModelFromSn(String sn) {
        QueryWrapper<EsHikExceptionOrder> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(EsHikExceptionOrder::getOrderSn,sn);
        EsHikExceptionOrder hikExceptionOrder = this.hikExceptionMapper.selectOne(queryWrapper);
        return hikExceptionOrder;
    }

    private QueryWrapper<EsHikExceptionOrder> joinExceptionOrder(EsHikExceptionOrderDTO esHikExceptionOrderDTO){
        if(esHikExceptionOrderDTO == null){
            throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        QueryWrapper<EsHikExceptionOrder> queryWrapper = new QueryWrapper<>();
        //根据ID排序 降序
        queryWrapper.orderByDesc("id");
        //手机号
        if(!StringUtils.isBlank(esHikExceptionOrderDTO.getMobile())){
            queryWrapper.lambda().eq(EsHikExceptionOrder::getMobile,esHikExceptionOrderDTO.getMobile());
        }
        //联系人
        if(!StringUtils.isBlank(esHikExceptionOrderDTO.getName())){
            queryWrapper.lambda().eq(EsHikExceptionOrder::getName,esHikExceptionOrderDTO.getName());
        }

        //手机号
        if(!StringUtils.isBlank(esHikExceptionOrderDTO.getOrderSn())){
            queryWrapper.lambda().eq(EsHikExceptionOrder::getOrderSn,esHikExceptionOrderDTO.getOrderSn());
        }
        //状态
        if(null != esHikExceptionOrderDTO.getStatus()){
            queryWrapper.lambda().eq(EsHikExceptionOrder::getStatus,esHikExceptionOrderDTO.getStatus());
        }
        return queryWrapper;
    }

}
