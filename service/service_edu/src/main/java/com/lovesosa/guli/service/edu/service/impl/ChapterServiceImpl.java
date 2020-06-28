package com.lovesosa.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lovesosa.guli.service.edu.entity.Chapter;
import com.lovesosa.guli.service.edu.entity.Video;
import com.lovesosa.guli.service.edu.entity.vo.ChapterVO;
import com.lovesosa.guli.service.edu.entity.vo.VideoVO;
import com.lovesosa.guli.service.edu.mapper.ChapterMapper;
import com.lovesosa.guli.service.edu.mapper.VideoMapper;
import com.lovesosa.guli.service.edu.service.IChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements IChapterService {

    @Resource
    private VideoMapper videoMapper;

    @Transactional
    @Override
    public boolean removeChapterById(String id) {
        // 删除video(课时)
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", id);
        videoMapper.delete(videoQueryWrapper);

        // 删除章节
        return this.removeById(id);
    }

    @Override
    public List<ChapterVO> nestedList(String courseId) {
        // 获取章节信息列表
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        chapterQueryWrapper.orderByAsc("sort","id");
        List<Chapter> chapterList = baseMapper.selectList(chapterQueryWrapper);

        // 获取课时信息列表
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        videoQueryWrapper.orderByAsc("sort","id");
        List<Video> videoList = videoMapper.selectList(videoQueryWrapper);

        // 组装 chapterVOList
        List<ChapterVO> chapterVOList = new ArrayList<>();
        for (Chapter chapter : chapterList) {
            List<VideoVO> videoVOList = new ArrayList<>();
            for (Video video : videoList) {
                if (chapter.getId().equals(video.getChapterId())) {
                    VideoVO videoVO = new VideoVO();
                    BeanUtils.copyProperties(video, videoVO);
                    videoVOList.add(videoVO);
                }
            }

            ChapterVO chapterVO = new ChapterVO();
            chapterVO.setChildren(videoVOList);
            BeanUtils.copyProperties(chapter,chapterVO);
            chapterVOList.add(chapterVO);

        }


        return chapterVOList;
    }
}
