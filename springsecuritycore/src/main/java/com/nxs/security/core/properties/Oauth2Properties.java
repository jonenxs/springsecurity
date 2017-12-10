package com.nxs.security.core.properties;

import lombok.Data;

@Data
public class Oauth2Properties {

    private String jwtSigningKey = "nxs";

    private Oauth2ClientProperties[] clients = {};
}
