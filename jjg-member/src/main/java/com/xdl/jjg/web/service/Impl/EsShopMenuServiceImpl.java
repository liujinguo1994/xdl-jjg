package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsShopMenuDO;
import com.jjg.member.model.domain.EsShopMenuListDO;
import com.jjg.member.model.dto.EsShopMenuDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsShopMenu;
import com.xdl.jjg.mapper.EsShopMenuMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsShopMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺菜单管理 服务实现类
 * </p>
 *
 * @author yuanj 595831329.com
 * @since 2019-06-09
 */
@Service
public class EsShopMenuServiceImpl extends ServiceImpl<EsShopMenuMapper, EsShopMenu> implements IEsShopMenuService {

    private static Logger logger = LoggerFactory.getLogger(EsShopMenuServiceImpl.class);

    @Autowired
    private EsShopMenuMapper shopMenuMapper;

    /**
     * 插入店铺菜单管理数据
     *
     * @param shopMenuDTO 店铺菜单管理DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/19 15:16:30
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShopMenu(EsShopMenuDTO shopMenuDTO) {
        try {
            if (shopMenuDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            //对菜单的唯一标识做校验
            EsShopMenu menu = this.getMenuByIdentifier(shopMenuDTO.getIdentifier());
            if (menu != null) {
                throw new ArgumentException(MemberErrorCode.SHOP_MENU_Identifier.getErrorCode(), MemberErrorCode.SHOP_MENU_Identifier.getErrorMsg());
            }
            //判断父级菜单是否有效
            if (shopMenuDTO.getParentId() != 0) {
                EsShopMenu esShopMenu = this.shopMenuMapper.selectById(shopMenuDTO.getParentId());
                if (!shopMenuDTO.getParentId().equals(0) && esShopMenu == null) {
                    throw new ArgumentException(MemberErrorCode.SHOP_MENU_PARENT.getErrorCode(), MemberErrorCode.SHOP_MENU_PARENT.getErrorMsg());
                }
                //校验菜单级别是否超出限制
                if (!shopMenuDTO.getParentId().equals(0) && esShopMenu.getGrade() >= 3) {
                    throw new ArgumentException(MemberErrorCode.SHOP_MENU_OUTINDEX.getErrorCode(), MemberErrorCode.SHOP_MENU_OUTINDEX.getErrorMsg());
                }
            }
            EsShopMenu shopMenu = new EsShopMenu();
            BeanUtil.copyProperties(shopMenuDTO, shopMenu);
            shopMenu.setIsDel(0);
            this.shopMenuMapper.insert(shopMenu);
            this.updateMenu(shopMenu);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺菜单管理新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("店铺菜单管理新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * @param identifier
     * @return EsShopMenu
     * @Des 根据唯一标志符查询菜单
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/6/19 14:47:10
     */
    public EsShopMenu getMenuByIdentifier(String identifier) {
        QueryWrapper<EsShopMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsShopMenu::getIdentifier, identifier);
        EsShopMenu shopMenu = this.shopMenuMapper.selectOne(queryWrapper);
        return shopMenu;
    }

    /**
     * 根据条件更新店铺菜单管理数据
     *
     * @param shopMenuDTO 店铺菜单管理DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/6/19 14:47:10
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShopMenu(EsShopMenuDTO shopMenuDTO) {
        try {
            if (shopMenuDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //校验当前菜单是否存在
            EsShopMenu menu = this.shopMenuMapper.selectById(shopMenuDTO.getId());
            if (menu == null) {
                throw new ArgumentException(MemberErrorCode.SHOP_MENU_NOTEXIT.getErrorCode(), MemberErrorCode.SHOP_MENU_NOTEXIT.getErrorMsg());
            }
            //校验菜单唯一标识重复
            EsShopMenu esShopMenu = this.getMenuByIdentifier(shopMenuDTO.getIdentifier());

            EsShopMenu aprentMenu = this.shopMenuMapper.selectById(shopMenuDTO.getParentId());
            if (esShopMenu != null && !esShopMenu.getId().equals(shopMenuDTO.getId())) {
                throw new ArgumentException(MemberErrorCode.SHOP_MENU_Identifier.getErrorCode(), MemberErrorCode.SHOP_MENU_Identifier.getErrorMsg());
            }
            //校验父级菜单不存在
            if (!shopMenuDTO.getParentId().equals(0) && aprentMenu == null) {
                throw new ArgumentException(MemberErrorCode.SHOP_MENU_PARENT.getErrorCode(), MemberErrorCode.SHOP_MENU_PARENT.getErrorMsg());
            }
            //校验菜单级别是否超出限制
            if (!shopMenuDTO.getParentId().equals(0) && aprentMenu.getGrade() >= 3) {
                throw new ArgumentException(MemberErrorCode.SHOP_MENU_OUTINDEX.getErrorCode(), MemberErrorCode.SHOP_MENU_OUTINDEX.getErrorMsg());
            }
            EsShopMenu shopMenu = new EsShopMenu();
            BeanUtil.copyProperties(shopMenuDTO, shopMenu);
            QueryWrapper<EsShopMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopMenu::getId, shopMenuDTO.getId());
            this.shopMenuMapper.update(shopMenu, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺菜单管理更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺菜单管理更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺菜单管理详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/6/19 14:53:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    @Override
    public DubboResult<EsShopMenuDO> getShopMenu(Long id) {
        try {
            EsShopMenu shopMenu = this.shopMenuMapper.selectById(id);
            EsShopMenuDO shopMenuDO = new EsShopMenuDO();
            if (shopMenu == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shopMenu, shopMenuDO);
            return DubboResult.success(shopMenuDO);
        } catch (ArgumentException ae) {
            logger.error("店铺菜单管理查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺菜单管理查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺菜单管理列表
     *
     * @param shopMenuDTO 店铺菜单管理DTO
     * @param pageSize    页码
     * @param pageNum     页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/19 15:13:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopMenuDO>
     */
    @Override
    public DubboPageResult<EsShopMenuDO> getShopMenuList(EsShopMenuDTO shopMenuDTO, int pageSize, int pageNum) {
        QueryWrapper<EsShopMenu> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            Page<EsShopMenu> page = new Page<>(pageNum, pageSize);
            IPage<EsShopMenu> iPage = this.page(page, queryWrapper);
            List<EsShopMenuDO> shopMenuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                shopMenuDOList = iPage.getRecords().stream().map(shopMenu -> {
                    EsShopMenuDO shopMenuDO = new EsShopMenuDO();
                    BeanUtil.copyProperties(shopMenu, shopMenuDO);
                    return shopMenuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), shopMenuDOList);
        } catch (ArgumentException ae) {
            logger.error("店铺菜单管理分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺菜单管理分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 查询店铺菜单管理列表
     *
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 10:18:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopMenuDO>
     */
    @Override
    public DubboPageResult<EsShopMenuDO> getMenuList() {
        try {
            QueryWrapper<EsShopMenu> queryWrapper = new QueryWrapper<>();
            List<EsShopMenu> esShopMenus = this.shopMenuMapper.selectList(queryWrapper);
            List<EsShopMenuDO> shopMenuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esShopMenus)) {
                shopMenuDOList = esShopMenus.stream().map(shopMenu -> {
                    EsShopMenuDO shopMenuDO = new EsShopMenuDO();
                    BeanUtil.copyProperties(shopMenu, shopMenuDO);
                    return shopMenuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(shopMenuDOList);
        } catch (ArgumentException ae) {
            logger.error("店铺菜单管理分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺菜单管理分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除店铺菜单管理数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/5/19 14:55:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteShopMenu(Long id) {
        try {
            if (id == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            EsShopMenu esShopMenu = this.shopMenuMapper.selectById(id);
            if (esShopMenu == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            this.shopMenuMapper.deleteById(id);
//            //删除父菜单，子菜单也删除
//            this.shopMenuMapper.deleteById(esShopMenu.getParentId());
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺菜单管理删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺菜单管理删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 查询店铺菜单
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/5/19 14:55:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopMenuDO>
     */
    @Override
    public DubboPageResult<EsShopMenuListDO> getMenuTree(Long id) {
        if( null == id){
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            QueryWrapper<EsShopMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("id");
            List<EsShopMenu> esShopMenuList = this.shopMenuMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(esShopMenuList)) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            List<EsShopMenuListDO> topMenuList = new ArrayList<>();
            List<EsShopMenuListDO> esShopMenuDOList = BeanUtil.copyList(esShopMenuList, EsShopMenuListDO.class);
            for (EsShopMenuListDO menu : esShopMenuDOList) {
                if (menu.getParentId().compareTo(id) == 0) {
                    List<EsShopMenuListDO> children = this.getChildren(esShopMenuDOList, menu.getId());
                    menu.setChildren(children);
                    topMenuList.add(menu);
                }
            }
            if (CollectionUtils.isEmpty(topMenuList)) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            return DubboPageResult.success(topMenuList);
        } catch (ArgumentException ae) {
            logger.error("店铺菜单管理分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺菜单管理分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 在一个集合中查找子
     *mvn clean package -DskipTest -U
     * @param menuList 所有菜单集合
     * @return 找到的子集合
     */
    private List<EsShopMenuListDO> getChildren(List<EsShopMenuListDO> menuList, Long parentId) {
        List<EsShopMenuListDO> children = new ArrayList<>();
        for (EsShopMenuListDO menu : menuList) {
            if (menu.getParentId().compareTo(parentId) == 0) {
                menu.setChildren(this.getChildren(menuList, menu.getId()));
                children.add(menu);
            }
        }
        return children;
    }
    private EsShopMenu updateMenu(EsShopMenu menu) {
        //判断父级菜单是否有效
        EsShopMenu parentMenu = this.getById(menu.getParentId());
        if (menu.getParentId() != 0 && parentMenu == null) {
            throw new ResourceNotFoundException("父级菜单不存在");
        }
        //校验菜单级别是否超出限制
        if (menu.getParentId() != 0 && parentMenu.getGrade() >= 3) {
            throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), "菜单级别最多为3级");
        }
        String menuPath = null;
        if (menu.getParentId() == 0) {
            menuPath = "," + menu.getId() + ",";
        } else {
            menuPath = parentMenu.getPath() + menu.getId() + ",";
        }
        String subMenu = menuPath.substring(0, menuPath.length() - 1);
        String[] menus = subMenu.substring(1).split(",");
        menu.setGrade(menus.length);
        menu.setPath(menuPath);
        this.updateById(menu);
        return menu;
    }

}
