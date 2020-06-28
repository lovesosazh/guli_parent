package com.lovesosa.guli.service.edu.service;

import com.lovesosa.guli.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovesosa.guli.service.edu.entity.vo.CourseCollectVO;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface ICourseCollectService extends IService<CourseCollect> {

    void saveCourseCollect(String courseId, String memberId);

    boolean isCollect(String courseId, String memberId);

    List<CourseCollectVO> selectListByMemberId(String memberId);

    boolean removeCourseCollect(String courseId, String memberId);
}
