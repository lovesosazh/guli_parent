package com.lovesosa.guli.service.trade.service;

import java.util.Map;

/**
 * @author lovesosa
 */
public interface WeixinPayService {

    Map<String, Object> createNative(String orderNo, String remoteAddr);
}
