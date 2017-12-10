package com.nxs.security.core.properties;

import lombok.Data;

@Data
public class Oauth2ClientProperties {

    private String clientId;

    private String clientSecret;

    private int accessTokenValiditySeconds = 7200;
}
