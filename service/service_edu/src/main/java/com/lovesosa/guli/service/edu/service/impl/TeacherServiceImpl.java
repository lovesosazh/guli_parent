package com.lovesosa.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.edu.entity.Course;
import com.lovesosa.guli.service.edu.entity.Teacher;
import com.lovesosa.guli.service.edu.entity.vo.TeacherQueryVO;
import com.lovesosa.guli.service.edu.feign.OssFileService;
import com.lovesosa.guli.service.edu.mapper.CourseMapper;
import com.lovesosa.guli.service.edu.mapper.TeacherMapper;
import com.lovesosa.guli.service.edu.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Resource
    private OssFileService ossFileService;
    @Resource
    private CourseMapper courseMapper;

    @Override
    public IPage<Teacher> selectPage(Page<Teacher> teacherPage, TeacherQueryVO teacherQueryVO) {
        // 显示分页查询列表
        // 1、排序：按照sort字段排序
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        // 2、分页查询
        if (teacherQueryVO == null) {
            return baseMapper.selectPage(teacherPage,queryWrapper);
        }

        // 3.条件查询
        String name = teacherQueryVO.getName();
        Integer level = teacherQueryVO.getLevel();
        String joinDateBegin = teacherQueryVO.getJoinDateBegin();
        String joinDateEnd = teacherQueryVO.getJoinDateEnd();

        if (!StringUtils.isEmpty(name)) {
            queryWrapper.likeRight("name",name);
        }

        if (level != null) {
            queryWrapper.eq("level",level);
        }

        if (!StringUtils.isEmpty(joinDateBegin)) {
            queryWrapper.ge("join_date",joinDateBegin);
        }

        if (!StringUtils.isEmpty(joinDateEnd)) {
            queryWrapper.le("join_date",joinDateEnd);
        }


        return baseMapper.selectPage(teacherPage,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameList(String key) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.select("name");
        teacherQueryWrapper.likeRight("name",key);
        List<Map<String, Object>> maps = baseMapper.selectMaps(teacherQueryWrapper);
        return maps;
    }

    @Override
    public boolean removeAvatarById(String id) {
        Teacher teacher = baseMapper.selectById(id);
        if (teacher != null) {
            String avatar = teacher.getAvatar();
            if (!StringUtils.isEmpty(avatar)) {
                R r = ossFileService.removeFile(avatar);
                return r.getSuccess();
            }
        }

        return false;
    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {
        Teacher teacher = baseMapper.selectById(id);

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",id);
        List<Course> courseList = courseMapper.selectList(queryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("teacher",teacher);
        map.put("courseList", courseList);

        return map;
    }


    @Cacheable(value = "index", key="'selectHotTeacher'")
    @Override
    public List<Teacher> selectHotTeacher() {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        queryWrapper.last("limit 4");

        return baseMapper.selectList(queryWrapper);
    }
}
