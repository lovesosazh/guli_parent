package com.lovesosa.guli.service.cms.feign.fallback;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.cms.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lovesosa
 */
@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error().message("调用超时");
    }
}
