package com.lovesosa.guli.service.trade.feign;

import com.lovesosa.guli.service.base.dto.MemberDto;
import com.lovesosa.guli.service.trade.feign.fallback.UcenterMemberServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lovesosa
 */
@FeignClient(value = "service-ucenter", fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {

    @GetMapping("/api/ucenter/member/inner/get-member-dto/{memberId}")
    MemberDto getMemberDtoByMemberId(@PathVariable("memberId") String memberId);
}
