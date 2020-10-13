package com.xdl.jjg.shiro.oath;


import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsMemberTokenDO;
import com.shopx.member.api.model.domain.dto.EsMemberDTO;
import com.shopx.member.api.model.domain.dto.EsMemberTokenDTO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.member.api.service.IEsMemberTokenService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 身份校验核心类
 */
public class OAuth2Realm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Realm.class);

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsMemberTokenService ieMemberUserTokenService;
   // private IEMemberUserTokenService userTokenService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsMemberService iEsMemberService;
    //private IEMemberUserService userService;

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
        EsMemberTokenDTO userTokenDTO = new EsMemberTokenDTO();
        userTokenDTO.setToken(accessToken);
        DubboResult<EsMemberTokenDO> tokenResult = ieMemberUserTokenService.getMemberTokenInfo(userTokenDTO);
        if(!tokenResult.isSuccess()){
            throw new IncorrectCredentialsException("登录失败，请重新登录");
        }
        EsMemberTokenDO userTokenDO = tokenResult.getData();
        if (null == userTokenDO || userTokenDO.getExpireTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        // 通过UserId从数据库中查找 UserVo对象
        EsMemberDTO userDTO = new EsMemberDTO();
        userDTO.setId(userTokenDO.getMemberId());
        DubboResult<EsMemberDO> userResult = iEsMemberService.getMemberById(userDTO.getId());
        if(!userResult.isSuccess()){
            throw new IncorrectCredentialsException("登录失败，请重新登录");
        }
        EsMemberDO userDO = userResult.getData();
        // 账号不存在
        if (null == userDO) {
            throw new IncorrectCredentialsException("账号不存在");
        }
        // 账号被禁用
        if (userDO.getState() == 1) {
            ShiroKit.getSubject().logout();
            throw new LockedAccountException("账号已被禁用，请联系管理员!");
        }
        ShiroUser su = new ShiroUser();
        BeanUtil.copyProperties(userDO,su);
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        su.setRoles(roles);

        List<String> urlSets = new ArrayList<>();
       // urlSets.add("user:update");
      //  urlSets.add("user:delete");
        su.setUrlSet(urlSets);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(su, accessToken, getName());
        //return null;
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
        logger.info("Shiro开始配置角色菜单权限");
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
