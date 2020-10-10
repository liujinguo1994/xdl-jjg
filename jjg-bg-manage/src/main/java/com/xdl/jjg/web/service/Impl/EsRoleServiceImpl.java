package com.xdl.jjg.web.service.Impl;

import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsRole;
import com.xdl.jjg.entity.Menus;
import com.xdl.jjg.mapper.EsRoleMapper;
import com.xdl.jjg.model.domain.EsRoleDO;
import com.xdl.jjg.model.dto.EsRoleDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.service.ErrorCodeEnum;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsRoleService;
import com.xdl.jjg.web.service.SnowFlakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@Service
public class EsRoleServiceImpl implements IEsRoleService {

    private static Logger logger = LoggerFactory.getLogger(EsRoleServiceImpl.class);

    @Autowired
    private EsRoleMapper roleMapper;

    @Autowired
    private SnowFlakeService snowFlakeService;

    /**
     * 插入数据
     *
     * @param roleDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertRole(EsRoleDTO roleDTO) {
        try {
            if (roleDTO == null) {
                throw new ArgumentException(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMassage());
            }

            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), ErrorCodeEnum.SYS_ERROR.getErrorMassage());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param roleDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateRole(EsRoleDTO roleDTO) {
        try {
            if (roleDTO == null) {
                throw new ArgumentException(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMassage());
            }

            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), ErrorCodeEnum.SYS_ERROR.getErrorMassage());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     */
    @Override
    public DubboResult<EsRole> getRole(String id) {
        try {

            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param delFlag
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     */
    @Override
    public DubboPageResult getRoleList(Boolean delFlag, int pageSize, int pageNum) {

        try {

            return DubboPageResult.success(1l,new ArrayList<>());
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 批量删除
     * 根据主键删除数据
     *
     * @param collectionIds 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteRole(List<String> collectionIds) {
        try {
            if (CollectionUtils.isEmpty(collectionIds)) {
                throw new ArgumentException(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", collectionIds));
            }

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("查询删除失败");
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), ErrorCodeEnum.SYS_ERROR.getErrorMassage());
        }
    }

    @Override
    public DubboResult<EsRoleDO> getEsRole(Long id) {
        try {
            if(StringUtils.isEmpty(id)){
                throw new ArgumentException(ErrorCode.ID_IS_NULL.getErrorCode(),"id为空");
            }
            EsRole esRole = roleMapper.selectById(id);
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

    //根据角色id获取所属菜单权限表达式列表
    @Override
    public DubboResult<List<String>> getAuthExpressionList(Long id) {
        try {
            EsRole esRole = roleMapper.selectById(id);
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
}
