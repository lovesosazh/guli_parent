package com.lovesosa.guli.service.edu.service;

import com.lovesosa.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovesosa.guli.service.edu.entity.vo.SubjectVO;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface ISubjectService extends IService<Subject> {

    void batchImport(InputStream inputStream);


    List<SubjectVO> nestedList();

}
