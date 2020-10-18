package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jjg.system.model.domain.EsAdminUserDO;
import com.jjg.system.model.dto.EsAdminUserDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsAdminUser;
import com.xdl.jjg.mapper.EsAdminUserMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.service.ErrorCodeEnum;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsAdminUserService;
import com.xdl.jjg.web.service.SnowFlakeService;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@Service
public class EsAdminUserServiceImpl implements IEsAdminUserService {

    private static Logger logger = LoggerFactory.getLogger(EsAdminUserServiceImpl.class);

    @Autowired
    private EsAdminUserMapper adminUserMapper;

    @Autowired
    private SnowFlakeService snowFlakeService;


    /**
     * 插入数据
     *
     * @param esAdminUserDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAdminUser(EsAdminUserDTO esAdminUserDTO) {
        try {
            //校验密码格式
            boolean bool = Pattern.matches("[a-fA-F0-9]{32}", esAdminUserDTO.getPassword());
            if (!bool) {
                throw new ArgumentException(ErrorCode.PASSWORD_FORMAT_ERROR.getErrorCode(), "密码格式不正确");
            }
            //校验用户名称是否重复
            QueryWrapper<EsAdminUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdminUser::getUsername, esAdminUserDTO.getUsername()).eq(EsAdminUser::getIsDel, 0);
            EsAdminUser esAdminUser = adminUserMapper.selectOne(queryWrapper);
            if (esAdminUser != null) {
                throw new ArgumentException(ErrorCode.USERNAME_EXIT.getErrorCode(), "管理员名称重复");
            }
            //不是超级管理员的情况下再校验角色是否为空
            if (esAdminUserDTO.getIsAdmin() != 1) {
                if (StringUtils.isEmpty(esAdminUserDTO.getRoleId())) {
                    throw new ArgumentException(ErrorCode.ROLE_IS_NULL.getErrorCode(), "角色为空");
                }
            } else {
                esAdminUserDTO.setRoleId(Long.valueOf(0));
            }
            //校验部门是否为空
            if (StringUtils.isEmpty(esAdminUserDTO.getDepartment())) {
                throw new ArgumentException(ErrorCode.DEPARTMENT_IS_NULL.getErrorCode(), "部门为空");
            }
            //校验图像是否为空
            if (StringUtils.isEmpty(esAdminUserDTO.getFace())) {
                throw new ArgumentException(ErrorCode.FACE_IS_NULL.getErrorCode(), "图像为空");
            }
            //校验手机号码是否为空
            if (StringUtils.isEmpty(esAdminUserDTO.getMobile())) {
                throw new ArgumentException(ErrorCode.MOBILE_IS_NULL.getErrorCode(), "手机号码不能为空");
            }
            EsAdminUser adminUser = new EsAdminUser();
            BeanUtil.copyProperties(esAdminUserDTO, adminUser);
            adminUser.setSalt(RandomStringUtils.randomAlphabetic(6));
            String password = esAdminUserDTO.getPassword();
            adminUser.setPassword(StringUtil.md5(password + adminUser.getSalt()));
            adminUser.setIsDel(0);
            adminUserMapper.insert(adminUser);
            return DubboResult.success();
        } catch (ArgumentException e) {
            String errorMessage = String.format("添加平台管理员失败，esAdminUserDTO:%s", esAdminUserDTO);
            logger.error(errorMessage, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("添加平台管理员失败，esAdminUserDTO:%s", esAdminUserDTO);
            logger.error(errorMessage, th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param adminUserDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAdminUser(EsAdminUserDTO adminUserDTO) {
        try {
            if (adminUserDTO == null) {
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
    public DubboResult<EsAdminUser> getAdminUser(String id) {
        try {
            EsAdminUser esAdminUser = this.adminUserMapper.selectById(id);
            return DubboResult.success(esAdminUser);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCodeEnum.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param delFlag
     * @param pageSize 行数
     * @param pageNum  页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     */
    @Override
    public DubboPageResult getAdminUserList(Boolean delFlag, int pageSize, int pageNum) {

        try {

            return DubboPageResult.success(1l, new ArrayList());
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
    public DubboResult deleteAdminUser(List<String> collectionIds) {
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

    //根据条件查询用户信息
    @Override
    public DubboResult<EsAdminUserDO> getUserInfo(EsAdminUserDTO esAdminUserDTO) {
        try {
            QueryWrapper<EsAdminUser> queryWrapper = new QueryWrapper<>();
            if (!StringUtil.isEmpty(esAdminUserDTO.getUsername())) {
                queryWrapper.lambda().eq(EsAdminUser::getUsername, esAdminUserDTO.getUsername());
            }
            if (esAdminUserDTO.getId() != null) {
                queryWrapper.lambda().eq(EsAdminUser::getId, esAdminUserDTO.getId());
            }
            EsAdminUser adminUser = adminUserMapper.selectOne(queryWrapper);
            if (adminUser == null) {
                throw new ArgumentException(ErrorCode.ITEM_NOT_FOUND.getErrorCode(), "用户未找到");
            }
            EsAdminUserDO adminUserDO = new EsAdminUserDO();
            BeanUtil.copyProperties(adminUser, adminUserDO);
            return DubboResult.success(adminUserDO);
        } catch (ArgumentException e) {
            logger.error("根据条件查询用户信息失败", e);
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        } catch (Throwable th) {
            logger.error("根据条件查询用户信息失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据id查询管理员
    @Override
    public DubboResult<EsAdminUserDO> getById(Long id) {
        try {
            EsAdminUser esAdminUser = adminUserMapper.selectById(id);
            EsAdminUserDO esAdminUserDO = new EsAdminUserDO();
            BeanUtil.copyProperties(esAdminUser, esAdminUserDO);
            return DubboResult.success(esAdminUserDO);
        } catch (ArgumentException e) {
            logger.error("根据id查询管理员失败", e);
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        } catch (Throwable th) {
            logger.error("根据id查询管理员失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult updateEsAdminUser(EsAdminUserDTO esAdminUserDTO) {
        try {
            if (StringUtils.isEmpty(esAdminUserDTO.getId())) {
                throw new ArgumentException(ErrorCode.ID_IS_NULL.getErrorCode(), "id为空");
            }
            //对要修改的管理员是否存在进行校验
            EsAdminUser adminUser = getModel(esAdminUserDTO.getId());
            if (adminUser == null) {
                throw new ArgumentException(ErrorCode.ADMIN_USER_IS_NULL.getErrorCode(), "当前管理员不存在");
            }
            //校验用户名称是否重复
            QueryWrapper<EsAdminUser> query = new QueryWrapper<>();
            query.lambda().eq(EsAdminUser::getUsername, esAdminUserDTO.getUsername());
            EsAdminUser esAdminUser = adminUserMapper.selectOne(query);
            if (esAdminUser != null && !Objects.equals(adminUser.getId(), esAdminUser.getId())) {
                throw new ArgumentException(ErrorCode.USERNAME_EXIT.getErrorCode(), "管理员名称重复");
            }
            //如果修改的是从超级管理员到普通管理员 需要校验此管理员是否是最后一个超级管理员
            if (adminUser.getIsAdmin() == 1 && esAdminUserDTO.getIsAdmin() != 1) {
                QueryWrapper<EsAdminUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsAdminUser::getIsAdmin, 1);
                List<EsAdminUser> esAdminUsers = adminUserMapper.selectList(queryWrapper);
                if (esAdminUsers.size() <= 1) {
                    throw new ArgumentException(ErrorCode.ADMIN_USER_MUST_HAVE_ONE.getErrorCode(), "必须保留一个超级管理员");
                }
            }
            if (esAdminUserDTO.getIsAdmin() != 1) {
                if (StringUtils.isEmpty(esAdminUserDTO.getRoleId())) {
                    throw new ArgumentException(ErrorCode.ROLE_IS_NULL.getErrorCode(), "角色为空");
                }
            } else {
                esAdminUserDTO.setRoleId(Long.valueOf(0));
            }
            //校验部门是否为空
            if (StringUtils.isEmpty(esAdminUserDTO.getDepartment())) {
                throw new ArgumentException(ErrorCode.DEPARTMENT_IS_NULL.getErrorCode(), "部门为空");
            }
            //校验图像是否为空
            if (StringUtils.isEmpty(esAdminUserDTO.getFace())) {
                throw new ArgumentException(ErrorCode.FACE_IS_NULL.getErrorCode(), "图像为空");
            }
            //校验手机号码是否为空
            if (StringUtils.isEmpty(esAdminUserDTO.getMobile())) {
                throw new ArgumentException(ErrorCode.MOBILE_IS_NULL.getErrorCode(), "手机号码不能为空");
            }
            //管理员原密码
            String password = adminUser.getPassword();
            //对管理员是否修改密码进行校验
            if (!StringUtil.isEmpty(esAdminUserDTO.getPassword())) {
                boolean bool = Pattern.matches("[a-fA-F0-9]{32}", esAdminUserDTO.getPassword());
                if (!bool) {
                    throw new ArgumentException(ErrorCode.PASSWORD_FORMAT_ERROR.getErrorCode(), "密码格式不正确");
                }
                esAdminUserDTO.setPassword(StringUtil.md5(esAdminUserDTO.getPassword() + adminUser.getSalt()));
            } else {
                esAdminUserDTO.setPassword(password);
            }
            BeanUtil.copyProperties(esAdminUserDTO, adminUser);
            adminUserMapper.updateById(adminUser);
            return DubboResult.success();
        } catch (ArgumentException e) {
            String errorMessage = String.format("修改平台管理员失败，esAdminUserDTO:%s", esAdminUserDTO);
            logger.error(errorMessage, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        } catch (Throwable th) {
            String errorMessage = String.format("修改平台管理员失败，esAdminUserDTO:%s", esAdminUserDTO);
            logger.error(errorMessage, th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动开启事务回滚
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据id查询平台管理员
    public EsAdminUser getModel(Long id) {
        QueryWrapper<EsAdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsAdminUser::getId, id).eq(EsAdminUser::getIsDel, 0);
        EsAdminUser esAdminUser = adminUserMapper.selectOne(queryWrapper);
        return esAdminUser;
    }


    //根据部门id查询管理员
    @Override
    public DubboResult<List<EsAdminUserDO>> getByDepartmentId(Long departmentId) {
        try {
            QueryWrapper<EsAdminUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdminUser::getDepartment, departmentId);
            List<EsAdminUser> esAdminUserList = adminUserMapper.selectList(queryWrapper);
            List<EsAdminUserDO> esAdminUserDOList = new ArrayList<>();
            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(esAdminUserList)) {
                //属性复制
                esAdminUserDOList = esAdminUserList.stream().map(esAdminUser -> {
                    EsAdminUserDO esAdminUserDO = new EsAdminUserDO();
                    BeanUtil.copyProperties(esAdminUser, esAdminUserDO);
                    return esAdminUserDO;
                }).collect(Collectors.toList());
            }
            return DubboResult.success(esAdminUserDOList);
        } catch (ArgumentException e) {
            logger.error("根据部门id查询管理员失败", e);
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        } catch (Throwable th) {
            logger.error("根据部门id查询管理员失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据角色id查询管理员列表
    @Override
    public DubboPageResult<EsAdminUserDO> getByRoleId(Long roleId) {
        try {
            QueryWrapper<EsAdminUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdminUser::getRoleId, roleId);
            List<EsAdminUser> adminUserList = adminUserMapper.selectList(queryWrapper);
            List<EsAdminUserDO> doList = (List<EsAdminUserDO>) BeanUtil.copyList(adminUserList, new EsAdminUserDO().getClass());
            return DubboPageResult.success(doList);
        } catch (ArgumentException e) {
            logger.error("根据角色id查询管理员列表失败", e);
            return DubboPageResult.fail(e.getExceptionCode(), e.getMessage());
        } catch (Throwable th) {
            logger.error("根据角色id查询管理员列表失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
