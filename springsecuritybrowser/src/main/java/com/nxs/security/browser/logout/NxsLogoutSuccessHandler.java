package com.nxs.security.browser.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nxs.security.core.properties.SecurityProperties;
import com.nxs.security.core.properties.SessionProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class NxsLogoutSuccessHandler implements LogoutSuccessHandler {

    public NxsLogoutSuccessHandler(String sginOutUrl) {
        this.sginOutUrl = sginOutUrl;
    }

    private String sginOutUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("【登出系统】退出成功");
        if (StringUtils.isBlank(sginOutUrl)) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString("退出成功"));
        } else {
            httpServletResponse.sendRedirect(sginOutUrl);
        }
    }
}
