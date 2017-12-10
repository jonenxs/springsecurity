package com.nxs.security.app.social.openid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class OpenIdAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    private String providerId;


    public OpenIdAuthenticationToken(String openId, String providerId) {
        super(null);
        this.principal = openId;
        this.providerId = providerId;
        setAuthenticated(false);
    }



    public OpenIdAuthenticationToken(UserDetails user, Collection<? extends GrantedAuthority> authorities) {
        super(null);
        this.principal = user.getUsername();
        Collection<GrantedAuthority> authorityCollection = getAuthorities();
        setAuthenticated(CollectionUtils.containsAny(authorityCollection, authorities));

    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getProviderId() {
        return providerId;
    }
}
