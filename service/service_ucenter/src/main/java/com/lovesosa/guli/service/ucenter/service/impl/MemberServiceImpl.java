package com.lovesosa.guli.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.FormUtils;
import com.lovesosa.guli.common.base.util.JwtInfo;
import com.lovesosa.guli.common.base.util.JwtUtils;
import com.lovesosa.guli.common.base.util.MD5;
import com.lovesosa.guli.service.base.dto.MemberDto;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.ucenter.entity.Member;
import com.lovesosa.guli.service.ucenter.entity.vo.LoginVO;
import com.lovesosa.guli.service.ucenter.entity.vo.RegisterVO;
import com.lovesosa.guli.service.ucenter.mapper.MemberMapper;
import com.lovesosa.guli.service.ucenter.service.IMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-02
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void register(RegisterVO registerVO) {

        // 校验参数
        String nickname = registerVO.getNickname();
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();

        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
             throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        // 校验验证码
        String checkCode = (String) redisTemplate.opsForValue().get(mobile);
        if (!code.equals(checkCode)) {
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }

        // 用户是否被注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer result = baseMapper.selectCount(queryWrapper);
        if (result > 0) {
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        // 注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("https://guli-edu-20200526.oss-cn-beijing.aliyuncs.com/avatar/2020/05/26/23d27222-441d-4276-9830-e91f57e285a4.png");
        member.setDisabled(false);
        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVO loginVO) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        // 校验参数
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        // 校验手机号是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Member member = baseMapper.selectOne(queryWrapper);
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        // 校验密码是否正确
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        // 校验用户是否被禁用
        if (member.getDisabled()) {
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        // 登录 生成token
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setAvatar(member.getAvatar());

        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1800);

        return jwtToken;
    }

    @Override
    public Member getByOpenid(String openid) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        Member member = baseMapper.selectById(memberId);
        MemberDto memberDto = new MemberDto();
        BeanUtils.copyProperties(member,memberDto);
        return memberDto;
    }

    @Override
    public Integer countRegisterNum(String day) {
        return baseMapper.selectRegisterNumByDay(day);
    }

}
