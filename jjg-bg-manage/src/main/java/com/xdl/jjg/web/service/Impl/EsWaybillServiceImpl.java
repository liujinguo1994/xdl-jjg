package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsWaybill;
import com.xdl.jjg.mapper.EsWaybillMapper;
import com.xdl.jjg.model.vo.EsConfigItemVO;
import com.xdl.jjg.model.vo.EsWaybillVO;
import com.xdl.jjg.plugin.waybill.WayBillEvent;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsWaybillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsWaybillServiceImpl extends ServiceImpl<EsWaybillMapper, EsWaybill> implements IEsWaybillService {

    private static Logger logger = LoggerFactory.getLogger(EsWaybillServiceImpl.class);

    @Autowired
    private EsWaybillMapper waybillMapper;

    @Autowired
    private List<WayBillEvent> wayBillEvents;


    /**
     * 根据条件更新数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateWaybill(EsWaybillVO waybillVO) {
        try {
            List<EsWaybillVO> list = this.getWayBills();
            for (EsWaybillVO vo : list) {
                this.add(vo);
            }
            EsWaybill waybill = getEsWaybill(waybillVO.getBean());
            if (waybill == null) {
                throw new ArgumentException(ErrorCode.WAYBILL_NOT_EXIT.getErrorCode(), "该电子面单不存在");
            }
            waybillVO.setId(waybill.getId());
            waybillMapper.updateById(new EsWaybill(waybillVO));
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //查询电子面单列表
    @Override
    public DubboPageResult<EsWaybillVO> getWaybillList() {
        List<EsWaybillVO> list = this.getWayBills();
        for (EsWaybillVO vo : list) {
            this.add(vo);
        }
        return DubboPageResult.success(list);
    }

    //开启某个电子面单
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult openWaybill(String bean) {
        try {
            List<EsWaybillVO> list = this.getWayBills();
            for (EsWaybillVO vo : list) {
                this.add(vo);
            }
            EsWaybill waybill = getEsWaybill(bean);
            if (waybill == null) {
                throw new ArgumentException(ErrorCode.WAYBILL_NOT_EXIT.getErrorCode(), "该电子面单不存在");
            }
            EsWaybill esWaybill = new EsWaybill();
            esWaybill.setOpen(0);
            QueryWrapper<EsWaybill> queryWrapper = new QueryWrapper<>();
            waybillMapper.update(esWaybill,queryWrapper);
            esWaybill.setOpen(1);
            esWaybill.setId(waybill.getId());
            waybillMapper.updateById(esWaybill);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("开启失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("开启失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 添加电子面单
     *
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult add(EsWaybillVO waybillVO) {
        try {
            EsWaybill waybill = new EsWaybill(waybillVO);
            if (waybill.getId() == null || waybill.getId() == 0) {
                EsWaybill esWaybill = getEsWaybill(waybill.getBean());
                if (esWaybill != null) {
                    throw new ArgumentException(ErrorCode.WAYBILL_EXIT.getErrorCode(), "该电子面单方案已经存在");
                }
                waybillMapper.insert(waybill);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("添加电子面单失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("添加电子面单失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据bean获取电子面单
    public EsWaybill getEsWaybill(String bean) {
        QueryWrapper<EsWaybill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsWaybill::getBean,bean);
        EsWaybill waybill = waybillMapper.selectOne(queryWrapper);
        return waybill;
    }


    /**
     * 获取所有的电子面单方案
     */
    private List<EsWaybillVO> getWayBills() {
        List<EsWaybillVO> resultList = new ArrayList<>();

        QueryWrapper<EsWaybill> queryWrapper = new QueryWrapper();
        List<EsWaybill> list = waybillMapper.selectList(queryWrapper);

        Map<String, EsWaybill> map = new HashMap<>(16);

        for (EsWaybill waybill : list) {
            map.put(waybill.getBean(), waybill);
        }
        for (WayBillEvent plugin : wayBillEvents) {
            EsWaybill wayBill = map.get(plugin.getPluginId());
            EsWaybillVO result = null;

            if (wayBill != null) {
                result = getEsWaybillVO(wayBill);
            } else {
                result = getVO(plugin);
            }

            resultList.add(result);
        }
        return resultList;
    }

    //给VO设置值
    public EsWaybillVO getEsWaybillVO(EsWaybill wayBill) {
        EsWaybillVO waybillVO = new EsWaybillVO();
        waybillVO.setId(wayBill.getId());
        waybillVO.setName(wayBill.getName());
        waybillVO.setOpen(wayBill.getOpen());
        waybillVO.setBean(wayBill.getBean());
        Gson gson = new Gson();
        waybillVO.setConfigItems(gson.fromJson(wayBill.getConfig(), new TypeToken<List<EsConfigItemVO>>() { }.getType()));
        return waybillVO;
    }

    //给VO设置值
    public EsWaybillVO getVO(WayBillEvent plugin) {
        EsWaybillVO vo = new EsWaybillVO();
        //vo.setId(new Long((long)0));
        vo.setName(plugin.getPluginName());
        vo.setOpen(plugin.getOpen());
        vo.setBean(plugin.getPluginId());
        vo.setConfigItems(plugin.definitionConfigItem());
        return vo;
    }
}
