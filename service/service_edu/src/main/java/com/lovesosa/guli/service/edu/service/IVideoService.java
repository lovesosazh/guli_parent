package com.lovesosa.guli.service.edu.service;

import com.lovesosa.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
public interface IVideoService extends IService<Video> {

    void removeMediaVideoById(String id);

    void removeMediaVideoByChapterId(String chapterId);

    void removeMediaVideoByCourseId(String courseId);
}
