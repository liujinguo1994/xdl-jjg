package com.xdl.jjg.shiro.oath;
import com.shopx.trade.web.shiro.cache.RedisCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Apache Shiro配置类
 */
@Configuration
public class ShiroJwtConfig {

    @Value("${zhuox.shiro.cookie}")
    private String shiroCookie;

    @Autowired
    public RedisCacheManager redisCacheManager;

    /**
     * ShiroPermissionFactory 处理拦截资源文件问题。 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filtersMap = shiroFilterFactoryBean.getFilters();
        //oauth过滤
        filtersMap.put("oauth2", new OAuth2Filter());
        //filtersMap.put("pathMatch",new URLPathMatchingFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 开放的静态资源
        filterChainDefinitionMap.put("/plugins/**", "anon");
        filterChainDefinitionMap.put("/xlsFile/**", "anon");
        filterChainDefinitionMap.put("/upload/**", "anon");
        filterChainDefinitionMap.put("/api/**", "anon");// API接口

        // swagger接口文档
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");

        //框架等接口
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/actuator/**", "anon");
        filterChainDefinitionMap.put("/ws/**", "anon");
        filterChainDefinitionMap.put("/qr/**", "anon");
        filterChainDefinitionMap.put("/test/**", "anon");

        // 商品相关
        filterChainDefinitionMap.put("/goods/**","anon");

        // 配置相关
        filterChainDefinitionMap.put("/esRegions/**","anon");

        //会员相关
        filterChainDefinitionMap.put("/member/captcha.jpg", "anon");// 图片验证码(kaptcha框架)
        filterChainDefinitionMap.put("/member/login", "anon");
        filterChainDefinitionMap.put("/member/pcInsertMember", "anon");
        filterChainDefinitionMap.put("/member/queryCompanyByNotes", "anon");
        filterChainDefinitionMap.put("/registerMember/queryRepeatMobile/**","anon");
        filterChainDefinitionMap.put("/registerMember/queryCompanyByNotes/**","anon");
        filterChainDefinitionMap.put("/registerMember/checkPassword","anon");
        filterChainDefinitionMap.put("/registerMember/newPassword","anon");
        filterChainDefinitionMap.put("/memberActiveConfig/**","anon");
        filterChainDefinitionMap.put("/zhuox/home/**","anon");
        filterChainDefinitionMap.put("/zhuox/pages/**","anon");
        filterChainDefinitionMap.put("/member/sendSMSCode/**","anon");
        filterChainDefinitionMap.put("/member/fastLogin", "anon");
        filterChainDefinitionMap.put("/member/checkCode", "anon");
        filterChainDefinitionMap.put("/zhuox/goods/shop/**", "anon");
        filterChainDefinitionMap.put("/zhuox/esMemberComment/getDetailCommentList/**", "anon");
        filterChainDefinitionMap.put("/zhuox/esMemberComment/getCountComment/**", "anon");
        filterChainDefinitionMap.put("/zhuox/esMemberComment/getGoodCommentRate/**", "anon");
        filterChainDefinitionMap.put("/zhuox/esMemberComment/getLabelsGroup/**", "anon");
        filterChainDefinitionMap.put("/site-show/**", "anon");
        filterChainDefinitionMap.put("/seller/trade/orders/**", "anon");


        //wap会员登录
        filterChainDefinitionMap.put("/wap/member/memberLogin/login", "anon");
        filterChainDefinitionMap.put("/wap/member/memberLogin/fastLogin", "anon");
        filterChainDefinitionMap.put("/wap/member/memberLogin/sendSMSCode", "anon");

        //wap会员注册
        filterChainDefinitionMap.put("/wap/registerMember/**", "anon");

        //会员支付跳转 暂时放开
        filterChainDefinitionMap.put("/order/pay/wechat/**", "anon");
        filterChainDefinitionMap.put("/order/pay/**", "anon");

        // 手机端商品相关
        filterChainDefinitionMap.put("/wap/goods/goods/**","anon");
        filterChainDefinitionMap.put("/wap/home/**","anon");
        filterChainDefinitionMap.put("/wap/goods/category/**","anon");
        filterChainDefinitionMap.put("/wap/goods/shopCa/**","anon");

        // 移动端秒杀相关
        filterChainDefinitionMap.put("/wap/promotion/seckill/timetableToday","anon");
        filterChainDefinitionMap.put("/wap/promotion/seckill/seckillTimelineGoodsList","anon");

        filterChainDefinitionMap.put("/wap/member/comment/getDetailCommentList","anon");
        filterChainDefinitionMap.put("/wap/member/comment/getGoodCommentRate/**","anon");
        filterChainDefinitionMap.put("/wap/member/comment/getEsCommentCategoryList","anon");

        // 过滤掉小程序相关接口
        filterChainDefinitionMap.put("/applet/**","anon");

        // 用户协议
        filterChainDefinitionMap.put("/esAgreement/**","anon");
        filterChainDefinitionMap.put("/esWapAgreement/**","anon");
        //app端支付回调
        filterChainDefinitionMap.put("/wap/trade/pay/callback/**", "anon");
        filterChainDefinitionMap.put("/wap/trade/pay/wxCallback/**", "anon");
        filterChainDefinitionMap.put("/**", "oauth2");
        //filterChainDefinitionMap.put("/**", "pathMatch");



        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(oAuth2Realm());
        // 注入缓存管理器
        securityManager.setCacheManager(redisCacheManager);
        // 注入记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());
        /*
         * 关闭shiro自带的session，详情见文档
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     */
    @Bean
    public OAuth2Realm oAuth2Realm() {
        OAuth2Realm oAuth2Realm = new OAuth2Realm();
        oAuth2Realm.setCacheManager(redisCacheManager);
        // 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        oAuth2Realm.setAuthenticationCachingEnabled(true);
        // 缓存AuthenticationInfo信息的缓存名称
        oAuth2Realm.setAuthenticationCacheName("authenticationCache");
        // 缓存AuthorizationInfo信息的缓存名称
        oAuth2Realm.setAuthorizationCacheName("authorizationCache");
        return oAuth2Realm;
    }

    /**
     * cookie对象;
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("zhuox-session-z-id");
        // 记住我cookie生效时间1小时 ,单位秒
        simpleCookie.setMaxAge(60 * 60 * 1 * 1);
        simpleCookie.setPath(shiroCookie);
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("5aaC5qKm5oqA5pyvAAAAAA=="));
        return cookieRememberMeManager;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式; 所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
