package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsComplaintTypeConfigDO;
import com.jjg.member.model.dto.EsComplaintTypeConfigDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsComplaintReasonConfig;
import com.xdl.jjg.entity.EsComplaintTypeConfig;
import com.xdl.jjg.mapper.EsComplaintReasonConfigMapper;
import com.xdl.jjg.mapper.EsComplaintTypeConfigMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsComplaintTypeConfigService;
import org.apache.dubbo.common.utils.CollectionUtils;
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
 *  投诉类型服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:55
 */
@Service
public class EsComplaintTypeConfigServiceImpl extends ServiceImpl<EsComplaintTypeConfigMapper, EsComplaintTypeConfig> implements IEsComplaintTypeConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsComplaintTypeConfigServiceImpl.class);

    @Autowired
    private EsComplaintTypeConfigMapper complaintTypeConfigMapper;
    @Autowired
    private EsComplaintReasonConfigMapper esComplaintReasonConfigMapper;

    /**
     * 插入数据
     *
     * @param complaintTypeConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertComplaintTypeConfig(EsComplaintTypeConfigDTO complaintTypeConfigDTO) {
        try {
            EsComplaintTypeConfig complaintTypeConfig = new EsComplaintTypeConfig();
            BeanUtil.copyProperties(complaintTypeConfigDTO, complaintTypeConfig);
            this.complaintTypeConfigMapper.insert(complaintTypeConfig);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param complaintTypeConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateComplaintTypeConfig(EsComplaintTypeConfigDTO complaintTypeConfigDTO) {
        if(null == complaintTypeConfigDTO || null == complaintTypeConfigDTO.getId()){
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            EsComplaintTypeConfig esComplaintTypeConfig = new EsComplaintTypeConfig();
            BeanUtil.copyProperties(complaintTypeConfigDTO, esComplaintTypeConfig);
            QueryWrapper<EsComplaintTypeConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsComplaintTypeConfig::getId, complaintTypeConfigDTO.getId());
            this.complaintTypeConfigMapper.update(esComplaintTypeConfig, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    @Override
    public DubboResult<EsComplaintTypeConfigDO> getComplaintTypeConfig(Long id) {
        try {
            EsComplaintTypeConfig complaintTypeConfig = this.complaintTypeConfigMapper.selectById(id);
            if (complaintTypeConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsComplaintTypeConfigDO complaintTypeConfigDO = new EsComplaintTypeConfigDO();
            BeanUtil.copyProperties(complaintTypeConfig, complaintTypeConfigDO);
            return DubboResult.success(complaintTypeConfigDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param complaintTypeConfigDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintTypeConfigDO>
     */
    @Override
    public DubboPageResult<EsComplaintTypeConfigDO> getComplaintTypeConfigList(EsComplaintTypeConfigDTO complaintTypeConfigDTO, int pageSize, int pageNum) {
        QueryWrapper<EsComplaintTypeConfig> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsComplaintTypeConfig> page = new Page<>(pageNum, pageSize);
            IPage<EsComplaintTypeConfig> iPage = this.page(page, queryWrapper);
            List<EsComplaintTypeConfigDO> complaintTypeConfigDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                complaintTypeConfigDOList = iPage.getRecords().stream().map(complaintTypeConfig -> {
                    EsComplaintTypeConfigDO complaintTypeConfigDO = new EsComplaintTypeConfigDO();
                    BeanUtil.copyProperties(complaintTypeConfig, complaintTypeConfigDO);
                    return complaintTypeConfigDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),complaintTypeConfigDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteComplaintTypeConfig(Long id) {
        QueryWrapper<EsComplaintReasonConfig> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsComplaintReasonConfig::getComplainTypeId,id);
            //删除投诉类型时候也删除投诉原因和类型
            List<EsComplaintReasonConfig> esComplaintReasonConfigList = this.esComplaintReasonConfigMapper.selectList(queryWrapper);
            if( CollectionUtils.isNotEmpty(esComplaintReasonConfigList) && esComplaintReasonConfigList.size() >=1){
                throw new ArgumentException(MemberErrorCode.COMPLAINT_TYPE_BIND_REASON.getErrorCode(), MemberErrorCode.COMPLAINT_TYPE_BIND_REASON.getErrorMsg());
            }
            this.complaintTypeConfigMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 列表
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintTypeConfigDO>
     */
    @Override
    public DubboPageResult<EsComplaintTypeConfigDO> getComplaintTypeConfigListInfo() {
        try {
            // 查询条件
            List<EsComplaintTypeConfig> complaintTypeConfigDOList = this.list();
            List<EsComplaintTypeConfigDO>  es =BeanUtil.copyList(complaintTypeConfigDOList,EsComplaintTypeConfigDO.class);
            return DubboPageResult.success(es);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
