package com.lovesosa.guli.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lovesosa.guli.service.base.dto.CourseDto;
import com.lovesosa.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovesosa.guli.service.edu.entity.vo.CoursePublishVO;
import com.lovesosa.guli.service.edu.entity.vo.CourseVo;
import com.lovesosa.guli.service.edu.entity.vo.WebCourseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface CourseMapper extends BaseMapper<Course> {


    List<CourseVo> selectPageByCourseQueryVO(Page<CourseVo> courseVoPage,
                                             @Param(Constants.WRAPPER) QueryWrapper<CourseVo> queryWrapper);

    CoursePublishVO selectPublishVoById(String id);

    WebCourseVO selectWebCourseVoById(String courseId);

    CourseDto selectCourseDtoById(String courseId);
}
