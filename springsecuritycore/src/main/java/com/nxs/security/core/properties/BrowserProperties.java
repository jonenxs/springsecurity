package com.nxs.security.core.properties;

import lombok.Data;

@Data
public class BrowserProperties {

    private String loginPage = "/login.html";

    private LoginType loginType = LoginType.REDIRECT;

    private int rememberMeSeconds = 3600;

    private String signUpUrl = "/signUp.html";

    private String signOutUrl;

    private SessionProperties session = new SessionProperties();

}
