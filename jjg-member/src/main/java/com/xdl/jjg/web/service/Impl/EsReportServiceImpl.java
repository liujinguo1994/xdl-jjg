package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.DateUtils;
import com.shopx.common.util.StringUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsComrImglDO;
import com.shopx.member.api.model.domain.EsReportDO;
import com.shopx.member.api.model.domain.dto.EsComrImglDTO;
import com.shopx.member.api.model.domain.dto.EsReportDTO;
import com.shopx.member.api.model.domain.dto.ReportQueryParam;
import com.shopx.member.api.model.domain.enums.ReportEnum;
import com.shopx.member.api.service.IEsComrImglService;
import com.shopx.member.api.service.IEsReportService;
import com.xdl.jjg.entity.EsReport;
import com.shopx.member.dao.mapper.EsReportMapper;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * <p>
 * 店员 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-25
 */
@Service
public class EsReportServiceImpl extends ServiceImpl<EsReportMapper, EsReport> implements IEsReportService {

    private static Logger logger = LoggerFactory.getLogger(EsReportServiceImpl.class);

    @Autowired
    private EsReportMapper reportMapper;

    @Autowired
    private IEsComrImglService comrImglService;


    /**
     * 插入举报数据
     *
     * @param reportDTO 举报DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 11:19:30
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsReportDO> insertReport(EsReportDTO reportDTO) {
        try {

            reportDTO.setCreateTime(DateUtils.MILLIS_PER_SECOND);
            EsReport esReport = new EsReport();
            BeanUtil.copyProperties(reportDTO, esReport);
            esReport.setState(ReportEnum.WAIT.value());
            this.reportMapper.insert(esReport);
            Long comId = esReport.getId();

            //插入图片
            List<EsComrImglDTO> comrImglDTOList = reportDTO.getComrImglDTOList();
            for (EsComrImglDTO com:comrImglDTOList) {
                com.setComId(comId);
                com.setType(1);
                this.comrImglService.insertComr(com);
            }
            return DubboResult.success(reportDTO);
        } catch (ArgumentException ae){
            logger.error("新增举报失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增举报失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 更新举报数据
     *
     * @param dealContent 处理内容
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 13:19:30
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult dealReport(Long id,String state,String dealContent) {
        try {


            EsReport esReport = this.reportMapper.selectById(id);
            if (esReport==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            esReport.setState(state);
            if (StringUtil.notEmpty(dealContent)){
                esReport.setDealContent(dealContent);
            }
            this.reportMapper.updateById(esReport);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("举报更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("举报更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 11:19:30
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    @Override
    public DubboResult<EsReportDO> getReport(Long id) {
        try {
            EsReport esReport = this.reportMapper.selectById(id);
            if (esReport == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsReportDO reportDO=new EsReportDO();
            BeanUtil.copyProperties(esReport, reportDO);
            return DubboResult.success(reportDO);
        } catch (ArgumentException ae){
            logger.error("查询举报详情失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询举报详情失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }



    /**
     * 获取举报列表
     * @param reportQueryParam  举报查询
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 11:19:30
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    @Override
    public DubboPageResult<EsReportDO> getReportList(ReportQueryParam reportQueryParam, int pageSize, int pageNum) {
        try {
            IPage<EsReportDO> page = this.reportMapper.getAllreport(new Page(pageNum,pageSize),reportQueryParam);
            List<EsReportDO> data = page.getRecords();
            if (data!=null){
                for (EsReportDO comp:data) {
                    DubboPageResult<EsComrImglDO> result = this.comrImglService.getComrList(comp.getId(), 1);
                    if (result.getData()!=null){
                        comp.setComrImglDOList(result.getData().getList());
                    }
                }
            }
            return DubboPageResult.success(page.getTotal(),page.getRecords());
        } catch (ArgumentException ae){
            logger.error("获取举报列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("获取举报列表失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据id删除举报
     *
     * @param ids 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 11:19:30
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteReport(Integer [] ids) {
        try {
            QueryWrapper<EsReport> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsReport::getId, ids);
            this.reportMapper.delete(deleteWrapper);
            //删除图片
            this.comrImglService.deleteComr(ids,1);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("举报信息删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("举报信息删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
