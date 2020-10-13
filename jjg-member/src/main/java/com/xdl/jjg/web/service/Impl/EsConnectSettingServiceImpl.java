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
import com.shopx.member.api.model.domain.EsConnectSettingDO;
import com.shopx.member.api.model.domain.dto.EsConnectSettingDTO;
import com.shopx.member.api.service.IEsConnectSettingService;
import com.xdl.jjg.entity.EsConnectSetting;
import  com.xdl.jjg.mapper.EsConnectSettingMapper;
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
 * 第三方登录参数 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-18
 */
@Service
public class EsConnectSettingServiceImpl extends ServiceImpl<EsConnectSettingMapper, EsConnectSetting> implements IEsConnectSettingService {

    private static Logger logger = LoggerFactory.getLogger(EsConnectSettingServiceImpl.class);

    @Autowired
    private EsConnectSettingMapper connectSettingMapper;

    /**
     * 插入第三方登录参数数据
     *
     * @param connectSettingDTO 第三方登录参数DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertConnectSetting(EsConnectSettingDTO connectSettingDTO) {
        try {
            if (connectSettingDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsConnectSetting connectSetting = new EsConnectSetting();
            BeanUtil.copyProperties(connectSettingDTO, connectSetting);
            this.connectSettingMapper.insert(connectSetting);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("第三方登录参数新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("第三方登录参数新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新第三方登录参数数据
     *
     * @param connectSettingDTO 第三方登录参数DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateConnectSetting(EsConnectSettingDTO connectSettingDTO) {
        try {
            EsConnectSetting esConnectSetting = this.connectSettingMapper.selectById(connectSettingDTO.getId());
            if (esConnectSetting==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsConnectSetting connectSetting = new EsConnectSetting();
            BeanUtil.copyProperties(connectSettingDTO, connectSetting);
            QueryWrapper<EsConnectSetting> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsConnectSetting::getId, connectSettingDTO.getId());
            this.connectSettingMapper.update(connectSetting, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("第三方登录参数更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("第三方登录参数更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取第三方登录参数详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    @Override
    public DubboResult<EsConnectSettingDO> getConnectSetting(Long id) {
        try {
            EsConnectSetting connectSetting = this.connectSettingMapper.selectById(id);
            EsConnectSettingDO connectSettingDO = new EsConnectSettingDO();
            if (connectSetting == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(connectSetting, connectSettingDO);
            return DubboResult.success(connectSettingDO);
        } catch (ArgumentException ae){
            logger.error("第三方登录参数查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("第三方登录参数查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询第三方登录参数列表
     *
     * @param connectSettingDTO 第三方登录参数DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsConnectSettingDO>
     */
    @Override
    public DubboPageResult<EsConnectSettingDO> getConnectSettingList(EsConnectSettingDTO connectSettingDTO, int pageSize, int pageNum) {
        QueryWrapper<EsConnectSetting> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsConnectSetting> page = new Page<>(pageNum, pageSize);
            IPage<EsConnectSetting> iPage = this.page(page, queryWrapper);
            List<EsConnectSettingDO> connectSettingDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                connectSettingDOList = iPage.getRecords().stream().map(connectSetting -> {
                    EsConnectSettingDO connectSettingDO = new EsConnectSettingDO();
                    BeanUtil.copyProperties(connectSetting, connectSettingDO);
                    return connectSettingDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(connectSettingDOList);
        } catch (ArgumentException ae){
            logger.error("第三方登录参数分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("第三方登录参数分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 查询列表
     *
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 14:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    @Override
    public DubboPageResult<EsConnectSettingDO> getList() {
        try{
            QueryWrapper<EsConnectSetting> queryWrapper=new QueryWrapper<>();
            List<EsConnectSetting> connectSettingList = this.connectSettingMapper.selectList(queryWrapper);
            List<EsConnectSettingDO> connectSettingDOS = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(connectSettingList)) {
                connectSettingDOS = connectSettingList.stream().map(shopMenu -> {
                    EsConnectSettingDO esConnectSettingDO = new EsConnectSettingDO();
                    BeanUtil.copyProperties(shopMenu, esConnectSettingDO);
                    return esConnectSettingDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(connectSettingDOS);
        } catch (ArgumentException ae){
            logger.error("店铺菜单管理分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺菜单管理分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除第三方登录参数数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteConnectSetting(Long id) {
        try {
            this.connectSettingMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("第三方登录参数删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("第三方登录参数删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
