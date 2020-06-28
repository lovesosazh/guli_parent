package com.lovesosa.guli.service.edu.controller.api;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.base.dto.CourseDto;
import com.lovesosa.guli.service.edu.entity.Course;
import com.lovesosa.guli.service.edu.entity.vo.ChapterVO;
import com.lovesosa.guli.service.edu.entity.vo.WebCourseQueryVO;
import com.lovesosa.guli.service.edu.entity.vo.WebCourseVO;
import com.lovesosa.guli.service.edu.service.IChapterService;
import com.lovesosa.guli.service.edu.service.ICourseService;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lovesosa
 */
@Api(tags="课程")
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    private ICourseService courseService;
    @Autowired
    private IChapterService chapterService;

    @ApiOperation("课程列表")
    @GetMapping("/list")
    public R pageList(@ApiParam(value = "查询对象") WebCourseQueryVO webCourseQueryVO) {

        List<Course> courseList =  courseService.webSelectList(webCourseQueryVO);
        return R.ok().data("courseList",courseList);
    }

    @ApiOperation("根据id查询课程")
    @GetMapping("/get/{courseId}")
    public R getById(@ApiParam(value = "课程id",required = true) @PathVariable("courseId") String courseId) {
        // 查询课程信息和讲师信息
        WebCourseVO webCourseVO = courseService.selectWebCourseVoById(courseId);

        // 查询当前课程的章节和课时信息
        List<ChapterVO> chapterVOList = chapterService.nestedList(courseId);

        return R.ok().data("course",webCourseVO).data("chapterVOList",chapterVOList);
    }


    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("/inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String courseId){
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return courseDto;
    }

    @ApiOperation("根据课程id更改销售量")
    @GetMapping("/inner/update-buy-count/{id}")
    public R updateBuyCountById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String id){
        courseService.updateBuyCountById(id);
        return R.ok();
    }
}
