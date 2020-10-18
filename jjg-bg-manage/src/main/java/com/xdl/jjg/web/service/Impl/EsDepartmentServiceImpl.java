package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsDepartmentDO;
import com.jjg.system.model.dto.EsDepartmentDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsDepartment;
import com.xdl.jjg.mapper.EsDepartmentMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsDepartmentService;
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
 * 服务实现类-部门
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-18
 */
@Service
public class EsDepartmentServiceImpl extends ServiceImpl<EsDepartmentMapper, EsDepartment> implements IEsDepartmentService {

    private static Logger logger = LoggerFactory.getLogger(EsDepartmentServiceImpl.class);

    @Autowired
    private EsDepartmentMapper departmentMapper;

    /**
     * 插入数据
     *
     * @param departmentDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-18
     * @return: com.shopx.common.model.result.DubboResult<EsDepartmentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertDepartment(EsDepartmentDTO departmentDTO) {
        try {
            EsDepartment parent = null;
            // 非顶级部门
            if (departmentDTO.getParentId() != null && departmentDTO.getParentId() != 0) {
                parent = departmentMapper.selectById(departmentDTO.getParentId());
                if (parent == null) {
                    throw new ArgumentException(ErrorCode.PARENT_DEPARTMENT_IS_NULL.getErrorCode(), "父部门不存在");
                }
                // 替换path 根据path规则来匹配级别
                String path = parent.getPath().replace("|", ",");
                String[] str = path.split(",");
                // 如果当前的Path length 大于4 证明当前部门级别超过三级
                if (str.length >= 4) {
                    throw new ArgumentException(ErrorCode.DEPARTMENT_GRADE_MORE_THAN_THREE.getErrorCode(), "最多为三级部门,添加失败");
                }
            }
            EsDepartment department = new EsDepartment();
            BeanUtil.copyProperties(departmentDTO, department);
            department.setIsDel(0);
            departmentMapper.insert(department);

            EsDepartment esDepartment = new EsDepartment();
            esDepartment.setId(department.getId());
            // 不是顶级部门
            if (parent != null) {
                esDepartment.setPath(parent.getPath() + esDepartment.getId() + "|");
            } else {// 是顶级部门
                esDepartment.setPath("0|" + esDepartment.getId() + "|");
            }
            departmentMapper.updateById(esDepartment);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param departmentDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-18
     * @return: com.shopx.common.model.result.DubboResult<EsDepartmentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateDepartment(EsDepartmentDTO departmentDTO) {
        try {
            EsDepartment parent = null;
            EsDepartment esDepartment = departmentMapper.selectById(departmentDTO.getId());

            // 如果有子部门则不能更换上级部门
            if (!departmentDTO.getParentId().equals(esDepartment.getParentId())) {
                // 查看是否有子分类
                DubboResult<List<EsDepartmentDO>> children = getChildren(departmentDTO.getId());
                List<EsDepartmentDO> list = children.getData();
                if (list != null && list.size() > 0) {
                    throw new ArgumentException(ErrorCode.HAVE_CHILDREN_DEPARTMENT.getErrorCode(), "当前部门有子部门，不能更换上级部门");
                } else {
                    parent = departmentMapper.selectById(departmentDTO.getParentId());
                    if (parent == null) {
                        throw new ArgumentException(ErrorCode.PARENT_DEPARTMENT_IS_NULL.getErrorCode(), "父部门不存在");
                    }
                    // 替换Path 根据Path规则来匹配级别
                    String path = parent.getPath().replace("|", ",");
                    String[] str = path.split(",");
                    // 如果当前的Path length 大于4 证明当前部门级别超过三级
                    if (str.length >= 4) {
                        throw new ArgumentException(ErrorCode.DEPARTMENT_GRADE_MORE_THAN_THREE.getErrorCode(), "最多为三级部门,添加失败");
                    }
                    departmentDTO.setPath(parent.getPath() + departmentDTO.getId() + "|");
                }
            }
            EsDepartment department = new EsDepartment();
            BeanUtil.copyProperties(departmentDTO, department);
            departmentMapper.updateById(department);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-18
     * @return: com.shopx.common.model.result.DubboResult<EsDepartmentDO>
     */
    @Override
    public DubboResult<EsDepartmentDO> getDepartment(Long id) {
        try {
            QueryWrapper<EsDepartment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDepartment::getId, id);
            EsDepartment department = this.departmentMapper.selectOne(queryWrapper);
            EsDepartmentDO departmentDO = new EsDepartmentDO();
            if (department == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(department, departmentDO);
            return DubboResult.success(departmentDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param departmentDTO DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-18
     * @return: com.shopx.common.model.result.DubboPageResult<EsDepartmentDO>
     */
    @Override
    public DubboPageResult<EsDepartmentDO> getDepartmentList(EsDepartmentDTO departmentDTO, int pageSize, int pageNum) {
        QueryWrapper<EsDepartment> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsDepartment::getDepartmentName, departmentDTO.getDepartmentName());

            Page<EsDepartment> page = new Page<>(pageNum, pageSize);
            IPage<EsDepartment> iPage = this.page(page, queryWrapper);
            List<EsDepartmentDO> departmentDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                departmentDOList = iPage.getRecords().stream().map(department -> {
                    EsDepartmentDO departmentDO = new EsDepartmentDO();
                    BeanUtil.copyProperties(department, departmentDO);
                    return departmentDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(departmentDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-18
     * @return: com.shopx.common.model.result.DubboResult<EsDepartmentDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteDepartment(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            // 查看是否有子部门
            QueryWrapper<EsDepartment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDepartment::getParentId, id);
            List<EsDepartment> list = departmentMapper.selectList(queryWrapper);
            if (list != null && list.size() > 0) {
                throw new ArgumentException(ErrorCode.HAVE_CHILDREN_DEPARTMENT_NOT_DELETE.getErrorCode(), "当前部门有子部门，不能删除");
            }
            QueryWrapper<EsDepartment> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsDepartment::getId, id);
            this.departmentMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //部门下拉框
    @Override
    public DubboPageResult<EsDepartmentDO> getEsDepartmentComboBox() {
        try {
            QueryWrapper<EsDepartment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByAsc(EsDepartment::getId);
            //取出所有没删除的部门
            List<EsDepartment> esDepartmentList = departmentMapper.selectList(queryWrapper);
            List<EsDepartmentDO> topList = new ArrayList<>();
            List<EsDepartmentDO> esDepartmentDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esDepartmentList)) {
                //属性复制
                esDepartmentDOList = esDepartmentList.stream().map(esDepartment -> {
                    EsDepartmentDO esDepartmentDO = new EsDepartmentDO();
                    BeanUtil.copyProperties(esDepartment, esDepartmentDO);
                    return esDepartmentDO;
                }).collect(Collectors.toList());

                for (EsDepartmentDO esDepartmentDO : esDepartmentDOList) {
                    if (esDepartmentDO.getParentId() == 0) {
                        List<EsDepartmentDO> children = getChildren(esDepartmentDOList, esDepartmentDO.getId());
                        esDepartmentDO.setChildren(children);
                        topList.add(esDepartmentDO);
                    }
                }
            }
            return DubboPageResult.success(topList);
        } catch (ArgumentException ae) {
            logger.error("部门下拉框查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("部门下拉框查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 在一个集合中查找子
     */
    private List<EsDepartmentDO> getChildren(List<EsDepartmentDO> esDepartmentDOList, Long parentid) {
        List<EsDepartmentDO> children = new ArrayList<>();
        for (EsDepartmentDO departmentDO : esDepartmentDOList) {
            if (departmentDO.getParentId().compareTo(parentid) == 0) {
                departmentDO.setChildren(this.getChildren(esDepartmentDOList, departmentDO.getId()));
                children.add(departmentDO);
            }
        }
        return children;
    }

    //查询某部门下的子部门列表
    @Override
    public DubboResult<List<EsDepartmentDO>> getChildren(Long parentId) {
        try {
            if (StringUtils.isEmpty(parentId)) {
                throw new ArgumentException(ErrorCode.PARENT_ID_IS_NULL.getErrorCode(), "父id为空");
            }
            QueryWrapper<EsDepartment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDepartment::getParentId, parentId).orderByAsc(EsDepartment::getCreateTime);
            List<EsDepartment> esDepartmentList = departmentMapper.selectList(queryWrapper);
            List<EsDepartmentDO> departmentDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esDepartmentList)) {
                departmentDOList = esDepartmentList.stream().map(department -> {
                    EsDepartmentDO departmentDO = new EsDepartmentDO();
                    BeanUtil.copyProperties(department, departmentDO);
                    return departmentDO;
                }).collect(Collectors.toList());
            }
            return DubboResult.success(departmentDOList);
        } catch (ArgumentException ae) {
            logger.error("查询某部门下的子部门列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询某部门下的子部门列表失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

}
