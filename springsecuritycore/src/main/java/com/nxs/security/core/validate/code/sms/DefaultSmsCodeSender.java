package com.nxs.security.core.validate.code.sms;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {
    @Override
    public void send(String mobile, String code) {
        log.info("【短信验证码】默认发送验证码实现；向mobile={}发送的验证码是code={}", mobile, code);
    }
}
