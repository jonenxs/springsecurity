package com.nxs.security.browser;

import com.nxs.security.core.aunthentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.nxs.security.core.properties.SecurityProperties;
import com.nxs.security.core.validate.code.SmsCodeFilter;
import com.nxs.security.core.validate.code.ValidateCodeFilter;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

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
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler nxsAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler nxsAuthenticationFailureHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(nxsAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();
        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(nxsAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        smsCodeFilter.afterPropertiesSet();
        http
            .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
            //      .httpBasic()//弹框登录
            .formLogin()//表单登录
            .loginPage("/authentication/require")
            .loginProcessingUrl("/authentication/form")
            .successHandler(nxsAuthenticationSuccessHandler)
            .failureHandler(nxsAuthenticationFailureHandler)
            .and()
                .rememberMe()//记住我
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
            .and()
                .authorizeRequests()//对请求授权
                    .antMatchers("/authentication/require",
                            securityProperties.getBrowser().getLoginPage(),
                            "/code/*").permitAll()
                    .anyRequest()//所有请求
                    .authenticated()//需要身份认证
            .and()
                .csrf()
                    .disable()
            .apply(smsCodeAuthenticationSecurityConfig);

    }
}
