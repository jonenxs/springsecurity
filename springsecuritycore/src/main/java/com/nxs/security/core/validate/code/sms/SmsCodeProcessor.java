package com.nxs.security.core.validate.code.sms;

import com.nxs.security.core.validate.code.ValidateCode;
import com.nxs.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

@Component("smsCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Autowired
    private SmsCodeSender smsCodeSender;


    /**
     * 短信验证码发送器
     * @param request
     * @param smsCode
     * @throws Exception
     */
    @Override
    protected void send(ServletWebRequest request, ValidateCode smsCode) throws Exception {
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile");
        smsCodeSender.send(mobile,smsCode.getCode());
    }
}
