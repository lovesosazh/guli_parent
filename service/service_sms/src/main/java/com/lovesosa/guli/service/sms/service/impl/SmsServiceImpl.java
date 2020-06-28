package com.lovesosa.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.sms.service.SmsService;
import com.lovesosa.guli.service.sms.util.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lovesosa
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void send(String mobile, String checkCode) throws ClientException {
        String regionId = smsProperties.getRegionId();
        String accessKeyId = smsProperties.getKeyId();
        String keySecret = smsProperties.getKeySecret();
        String signName = smsProperties.getSignName();
        String templateCode = smsProperties.getTemplateCode();

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, keySecret);

        IAcsClient client = new DefaultAcsClient(profile);


        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);

        Map<String,String> param = new HashMap<>();
        param.put("code",checkCode);
        Gson gson = new Gson();
        String jsonCode = gson.toJson(param);

        request.putQueryParameter("TemplateParam", jsonCode);

        CommonResponse response = client.getCommonResponse(request);
        String data = response.getData();

        HashMap<String,String> resultMap = gson.fromJson(data, HashMap.class);
        String code = resultMap.get("Code");
        String message = resultMap.get("Message");

        if ("isv.BUSINESS_LIMIT_CONTROL".equals(code)) {
            log.error("短信发送过于频繁！" + "code:" + code + ", message:" + message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }

        if (!"OK".equals(code)) {
            log.error("短信发送失败！" + "code:" + code + ", message:" + message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }

    }
}
