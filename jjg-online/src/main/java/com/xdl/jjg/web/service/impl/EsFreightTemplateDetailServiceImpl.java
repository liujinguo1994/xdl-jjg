package com.xdl.jjg.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsFreightTemplateDetailDO;
import com.jjg.trade.model.dto.EsFreightTemplateDetailDTO;
import com.jjg.trade.model.vo.AreaVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsFreightTemplateDetail;
import com.xdl.jjg.mapper.EsFreightTemplateDetailMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsFreightTemplateDetailService;
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
 * 运费模板详情表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsFreightTemplateDetailService.class, timeout = 50000)
public class EsFreightTemplateDetailServiceImpl extends ServiceImpl<EsFreightTemplateDetailMapper, EsFreightTemplateDetail> implements IEsFreightTemplateDetailService {

    private static Logger logger = LoggerFactory.getLogger(EsFreightTemplateDetailServiceImpl.class);

    @Autowired
    private EsFreightTemplateDetailMapper freightTemplateDetailMapper;

    /**
     * 插入运费模板详情表数据
     *
     * @param freightTemplateDetailDTO 运费模板详情表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsFreightTemplateDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertFreightTemplateDetail(EsFreightTemplateDetailDTO freightTemplateDetailDTO) {
        try {
            EsFreightTemplateDetail freightTemplateDetail = new EsFreightTemplateDetail();
            BeanUtil.copyProperties(freightTemplateDetailDTO, freightTemplateDetail);
            this.freightTemplateDetailMapper.insert(freightTemplateDetail);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("运费模板详情表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("运费模板详情表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新运费模板详情表数据
     *
     * @param freightTemplateDetailDTO 运费模板详情表DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsFreightTemplateDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateFreightTemplateDetail(EsFreightTemplateDetailDTO freightTemplateDetailDTO, Long id) {
        try {
            EsFreightTemplateDetail freightTemplateDetail = this.freightTemplateDetailMapper.selectById(id);
            if (freightTemplateDetail == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(freightTemplateDetailDTO, freightTemplateDetail);
            QueryWrapper<EsFreightTemplateDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFreightTemplateDetail::getId, id);
            this.freightTemplateDetailMapper.update(freightTemplateDetail, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("运费模板详情表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("运费模板详情表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取运费模板详情表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsFreightTemplateDetailDO>
     */
    @Override
    public DubboResult<EsFreightTemplateDetailDO> getFreightTemplateDetail(Long id) {
        try {
            EsFreightTemplateDetail freightTemplateDetail = this.freightTemplateDetailMapper.selectById(id);
            if (freightTemplateDetail == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsFreightTemplateDetailDO freightTemplateDetailDO = new EsFreightTemplateDetailDO();
            BeanUtil.copyProperties(freightTemplateDetail, freightTemplateDetailDO);
            return DubboResult.success(freightTemplateDetailDO);
        } catch (ArgumentException ae){
            logger.error("运费模板详情表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("运费模板详情表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询运费模板详情表列表
     *
     * @param freightTemplateDetailDTO 运费模板详情表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsFreightTemplateDetailDO>
     */
    @Override
    public DubboPageResult<EsFreightTemplateDetailDO> getFreightTemplateDetailList(EsFreightTemplateDetailDTO freightTemplateDetailDTO, int pageSize, int pageNum) {
        QueryWrapper<EsFreightTemplateDetail> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsFreightTemplateDetail> page = new Page<>(pageNum, pageSize);
            IPage<EsFreightTemplateDetail> iPage = this.page(page, queryWrapper);
            List<EsFreightTemplateDetailDO> freightTemplateDetailDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                freightTemplateDetailDOList = iPage.getRecords().stream().map(freightTemplateDetail -> {
                    EsFreightTemplateDetailDO freightTemplateDetailDO = new EsFreightTemplateDetailDO();
                    BeanUtil.copyProperties(freightTemplateDetail, freightTemplateDetailDO);
                    return freightTemplateDetailDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(freightTemplateDetailDOList);
        } catch (ArgumentException ae){
            logger.error("运费模板详情表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("运费模板详情表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除运费模板详情表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsFreightTemplateDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteFreightTemplateDetail(Long id) {
        try {
            this.freightTemplateDetailMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("运费模板详情表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("运费模板详情表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据模板ID查询模板详情
     *
     * @param modeId 模板ID
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsFreightTemplateDetailDO>
     */
    @Override
    public DubboPageResult<EsFreightTemplateDetailDO> getFreightTemplateDetailListByModeId(Long modeId) {

        try {
            // 查询条件
            QueryWrapper<EsFreightTemplateDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFreightTemplateDetail::getModeId, modeId);

            List<EsFreightTemplateDetail> list = this.freightTemplateDetailMapper.selectList(queryWrapper);
            List<EsFreightTemplateDetailDO> freightTemplateDetailListDO = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(list)) {
                freightTemplateDetailListDO = list.stream().map(freightTemplateDetail -> {
                    EsFreightTemplateDetailDO freightTemplateDetailDO = new EsFreightTemplateDetailDO();
                    BeanUtil.copyProperties(freightTemplateDetail, freightTemplateDetailDO);
                    List<AreaVO> area = JSONObject.parseArray(freightTemplateDetail.getArea(), AreaVO.class);
                    freightTemplateDetailDO.setAreaId(area.stream().map(AreaVO::getId).collect(Collectors.toList()));
                    return freightTemplateDetailDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(freightTemplateDetailListDO);
        } catch (ArgumentException ae){
            logger.error("运费模板详情表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("运费模板详情表查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
