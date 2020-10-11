package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsRoleDO;
import com.xdl.jjg.model.dto.EsRoleDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类-角色管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
public interface IEsRoleService {

    //添加角色
    DubboResult insertEsRole(EsRoleDTO esRoleDTO);

    //根据id查询角色
    DubboResult<EsRoleDO> getEsRole(Long id);

    //修改角色
    DubboResult updateEsRole(EsRoleDTO esRoleDTO);

    //删除角色
    DubboResult deleteEsRole(Long id);

    //获取所有角色的权限对照表
    DubboResult<Map<String, List<String>>> getRoleMap();

    //根据角色id获取所属菜单
    DubboResult<List<String>> getRoleMenu(Long id);

    //根据角色id获取所属菜单权限表达式列表
    DubboResult<List<String>> getAuthExpressionList(Long id);

    //分页查询角色列表
    DubboPageResult<EsRoleDO> getEsRoleList(EsRoleDTO esRoleDTO, int pageSize, int pageNum);

}
