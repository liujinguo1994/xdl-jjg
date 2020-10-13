package com.xdl.jjg.shiro.oath;

import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 身份校验核心类
 */
public class OAuth2Realm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Realm.class);

    @Autowired
    private IEsAdminUserTokenService userTokenService;

    @Autowired
    private IEsAdminUserService userService;

    @Autowired
    private IEsRoleService roleService;

    @Autowired
    private IEsMenuService menuService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 认证信息.(身份验证) Authentication 是用来验证用户身份
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        logger.info("Shiro开始权限认证");
        String accessToken = (String) token.getCredentials();
        EsAdminUserTokenDTO adminUserTokenDTO = new EsAdminUserTokenDTO();
        adminUserTokenDTO.setToken(accessToken);
        DubboResult<EsAdminUserTokenDO> tokenResult = userTokenService.getUserToken(adminUserTokenDTO);
        if (!tokenResult.isSuccess()) {
            throw new IncorrectCredentialsException("登录失败，请重新登录");
        }
        EsAdminUserTokenDO userTokenDO = tokenResult.getData();
        if (null == userTokenDO || userTokenDO.getExpireTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        // 通过UserId从数据库中查找 UserVo对象
        EsAdminUserDTO userDTO = new EsAdminUserDTO();
        userDTO.setId(userTokenDO.getUserId());
        DubboResult<EsAdminUserDO> result = userService.getUserInfo(userDTO);
        if (!result.isSuccess()) {
            throw new IncorrectCredentialsException("登录失败，请重新登录");
        }
        EsAdminUserDO adminUserDO = result.getData();
        // 账号不存在
        if (null == adminUserDO) {
            throw new IncorrectCredentialsException("账号不存在");
        }

        ShiroUser su = new ShiroUser();
        List<String> roles = new ArrayList<>();
        BeanUtil.copyProperties(adminUserDO, su);
        //判断是否为超级管理员
        if (adminUserDO.getIsAdmin() == 1) {
            //设置角色
            roles.add("superAdmin");
            su.setRoles(roles);
            //设置用户的权限
            DubboResult<List<String>> listDubboResult = menuService.getAuthExpressionList();
            if (!listDubboResult.isSuccess()) {
                throw new IncorrectCredentialsException("获取所有菜单权限表达式失败");
            }
            List<String> resultData = listDubboResult.getData();
            su.setUrlSet(resultData);
            logger.info("当前角色" + JSONArray.fromObject(roles).toString());
            logger.info("获取所有菜单菜单权限表达式列表:" + JSONArray.fromObject(resultData).toString());
        } else {
            //设置用户的角色
            DubboResult<EsRoleDO> roleDODubboResult = roleService.getEsRole(adminUserDO.getRoleId());
            if (!roleDODubboResult.isSuccess()) {
                throw new IncorrectCredentialsException("根据角色id获取角色失败");
            }
            EsRoleDO roleDO = roleDODubboResult.getData();
            roles.add(roleDO.getName());
            su.setRoles(roles);
            //设置用户的权限
            DubboResult<List<String>> dubboResult = roleService.getAuthExpressionList(adminUserDO.getRoleId());
            if (!dubboResult.isSuccess()) {
                throw new IncorrectCredentialsException("根据角色id获取所属菜单权限表达式列表失败");
            }
            List<String> data = dubboResult.getData();
            logger.info("当前角色" + JSONArray.fromObject(roles).toString());
            logger.info("根据角色id获取所属菜单权限表达式列表:" + JSONArray.fromObject(data).toString());
            su.setUrlSet(data);
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(su, accessToken, getName());
        return authenticationInfo;
    }

    /**
     * 此方法调用hasRole,hasPermission的时候才会进行回调.
     * <p>
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。 （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，调用clearCached方法；
     * Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("Shiro开始权限配置");
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        roles.addAll(shiroUser.getRoles());
        List<String> urlSet = new ArrayList<>();
        urlSet.addAll(shiroUser.getUrlSet());
        info.addRoles(roles);
        info.addStringPermissions(urlSet);
        return info;
    }
}
