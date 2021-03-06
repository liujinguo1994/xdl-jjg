package com.xdl.jjg.web.service;

import com.jjg.system.model.domain.EsAdminUserDO;
import com.jjg.system.model.dto.EsAdminUserDTO;
import com.xdl.jjg.entity.EsAdminUser;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
public interface IEsAdminUserService {

    /**
     * 插入数据
     *
     * @param adminUserDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsAdminUserDO>
     */
    DubboResult insertAdminUser(EsAdminUserDTO adminUserDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param adminUserDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsAdminUserDO>
     */
    DubboResult updateAdminUser(EsAdminUserDTO adminUserDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAdminUserDO>
     */
    DubboResult<EsAdminUser> getAdminUser(String id);

    /**
     * 根据查询条件查询列表
     *
     * @param delFlag
     * @param pageSize 页码
     * @param pageNum  页数
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsAdminUser>
     */
    DubboPageResult getAdminUserList(Boolean delFlag, int pageSize, int pageNum);

    /**
     * 根据主键删除数据  批量删除
     *
     * @param collectionIds 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsAdminUser>
     */
    DubboResult deleteAdminUser(List<String> collectionIds);

    //获取用户信息
    DubboResult<EsAdminUserDO> getUserInfo(EsAdminUserDTO esAdminUserDTO);

    //根据id查询管理员
    DubboResult<EsAdminUserDO> getById(Long id);

    //修改平台管理员
    DubboResult updateEsAdminUser(EsAdminUserDTO esAdminUserDTO);

    //根据部门id查询管理员列表
    DubboResult<List<EsAdminUserDO>> getByDepartmentId(Long departmentId);

    //根据角色id查询管理员列表
    DubboPageResult<EsAdminUserDO> getByRoleId(Long roleId);


}
