package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsMenu;
import com.xdl.jjg.mapper.EsMenuMapper;
import com.xdl.jjg.model.domain.EsMenuDO;
import com.xdl.jjg.model.dto.EsMenuDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMenuService;
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
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类-菜单管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Service
public class EsMenuServiceImpl extends ServiceImpl<EsMenuMapper, EsMenu> implements IEsMenuService {

    private static Logger logger = LoggerFactory.getLogger(EsMenuServiceImpl.class);

    @Autowired
    private EsMenuMapper esMenuMapper;

    //添加菜单
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult insertEsMenu(EsMenuDTO esMenuDTO) {
        try {
            //对菜单的唯一标识做校验
            EsMenu esMenu = this.getMenuByIdentifier(esMenuDTO.getIdentifier());
            if (esMenu != null) {
                throw new ArgumentException(ErrorCode.MENU_IDENTIFIER_REPETITION.getErrorCode(),"菜单唯一标识重复");
            }
            //判断父级菜单是否有效
            EsMenu parentMenu = this.getModel(esMenuDTO.getParentId());
            if (esMenuDTO.getParentId() !=0 && parentMenu == null) {
                throw new ArgumentException(ErrorCode.PARENT_MENU_NOT_EXIT.getErrorCode(),"父级菜单不存在");
            }
            //校验菜单级别是否超出限制
            if (esMenuDTO.getParentId() !=0 && parentMenu.getGrade() >= 4) {
                throw new ArgumentException(ErrorCode.MENU_GRADE_ERROR.getErrorCode(),"菜单级别最多为4级");
            }
            EsMenu menu=new EsMenu();
            BeanUtil.copyProperties(esMenuDTO,menu);
            menu.setIsDel(0);
            esMenuMapper.insert(menu);//mybatisplus会自动把当前插入对象在数据库中的id写回到该实体中
            logger.info("新增菜单的id:"+menu.getId());
            updateMenu(menu);
            return DubboResult.success();
        } catch (ArgumentException e) {
            String errorMessage = String.format("添加菜单失败，esMenuDTO:%s",esMenuDTO);
            logger.error(errorMessage,e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("添加菜单失败，esMenuDTO:%s",esMenuDTO);
            logger.error(errorMessage,th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //编辑菜单
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult updateEsMenu(EsMenuDTO esMenuDTO) {
        try {
            if (StringUtils.isEmpty(esMenuDTO.getId())) {
                throw new ArgumentException(ErrorCode.ID_IS_NULL.getErrorCode(), "id为空");
            }
            //校验当前菜单是否存在
            EsMenu esMenu = this.getModel(esMenuDTO.getId());
            if (esMenu == null) {
                throw new ArgumentException(ErrorCode.MENU_NOT_EXIT.getErrorCode(),"当前菜单不存在");
            }
            //校验菜单唯一标识重复
            EsMenu valMenu = this.getMenuByIdentifier(esMenu.getIdentifier());
            if (valMenu != null && !valMenu.getId().equals(esMenuDTO.getId())) {
                throw new ArgumentException(ErrorCode.MENU_IDENTIFIER_REPETITION.getErrorCode(),"菜单唯一标识重复");
            }
            EsMenu esMenu1 = new EsMenu();
            BeanUtil.copyProperties(esMenuDTO,esMenu1);
            //执行修改
            updateMenu(esMenu1);
            return DubboResult.success();
        } catch (ArgumentException e) {
            String errorMessage = String.format("编辑菜单失败，参数esMenuDTO:%s",esMenuDTO);
            logger.error(errorMessage,e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("编辑菜单失败，参数esMenuDTO:%s",esMenuDTO);
            logger.error(errorMessage,th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult deleteEsMenu(Long id) {
        try {
            if(StringUtils.isEmpty(id)){
                throw new ArgumentException(ErrorCode.ID_IS_NULL.getErrorCode(),"id为空");
            }
            EsMenu esMenu = getModel(id);
            if (esMenu == null) {
                throw new ArgumentException(ErrorCode.MENU_NOT_EXIT.getErrorCode(),"当前菜单不存在");
            }
            //判断是否有子菜单
            QueryWrapper<EsMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMenu::getParentId,id);
            List<EsMenu> esMenuList = esMenuMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(esMenuList)){
                throw new ArgumentException(ErrorCode.EXIT_CHILDREN_MENU_NOT_DEL.getErrorCode(),"存在子菜单,不能删除");
            }
            esMenuMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException e) {
            String errorMessage = String.format("删除菜单失败，参数id:%s",id);
            logger.error(errorMessage,e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("删除菜单失败，参数id:%s",id);
            logger.error(errorMessage,th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<List<EsMenuDO>> getEsMenuTree(Long parentId) {
        try {
            if(StringUtils.isEmpty(parentId)){
                throw new ArgumentException(ErrorCode.ID_IS_NULL.getErrorCode(),"id为空");
            }
            QueryWrapper<EsMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByAsc(EsMenu::getId);
            //取出所有没删除的菜单
            List<EsMenu> esMenuList = esMenuMapper.selectList(queryWrapper);
            List<EsMenuDO> topMenuList = new ArrayList<>();
            List<EsMenuDO> esMenuDOlist = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esMenuList)){
                //属性复制
                esMenuDOlist = esMenuList.stream().map(esMenu -> {
                    EsMenuDO esMenuDO = new EsMenuDO();
                    BeanUtil.copyProperties(esMenu, esMenuDO);
                    return esMenuDO;
                }).collect(Collectors.toList());

                for (EsMenuDO menu: esMenuDOlist) {
                    if (menu.getParentId().compareTo(parentId) == 0) {
                        List<EsMenuDO> children = getChildren(esMenuDOlist, menu.getId());
                        menu.setChildren(children);
                        topMenuList.add(menu);
                    }
                }
            }
            return DubboResult.success(topMenuList);
        } catch (ArgumentException e) {
            logger.error("根据父id查询所有菜单失败",e);
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("根据父id查询所有菜单失败",th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //获取所有菜单权限表达式
    @Override
    public DubboResult<List<String>> getAuthExpressionList() {
        try {
            QueryWrapper<EsMenu> queryWrapper = new QueryWrapper<>();
            List<EsMenu> esMenuList = esMenuMapper.selectList(queryWrapper);
            List<String> authList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esMenuList)){
                esMenuList.stream().forEach(esMenu -> {
                    authList.add(esMenu.getAuthExpression());
                });
            }
            return DubboResult.success(authList);
        } catch (ArgumentException e) {
            logger.error("获取所有菜单权限表达式失败",e);
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("获取所有菜单权限表达式失败",th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 在一个集合中查找子
     *
     * @param esMenuDOlist 所有菜单集合
     * @param parentid 父id
     * @return 找到的子集合
     */
    private List<EsMenuDO> getChildren(List<EsMenuDO> esMenuDOlist, Long parentid) {
        List<EsMenuDO> children = new ArrayList<EsMenuDO>();
        for (EsMenuDO menu : esMenuDOlist) {
            if (menu.getParentId().compareTo(parentid) == 0) {
                menu.setChildren(this.getChildren(esMenuDOlist, menu.getId()));
                children.add(menu);
            }
        }
        return children;
    }


    //唯一标识做校验
    public EsMenu getMenuByIdentifier(String identifier) {
        QueryWrapper<EsMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsMenu::getIdentifier,identifier).eq(EsMenu::getIsDel,0);
        EsMenu esMenu = esMenuMapper.selectOne(queryWrapper);
        return esMenu;
    }

    //判断菜单是否有效
    public EsMenu getModel(Long id) {
        QueryWrapper<EsMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id).eq("is_del",0);
        EsMenu esMenu = esMenuMapper.selectOne(queryWrapper);
        return esMenu;
    }

    /**
     * 执行修改菜单操作
     *
     * @param menu 菜单对象
     * @return 菜单对象
     */
    private void updateMenu(EsMenu menu) {
        //判断父级菜单是否有效
        EsMenu parentMenu = this.getModel(menu.getParentId());
        if (menu.getParentId() !=0 && parentMenu == null) {
            throw new ArgumentException(ErrorCode.PARENT_MENU_NOT_EXIT.getErrorCode(),"父级菜单不存在");
        }
        //校验菜单级别是否超出限制
        if (menu.getParentId() !=0 && parentMenu.getGrade() >= 4) {
            throw new ArgumentException(ErrorCode.MENU_GRADE_ERROR.getErrorCode(),"菜单级别最多为4级");
        }
        String menuPath = null;
        if (menu.getParentId().equals(Long.valueOf(0))) {
            menuPath = "," + menu.getId() + ",";
        } else {
            menuPath = parentMenu.getPath() + menu.getId() + ",";
        }
        String subMenu = menuPath.substring(0, menuPath.length() - 1);
        String[] menus = subMenu.substring(1).split(",");
        menu.setGrade(menus.length);
        menu.setPath(menuPath);
        esMenuMapper.updateById(menu);
    }
}
