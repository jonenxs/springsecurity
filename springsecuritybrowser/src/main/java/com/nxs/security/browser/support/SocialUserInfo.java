package com.nxs.security.browser.support;

import lombok.Data;

@Data
public class SocialUserInfo {

    private String providerId;

    private String providerUserId;

    private String nickname;

    private String heading;
}
