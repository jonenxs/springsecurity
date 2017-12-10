package com.nxs.security.app;

import com.nxs.security.core.social.NxsSpringSocialConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.equals(beanName, "nxsSocialSecurityConfig")) {
            NxsSpringSocialConfig config = (NxsSpringSocialConfig) bean;
            config.signupUrl("social/signUp");
            return config;
        }
        return bean;
    }
}
