package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsBillDetailDO;
import com.jjg.trade.model.dto.EsBillDetailDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsBillDetail;
import com.xdl.jjg.mapper.EsBillDetailMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsBillDetailService;
import org.apache.commons.lang3.StringUtils;
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
 * 结算单 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:09
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsBillDetailService.class,
        timeout = 50000)
public class EsBillDetailServiceImpl extends ServiceImpl<EsBillDetailMapper, EsBillDetail> implements IEsBillDetailService {

    private static Logger logger = LoggerFactory.getLogger(EsBillDetailServiceImpl.class);

    @Autowired
    private EsBillDetailMapper billDetailMapper;

    // TODO 判断签约公司存不存在
//    @Autowired

    // TODO 判断卖家存不存在
//    @Reference(version = "${dubbo.application.version}" , timeout = 5000, check = false)

    /**
     * 插入结算单数据
     *
     * @param billDetailDTO 结算单DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsBillDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertBillDetail(EsBillDetailDTO billDetailDTO) {
        try {

            if (StringUtils.isEmpty(billDetailDTO.getOrderSn())) {
                throw new ArgumentException(TradeErrorCode.ORDER_SN_NOT_NULL.getErrorCode(), TradeErrorCode.ORDER_SN_NOT_NULL.getErrorMsg());
            }

            // 判断结算单中是否存在
            if (isExist(billDetailDTO.getOrderSn(), billDetailDTO.getType())) {
                throw new ArgumentException(TradeErrorCode.ORDER_SN_NOT_NULL.getErrorCode(), TradeErrorCode.ORDER_SN_NOT_NULL.getErrorMsg());
            }

            EsBillDetail billDetail = new EsBillDetail();
            BeanUtil.copyProperties(billDetailDTO, billDetail);
            this.billDetailMapper.insert(billDetail);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("结算单新增失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("结算单新增失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新结算单数据
     *
     * @param billDetailDTO 结算单DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsBillDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateBillDetail(EsBillDetailDTO billDetailDTO, Long id) {
        try {
            EsBillDetail billDetail = new EsBillDetail();
            BeanUtil.copyProperties(billDetailDTO, billDetail);
            QueryWrapper<EsBillDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsBillDetail::getId, id);
            this.billDetailMapper.update(billDetail, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("结算单更新失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("结算单更新失败" , th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取结算单详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsBillDetailDO>
     */
    @Override
    public DubboResult<EsBillDetailDO> getBillDetail(Long id) {
        try {
            QueryWrapper<EsBillDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsBillDetail::getId, id);
            EsBillDetail billDetail = this.billDetailMapper.selectOne(queryWrapper);
            EsBillDetailDO billDetailDO = new EsBillDetailDO();
            if (billDetail == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(billDetail, billDetailDO);
            return DubboResult.success(billDetailDO);
        } catch (ArgumentException ae) {
            logger.error("结算单查询失败" , ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("结算单查询失败" , th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询结算单列表
     *
     * @param billDetailDTO 结算单DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsBillDetailDO>
     */
    @Override
    public DubboPageResult<EsBillDetailDO> getBillDetailList(EsBillDetailDTO billDetailDTO, int pageSize, int pageNum) {
        QueryWrapper<EsBillDetail> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsBillDetail> page = new Page<>(pageNum, pageSize);
            IPage<EsBillDetail> iPage = this.page(page, queryWrapper);
            List<EsBillDetailDO> billDetailDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                billDetailDOList = iPage.getRecords().stream().map(billDetail -> {
                    EsBillDetailDO billDetailDO = new EsBillDetailDO();
                    BeanUtil.copyProperties(billDetail, billDetailDO);
                    return billDetailDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(billDetailDOList);
        } catch (ArgumentException ae) {
            logger.error("结算单分页查询失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("结算单分页查询失败" , th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除结算单数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsBillDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteBillDetail(Long id) {
        try {
            QueryWrapper<EsBillDetail> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsBillDetail::getId, id);
            this.billDetailMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("结算单删除失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("结算单删除失败" , th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 重复性检查check
     *
     * @param orderSn 订单编号
     * @param type    类型
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/05 20:52:37
     * @return: boolean
     */
    private boolean isExist(String orderSn, Integer type) {
        QueryWrapper<EsBillDetail> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(EsBillDetail::getOrderSn, orderSn);
        queryWrapper.lambda().eq(EsBillDetail::getType, type);

        int count = this.billDetailMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
