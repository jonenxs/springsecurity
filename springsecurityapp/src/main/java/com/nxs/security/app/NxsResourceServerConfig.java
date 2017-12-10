package com.nxs.security.app;

import com.nxs.security.app.social.openid.OpenIdAuthenticationSecurityConfig;
import com.nxs.security.core.aunthentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.nxs.security.core.authorize.AuthorizeConfigManager;
import com.nxs.security.core.properties.SecurityConstants;
import com.nxs.security.core.properties.SecurityProperties;
import com.nxs.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableResourceServer
public class NxsResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    protected AuthenticationSuccessHandler nxsAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler nxsAuthenticationFailureHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer nxsSocialConfigurer;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UN_AUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(nxsAuthenticationSuccessHandler)
                .failureHandler(nxsAuthenticationFailureHandler);

        http

                .apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(nxsSocialConfigurer)
                .and()
                .apply(openIdAuthenticationSecurityConfig)
//                .and()
//                .authorizeRequests()//对请求授权
//                .antMatchers(
//                        SecurityConstants.DEFAULT_UN_AUTHENTICATION_URL,
//                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
//                        securityProperties.getBrowser().getLoginPage(),
//                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
//                        securityProperties.getBrowser().getSignUpUrl(),
//                        securityProperties.getBrowser().getSession().getSessionInvalidUrl() + ".json",
//                        securityProperties.getBrowser().getSession().getSessionInvalidUrl() + ".html", "user/register","/social/signUp",
//                        securityProperties.getBrowser().getSignOutUrl()).permitAll()
//                .anyRequest()//所有请求
//                .authenticated()//需要身份认证
                .and()
                .csrf()
                .disable();
        authorizeConfigManager.config(http.authorizeRequests());
    }
}
