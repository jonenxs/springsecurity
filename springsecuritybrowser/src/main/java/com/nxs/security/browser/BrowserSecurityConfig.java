package com.nxs.security.browser;

import com.nxs.security.browser.session.NxsExpiredSessionStrategy;
import com.nxs.security.core.aunthentication.AbstractChannelSecurityConfig;
import com.nxs.security.core.aunthentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.nxs.security.core.properties.SecurityConstants;
import com.nxs.security.core.properties.SecurityProperties;
import com.nxs.security.core.validate.code.ValidateCodeSecurityConfig;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig{

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired
    private SpringSocialConfigurer nxsSocialConfigurer;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyPasswordAuthenticationConfig(http);//表单登录
        http.apply(validateCodeSecurityConfig)
                .and()
                    .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                    .apply(nxsSocialConfigurer)
                .and()
                    .rememberMe()//记住我
                        .tokenRepository(persistentTokenRepository())
                            .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                            .userDetailsService(userDetailsService)
                .and()
                    .sessionManagement()
                        .invalidSessionStrategy(invalidSessionStrategy)
                        .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                        .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                        .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                    .authorizeRequests()//对请求授权
                        .antMatchers(
                            SecurityConstants.DEFAULT_UN_AUTHENTICATION_URL,
                            SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                            securityProperties.getBrowser().getLoginPage(),
                            SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                            securityProperties.getBrowser().getSignUpUrl(),
                            securityProperties.getBrowser().getSession().getSessionInvalidUrl()+".json",
                            securityProperties.getBrowser().getSession().getSessionInvalidUrl()+".html","user/register").permitAll()
                        .anyRequest()//所有请求
                        .authenticated()//需要身份认证
                .and()
                    .csrf()
                        .disable();
    }
}
