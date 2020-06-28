package com.lovesosa.guli.service.statistics.feign;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.statistics.feign.fallback.UcenterMemberServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lovesosa
 */
@FeignClient(value = "service-ucenter", fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {

    @GetMapping("/admin/ucenter/member/count/register-num/{day}")
    R countRegisterNum(@PathVariable("day") String day);
}
