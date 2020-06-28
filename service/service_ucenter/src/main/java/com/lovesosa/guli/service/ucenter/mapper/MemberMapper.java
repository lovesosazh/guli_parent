package com.lovesosa.guli.service.ucenter.mapper;

import com.lovesosa.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-02
 */
@Repository
public interface MemberMapper extends BaseMapper<Member> {

    Integer selectRegisterNumByDay(String day);
}
