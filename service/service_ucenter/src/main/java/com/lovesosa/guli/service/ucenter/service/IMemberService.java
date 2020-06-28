package com.lovesosa.guli.service.ucenter.service;

import com.lovesosa.guli.service.base.dto.MemberDto;
import com.lovesosa.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovesosa.guli.service.ucenter.entity.vo.LoginVO;
import com.lovesosa.guli.service.ucenter.entity.vo.RegisterVO;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-02
 */
public interface IMemberService extends IService<Member> {

    void register(RegisterVO registerVO);

    String login(LoginVO loginVO);

    Member getByOpenid(String openid);

    MemberDto getMemberDtoByMemberId(String memberId);

    Integer countRegisterNum(String day);
}
