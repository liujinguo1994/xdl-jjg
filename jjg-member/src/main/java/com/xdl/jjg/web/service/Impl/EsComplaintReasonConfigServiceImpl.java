package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsComplaintReasonConfigDO;
import com.shopx.member.api.model.domain.dto.EsComplaintReasonConfigDTO;
import com.shopx.member.api.service.IEsComplaintReasonConfigService;
import com.xdl.jjg.entity.EsComplaintReasonConfig;
import com.xdl.jjg.entity.EsComplaintTypeConfig;
import  com.xdl.jjg.mapper.EsComplaintReasonConfigMapper;
import com.shopx.member.dao.mapper.EsComplaintTypeConfigMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:55
 */
@Service(version = "${dubbo.application.version}" , interfaceClass = IEsComplaintReasonConfigService.class, timeout = 50000)
public class EsComplaintReasonConfigServiceImpl extends ServiceImpl<EsComplaintReasonConfigMapper, EsComplaintReasonConfig> implements IEsComplaintReasonConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsComplaintReasonConfigServiceImpl.class);

    @Autowired
    private EsComplaintReasonConfigMapper complaintReasonConfigMapper;
    @Autowired
    private EsComplaintTypeConfigMapper complaintTypeConfigMapper;

    /**
     * 插入数据
     *
     * @param complaintReasonConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertComplaintReasonConfig(EsComplaintReasonConfigDTO complaintReasonConfigDTO) {
        try {
            EsComplaintReasonConfig complaintReasonConfig = new EsComplaintReasonConfig();
            BeanUtil.copyProperties(complaintReasonConfigDTO, complaintReasonConfig);
            this.complaintReasonConfigMapper.insert(complaintReasonConfig);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param complaintReasonConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateComplaintReasonConfig(EsComplaintReasonConfigDTO complaintReasonConfigDTO) {
        QueryWrapper<EsComplaintReasonConfig> queryWrapper = new QueryWrapper<>();
        if (null == complaintReasonConfigDTO || null == complaintReasonConfigDTO.getId()) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            EsComplaintReasonConfig es = new EsComplaintReasonConfig();
            BeanUtil.copyProperties(complaintReasonConfigDTO, es);
            queryWrapper.lambda().eq(EsComplaintReasonConfig::getId, complaintReasonConfigDTO.getId());
            this.complaintReasonConfigMapper.update(es, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败" , th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    @Override
    public DubboResult<EsComplaintReasonConfigDO> getComplaintReasonConfig(Long id) {
        try {
            EsComplaintReasonConfig complaintReasonConfig = this.complaintReasonConfigMapper.selectById(id);
            if (complaintReasonConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsComplaintReasonConfigDO complaintReasonConfigDO = new EsComplaintReasonConfigDO();
            BeanUtil.copyProperties(complaintReasonConfig, complaintReasonConfigDO);
            return DubboResult.success(complaintReasonConfigDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败" , ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败" , th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param complaintReasonConfigDTO DTO
     * @param pageSize                 页码
     * @param pageNum                  页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintReasonConfigDO>
     */
    @Override
    public DubboPageResult<EsComplaintReasonConfigDO> getComplaintReasonConfigListBuyer(EsComplaintReasonConfigDTO complaintReasonConfigDTO, int pageSize, int pageNum) {
        try {
            // 查询条件

            Page<EsComplaintReasonConfig> page = new Page<>(pageNum, pageSize);
            IPage<EsComplaintReasonConfigDO> list = this.complaintReasonConfigMapper.getComplaintReasonConfigList(page);
            return DubboPageResult.success(list.getTotal(),list.getRecords());
        } catch (ArgumentException ae) {
            logger.error("分页查询失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败" , th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsComplaintReasonConfigDO> getComplaintReasonConfigList(EsComplaintReasonConfigDTO complaintReasonConfigDTO, int pageSize, int pageNum) {
        try {
            // 查询条件
            Page<EsComplaintReasonConfig> page = new Page<>(pageNum, pageSize);
            IPage<EsComplaintReasonConfigDO> list = this.complaintReasonConfigMapper.getComplaintReasonConfigList(page);
            if(CollectionUtils.isNotEmpty(list.getRecords())){
                list.getRecords().stream().peek(e -> {
                    EsComplaintTypeConfig complaintTypeConfig = complaintTypeConfigMapper.selectById(e.getComplainTypeId());
                    e.setComplaintTypeName(complaintTypeConfig.getComplainType());
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(list.getTotal(),list.getRecords());
        } catch (ArgumentException ae) {
            logger.error("分页查询失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败" , th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintReasonConfigDO>
     */
    @Override
    public DubboPageResult<EsComplaintReasonConfigDO> getComplaintReasonConfigBuyerList() {
        try {
            List<EsComplaintReasonConfig> complaintReasonConfigList = this.list();
            List<EsComplaintReasonConfigDO> list = BeanUtil.copyList(complaintReasonConfigList, EsComplaintReasonConfigDO.class);
            if(list.size() > 0){
                list = list.stream().map(e -> {
                    EsComplaintTypeConfig complaintTypeConfig = complaintTypeConfigMapper.selectById(e.getComplainTypeId());
                    if(complaintTypeConfig != null){
                        e.setComplaintTypeName(complaintTypeConfig.getComplainType());
                    }
                    return e;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(list);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败" , th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteComplaintReasonConfig(Long id) {
        try {
            this.complaintReasonConfigMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败" , th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 移动端-根据投诉类型ID查询投诉原因列表
     */
    @Override
    public DubboPageResult<EsComplaintReasonConfigDO> getByTypeId(Long complainTypeId) {
        try {
            QueryWrapper<EsComplaintReasonConfig> queryWrapper = new QueryWrapper<EsComplaintReasonConfig>();
            queryWrapper.lambda().eq(EsComplaintReasonConfig::getComplainTypeId,complainTypeId);
            List<EsComplaintReasonConfig> reasonConfigList = complaintReasonConfigMapper.selectList(queryWrapper);
            List<EsComplaintReasonConfigDO> doList = BeanUtil.copyList(reasonConfigList, EsComplaintReasonConfigDO.class);
            return DubboPageResult.success(doList);
        } catch (ArgumentException ae) {
            logger.error("移动端-根据投诉类型ID查询投诉原因列表失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("移动端-根据投诉类型ID查询投诉原因列表失败" , th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
