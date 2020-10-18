package com.xdl.jjg.web.service;

import com.jjg.system.model.domain.EsMenuDO;
import com.jjg.system.model.dto.EsMenuDTO;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

//菜单管理接口
public interface IEsMenuService {

    //添加菜单
    DubboResult insertEsMenu(EsMenuDTO esMenuDTO);

    //编辑菜单
    DubboResult updateEsMenu(EsMenuDTO esMenuDTO);

    //删除菜单
    DubboResult deleteEsMenu(Long id);

    //根据父id查询所有菜单
    DubboResult<List<EsMenuDO>> getEsMenuTree(Long parentId);

    //获取所有菜单权限表达式
    DubboResult<List<String>> getAuthExpressionList();


}
