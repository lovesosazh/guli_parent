package com.lovesosa.guli.service.edu.feign.fallback;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.edu.feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lovesosa
 */
@Component
@Slf4j
public class VodMediaServiceFallBack implements VodMediaService {


    @Override
    public R removeVideo(String vodId) {
        log.error("......熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> videoIdList) {
        log.error("......熔断保护");
        return R.error();
    }
}
