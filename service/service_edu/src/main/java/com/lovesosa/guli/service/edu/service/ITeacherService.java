package com.lovesosa.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lovesosa.guli.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovesosa.guli.service.edu.entity.vo.TeacherQueryVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface ITeacherService extends IService<Teacher> {

    IPage<Teacher> selectPage(Page<Teacher> teacherPage, TeacherQueryVO teacherQueryVO);

    List<Map<String, Object>> selectNameList(String key);

    boolean removeAvatarById(String id);

    Map<String,Object> selectTeacherInfoById(String id);

    List<Teacher> selectHotTeacher();

}
