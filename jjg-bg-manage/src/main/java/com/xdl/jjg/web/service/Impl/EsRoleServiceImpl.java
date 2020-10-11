package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsRole;
import com.xdl.jjg.entity.Menus;
import com.xdl.jjg.mapper.EsRoleMapper;
import com.xdl.jjg.model.domain.EsRoleDO;
import com.xdl.jjg.model.dto.EsRoleDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.*;
import com.xdl.jjg.web.service.IEsRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类-角色管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Service
public class EsRoleServiceImpl extends ServiceImpl<EsRoleMapper, EsRole> implements IEsRoleService {

    private static Logger logger = LoggerFactory.getLogger(EsRoleServiceImpl.class);

    @Autowired
    private EsRoleMapper esRoleMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult insertEsRole(EsRoleDTO esRoleDTO) {
        try {
            EsRole esRole=new EsRole();
            esRole.setRoleName(esRoleDTO.getRoleName());
            esRole.setRoleDescribe(esRoleDTO.getRoleDescribe());
            esRole.setAuthIds(JsonUtil.objectToJson(esRoleDTO.getMenus()));
            String pinYin = PinyinUtil.getPinYin(esRoleDTO.getRoleName());
            esRole.setName(pinYin);
            esRoleMapper.insert(esRole);
            return DubboResult.success();
        } catch (ArgumentException e) {
            String errorMessage = String.format("添加角色失败，esRoleDTO:%s",esRoleDTO);
            logger.error(errorMessage,e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("添加角色失败，esRoleDTO:%s",esRoleDTO);
            logger.error(errorMessage,th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsRoleDO> getEsRole(Long id) {
        try {
            if(StringUtils.isEmpty(id)){
                throw new ArgumentException(ErrorCode.ID_IS_NULL.getErrorCode(),"id为空");
            }
            EsRole esRole = esRoleMapper.selectById(id);
            EsRoleDO esRoleDO=new EsRoleDO();
            BeanUtil.copyProperties(esRole,esRoleDO);
            return DubboResult.success(esRoleDO);
        } catch (ArgumentException e) {
            String errorMessage = String.format("根据id查询角色失败，参数id:%s",id);
            logger.error(errorMessage,e);
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("根据id查询角色失败，参数id:%s",id);
            logger.error(errorMessage,th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult updateEsRole(EsRoleDTO esRoleDTO) {
        try {
            if(StringUtils.isEmpty(esRoleDTO.getId())){
                throw new ArgumentException(ErrorCode.ID_IS_NULL.getErrorCode(),"id为空");
            }
            EsRole esRole = esRoleMapper.selectById(esRoleDTO.getId());
            if (esRole == null){
                throw new ArgumentException(ErrorCode.ROLE_NOT_EXIT.getErrorCode(),"角色不存在");
            }
            esRole.setRoleName(esRoleDTO.getRoleName());
            esRole.setAuthIds(JsonUtil.objectToJson(esRoleDTO.getMenus()));
            esRole.setRoleDescribe(esRoleDTO.getRoleDescribe());
            String pinYin = PinyinUtil.getPinYin(esRoleDTO.getRoleName());
            esRole.setName(pinYin);
            esRoleMapper.updateById(esRole);
            return DubboResult.success();
        } catch (ArgumentException e) {
            String errorMessage = String.format("修改角色失败，esRoleDTO:%s",esRoleDTO);
            logger.error(errorMessage,e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("修改角色失败，esRoleDTO:%s",esRoleDTO);
            logger.error(errorMessage,th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult deleteEsRole(Long id) {
        try {
            if(StringUtils.isEmpty(id)){
                throw new ArgumentException(ErrorCode.ID_IS_NULL.getErrorCode(),"id为空");
            }
            EsRole esRole = esRoleMapper.selectById(id);
            if (esRole == null){
                throw new ArgumentException(ErrorCode.ROLE_NOT_EXIT.getErrorCode(),"角色不存在");
            }
            esRoleMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException e) {
            String errorMessage = String.format("删除角色失败，参数id:%s",id);
            logger.error(errorMessage,e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("删除角色失败，参数id:%s",id);
            logger.error(errorMessage,th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<Map<String, List<String>>> getRoleMap() {
        try {
            Map<String, List<String>> roleMap = new HashMap<>(16);
            QueryWrapper<EsRole> queryWrapper = new QueryWrapper<>();
            List<EsRole> roles = esRoleMapper.selectList(queryWrapper);
            for (int i = 0; i < roles.size(); i++) {
                List<Menus> menusList = JsonUtil.jsonToList(roles.get(i).getAuthIds(), Menus.class);
                if (menusList != null && menusList.size() > 0) {
                    List<String> authList = new ArrayList<>();
                    //递归查询角色所拥有的菜单权限
                    this.getChildren(menusList, authList);
                    roleMap.put(roles.get(i).getRoleName(), authList);
                }
            }
            return DubboResult.success(roleMap);
        } catch (ArgumentException e) {
            logger.error("获取所有角色的权限对照表失败",e);
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("获取所有角色的权限对照表失败",th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据角色id获取所属菜单
    @Override
    public DubboResult<List<String>> getRoleMenu(Long id) {
        try {
            EsRole esRole = esRoleMapper.selectById(id);
            if (esRole == null) {
                throw new ArgumentException(ErrorCode.ROLE_NOT_EXIT.getErrorCode(),"角色不存在");
            }
            List<Menus> menusList = JsonUtil.jsonToList(esRole.getAuthIds(), Menus.class);
            List<String> authList = new ArrayList<>();
            //筛选菜单
            reset(menusList, authList);
            return DubboResult.success(authList);
        }catch (ArgumentException e) {
            logger.error("根据角色id获取所属菜单失败",e);
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("根据角色id获取所属菜单失败",th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据角色id获取所属菜单权限表达式列表
    @Override
    public DubboResult<List<String>> getAuthExpressionList(Long id) {
        try {
            EsRole esRole = esRoleMapper.selectById(id);
            if (esRole == null) {
                throw new ArgumentException(ErrorCode.ROLE_NOT_EXIT.getErrorCode(),"角色不存在");
            }
            List<Menus> menusList = JsonUtil.jsonToList(esRole.getAuthIds(), Menus.class);
            List<String> authList = new ArrayList<>();
            //筛选菜单的权限表达式
            filter(menusList, authList);
            return DubboResult.success(authList);
        }catch (ArgumentException e) {
            logger.error("根据角色id获取所属菜单权限表达式列表失败",e);
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("根据角色id获取所属菜单权限表达式列表失败",th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsRoleDO> getEsRoleList(EsRoleDTO esRoleDTO, int pageSize, int pageNum) {
        try {
            Page<EsRole> page=new Page<>(pageNum,pageSize);
            QueryWrapper<EsRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StringUtil.notEmpty(esRoleDTO.getRoleName()), EsRole::getRoleName,esRoleDTO.getRoleName());
            IPage<EsRole> iPage = esRoleMapper.selectPage(page, queryWrapper);
            List<EsRoleDO> esRoleDOList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(iPage.getRecords())){
                esRoleDOList = iPage.getRecords().stream().map(esRole -> {
                    EsRoleDO esRoleDO = new EsRoleDO();
                    BeanUtil.copyProperties(esRole,esRoleDO);
                    return esRoleDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),esRoleDOList);
        }catch (ArgumentException e) {
            logger.error("分页查询角色列表失败",e);
            return DubboPageResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询角色列表失败",th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 筛选checked为true的菜单
     *
     * @param menuList 菜单集合
     */
    private void reset(List<Menus> menuList, List<String> authList) {
        for (Menus menus : menuList) {
            //将此角色拥有的菜单唯一标识放入list中
            if (menus.isChecked()) {
                authList.add(menus.getIdentifier());
            }
            if (!menus.getChildren().isEmpty()) {
                reset(menus.getChildren(), authList);
            }
        }
    }

    /**
     * 筛选checked为true的菜单的权限表达式
     *
     * @param menuList 菜单集合
     */
    private void filter(List<Menus> menuList, List<String> authList) {
        for (Menus menus : menuList) {
            //将此角色拥有的菜单的权限表达式放入list中
            if (menus.isChecked()) {
                authList.add(menus.getAuthExpression());
            }
            if (!menus.getChildren().isEmpty()) {
                reset(menus.getChildren(), authList);
            }
        }
    }

    /**
     * 递归将此角色锁拥有的菜单权限保存到list
     *
     * @param menuList 菜单集合
     * @param authList 权限组集合
     */
    private void getChildren(List<Menus> menuList, List<String> authList) {
        for (Menus menus : menuList) {
            //将此角色拥有的菜单权限放入list中
            if (menus.isChecked()) {
                authList.add(menus.getAuthExpression());
            }
            if (!menus.getChildren().isEmpty()) {
                getChildren(menus.getChildren(), authList);
            }
        }
    }
}

