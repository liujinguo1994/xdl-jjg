package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsShopMenuDO;
import com.shopx.member.api.model.domain.EsShopRoleDO;
import com.shopx.member.api.model.domain.dto.EsShopRoleDTO;
import com.shopx.member.api.service.IEsShopRoleService;
import com.xdl.jjg.entity.EsShopRole;
import com.shopx.member.dao.mapper.EsShopRoleMapper;
import org.apache.dubbo.common.utils.Assert;
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
 * 店铺角色 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-21
 */
@Service
public class EsShopRoleServiceImpl extends ServiceImpl<EsShopRoleMapper, EsShopRole> implements IEsShopRoleService {

    private static Logger logger = LoggerFactory.getLogger(EsShopRoleServiceImpl.class);

    @Autowired
    private EsShopRoleMapper shopRoleMapper;

    /**
     * 插入店铺角色数据
     *
     * @param shopRoleDTO 店铺角色DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsShopRoleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShopRole(EsShopRoleDTO shopRoleDTO) {
        try {
            if (shopRoleDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsShopRole shopRole = new EsShopRole();
            BeanUtil.copyProperties(shopRoleDTO, shopRole);
            this.shopRoleMapper.insert(shopRole);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺角色新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺角色新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店铺角色数据
     *
     * @param shopRoleDTO 店铺角色DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/21 13:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsShopRoleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShopRole(EsShopRoleDTO shopRoleDTO) {
        try {
            if (shopRoleDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsShopRole esShopRole = this.shopRoleMapper.selectById(shopRoleDTO.getId());
            if (esShopRole == null) {
                throw new ArgumentException(MemberErrorCode.CUSTOM_NOT_ROLE.getErrorCode(), MemberErrorCode.CUSTOM_NOT_ROLE.getErrorMsg());
            }
            if (shopRoleDTO.getShopId()!=esShopRole.getShopId()){
                throw new ArgumentException(MemberErrorCode.ROLE_SHOP.getErrorCode(), MemberErrorCode.ROLE_SHOP.getErrorMsg());
            }
            EsShopRole shopRole = new EsShopRole();
            BeanUtil.copyProperties(shopRoleDTO, shopRole);
            QueryWrapper<EsShopRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopRole::getId, shopRoleDTO.getId());
            this.shopRoleMapper.update(shopRole, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺角色更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺角色更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺角色详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/21 13:27:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopRoleDO>
     */
    @Override
    public DubboResult<EsShopRoleDO> getShopRole(Long id) {
        try {
            EsShopRole shopRole = this.shopRoleMapper.selectById(id);
            EsShopRoleDO shopRoleDO = new EsShopRoleDO();
            if (shopRole == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shopRole, shopRoleDO);
            return DubboResult.success(shopRoleDO);
        } catch (ArgumentException ae){
            logger.error("店铺角色查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺角色查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据查询店铺角色列表
     *
     * @param shopRoleDTO 店铺角色DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/21 13:15:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopRoleDO>
     */
    @Override
    public DubboPageResult<EsShopRoleDO> getShopRoleList(EsShopRoleDTO shopRoleDTO, int pageSize, int pageNum) {
        QueryWrapper<EsShopRole> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsShopRole::getShopId,shopRoleDTO.getShopId());
            Page<EsShopRole> page = new Page<>(pageNum, pageSize);
            IPage<EsShopRole> iPage = this.page(page, queryWrapper);
            List<EsShopRoleDO> shopRoleDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                shopRoleDOList = iPage.getRecords().stream().map(shopRole -> {
                    EsShopRoleDO shopRoleDO = new EsShopRoleDO();
                    BeanUtil.copyProperties(shopRole, shopRoleDO);
                    return shopRoleDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),shopRoleDOList);
        } catch (ArgumentException ae){
            logger.error("店铺角色分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺角色分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除店铺角色数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/21 13:15:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopRoleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteShopRole(Long id,Long shopId) {
        try {
            Assert.notNull(id,"角色id不能为空");
            Assert.notNull(shopId,"店铺id不能为空");
            EsShopRole esShopRole = this.shopRoleMapper.selectById(id);
            if (esShopRole == null) {
                throw new ArgumentException(MemberErrorCode.CUSTOM_NOT_ROLE.getErrorCode(), MemberErrorCode.CUSTOM_NOT_ROLE.getErrorMsg());
            }
            if (shopId!=esShopRole.getShopId()){
                throw new ArgumentException(MemberErrorCode.ROLE_SHOP.getErrorCode(), MemberErrorCode.ROLE_SHOP.getErrorMsg());
            }
            this.shopRoleMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店铺角色删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺角色删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsShopMenuDO> getMenuByRoleBy(Long roleId) {
        try {
            EsShopRole shopRole = this.shopRoleMapper.selectById(roleId);
            if (shopRole == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            List<EsShopMenuDO> shopRoleDO = JsonUtil.jsonToList(shopRole.getAuthIds(), EsShopMenuDO.class);
            return DubboPageResult.success(shopRoleDO);
        } catch (ArgumentException ae){
            logger.error("店铺角色查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺角色查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<List<String>> getRoleMenu(Long id,Long shopId) {
        try{
            EsShopRole esShopRole = this.getById(id);
            if (esShopRole == null || esShopRole.getShopId().longValue() != shopId.longValue()) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), "角色不存在或无权限访问");
            }
            List<EsShopMenuDO>  menuList = JsonUtil.jsonToList(esShopRole.getAuthIds(),EsShopMenuDO.class);
            List<String> authList = new ArrayList<>();
            this.reset(menuList, authList);
            return DubboResult.success(authList);
        } catch (ArgumentException ae){
            logger.error("店铺角色查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺角色查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 筛选checked为true的菜单
     *
     * @param menuList 菜单集合
     */
    private void reset(List<EsShopMenuDO> menuList, List<String> authList) {
        for (EsShopMenuDO menus : menuList) {
            //将此角色拥有的菜单权限放入list中
            if (menus.isChecked()) {
                authList.add(menus.getIdentifier());
            }
            if (!menus.getChildren().isEmpty()) {
                reset(menus.getChildren(), authList);
            }
        }
    }
}
