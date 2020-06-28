package com.lovesosa.guli.service.statistics.feign.fallback;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.statistics.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lovesosa
 */
@Component
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {

    @Override
    public R countRegisterNum(String day) {
        log.error("熔断保护...");
        return R.ok().data("registerNum",0);
    }

}
