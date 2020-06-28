package com.lovesosa.guli.service.sms.service;

import com.aliyuncs.exceptions.ClientException;

/**
 * @author lovesosa
 */
public interface SmsService {
    void send(String mobile, String checkCode) throws ClientException;
}
