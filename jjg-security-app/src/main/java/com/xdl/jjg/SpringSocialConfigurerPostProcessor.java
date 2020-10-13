package com.xdl.jjg;

import com.xdl.jjg.social.EasySpringSocialConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author: ciyuan
 * @Date: 2019/6/29 11:34
 * 在Spring容器中所有bean初始化之前和初始化之后都需要经过该类的两个方法
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    /**
     * 需要做处理的bean的名称
     */
    private static final String CHECK_BEAN_NAME = "easySocialConfigurer";


    /**
     * bean初始化之前
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * bean初始化之后
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException 在app环境下cy.security.core.social.SocialConfig#easySocialConfigurer()初始化好以后 将signUp修改成自定义的注册路由
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.equals(beanName, CHECK_BEAN_NAME)) {
            EasySpringSocialConfigurer configurer = (EasySpringSocialConfigurer) bean;
            configurer.signupUrl("/social/signUp");
            return configurer;
        }
        return bean;
    }
}
