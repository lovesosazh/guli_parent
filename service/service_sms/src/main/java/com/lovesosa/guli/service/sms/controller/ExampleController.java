package com.lovesosa.guli.service.sms.controller;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.sms.util.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lovesosa
 */
@RefreshScope
@RestController
@RequestMapping("/sms/sample")
public class ExampleController {

    @Autowired
    private SmsProperties smsProperties;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @GetMapping("/test-sign-name")
    public R testSignName() {
        return R.ok().data("signName", signName);
    }

    @GetMapping("/test-sms-properties")
    public R testSmsProperties() {
        return R.ok().data("smsProperties", smsProperties);
    }
}
