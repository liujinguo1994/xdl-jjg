package com.xdl.jjg.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsSettingsDO;
import com.jjg.system.model.enums.SettingGroup;
import com.jjg.system.model.vo.EsClearingCycleVO;
import com.jjg.trade.model.domain.EsBillDO;
import com.jjg.trade.model.domain.EsHeaderDO;
import com.jjg.trade.model.domain.EsSettlement;
import com.jjg.trade.model.domain.ExcelDO;
import com.jjg.trade.model.dto.EsBillDTO;
import com.jjg.trade.model.dto.EsBillDetailDTO;
import com.jjg.trade.model.enums.SettlementType;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.context.ApplicationContextHolder;
import com.xdl.jjg.entity.EsBill;
import com.xdl.jjg.manager.StatementSettlementManager;
import com.xdl.jjg.mapper.EsBillMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsBillService;
import com.xdl.jjg.web.service.feign.system.SettingsService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 账单 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service
public class EsBillServiceImpl extends ServiceImpl<EsBillMapper, EsBill> implements IEsBillService {

    private static Logger logger = LoggerFactory.getLogger(EsBillServiceImpl.class);

    @Autowired
    private EsBillMapper billMapper;

    @Autowired
    private SettingsService settingsService;

    /**
     * 根据条件查询账单列表
     *
     * @param billDTO  签约公司结算单DTO
     * @param pageSize 页码
     * @param pageNum  页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsBillDO>
     */
    @Override
    public DubboPageResult getBillList(EsBillDTO billDTO, int pageSize, int pageNum) {
        QueryWrapper<EsBill> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().like(!StringUtil.isEmpty(billDTO.getBillSn()), EsBill::getBillSn, billDTO.getBillSn());
//            queryWrapper.lambda().eq(billDTO.getState() != null, EsBill::getState, billDTO.getState());
            queryWrapper.lambda().lt(billDTO.getEndCreateTime()!=null, EsBill::getCreateTime, billDTO.getEndCreateTime())
                    .gt(billDTO.getStartCreateTime()!=null, EsBill::getCreateTime, billDTO.getStartCreateTime());
            queryWrapper.lambda().eq(EsBill::getType, billDTO.getType());
            Page<EsBill> page = new Page<>(pageNum, pageSize);
            IPage<EsBill> iPage = this.page(page, queryWrapper);
            List<EsBillDO> billDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                billDOList = iPage.getRecords().stream().map(bill -> {
                    EsBillDO billDO = new EsBillDO();
                    BeanUtil.copyProperties(bill, billDO);
                    return billDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(billDOList);
        } catch (ArgumentException ae) {
            logger.error("签约公司结算单分页查询失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("签约公司结算单分页查询失败" , th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 结算单汇总
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/08/17 11:01:54
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult summaryOfStatement(int type) {
        try {
            // 获取结算周期配置
            DubboResult result = settingsService.getByCfgGroup(SettingGroup.CLEARING.name());
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }
            EsSettingsDO settingsDO = (EsSettingsDO) result.getData();
            List<EsClearingCycleVO> list = JSON.parseArray(settingsDO.getCfgValue(), EsClearingCycleVO.class);

            if (list == null || list.size() == 0) {
                throw new ArgumentException(TradeErrorCode.CONFIGURATION_DOES_NOT_EXIST.getErrorCode(),
                        TradeErrorCode.CONFIGURATION_DOES_NOT_EXIST.getErrorMsg());
            }

            for (EsClearingCycleVO clearingCycleVO : list) {
                // 店铺结算
                if (clearingCycleVO.getType() == type) {
                    StatementSettlementManager statementSettlementManager =
                            (StatementSettlementManager) ApplicationContextHolder.getBean(SettlementType.getName(type));
                    statementSettlementManager.settlement(clearingCycleVO);
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("汇总结算单失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("汇总结算单失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 获取结算单中的账单详情
     *
     * @param billDetailDTO 账单DTO
     * @param pageSize
     * @param pageNum
     * @author: libw 981087977@qq.com
     * @date: 2019/08/17 11:01:54
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboPageResult getBillDetail(EsBillDetailDTO billDetailDTO, int pageSize, int pageNum) {
        try {
            StatementSettlementManager statementSettlementManager =
                    (StatementSettlementManager) ApplicationContextHolder.getBean(SettlementType.getName(billDetailDTO.getType()));
            EsSettlement settlement = statementSettlementManager.getBillDetail(billDetailDTO, pageSize, pageNum);

            return DubboPageResult.success(settlement.getTotal(), settlement.getBillDetailList());
        } catch (ArgumentException ae) {
            logger.error("查询结算单失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询结算单失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 获取结算单详情
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:21:41
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult getSettlementInfo(Long settlementId, int type) {
        try {

            StatementSettlementManager statementSettlementManager =
                    (StatementSettlementManager) ApplicationContextHolder.getBean(SettlementType.getName(type));
            EsHeaderDO headerDO = statementSettlementManager.getSettlementInfo(settlementId);

            return DubboResult.success(headerDO);
        } catch (ArgumentException ae) {
            logger.error("获取结算单详情", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("获取结算单详情", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 获取结算单总订单明细
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:25:19
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    @Override
    public DubboPageResult getSettlementOrderDetail(Long settlementId, int type, int pageSize, int pageNum) {
        try {
            StatementSettlementManager statementSettlementManager =
                (StatementSettlementManager) ApplicationContextHolder.getBean(SettlementType.getName(type));
            EsSettlement settlement = statementSettlementManager.getSettlementOrderDetail(settlementId, pageSize, pageNum);
            return DubboPageResult.success(settlement.getTotal(), settlement.getSettlementDetailList());
        } catch (ArgumentException ae) {
            logger.error("查询结算单失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询结算单失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 获取结算单退款订单金额明细
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:25:19
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    @Override
    public DubboPageResult getSettlementRefundOrderDetail(Long settlementId, int type, int pageSize, int pageNum) {
        try {
            StatementSettlementManager statementSettlementManager =
                    (StatementSettlementManager) ApplicationContextHolder.getBean(SettlementType.getName(type));
            EsSettlement settlement = statementSettlementManager.getSettlementRefundOrderDetail(settlementId, pageSize, pageNum);
            return DubboPageResult.success(settlement.getTotal(), settlement.getSettlementDetailList());
        } catch (ArgumentException ae) {
            logger.error("查询结算单失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询结算单失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 导出EXCEL
     *
     * @param settlementId 结算单id
     * @param type         结算单类型
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult exportExcel(Long settlementId, int type) {
        try {
            StatementSettlementManager statementSettlementManager =
                    (StatementSettlementManager) ApplicationContextHolder.getBean(SettlementType.getName(type));
            ExcelDO excel = statementSettlementManager.exportExcel(settlementId);
            return DubboResult.success(excel);
        } catch (ArgumentException ae) {
            logger.error("查询结算单失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询结算单失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 更改结算状态
     *
     * @param settlementId  结算单id
     * @param type          结算单类型
     * @param status        结算状态
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult updateStatus(Long settlementId, int type, int status){
        try {
            StatementSettlementManager statementSettlementManager =
                    (StatementSettlementManager) ApplicationContextHolder.getBean(SettlementType.getName(type));
            statementSettlementManager.updateStatus(settlementId, status);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("查询结算单失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询结算单失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
