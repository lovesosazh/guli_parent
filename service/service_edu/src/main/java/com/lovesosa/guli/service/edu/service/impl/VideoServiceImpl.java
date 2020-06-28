package com.lovesosa.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lovesosa.guli.service.edu.entity.Video;
import com.lovesosa.guli.service.edu.feign.VodMediaService;
import com.lovesosa.guli.service.edu.mapper.VideoMapper;
import com.lovesosa.guli.service.edu.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {


    @Autowired
    private VodMediaService vodMediaService;

    @Override
    public void removeMediaVideoById(String id) {
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        vodMediaService.removeVideo(videoSourceId);
    }

    @Override
    public void removeMediaVideoByChapterId(String chapterId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("chapter_id", chapterId);

        List<String> videoIdList = getVideoSourceIdList(queryWrapper);

        vodMediaService.removeVideoByIdList(videoIdList);
    }

    @Override
    public void removeMediaVideoByCourseId(String courseId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("course_id", courseId);

        List<String> videoIdList = getVideoSourceIdList(queryWrapper);

        vodMediaService.removeVideoByIdList(videoIdList);
    }

    private List<String> getVideoSourceIdList(QueryWrapper queryWrapper) {
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        ArrayList<String> videoIdList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            String videoSourceId = (String) map.get("video_source_id");
            videoIdList.add(videoSourceId);
        }
        return videoIdList;
    }
}
