package com.lovesosa.guli.service.cms.feign;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.cms.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lovesosa
 */
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);
}
