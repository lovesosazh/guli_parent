package com.lovesosa.guli.service.sms.controller.api;

import com.aliyuncs.exceptions.ClientException;
import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.FormUtils;
import com.lovesosa.guli.common.base.util.RandomUtils;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author lovesosa
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@Slf4j
public class ApiSmsController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException {
        // 校验手机受否合法
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            log.error("手机号不正确");
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        // 生成验证码
        String checkCode = RandomUtils.getFourBitRandom();
        // 发送验证码
//        smsService.send(mobile,checkCode);
        // 将验证码存到Redis中
        redisTemplate.opsForValue().set(mobile,checkCode,1, TimeUnit.MINUTES);
        return R.ok().message("短信发送成功");
    }

}
