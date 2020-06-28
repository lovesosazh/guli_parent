package com.lovesosa.guli.service.edu.mapper;

import com.lovesosa.guli.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovesosa.guli.service.edu.entity.vo.CourseCollectVO;

import java.util.List;

/**
 * <p>
 * 课程收藏 Mapper 接口
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface CourseCollectMapper extends BaseMapper<CourseCollect> {

    List<CourseCollectVO> selectPageByMemberId(String memberId);
}
