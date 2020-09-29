package com.xdl.jjg.web.service.impl;

import com.xdl.jjg.enums.NoTypeEnum;
import com.xdl.jjg.mapper.user.SysUserAccountExpandMapper;
import com.xdl.jjg.pojo.user.SysUser;
import com.xdl.jjg.pojo.user.SysUserAccount;
import com.xdl.jjg.pojo.user.SysUsersRoles;
import com.xdl.jjg.util.ShareCodeUtil;
import com.xdl.jjg.util.SnowFlakeProperties;
import com.xdl.jjg.web.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年10月2019/10/8日
 * @Description TODO
 */
@Service
public class SysUserServiceImpl implements SysUserService {


    @Autowired
    private SysUserAccountExpandMapper userAccountExpandMapper;
    @Autowired
    private SnowFlakeProperties snowFlakeProperties;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser() {


        System.out.println("************************数据迁移开始**********************");

        SnowFlakeImpl snowFlake = new SnowFlakeImpl(snowFlakeProperties.getDatacenterId(), snowFlakeProperties.getMachineId());
        //新增用户
        userAccountExpandMapper.saveUser();
        List<SysUser> userList = userAccountExpandMapper.listUser();
        if (CollectionUtils.isEmpty(userList)){
            return;
        }
        for (SysUser user : userList) {
            user.setUserNo(NoTypeEnum.USER_NO.getCode() + snowFlake.nextId());
        }

        userAccountExpandMapper.updateUser(userList);
       List<String> userNoList = userList.stream().map(p->p.getUserNo()).collect(Collectors.toList());
        //新增普通前台用户
        userAccountExpandMapper.saveUserAccount(userNoList);
        List<SysUserAccount> items = new ArrayList<>();
        SysUserAccount sysUserAccount = null;
        for (SysUser user : userList) {
            long id = Long.parseLong(StringUtils.substringAfterLast(user.getUserNo(), NoTypeEnum.USER_NO.getCode()));
            String invitationCode = ShareCodeUtil.idToCode(id);
            sysUserAccount = SysUserAccount.builder()
                    .userNo(user.getUserNo())
                    .invitationCode(invitationCode)
                    .build();
            items.add(sysUserAccount);
        }

        userAccountExpandMapper.updateUserAccount(items);


        //新增用户关系表
        userAccountExpandMapper.saveUserRelationship(userNoList);


        //新增用户角色
        List<SysUsersRoles> usersRolesList = new ArrayList<>();
        SysUsersRoles usersRoles = null;
        for (SysUser user : userList) {
            usersRoles = SysUsersRoles.builder()
                    .userRoleNo(NoTypeEnum.USER_ROLE_NO.getCode() + snowFlake.nextId())
                    .userNo(user.getUserNo())
                    .roleId(4)
                    .build();
            usersRolesList.add(usersRoles);
        }

        userAccountExpandMapper.saveUserRoles(usersRolesList);


        System.out.println("************************数据迁移结束**********************");
    }



}
