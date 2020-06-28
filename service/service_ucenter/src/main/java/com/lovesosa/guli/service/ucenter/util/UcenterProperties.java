package com.lovesosa.guli.service.ucenter.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lovesosa
 */
@Data
@Component
@ConfigurationProperties(prefix="wx.open")
public class UcenterProperties {
    private String appId;
    private String appSecret;
    private String redirectUri;
}
