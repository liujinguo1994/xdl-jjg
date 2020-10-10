package com.xdl.jjg.web.service;

import com.xdl.jjg.entity.EsRole;
import com.xdl.jjg.model.domain.EsRoleDO;
import com.xdl.jjg.model.dto.EsRoleDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
public interface IEsRoleService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param roleDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsRoleDO>
     */
    DubboResult insertRole(EsRoleDTO roleDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param roleDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsRoleDO>
     */
    DubboResult updateRole(EsRoleDTO roleDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsRoleDO>
     */
    DubboResult<EsRole> getRole(String id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param delFlag
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsRole>
     */
    DubboPageResult getRoleList(Boolean delFlag, int pageSize, int pageNum);

    /**
     * 根据主键删除数据  批量删除
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param collectionIds    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsRole>
     */
    DubboResult deleteRole(List<String> collectionIds);

    //根据id查询角色
    DubboResult<EsRoleDO> getEsRole(Long id);

    //根据角色id获取所属菜单权限表达式列表
    DubboResult<List<String>> getAuthExpressionList(Long id);
}

