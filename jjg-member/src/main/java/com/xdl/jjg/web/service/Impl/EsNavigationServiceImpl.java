package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsNavigation;
import com.xdl.jjg.mapper.EsNavigationMapper;
import com.xdl.jjg.model.domain.EsNavigationDO;
import com.xdl.jjg.model.dto.EsNavigationDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsNavigationService;
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
 * 店铺导航管理 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-05
 */
@Service
public class EsNavigationServiceImpl extends ServiceImpl<EsNavigationMapper, EsNavigation> implements IEsNavigationService {

    private static Logger logger = LoggerFactory.getLogger(EsNavigationServiceImpl.class);

    @Autowired
    private EsNavigationMapper navigationMapper;

    /**
     * 插入店铺导航管理数据
     *
     * @param navigationDTO 店铺导航管理DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 11:14:40
     * @return: com.shopx.common.model.result.DubboResult<EsNavigationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertNavigation(EsNavigationDTO navigationDTO) {
        try {

            //导航名称长度不能超过6
            if (navigationDTO.getName().length()>6){
                throw new ArgumentException(MemberErrorCode.NAVIGA_OUT_LENGTH.getErrorCode(), MemberErrorCode.NAVIGA_OUT_LENGTH.getErrorMsg());
            }
            EsNavigation navigation = new EsNavigation();
            BeanUtil.copyProperties(navigationDTO, navigation);
            this.navigationMapper.insert(navigation);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺导航管理新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺导航管理新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店铺导航管理数据
     *
     * @param navigationDTO 店铺导航管理DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 10:52:40
     * @return: com.shopx.common.model.result.DubboResult<EsNavigationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateNavigation(EsNavigationDTO navigationDTO) {
        try {
            EsNavigation esNavigation = this.navigationMapper.selectById(navigationDTO.getId());
            if (esNavigation==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            //导航名称长度不能超过6
            if (navigationDTO.getName().length()>6){
                throw new ArgumentException(MemberErrorCode.NAVIGA_OUT_LENGTH.getErrorCode(), MemberErrorCode.NAVIGA_OUT_LENGTH.getErrorMsg());
            }
            EsNavigation navigation = new EsNavigation();
            BeanUtil.copyProperties(navigationDTO, navigation);
            QueryWrapper<EsNavigation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsNavigation::getId, navigationDTO.getId());
            this.navigationMapper.update(navigation, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺导航管理更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺导航管理更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺导航管理详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 10:30:40
     * @return: com.shopx.common.model.result.DubboResult<EsNavigationDO>
     */
    @Override
    public DubboResult<EsNavigationDO> getNavigation(Long id) {
        try {
            Assert.notNull(id,"主键不能为空");
            EsNavigation navigation = this.navigationMapper.selectById(id);
            EsNavigationDO navigationDO = new EsNavigationDO();
            if (navigation == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(navigation, navigationDO);
            return DubboResult.success(navigationDO);
        } catch (ArgumentException ae){
            logger.error("店铺导航管理查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺导航管理查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺导航管理列表
     *
     * @param navigationDTO 店铺导航管理DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 10:45:40
     * @return: com.shopx.common.model.result.DubboPageResult<EsNavigationDO>
     */
    @Override
    public DubboPageResult<EsNavigationDO> getNavigationList(EsNavigationDTO navigationDTO, int pageSize, int pageNum) {
        QueryWrapper<EsNavigation> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsNavigation> page = new Page<>(pageNum, pageSize);
            IPage<EsNavigation> iPage = this.page(page, queryWrapper);
            List<EsNavigationDO> navigationDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                navigationDOList = iPage.getRecords().stream().map(navigation -> {
                    EsNavigationDO navigationDO = new EsNavigationDO();
                    BeanUtil.copyProperties(navigation, navigationDO);
                    return navigationDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),navigationDOList);
        } catch (ArgumentException ae){
            logger.error("店铺导航管理分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺导航管理分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据主键删除店铺导航管理数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 10:40:40
     * @return: com.shopx.common.model.result.DubboResult<EsNavigationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteNavigation(Long id,Long shopId) {
        try {
            EsNavigation esNavigation = this.navigationMapper.selectById(id);
            if (esNavigation==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            if (esNavigation.getShopId()!=shopId){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            this.navigationMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店铺导航管理删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺导航管理删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
