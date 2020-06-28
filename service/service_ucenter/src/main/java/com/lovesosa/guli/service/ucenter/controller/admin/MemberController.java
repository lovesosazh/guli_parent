package com.lovesosa.guli.service.ucenter.controller.admin;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.ucenter.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lovesosa
 */
@Api(tags = "会员管理")
@RestController
@RequestMapping("/admin/ucenter/member")
public class MemberController {

    @Autowired
    private IMemberService memberService;

    @ApiOperation(value = "根据日期统计注册人数")
    @GetMapping("/count/register-num/{day}")
    public R countRegisterNum(@ApiParam(name = "day", value = "统计日期") @PathVariable("day") String day) {
        Integer registerNum = memberService.countRegisterNum(day);
        return R.ok().data("registerNum", registerNum);
    }


}
