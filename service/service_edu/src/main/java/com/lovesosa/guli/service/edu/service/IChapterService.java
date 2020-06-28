package com.lovesosa.guli.service.edu.service;

import com.lovesosa.guli.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovesosa.guli.service.edu.entity.vo.ChapterVO;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface IChapterService extends IService<Chapter> {

    boolean removeChapterById(String id);

    List<ChapterVO> nestedList(String courseId);


}
