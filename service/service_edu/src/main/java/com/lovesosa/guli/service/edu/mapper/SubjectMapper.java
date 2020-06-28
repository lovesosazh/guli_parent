package com.lovesosa.guli.service.edu.mapper;

import com.lovesosa.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovesosa.guli.service.edu.entity.vo.SubjectVO;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    List<SubjectVO> selectNestedListByParentId(String parentId);
}
