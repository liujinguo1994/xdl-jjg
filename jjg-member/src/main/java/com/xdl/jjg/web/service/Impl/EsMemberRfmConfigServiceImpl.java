package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsMemberRfmConfigDTO;
import com.jjg.member.model.dto.EsRfmConfigDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberRfmConfig;
import com.xdl.jjg.mapper.EsMemberRfmConfigMapper;
import com.xdl.jjg.model.domain.EsMemberRfmConfigDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMemberRfmConfigService;
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
 * RFM服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:55
 */
@Service
public class EsMemberRfmConfigServiceImpl extends ServiceImpl<EsMemberRfmConfigMapper, EsMemberRfmConfig> implements IEsMemberRfmConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberRfmConfigServiceImpl.class);

    @Autowired
    private EsMemberRfmConfigMapper memberRfmConfigMapper;

    /**
     * 插入数据
     *
     * @param memberRfmConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberRfmConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberRfmConfig(EsRfmConfigDTO memberRfmConfigDTO) {
        try {
            if(null == memberRfmConfigDTO && CollectionUtils.isEmpty(memberRfmConfigDTO.getRfmConfigVOList())){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            this.memberRfmConfigMapper.deleteRfmList();
            memberRfmConfigDTO.getRfmConfigVOList().stream().forEach(esRfmConfigDTO -> {
                EsMemberRfmConfig memberRfmConfig = new EsMemberRfmConfig();
                BeanUtil.copyProperties(esRfmConfigDTO, memberRfmConfig);
                this.memberRfmConfigMapper.insert(memberRfmConfig);
            });
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param memberRfmConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberRfmConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberRfmConfig(EsMemberRfmConfigDTO memberRfmConfigDTO) {
        if (null == memberRfmConfigDTO.getId()) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            EsMemberRfmConfig es = new EsMemberRfmConfig();
            BeanUtil.copyProperties(memberRfmConfigDTO, es);
            if(null != memberRfmConfigDTO.getMonetary()){
                es.setMonetary(memberRfmConfigDTO.getMonetary());
            }
            QueryWrapper<EsMemberRfmConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberRfmConfig::getId, memberRfmConfigDTO.getId());
            this.memberRfmConfigMapper.update(es, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
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
     * @return: com.shopx.common.model.result.DubboResult<EsMemberRfmConfigDO>
     */
    @Override
    public DubboResult<EsMemberRfmConfigDO> getMemberRfmConfig(Long id) {
        try {
            EsMemberRfmConfig memberRfmConfig = this.memberRfmConfigMapper.selectById(id);
            if (memberRfmConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsMemberRfmConfigDO memberRfmConfigDO = new EsMemberRfmConfigDO();
            BeanUtil.copyProperties(memberRfmConfig, memberRfmConfigDO);
            return DubboResult.success(memberRfmConfigDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param memberRfmConfigDTO DTO
     * @param pageSize           页码
     * @param pageNum            页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberRfmConfigDO>
     */
    @Override
    public DubboPageResult<EsMemberRfmConfigDO> getMemberRfmConfigList(EsMemberRfmConfigDTO memberRfmConfigDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberRfmConfig> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsMemberRfmConfig> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberRfmConfig> iPage = this.page(page, queryWrapper);
            List<EsMemberRfmConfigDO> memberRfmConfigDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberRfmConfigDOList = iPage.getRecords().stream().map(memberRfmConfig -> {
                    EsMemberRfmConfigDO memberRfmConfigDO = new EsMemberRfmConfigDO();
                    BeanUtil.copyProperties(memberRfmConfig, memberRfmConfigDO);
                    return memberRfmConfigDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberRfmConfigDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberRfmConfigDO>
     */
    @Override
    public DubboPageResult<EsMemberRfmConfigDO> getMemberRfmConfigListInfo() {
        QueryWrapper<EsMemberRfmConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("recency");
        try {
            // 查询条件
            List<EsMemberRfmConfig> es = this.memberRfmConfigMapper.selectList(queryWrapper);
            List<EsMemberRfmConfigDO> memberRfmConfigDOList = BeanUtil.copyList(es, EsMemberRfmConfigDO.class);
            return DubboPageResult.success(memberRfmConfigDOList);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberRfmConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberRfmConfig(Long id) {
        try {
            this.memberRfmConfigMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
