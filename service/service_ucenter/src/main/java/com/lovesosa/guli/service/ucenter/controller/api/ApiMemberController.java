package com.lovesosa.guli.service.ucenter.controller.api;


import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.JwtInfo;
import com.lovesosa.guli.common.base.util.JwtUtils;
import com.lovesosa.guli.service.base.dto.MemberDto;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.ucenter.entity.vo.LoginVO;
import com.lovesosa.guli.service.ucenter.entity.vo.RegisterVO;
import com.lovesosa.guli.service.ucenter.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-02
 */
@Api(tags = "会员管理")
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

    @Autowired
    private IMemberService memberService;

    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO) {
        memberService.register(registerVO);
        return R.ok().message("注册成功!");
    }

    @ApiOperation(value = "会员登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO) {
        String token = memberService.login(loginVO);

        return R.ok().data("token", token);
    }


    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("/get-login-info")
    public R getLoginInfo(HttpServletRequest request){

        try{
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            return R.ok().data("userInfo", jwtInfo);
        }catch (Exception e){
            log.error("解析用户信息失败，" + e.getMessage());
            throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

    @ApiOperation("根据会员id查询会员信息")
    @GetMapping("/inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoByMemberId(
            @ApiParam(value = "会员ID", required = true)
            @PathVariable String memberId){
        MemberDto memberDto = memberService.getMemberDtoByMemberId(memberId);
        return memberDto;
    }
}

