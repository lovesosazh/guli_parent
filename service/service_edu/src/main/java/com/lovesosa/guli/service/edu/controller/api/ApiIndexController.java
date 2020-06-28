package com.lovesosa.guli.service.edu.controller.api;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.edu.entity.Course;
import com.lovesosa.guli.service.edu.entity.Teacher;
import com.lovesosa.guli.service.edu.service.ICourseService;
import com.lovesosa.guli.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lovesosa
 */
@Api(tags="首页")
@RestController
@RequestMapping("/api/edu/index")
@Slf4j
public class ApiIndexController {

    @Autowired
    private ICourseService courseService;
    @Autowired
    private ITeacherService teacherService;

    @ApiOperation("课程和讲师的首页数据")
    @GetMapping
    public R index() {
        List<Course> courseList =  courseService.selectHotCourse();

        List<Teacher> teacherList = teacherService.selectHotTeacher();

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
