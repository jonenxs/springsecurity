package com.nxs.security.app.social.openid;

import com.nxs.security.core.properties.SecurityConstants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String openIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_OPENID;
    private String providerIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_PROVIDERID;
    private boolean postOnly = true;

    protected OpenIdAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_OPENID, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !httpServletRequest.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not suppord: " + httpServletRequest.getMethod());
        }
        String openid = obtainOpenId(httpServletRequest);
        String providerId = obtainProviderId(httpServletRequest);

        if (openid == null) {
            openid = "";
        }

        if (providerId == null) {
            providerId = "";
        }

        openid = openid.trim();
        providerId = providerId.trim();
        OpenIdAuthenticationToken authRequest = new OpenIdAuthenticationToken(openid, providerId);

        setDetails(httpServletRequest, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void setDetails(HttpServletRequest httpServletRequest, OpenIdAuthenticationToken authRequest) {

    }

    private String obtainProviderId(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter(providerIdParameter);
    }

    private String obtainOpenId(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter(openIdParameter);
    }
}
