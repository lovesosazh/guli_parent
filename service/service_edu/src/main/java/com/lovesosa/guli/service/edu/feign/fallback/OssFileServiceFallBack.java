package com.lovesosa.guli.service.edu.feign.fallback;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lovesosa
 */
@Component
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R test() {
        return R.error();
    }

    @Override
    public R removeFile(String url) {
        log.info("..........熔断保护");
        return R.error();
    }
}
