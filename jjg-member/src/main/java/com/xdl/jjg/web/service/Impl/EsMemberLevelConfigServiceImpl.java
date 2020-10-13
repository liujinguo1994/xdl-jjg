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
import com.shopx.member.api.model.domain.EsMemberLevelConfigDO;
import com.shopx.member.api.model.domain.dto.EsMemberLevelConfigDTO;
import com.shopx.member.api.model.domain.dto.EsQueryMemberLevelConfigDTO;
import com.shopx.member.api.service.IEsMemberLevelConfigService;
import com.xdl.jjg.entity.EsMemberLevelConfig;
import com.shopx.member.dao.mapper.EsMemberLevelConfigMapper;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-18 15:10:57
 */
@Service
public class EsMemberLevelConfigServiceImpl extends ServiceImpl<EsMemberLevelConfigMapper, EsMemberLevelConfig> implements IEsMemberLevelConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberLevelConfigServiceImpl.class);

    @Autowired
    private EsMemberLevelConfigMapper memberLevelConfigMapper;

    /**
     * 插入数据
     *
     * @param memberLevelConfigDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberLevelConfig(EsMemberLevelConfigDTO memberLevelConfigDTO) {
        try {
            EsMemberLevelConfig memberLevelConfig = new EsMemberLevelConfig();
            BeanUtil.copyProperties(memberLevelConfigDTO, memberLevelConfig);
            this.memberLevelConfigMapper.insert(memberLevelConfig);
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
     * @param memberLevelConfigDTO DTO
     * @param id                   主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberLevelConfig(EsMemberLevelConfigDTO memberLevelConfigDTO, Long id) {
        try {
            EsMemberLevelConfig memberLevelConfig = this.memberLevelConfigMapper.selectById(id);
            if (memberLevelConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberLevelConfigDTO, memberLevelConfig);
            QueryWrapper<EsMemberLevelConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberLevelConfig::getId, id);
            this.memberLevelConfigMapper.update(memberLevelConfig, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @Override
    public DubboResult<EsMemberLevelConfigDO> getMemberLevelConfig(Long id) {
        try {
            EsMemberLevelConfig memberLevelConfig = this.memberLevelConfigMapper.selectById(id);
            if (memberLevelConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsMemberLevelConfigDO memberLevelConfigDO = new EsMemberLevelConfigDO();
            BeanUtil.copyProperties(memberLevelConfig, memberLevelConfigDO);
            return DubboResult.success(memberLevelConfigDO);
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
     * @param memberLevelConfigDTO DTO
     * @param pageSize             页码
     * @param pageNum              页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberLevelConfigDO>
     */
    @Override
    public DubboPageResult<EsMemberLevelConfigDO> getMemberLevelConfigList(EsQueryMemberLevelConfigDTO memberLevelConfigDTO, int pageSize, int pageNum) {
        try {
            // 查询条件
            Page<EsMemberLevelConfigDO> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberLevelConfigDO> iPage = this.memberLevelConfigMapper.getMemberLevelConfigList(page, memberLevelConfigDTO);
            return DubboPageResult.success(iPage.getTotal(), iPage.getRecords());
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
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
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberLevelConfig(Long id) {
        try {
            this.memberLevelConfigMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
    }

    /**
     * 根据grade获取会员等级
     *
     * @param grade 主键 grade
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @Override
    public DubboResult<EsMemberLevelConfigDO> getMemberLevelByGrade(Integer grade) {
        QueryWrapper<EsMemberLevelConfig> queryWrapper = new QueryWrapper<>();
        try {
            if(grade != null){
                queryWrapper.lambda().le(EsMemberLevelConfig::getUnderLine, grade);
            }
            List<EsMemberLevelConfig> list = this.memberLevelConfigMapper.selectList(queryWrapper);
            Optional optional = list.stream().max(Comparator.comparingInt(EsMemberLevelConfig::getUnderLine));
            EsMemberLevelConfigDO memberLevelConfigDO = new EsMemberLevelConfigDO();
            if(optional.isPresent()){
                BeanUtil.copyProperties(optional.get(), memberLevelConfigDO);
            }
            return DubboResult.success(memberLevelConfigDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
