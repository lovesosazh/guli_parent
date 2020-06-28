package com.lovesosa.guli.service.edu.controller.api;


import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.common.base.util.JwtInfo;
import com.lovesosa.guli.common.base.util.JwtUtils;
import com.lovesosa.guli.service.edu.entity.CourseCollect;
import com.lovesosa.guli.service.edu.entity.vo.CourseCollectVO;
import com.lovesosa.guli.service.edu.service.ICourseCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
@Api(tags="课程收藏")
@RestController
@RequestMapping("/api/edu/course-collect")
@Slf4j
public class ApiCourseCollectController {

    @Autowired
    private ICourseCollectService courseCollectService;

    @ApiOperation("判断是否收藏")
    @GetMapping("/auth/is-collect/{courseId}")
    public R isCollect(@PathVariable("courseId") String courseId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean isCollect = courseCollectService.isCollect(courseId, jwtInfo.getId());
        return R.ok().data("isCollect", isCollect);
    }

    @ApiOperation("添加收藏")
    @PostMapping("/auth/save/{courseId}")
    public R save(@PathVariable("courseId") String courseId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        courseCollectService.saveCourseCollect(courseId, jwtInfo.getId());
        return R.ok();
    }

    @ApiOperation("获取课程收藏列表")
    @GetMapping("/auth/list")
    public R collectList(HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<CourseCollectVO> collectVOList = courseCollectService.selectListByMemberId(jwtInfo.getId());
        return R.ok().data("items", collectVOList);
    }

    @ApiOperation("取消课程收藏")
    @DeleteMapping("/auth/remove/{courseId}")
    public R remove(@PathVariable("courseId") String courseId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean result = courseCollectService.removeCourseCollect(courseId, jwtInfo.getId());
        if (result) {
            return R.ok().message("已取消");
        } else {
            return R.error().message("取消失败");
        }
    }

}

