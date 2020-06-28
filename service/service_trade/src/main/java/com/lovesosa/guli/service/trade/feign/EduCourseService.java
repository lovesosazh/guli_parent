package com.lovesosa.guli.service.trade.feign;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.base.dto.CourseDto;
import com.lovesosa.guli.service.trade.feign.fallback.EduCourseServiceFallBack;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lovesosa
 */
@FeignClient(value = "service-edu", fallback = EduCourseServiceFallBack.class)
public interface EduCourseService {

    @GetMapping("/api/edu/course/inner/get-course-dto/{courseId}")
    CourseDto getCourseDtoById(@PathVariable("courseId") String courseId);

    @GetMapping("/api/edu/course/inner/update-buy-count/{id}")
    R updateBuyCountById(@PathVariable("id") String id);
}
