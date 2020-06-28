package com.lovesosa.guli.service.ucenter.controller.api;

import com.google.gson.Gson;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.ExceptionUtils;
import com.lovesosa.guli.common.base.util.HttpClientUtils;
import com.lovesosa.guli.common.base.util.JwtInfo;
import com.lovesosa.guli.common.base.util.JwtUtils;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.ucenter.entity.Member;
import com.lovesosa.guli.service.ucenter.service.IMemberService;
import com.lovesosa.guli.service.ucenter.util.UcenterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lovesosa
 */

@Controller
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {

    @Autowired
    private UcenterProperties ucenterProperties;
    @Autowired
    private IMemberService memberService;

    @GetMapping("/login")
    public String genQrConnect(HttpSession session) {

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 组装URL
        String appId = ucenterProperties.getAppId();
        // 将回调url进行转码
        String redirectURI = null;
        try {
            redirectURI = URLEncoder.encode(ucenterProperties.getRedirectUri(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }
        // 生成随机state，防止csrf攻击
        String state = UUID.randomUUID().toString();
        session.setAttribute("wx_open_state",state);

        String qrCodeUrl = String.format(baseUrl, appId, redirectURI, state);

        // 跳转到组装的URL地址
        return "redirect:" + qrCodeUrl;
    }


    @GetMapping("/callback")
    public String callback(String code, String state, HttpSession session) {
        log.info("callback被调用");
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        // 从redis中取出session并进行比对
        String sessionState = (String) session.getAttribute("wx_open_state");
        if (!state.equals(sessionState)) {
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        // 携带code临时票据，和appid以及appsecret请求access_token和openid（微信唯一标识）
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        // 组装参数
        Map<String,String> accessTokenParam = new HashMap<>();
        accessTokenParam.put("appid",ucenterProperties.getAppId());
        accessTokenParam.put("secret", ucenterProperties.getAppSecret());
        accessTokenParam.put("code",code);
        accessTokenParam.put("grant_type","authorization_code");
        HttpClientUtils clientUtils = new HttpClientUtils(accessTokenUrl,accessTokenParam);

        String result = "";
        try {
            // 发送请求
            clientUtils.get();
            // 得到响应
            result = clientUtils.getContent();
            log.info("result:" + result);
        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        // 解析返回的json结果
        Gson gson = new Gson();
        HashMap<String,Object> resultMap = gson.fromJson(result, HashMap.class);

        // 失败的响应
        Object errcode = resultMap.get("errcode");
        if (errcode != null) {
            Double errorCode = (Double) errcode;
            String errorMsg = (String) resultMap.get("errmsg");
            log.error("获取access_token失败" + "code:" + errorCode + ", message:" + errorMsg);
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        // 成功的响应
        // 解析出结果中的access_token和openid
        String accessToken = (String) resultMap.get("access_token");
        String openid = (String) resultMap.get("openid");
        log.info("accessToken:" + accessToken);
        log.info("openid:" + openid);

        // 在本地数据库中查找当前微信用户的信息
        Member member = memberService.getByOpenid(openid);
        if (member == null) {
            // 如果不存在则去获取微信用户个人信息
            member = new Member();
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
            Map<String, String> baseUserInfoParam = new HashMap<>();
            baseUserInfoParam.put("access_token",accessToken);
            baseUserInfoParam.put("openid",openid);
            clientUtils = new HttpClientUtils(baseUserInfoUrl, baseUserInfoParam);
            String resultUserInfo = "";
            try {
                clientUtils.get();
                resultUserInfo = clientUtils.getContent();
            } catch (Exception e) {
                log.error("获取获取用户信息失败");
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            Map<String,Object> userInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            // 失败的响应
            errcode = userInfoMap.get("errcode");
            if (errcode != null) {
                Double errorCode = (Double) errcode;
                String errorMsg = (String) userInfoMap.get("errmsg");
                log.error("获取用户信息失败" + "code:" + errorCode + ", message:" + errorMsg);
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            // 成功的响应，解析出结果中的用户信息
            String nickname = (String) userInfoMap.get("nickname");
            String avatar = (String) userInfoMap.get("headimgurl");
            Double sex = (Double) userInfoMap.get("sex");
            // 在本地数据库中插入当前微信用户的信息（使用微信账号在本地服务器注册账号）
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(avatar);
            member.setSex(sex.intValue());
            memberService.save(member);
        }

        // 如果当前用户已存在，则直接使用当前用户的信息登录（生成jwt的过程）
        JwtInfo jwtInfo = new JwtInfo();
        BeanUtils.copyProperties(member,jwtInfo);
        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1800);

        return "redirect:http://localhost:3000?token=" + jwtToken;
    }
}
