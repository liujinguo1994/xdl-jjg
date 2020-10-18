package com.xdl.jjg.web.service;

import com.jjg.system.model.domain.EsDepartmentDO;
import com.jjg.system.model.dto.EsDepartmentDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 服务类-部门
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-18
 */
public interface IEsDepartmentService {

    /**
     * 插入数据
     *
     * @param departmentDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsDepartmentDO>
     * @since 2019-06-18
     */
    DubboResult insertDepartment(EsDepartmentDTO departmentDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param departmentDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsDepartmentDO>
     * @since 2019-06-18
     */
    DubboResult updateDepartment(EsDepartmentDTO departmentDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsDepartmentDO>
     * @since 2019-06-18
     */
    DubboResult<EsDepartmentDO> getDepartment(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param departmentDTO DTO
     * @param pageSize      行数
     * @param pageNum       页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsDepartmentDO>
     * @since 2019-06-18
     */
    DubboPageResult<EsDepartmentDO> getDepartmentList(EsDepartmentDTO departmentDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsDepartmentDO>
     * @since 2019-06-18
     */
    DubboResult deleteDepartment(Long id);

    //部门下拉框
    DubboPageResult<EsDepartmentDO> getEsDepartmentComboBox();

    //查询某部门下的子部门列表
    DubboResult<List<EsDepartmentDO>> getChildren(Long parentId);


}
