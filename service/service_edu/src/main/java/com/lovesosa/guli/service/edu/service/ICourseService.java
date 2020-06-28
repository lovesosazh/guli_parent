package com.lovesosa.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lovesosa.guli.service.base.dto.CourseDto;
import com.lovesosa.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovesosa.guli.service.edu.entity.form.CourseInfoForm;
import com.lovesosa.guli.service.edu.entity.vo.*;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface ICourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoById(String id);


    void updateCourseInfoById(CourseInfoForm courseInfoForm);


    IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVo courseQueryVo);

    boolean removeCoverById(String id);

    boolean removeCourseById(String id);

    CoursePublishVO getCoursePublishVoById(String id);

    boolean publishCourseById(String id);

    List<Course> webSelectList(WebCourseQueryVO webCourseQueryVO);

    /**
     * 获取课程信息并更新浏览量
     * @param id
     * @return
     */
    WebCourseVO selectWebCourseVoById(String id);

    List<Course> selectHotCourse();

    CourseDto getCourseDtoById(String courseId);

    void updateBuyCountById(String id);
}
