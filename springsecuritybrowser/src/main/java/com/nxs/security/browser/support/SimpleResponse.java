package com.nxs.security.browser.support;

import lombok.Data;

@Data
public class SimpleResponse {

    public SimpleResponse(Object content) {
        this.content = content;
    }

    private Object content;


}
