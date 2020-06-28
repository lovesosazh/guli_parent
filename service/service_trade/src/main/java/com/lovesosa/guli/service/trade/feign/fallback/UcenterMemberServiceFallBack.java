package com.lovesosa.guli.service.trade.feign.fallback;

import com.lovesosa.guli.service.base.dto.MemberDto;
import com.lovesosa.guli.service.trade.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lovesosa
 */
@Component
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {
    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        log.error("熔断保护");
        return null;
    }
}
