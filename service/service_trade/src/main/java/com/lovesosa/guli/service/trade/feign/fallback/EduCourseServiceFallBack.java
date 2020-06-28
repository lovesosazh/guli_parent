package com.lovesosa.guli.service.trade.feign.fallback;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.base.dto.CourseDto;
import com.lovesosa.guli.service.trade.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lovesosa
 */
@Component
@Slf4j
public class EduCourseServiceFallBack implements EduCourseService
{
    @Override
    public CourseDto getCourseDtoById(String courseId) {
        log.info("熔断保护");
        return null;
    }

    @Override
    public R updateBuyCountById(String id) {
        log.info("熔断保护");
        return R.error();
    }
}
