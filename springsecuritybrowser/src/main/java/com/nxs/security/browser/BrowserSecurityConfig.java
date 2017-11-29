package com.nxs.security.browser;

import com.nxs.security.core.properties.SecurityProperties;
import com.nxs.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler nxsAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler nxsAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(nxsAuthenticationFailureHandler);

        http
            .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
            //      .httpBasic()//弹框登录
            .formLogin()//表单登录
            .loginPage("/authentication/require")
            .loginProcessingUrl("/authentication/form")
            .successHandler(nxsAuthenticationSuccessHandler)
            .failureHandler(nxsAuthenticationFailureHandler)
            .and()
            .authorizeRequests()//对请求授权
            .antMatchers("/authentication/require",
                    securityProperties.getBrowser().getLoginPage(),
                    "/code/image").permitAll()
            .anyRequest()//所有请求
            .authenticated()//需要身份认证
            .and()
            .csrf().disable();

    }
}
