package com.lovesosa.guli.service.edu.feign;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.edu.feign.fallback.OssFileServiceFallBack;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lovesosa
 */
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService {


    /**
     * 测试
     * @return
     */
    @GetMapping("/admin/oss/file/test")
    R test();



    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);

}
