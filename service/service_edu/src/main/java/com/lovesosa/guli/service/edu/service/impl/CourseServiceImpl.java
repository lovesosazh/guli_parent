package com.lovesosa.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.base.dto.CourseDto;
import com.lovesosa.guli.service.edu.entity.*;
import com.lovesosa.guli.service.edu.entity.form.CourseInfoForm;
import com.lovesosa.guli.service.edu.entity.vo.*;
import com.lovesosa.guli.service.edu.feign.OssFileService;
import com.lovesosa.guli.service.edu.mapper.*;
import com.lovesosa.guli.service.edu.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private CourseDescriptionMapper courseDescriptionMapper;
    @Resource
    private OssFileService ossFileService;
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private ChapterMapper chapterMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CourseCollectMapper courseCollectMapper;
    @Resource
    private TeacherMapper teacherMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {

        // 保存Course
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        course.setStatus(Course.COURSE_DRAFT);
        baseMapper.insert(course);
        // 保存CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        String description = courseInfoForm.getDescription();
        courseDescription.setDescription(description);
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {

        Course course = baseMapper.selectById(id);
        if (course == null) {
            return null;
        }

        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);

        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course,courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        // 更新Course
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.updateById(course);
        // 更新CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        String description = courseInfoForm.getDescription();
        courseDescription.setDescription(description);
        courseDescription.setId(course.getId());
        courseDescriptionMapper.updateById(courseDescription);
    }

    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit,  CourseQueryVo courseQueryVo) {
        QueryWrapper<CourseVo> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("c.gmt_create");
        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();

        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("c.title",title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("c.teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("c.subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("c.subject_id",subjectId);
        }

        Page<CourseVo> courseVoPage = new Page<>(page,limit);
        List<CourseVo> records = baseMapper.selectPageByCourseQueryVO(courseVoPage, queryWrapper);
        courseVoPage.setRecords(records);

        return courseVoPage;
    }

    @Override
    public boolean removeCoverById(String id) {
        // 根据id获取课程Cover 的url
        Course course = baseMapper.selectById(id);
        if (course != null) {
            String CoverUrl = course.getCover();
            if (!StringUtils.isEmpty(CoverUrl)) {
                R r = ossFileService.removeFile(CoverUrl);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id) {

        // 删除video(课时)
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        // 删除chapter(章节)
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",id);
        chapterMapper.delete(chapterQueryWrapper);


        // 删除Comment(评论)
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        // 删除CourseCollect (课程收藏)
        QueryWrapper<CourseCollect> collectQueryWrapper = new QueryWrapper<>();
        collectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(collectQueryWrapper);


        // 删除Description(课程详情)
        courseDescriptionMapper.deleteById(id);

        // 删除Course(课程)
        return this.removeById(id);
    }

    @Override
    public CoursePublishVO getCoursePublishVoById(String id) {

        return baseMapper.selectPublishVoById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVO webCourseQueryVO) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",Course.COURSE_NORMAL);

        if (!StringUtils.isEmpty(webCourseQueryVO.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id",webCourseQueryVO.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(webCourseQueryVO.getSubjectId())) {
            queryWrapper.eq("subject_id",webCourseQueryVO.getSubjectId());
        }


        if (!StringUtils.isEmpty(webCourseQueryVO.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(webCourseQueryVO.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(webCourseQueryVO.getPriceSort())) {
            queryWrapper.orderByDesc("price");
        }

        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVO selectWebCourseVoById(String id) {
        Course course = baseMapper.selectById(id);
        // 更新课程浏览数
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
        // 获取课程信息
        WebCourseVO webCourseVO = baseMapper.selectWebCourseVoById(id);
        return webCourseVO;
    }

    @Cacheable(value = "index", key="'selectHotCourse'")
    @Override
    public List<Course> selectHotCourse() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {
        return baseMapper.selectCourseDtoById(courseId);
    }

    @Override
    public void updateBuyCountById(String id) {

        Course course = baseMapper.selectById(id);
        long buyCount = course.getBuyCount() + 1;
        course.setBuyCount(buyCount);
        baseMapper.updateById(course);
    }

//    @Override
//    public CourseDto getCourseDtoById(String courseId) {
//        Course course = baseMapper.selectById(courseId);
//        String teacherId = course.getTeacherId();
//
//        Teacher teacher = teacherMapper.selectById(teacherId);
//
//        CourseDto courseDto = new CourseDto();
//        courseDto.setId(course.getId());
//        courseDto.setCover(course.getCover());
//        courseDto.setPrice(course.getPrice());
//        courseDto.setTitle(course.getTitle());
//        courseDto.setTeacherName(teacher.getName());
//        return courseDto;
//    }




}
